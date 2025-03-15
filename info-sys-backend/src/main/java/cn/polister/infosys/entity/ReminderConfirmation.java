package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * 提醒确认记录表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("reminder_confirmation")
public class ReminderConfirmation {
    @TableId
    private Long confirmationId;
    private Long reminderId;
    private String confirmationToken;
    private Integer isConfirmed;
    private Date createdAt;
    private Date confirmedAt;
}