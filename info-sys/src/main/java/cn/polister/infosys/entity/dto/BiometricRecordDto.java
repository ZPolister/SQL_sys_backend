package cn.polister.infosys.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BiometricRecordDto {
    @DecimalMin(value = "50.0", message = "身高最小50cm")
    private Double heightCm;

    @DecimalMin(value = "2.5", message = "体重最小2.5kg")
    private Double weightKg;

    @NotNull(message = "收缩压不能为空")
    @Min(60) @Max(250)
    private Integer systolicPressure;

    @NotNull(message = "舒张压不能为空")
    @Min(40) @Max(150)
    private Integer diastolicPressure;

    @DecimalMin("2.0") @DecimalMax("30.0")
    private Double bloodGlucose;

    @DecimalMin("0.5") @DecimalMax("10.0")
    private Double bloodLipid;

    @NotNull
    private LocalDateTime measurementTime;
}