package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.MedicationReminderTime;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.MedicationReminderMapper;
import cn.polister.infosys.mapper.MedicationReminderTimeMapper;
import cn.polister.infosys.service.MedicationReminderService;
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

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service("medicationReminderService")
@Slf4j
public class MedicationReminderServiceImpl extends ServiceImpl<MedicationReminderMapper, MedicationReminder> implements MedicationReminderService {

    @Resource
    private MedicationReminderTimeMapper medicationReminderTimeMapper;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    @Transactional
    public ResponseResult createReminder(MedicationReminder reminder) {
        // 参数校验
        if (reminder.getMedicationFrequency() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "每日服药次数必须大于0");
        }
        if (reminder.getMedicationDuration() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "服药天数必须大于0");
        }
        if (reminder.getReminderTimes() == null || reminder.getReminderTimes().isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "必须设置服药时间点");
        }
        if (reminder.getReminderTimes().size() != reminder.getMedicationFrequency()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "服药时间点数量必须等于每日服药次数");
        }

        reminder.setCompletionStatus(0);
        reminder.setReminderCount(0);
        reminder.setNextReminderTime(reminder.getStartTime());
        this.save(reminder);

        // 保存提醒时间点
        for (MedicationReminderTime time : reminder.getReminderTimes()) {
            time.setReminderId(reminder.getReminderId());
            medicationReminderTimeMapper.insert(time);
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateReminder(MedicationReminder reminder) {
        // 更新主表
        if (!this.updateById(reminder)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "更新失败");
        }

        // 如果有更新时间点，先删除旧的，再插入新的
        if (reminder.getReminderTimes() != null && !reminder.getReminderTimes().isEmpty()) {
            medicationReminderTimeMapper.delete(
                    new QueryWrapper<MedicationReminderTime>()
                            .eq("reminder_id", reminder.getReminderId())
            );

            for (MedicationReminderTime time : reminder.getReminderTimes()) {
                time.setReminderId(reminder.getReminderId());
                medicationReminderTimeMapper.insert(time);
            }
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteReminder(Long reminderId) {
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
            wrapper.le("start_time", endDate);
        }
        wrapper.orderByAsc("start_time");

        Page<MedicationReminder> page = this.page(new Page<>(pageNum, pageSize), wrapper);

        // 为每个提醒加载时间点
        page.getRecords().forEach(reminder -> {
            List<MedicationReminderTime> times = medicationReminderTimeMapper.selectList(
                    new QueryWrapper<MedicationReminderTime>()
                            .eq("reminder_id", reminder.getReminderId())
            );
            reminder.setReminderTimes(times);
        });

        return page;
    }

    @Override
    // @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void processReminders() {
        Date now = new Date();
        List<MedicationReminder> reminders = this.list(new QueryWrapper<MedicationReminder>()
                .eq("completion_status", 0)
                .le("next_reminder_time", now));

        for (MedicationReminder reminder : reminders) {
            List<MedicationReminderTime> times = medicationReminderTimeMapper.selectList(
                    new QueryWrapper<MedicationReminderTime>()
                            .eq("reminder_id", reminder.getReminderId()));

            LocalTime currentTime = LocalTime.now();
            boolean shouldRemind = times.stream()
                    .anyMatch(t -> t.getReminderTime().toLocalTime().equals(currentTime));

            if (shouldRemind) {
                sendMedicationReminder(reminder);
                reminder.setLastReminderSent(now);
                reminder.setReminderCount(reminder.getReminderCount() + 1);

                // 更新下次提醒时间
                reminder.setNextReminderTime(calculateNextReminderTime(reminder, times));

                // 检查是否完成所有提醒
                if (reminder.getReminderCount() >= reminder.getMedicationDuration() * reminder.getMedicationFrequency()) {
                    reminder.setCompletionStatus(1);
                }

                this.updateById(reminder);
            }
        }
    }

    private Date calculateNextReminderTime(MedicationReminder reminder, List<MedicationReminderTime> times) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nextTime = times.stream()
                .map(MedicationReminderTime::getReminderTime)
                .map(Time::toLocalTime)
                .filter(t -> t.isAfter(now.toLocalTime()))
                .min(LocalTime::compareTo)
                .orElse(times.get(0).getReminderTime().toLocalTime());

        LocalDateTime nextDateTime = now.with(nextTime);
        if (nextDateTime.isBefore(now) || nextDateTime.equals(now)) {
            nextDateTime = nextDateTime.plusDays(1);
        }

        return Date.from(nextDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void sendMedicationReminder(MedicationReminder reminder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(getEmailByAccountId(reminder.getAccountId()));
        message.setSubject("【服药提醒】");
        message.setText("请记得服药：" + reminder.getMedicationName() +
                "\n用量: " + reminder.getMedicationDosage());
        mailSender.send(message);
    }

    private String getEmailByAccountId(Long accountId) {
        // 实现获取用户邮箱的逻辑
        return "user@example.com";
    }
}