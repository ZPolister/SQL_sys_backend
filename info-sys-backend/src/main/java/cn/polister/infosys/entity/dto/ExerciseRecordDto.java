package cn.polister.infosys.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExerciseRecordDto {
    @NotBlank(message = "运动类型不能为空")
    private String type;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Min(1) @Max(1440)
    private Integer duration;

    @DecimalMin("0.1")
    private BigDecimal distance;

    @Min(1)
    private Integer calories;
}