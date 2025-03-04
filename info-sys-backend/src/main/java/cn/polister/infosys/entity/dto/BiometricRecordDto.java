package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BiometricRecordDto {

    @Schema(description = "身高(cm)", minimum = "50.0", example = "175.5")
    private Double heightCm;

    @Schema(description = "体重(kg)", minimum = "2.5", example = "65.3")
    private Double weightKg;

    @Schema(description = "收缩压(mmHg)", minimum = "60", maximum = "250", example = "120")
    private Integer systolicPressure;

    @Schema(description = "舒张压(mmHg)", minimum = "40", maximum = "150", example = "80")
    private Integer diastolicPressure;

    @Schema(description = "血糖(mmol/L)", minimum = "2.0", maximum = "30.0", example = "5.6")
    private Double bloodGlucose;

    @Schema(description = "血脂(mmol/L)", minimum = "0.5", maximum = "10.0", example = "1.8")
    private Double bloodLipid;

    @Schema(description = "测量时间", example = "2024-03-15T14:30:00")
    private LocalDateTime measurementTime;
}