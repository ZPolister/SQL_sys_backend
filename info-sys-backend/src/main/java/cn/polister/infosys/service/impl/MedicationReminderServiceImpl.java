package cn.polister.infosys.service.impl;

import cn.hutool.json.JSONUtil;
import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.entity.vo.MedicationReminderVo;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.AccountMapper;
import cn.polister.infosys.mapper.MedicationReminderMapper;
import cn.polister.infosys.service.AIService;
import cn.polister.infosys.service.MedicationReminderService;
import cn.polister.infosys.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("medicationReminderService")
@Slf4j
public class MedicationReminderServiceImpl extends ServiceImpl<MedicationReminderMapper, MedicationReminder> implements MedicationReminderService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private AIService aiService;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    @Transactional
    public ResponseResult<Void> createReminder(MedicationReminder reminder) {
        // 参数校验
        if (reminder.getMedicationFrequency() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "每日服药次数必须大于0");
        }
        if (reminder.getMedicationDuration() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "服药天数必须大于0");
        }
        if (reminder.getReminderTime() == null || reminder.getReminderTime().isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "必须设置服药时间点");
        }
        if (reminder.getReminderTime().split(",").length != reminder.getMedicationFrequency()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "服药时间点数量必须等于每日服药次数");
        }

        reminder.setCompletionStatus(0);
        reminder.setReminderCount(0);
        reminder.setNextReminderTime(this.calculateNextReminderTime(reminder));
        this.save(reminder);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Void> updateReminder(MedicationReminder reminder) {
        // 更新主表
        if (!this.updateById(reminder)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "更新失败");
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Void> deleteReminder(Long reminderId) {
        // 由于设置了级联删除，只需要删除主表即可
        if (!this.removeById(reminderId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "删除失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    public Page<MedicationReminder> getReminderList(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        QueryWrapper<MedicationReminder> wrapper = new QueryWrapper<>();
        if (startDate != null) {
            wrapper.ge("start_time", startDate);
        }
        if (endDate != null) {
            endDate = DateUtil.handleDate(endDate);
            wrapper.le("start_time", endDate);
        }
        wrapper.orderByDesc("start_time");

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void processReminders() {
        Date now = new Date();
        List<MedicationReminder> reminders = this.list(new LambdaQueryWrapper<MedicationReminder>()
                .eq(MedicationReminder::getCompletionStatus, 0)
                .le(MedicationReminder::getStartTime, now)
                .le(MedicationReminder::getNextReminderTime, now));

        reminders.forEach(r -> {
            // 如果提醒次数够了，标记已完成
            if (r.getReminderCount() + 1 >= r.getMedicationFrequency() * r.getMedicationDuration()) {
                r.setCompletionStatus(1);
                this.updateById(r);
                return;
            }
            this.sendMedicationReminder(r);
            r.setReminderCount(r.getReminderCount() + 1);
            r.setNextReminderTime(this.calculateNextReminderTime(r));
            this.updateById(r);
        });

    }

    private Date calculateNextReminderTime(MedicationReminder reminder) {
        // 将时间字符串转换为LocalTime并排序
        var timeList = JSONUtil.parseArray(reminder.getReminderTime()).toList(String.class);
        List<LocalTime> scheduleTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (String timeStr : timeList) {
            LocalTime time = LocalTime.parse(timeStr, formatter);
            scheduleTimes.add(time);
        }
        Collections.sort(scheduleTimes);

        // 转换当前Date为LocalDateTime
        Date currentDate = new Date();
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        LocalTime currentTime = currentDateTime.toLocalTime();

        // 查找下一个时间点
        LocalTime nextTime = null;
        for (LocalTime t : scheduleTimes) {
            if (t.isAfter(currentTime)) {
                nextTime = t;
                break;
            }
        }

        LocalDateTime nextDateTime;
        if (nextTime != null) {
            nextDateTime = currentDateTime.toLocalDate().atTime(nextTime);
        } else {
            // 取次日的第一个时间
            nextDateTime = currentDateTime.toLocalDate().plusDays(1).atTime(scheduleTimes.get(0));
        }

        // 转换回Date对象
        return Date.from(nextDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void sendMedicationReminder(MedicationReminder reminder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(getEmailByAccountId(reminder.getAccountId()));
        message.setSubject("【Dian-Health】服药提醒");
        message.setText("请记得服药：" + reminder.getMedicationName()
                + "\n用法用量: " + reminder.getMedicationDosage());
        mailSender.send(message);
    }

    private String getEmailByAccountId(Long accountId) {
        return accountMapper.selectById(accountId).getEmail();
    }

    @Override
    public List<MedicationReminder> getNextReminders(Long accountId) {
        // 获取当前用户未完成的提醒
        LambdaQueryWrapper<MedicationReminder> wrapper = new LambdaQueryWrapper<MedicationReminder>()
                .eq(MedicationReminder::getAccountId, accountId)
                .eq(MedicationReminder::getCompletionStatus, 0)
                .orderByAsc(MedicationReminder::getNextReminderTime);

        List<MedicationReminder> reminders = this.list(wrapper);
        if (reminders.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取最早的下次提醒时间
        Date earliestTime = reminders.get(0).getNextReminderTime();

        // 返回所有具有相同最早提醒时间的提醒
        return reminders.stream()
                .filter(r -> r.getNextReminderTime().equals(earliestTime))
                .toList();
    }

    @Override
    public List<MedicationReminderVo> getInfoByPng(MultipartFile file) {
        return aiService.getMedicationReminderByPng(file);
    }
}