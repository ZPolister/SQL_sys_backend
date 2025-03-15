package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Time;

/**
 * 服药时间点表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("medication_reminder_time")
public class MedicationReminderTime {
    @TableId
    private Long timeId;
    private Long reminderId;
    private Time reminderTime;
}