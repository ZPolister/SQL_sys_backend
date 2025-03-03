package cn.polister.infosys.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BiometricRecordDto {
    @DecimalMin(value = "50.0", message = "身高最小50cm")
    private BigDecimal height;

    @DecimalMin(value = "2.5", message = "体重最小2.5kg")
    private BigDecimal weight;

    @NotNull(message = "收缩压不能为空")
    @Min(60) @Max(250)
    private Integer systolic;

    @NotNull(message = "舒张压不能为空")
    @Min(40) @Max(150)
    private Integer diastolic;

    @DecimalMin("2.0") @DecimalMax("30.0")
    private BigDecimal bloodSugar;

    @DecimalMin("0.5") @DecimalMax("10.0")
    private BigDecimal bloodLipid;

    @NotNull
    private LocalDateTime measurementTime;
}