package cn.polister.infosys.entity.excel;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BiometricRecordExcel {
    //身高(cm)
    @ExcelProperty("身高(cm)")
    private Double heightCm;
    //体重(kg)
    @ExcelProperty("体重(kg)")
    private Double weightKg;
    //收缩压(mmHg)
    @ExcelProperty("收缩压(mmHg)")
    private Integer systolicPressure;
    //舒张压(mmHg)
    @ExcelProperty("舒张压(mmHg)")
    private Integer diastolicPressure;
    //血糖(mmol/L)
    @ExcelProperty("血糖(mmol/L)")
    private Double bloodGlucose;
    //血脂(mmol/L)
    @ExcelProperty("血脂(mmol/L)")
    private Double bloodLipid;
    //测量时间
    @ExcelProperty("测量时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime measurementTime;
}
