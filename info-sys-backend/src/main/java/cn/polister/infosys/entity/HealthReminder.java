package cn.polister.infosys.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 健康提醒表(HealthReminder)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:52:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_reminder")
public class HealthReminder  {
//提醒ID
    @TableId
    private Long reminderId;

//账户ID
    private Long accountId;
//提醒类别
    private String reminderCategory;
//提醒内容
    private String reminderContent;
//预定时间
    private Date scheduledTime;
//完成状态（0-待处理，1-已完成）
    private Integer completionStatus;
//创建时间
    private Date createdAt;


}
