package cn.polister.infosys.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 饮食记录表(DietLog)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:46:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("diet_log")
public class DietLog  {
//日志ID（数据库自动生成，无需传输）
    @TableId
    private Long logId;

//账户ID
    private Long accountId;
//食物名称
    private String foodItem;
//数量(克)
    private Double quantityGrams;
//总热量(kcal)
    private Integer totalCalories;
//食用时间
    private Date consumptionTime;
//创建时间（自动生成，无需传输）
    private Date createdAt;
}
