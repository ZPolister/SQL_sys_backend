package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 健康目标表(HealthGoal)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:51:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_goal")
public class HealthGoal  {
//目标ID
    @TableId
    private Long goalId;

//账户ID
    private Long accountId;
//目标类别
    private String goalCategory;
//目标值
    private Double targetValue;
//当前值
    private Double currentValue;
//开始日期
    private Date startDate;
//目标日期
    private Date targetDate;
//目标状态（0-进行中，1-已达成，2-未达成）
    private Integer goalStatus;
//创建时间
    private Date createdAt;

}
