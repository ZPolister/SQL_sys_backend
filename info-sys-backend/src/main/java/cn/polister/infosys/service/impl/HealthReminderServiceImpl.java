package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.HealthReminder;
import cn.polister.infosys.entity.MedicationReminderTime;
import cn.polister.infosys.entity.ReminderConfirmation;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.HealthReminderMapper;
import cn.polister.infosys.mapper.MedicationReminderTimeMapper;
import cn.polister.infosys.mapper.ReminderConfirmationMapper;
import cn.polister.infosys.service.HealthReminderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 健康提醒表(HealthReminder)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:52:08
 */
@Service("healthReminderService")
@Slf4j
public class HealthReminderServiceImpl extends ServiceImpl<HealthReminderMapper, HealthReminder> implements HealthReminderService {

    @Resource
    private MedicationReminderTimeMapper medicationReminderTimeMapper;

    @Resource
    private ReminderConfirmationMapper reminderConfirmationMapper;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    @Transactional
    public ResponseResult<Void> createMedicationReminder(HealthReminder reminder) {
        reminder.setReminderCategory("MEDICATION");
        reminder.setCompletionStatus(0);
        reminder.setReminderCount(0);
        reminder.setNextReminderTime(new Date());
        this.save(reminder);

        // 保存提醒时间点
        if (reminder.getReminderTimes() != null) {
            for (MedicationReminderTime time : reminder.getReminderTimes()) {
                time.setReminderId(reminder.getReminderId());
                medicationReminderTimeMapper.insert(time);
            }
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<Void> createHealthCheckReminder(HealthReminder reminder) {
        // 校验体检频率设置
        if ((reminder.getCheckFrequencyMonths() == null || reminder.getCheckFrequencyMonths() <= 0)
            && (reminder.getCheckFrequencyDays() == null || reminder.getCheckFrequencyDays() <= 0)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "必须设置体检频率");
        }

        reminder.setReminderCategory("HEALTH_CHECK");
        reminder.setCompletionStatus(0);
        reminder.setReminderCount(0);
        reminder.setNextReminderTime(reminder.getScheduledTime());
        this.save(reminder);

        // 创建确认记录
        ReminderConfirmation confirmation = new ReminderConfirmation();
        confirmation.setReminderId(reminder.getReminderId());
        confirmation.setConfirmationToken(UUID.randomUUID().toString());
        confirmation.setIsConfirmed(0);
        reminderConfirmationMapper.insert(confirmation);

        return ResponseResult.okResult();
    }

    @Override
    // @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void processMedicationReminders() {
        Date now = new Date();
        List<HealthReminder> reminders = this.list(new QueryWrapper<HealthReminder>()
                .eq("reminder_category", "MEDICATION")
                .eq("completion_status", 0)
                .le("next_reminder_time", now));

        for (HealthReminder reminder : reminders) {
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

    private Date calculateNextReminderTime(HealthReminder reminder, List<MedicationReminderTime> times) {
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

    @Override
    @Scheduled(cron = "0 0 8 * * ?") // 每天早上8点执行
    public void processHealthCheckReminders() {
        Date now = new Date();
        List<HealthReminder> reminders = this.list(new QueryWrapper<HealthReminder>()
                .eq("reminder_category", "HEALTH_CHECK")
                .eq("completion_status", 0));

        for (HealthReminder reminder : reminders) {
            long daysUntilCheck = ChronoUnit.DAYS.between(LocalDate.now(),
                    reminder.getScheduledTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            if (daysUntilCheck <= 7 && daysUntilCheck > 0) {
                ReminderConfirmation confirmation = reminderConfirmationMapper.selectOne(
                        new QueryWrapper<ReminderConfirmation>()
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
                        // 创建下一次体检提醒
                        HealthReminder nextReminder = new HealthReminder();
                        nextReminder.setAccountId(reminder.getAccountId());
                        nextReminder.setReminderCategory("HEALTH_CHECK");
                        nextReminder.setReminderContent(reminder.getReminderContent());
                        nextReminder.setCheckFrequencyMonths(reminder.getCheckFrequencyMonths());
                        nextReminder.setCheckFrequencyDays(reminder.getCheckFrequencyDays());

                        // 计算下次体检时间
                        LocalDateTime nextCheckTime = reminder.getScheduledTime().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();

                        if (reminder.getCheckFrequencyMonths() != null && reminder.getCheckFrequencyMonths() > 0) {
                            nextCheckTime = nextCheckTime.plusMonths(reminder.getCheckFrequencyMonths());
                        }
                        if (reminder.getCheckFrequencyDays() != null && reminder.getCheckFrequencyDays() > 0) {
                            nextCheckTime = nextCheckTime.plusDays(reminder.getCheckFrequencyDays());
                        }

                        nextReminder.setScheduledTime(Date.from(nextCheckTime.atZone(ZoneId.systemDefault()).toInstant()));
                        nextReminder.setNextReminderTime(nextReminder.getScheduledTime());
                        nextReminder.setCompletionStatus(0);
                        nextReminder.setReminderCount(0);

                        this.save(nextReminder);

                        // 为新提醒创建确认记录
                        ReminderConfirmation nextConfirmation = new ReminderConfirmation();
                        nextConfirmation.setReminderId(nextReminder.getReminderId());
                        nextConfirmation.setConfirmationToken(UUID.randomUUID().toString());
                        nextConfirmation.setIsConfirmed(0);
                        reminderConfirmationMapper.insert(nextConfirmation);

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
    public ResponseResult confirmReminder(String token) {
        ReminderConfirmation confirmation = reminderConfirmationMapper.selectOne(
                new QueryWrapper<ReminderConfirmation>()
                        .eq("confirmation_token", token));

        if (confirmation == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "Invalid confirmation token");
        }

        if (confirmation.getIsConfirmed() == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "Reminder already confirmed");
        }

        confirmation.setIsConfirmed(1);
        confirmation.setConfirmedAt(new Date());
        reminderConfirmationMapper.updateById(confirmation);

        HealthReminder reminder = this.getById(confirmation.getReminderId());

        // 更新当前提醒为已完成
        reminder.setCompletionStatus(1);

        // 创建下一次体检提醒
        if (reminder.getCheckFrequencyMonths() != null || reminder.getCheckFrequencyDays() != null) {
            HealthReminder nextReminder = new HealthReminder();
            nextReminder.setAccountId(reminder.getAccountId());
            nextReminder.setReminderCategory("HEALTH_CHECK");
            nextReminder.setReminderContent(reminder.getReminderContent());
            nextReminder.setCheckFrequencyMonths(reminder.getCheckFrequencyMonths());
            nextReminder.setCheckFrequencyDays(reminder.getCheckFrequencyDays());

            // 计算下次体检时间
            LocalDateTime nextCheckTime = reminder.getScheduledTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            if (reminder.getCheckFrequencyMonths() != null && reminder.getCheckFrequencyMonths() > 0) {
                nextCheckTime = nextCheckTime.plusMonths(reminder.getCheckFrequencyMonths());
            }
            if (reminder.getCheckFrequencyDays() != null && reminder.getCheckFrequencyDays() > 0) {
                nextCheckTime = nextCheckTime.plusDays(reminder.getCheckFrequencyDays());
            }

            nextReminder.setScheduledTime(Date.from(nextCheckTime.atZone(ZoneId.systemDefault()).toInstant()));
            nextReminder.setNextReminderTime(nextReminder.getScheduledTime());
            nextReminder.setCompletionStatus(0);
            nextReminder.setReminderCount(0);

            this.save(nextReminder);

            // 为新提醒创建确认记录
            ReminderConfirmation nextConfirmation = new ReminderConfirmation();
            nextConfirmation.setReminderId(nextReminder.getReminderId());
            nextConfirmation.setConfirmationToken(UUID.randomUUID().toString());
            nextConfirmation.setIsConfirmed(0);
            reminderConfirmationMapper.insert(nextConfirmation);
        }

        this.updateById(reminder);
        return ResponseResult.okResult();
    }

    private void sendMedicationReminder(HealthReminder reminder) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(getEmailByAccountId(reminder.getAccountId()));
        message.setSubject("【服药提醒】");
        message.setText("请记得服药：" + reminder.getMedicationName() +
                "\n用量: " + reminder.getMedicationDosage());
        mailSender.send(message);
    }

    private void sendHealthCheckReminder(HealthReminder reminder, ReminderConfirmation confirmation, long daysUntilCheck) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(getEmailByAccountId(reminder.getAccountId()));
        message.setSubject("健康体检提醒");
        message.setText("距离需要提醒的时间还剩" + daysUntilCheck + " 天。\n" +
                "点此确认: http://yourwebsite.com/confirm?token=" + confirmation.getConfirmationToken());
        mailSender.send(message);
    }

    private String getEmailByAccountId(Long accountId) {
        // 实现获取用户邮箱的逻辑
        return "user@example.com";
    }
}
