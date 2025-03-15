package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 服药提醒表(MedicationReminder)表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("medication_reminder")
public class MedicationReminder {
    //提醒ID
    @TableId
    private Long reminderId;
    //账户ID
    private Long accountId;
    //药品名称
    private String medicationName;
    //服药剂量
    private String medicationDosage;
    //每日服药次数
    private Integer medicationFrequency;
    //服药天数
    private Integer medicationDuration;
    //开始服药时间
    private Date startTime;
    //完成状态（0-待处理，1-已完成）
    private Integer completionStatus;
    //下次提醒时间
    private Date nextReminderTime;
    //服药的时间(Json格式)
    private String reminderTime;
    //已发送提醒次数
    private Integer reminderCount;
    //创建时间
    private Date createdAt;
}