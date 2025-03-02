package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 运动记录表(ExerciseLog)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:51:15
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("exercise_log")
public class ExerciseLog  {
//日志ID@TableId
    private Long logId;

//账户ID
    private Long accountId;
//运动类型
    private String exerciseType;
//开始时间
    private Date startTimestamp;
//持续时间(分钟)
    private Integer durationMinutes;
//距离(km)
    private Double distanceKm;
//消耗卡路里
    private Integer caloriesBurned;
//创建时间
    private Date createdAt;


}
