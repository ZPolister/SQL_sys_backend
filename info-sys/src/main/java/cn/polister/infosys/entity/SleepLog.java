package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 睡眠记录表(SleepLog)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:52:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sleep_log")
public class SleepLog  {
//日志ID@TableId
    private Long logId;

//账户ID
    private Long accountId;
//入睡时间
    private Date sleepStart;
//醒来时间
    private Date sleepEnd;
//睡眠质量（1-5级）
    private Integer sleepQuality;
//创建时间
    private Date createdAt;


}
