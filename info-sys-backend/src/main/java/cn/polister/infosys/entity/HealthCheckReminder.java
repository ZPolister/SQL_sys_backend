package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 体检提醒表(HealthCheckReminder)表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_check_reminder")
public class HealthCheckReminder {
    //提醒ID
    @TableId
    private Long reminderId;
    //账户ID
    private Long accountId;
    //提醒内容
    private String reminderContent;
    //体检时间
    private Date scheduledTime;
    //完成状态（0-待处理，1-已完成）
    private Integer completionStatus;
    //体检频率（月）
    private Integer checkFrequencyMonths;
    //体检频率（天）
    private Integer checkFrequencyDays;
    //上次提醒发送时间
    private Date lastReminderSent;
    //下次提醒时间
    private Date nextReminderTime;
    //已发送提醒次数
    private Integer reminderCount;
    //创建时间
    private Date createdAt;
}