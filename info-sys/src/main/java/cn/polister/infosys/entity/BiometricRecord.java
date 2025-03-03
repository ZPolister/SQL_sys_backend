package cn.polister.infosys.entity;

import java.time.LocalDateTime;
import java.util.Date;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 生物特征记录表(BiometricRecord)表实体类
 *
 * @author Polister
 * @since 2025-03-02 20:44:36
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("biometric_record")
public class BiometricRecord  {
//记录ID
    @TableId
    private Long recordId;

//账户ID
    private Long accountId;
//身高(cm)
    private Double heightCm;
//体重(kg)
    private Double weightKg;
//收缩压(mmHg)
    private Integer systolicPressure;
//舒张压(mmHg)
    private Integer diastolicPressure;
//血糖(mmol/L)
    private Double bloodGlucose;
//血脂(mmol/L)
    private Double bloodLipid;
//测量时间
    private LocalDateTime measurementTime;
//创建时间
    private Date createdAt;


}
