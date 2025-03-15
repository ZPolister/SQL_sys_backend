package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.HealthCheckReminder;
import cn.polister.infosys.entity.HealthCheckConfirmation;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.HealthCheckReminderMapper;
import cn.polister.infosys.mapper.HealthCheckConfirmationMapper;
import cn.polister.infosys.service.HealthCheckReminderService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("healthCheckReminderService")
@Slf4j
public class HealthCheckReminderServiceImpl extends ServiceImpl<HealthCheckReminderMapper, HealthCheckReminder> implements HealthCheckReminderService {

    @Resource
    private HealthCheckConfirmationMapper healthCheckConfirmationMapper;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    @Transactional
    public ResponseResult<Void> createReminder(HealthCheckReminder reminder) {
        // 校验体检频率设置
        if ((reminder.getCheckFrequencyMonths() == null || reminder.getCheckFrequencyMonths() <= 0)
            && (reminder.getCheckFrequencyDays() == null || reminder.getCheckFrequencyDays() <= 0)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "必须设置体检频率");
        }

        reminder.setCompletionStatus(0);
        reminder.setReminderCount(0);
        reminder.setNextReminderTime(reminder.getScheduledTime());
        this.save(reminder);

        // 创建确认记录
        HealthCheckConfirmation confirmation = new HealthCheckConfirmation();
        confirmation.setReminderId(reminder.getReminderId());
        confirmation.setConfirmationToken(UUID.randomUUID().toString());
        confirmation.setIsConfirmed(0);
        healthCheckConfirmationMapper.insert(confirmation);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Void> updateReminder(HealthCheckReminder reminder) {
        if (!this.updateById(reminder)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "更新失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Void> deleteReminder(Long reminderId) {
        if (!this.removeById(reminderId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "删除失败");
        }
        return ResponseResult.okResult();
    }

    @Override
    public Page<HealthCheckReminder> getReminderList(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        QueryWrapper<HealthCheckReminder> wrapper = new QueryWrapper<>();
        if (startDate != null) {
            wrapper.ge("scheduled_time", startDate);
        }
        if (endDate != null) {
            wrapper.le("scheduled_time", endDate);
        }
        wrapper.orderByAsc("scheduled_time");
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?") // 每天早上8点执行
    public void processReminders() {
        Date now = new Date();
        List<HealthCheckReminder> reminders = this.list(new QueryWrapper<HealthCheckReminder>()
                .eq("completion_status", 0));

        for (HealthCheckReminder reminder : reminders) {
            long daysUntilCheck = ChronoUnit.DAYS.between(LocalDate.now(),
                    reminder.getScheduledTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            if (daysUntilCheck <= 7 && daysUntilCheck > 0) {
                HealthCheckConfirmation confirmation = healthCheckConfirmationMapper.selectOne(
                        new QueryWrapper<HealthCheckConfirmation>()
                                .eq("reminder_id", reminder.getReminderId())
                                .eq("is_confirmed", 0));

                if (confirmation != null && (daysUntilCheck == 7 || daysUntilCheck == 3 || daysUntilCheck == 1)) {
                    // 发送提醒邮件
                    sendHealthCheckReminder(reminder, confirmation, daysUntilCheck);

                    // 更新最后发送时间
                    reminder.setLastReminderSent(now);
                    reminder.setReminderCount(reminder.getReminderCount() + 1);

                    // 如果这是最后一次提醒（1天），并且用户没有确认，自动创建下一次提醒
                    if (daysUntilCheck == 1) {
                        createNextReminder(reminder);
                        // 将当前提醒标记为已完成
                        reminder.setCompletionStatus(1);
                    }

                    this.updateById(reminder);
                }
            }
        }
    }

    @Override
    @Transactional
    public ResponseResult<Void> confirmReminder(String token) {
        HealthCheckConfirmation confirmation = healthCheckConfirmationMapper.selectOne(
                new QueryWrapper<HealthCheckConfirmation>()
                        .eq("confirmation_token", token));

        if (confirmation == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "Invalid confirmation token");
        }

        if (confirmation.getIsConfirmed() == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "Reminder already confirmed");
        }

        confirmation.setIsConfirmed(1);
        confirmation.setConfirmedAt(new Date());
        healthCheckConfirmationMapper.updateById(confirmation);

        HealthCheckReminder reminder = this.getById(confirmation.getReminderId());

        // 更新当前提醒为已完成
        reminder.setCompletionStatus(1);
        this.updateById(reminder);

        // 创建下一次体检提醒
        createNextReminder(reminder);

        return ResponseResult.okResult();
    }

    private void createNextReminder(HealthCheckReminder currentReminder) {
        HealthCheckReminder nextReminder = new HealthCheckReminder();
        nextReminder.setAccountId(currentReminder.getAccountId());
        nextReminder.setReminderContent(currentReminder.getReminderContent());
        nextReminder.setCheckFrequencyMonths(currentReminder.getCheckFrequencyMonths());
        nextReminder.setCheckFrequencyDays(currentReminder.getCheckFrequencyDays());

        // 计算下次体检时间
        LocalDateTime nextCheckTime = currentReminder.getScheduledTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (currentReminder.getCheckFrequencyMonths() != null && currentReminder.getCheckFrequencyMonths() > 0) {
            nextCheckTime = nextCheckTime.plusMonths(currentReminder.getCheckFrequencyMonths());
        }
        if (currentReminder.getCheckFrequencyDays() != null && currentReminder.getCheckFrequencyDays() > 0) {
            nextCheckTime = nextCheckTime.plusDays(currentReminder.getCheckFrequencyDays());
        }

        nextReminder.setScheduledTime(Date.from(nextCheckTime.atZone(ZoneId.systemDefault()).toInstant()));
        nextReminder.setNextReminderTime(nextReminder.getScheduledTime());
        nextReminder.setCompletionStatus(0);
        nextReminder.setReminderCount(0);

        this.save(nextReminder);

        // 为新提醒创建确认记录
        HealthCheckConfirmation nextConfirmation = new HealthCheckConfirmation();
        nextConfirmation.setReminderId(nextReminder.getReminderId());
        nextConfirmation.setConfirmationToken(UUID.randomUUID().toString());
        nextConfirmation.setIsConfirmed(0);
        healthCheckConfirmationMapper.insert(nextConfirmation);
    }

    private void sendHealthCheckReminder(HealthCheckReminder reminder, HealthCheckConfirmation confirmation, long daysUntilCheck) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(getEmailByAccountId(reminder.getAccountId()));
        message.setSubject("健康体检提醒");
        message.setText("距离您的体检时间还有 " + daysUntilCheck + " 天。\n" +
                "点此确认: http://yourwebsite.com/confirm?token=" + confirmation.getConfirmationToken());
        mailSender.send(message);
    }

    private String getEmailByAccountId(Long accountId) {
        // 实现获取用户邮箱的逻辑
        return "user@example.com";
    }
}