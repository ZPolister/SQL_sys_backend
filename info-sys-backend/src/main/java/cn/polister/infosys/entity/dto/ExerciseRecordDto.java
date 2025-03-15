package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "运动记录数据传输对象")
public class ExerciseRecordDto {
    @NotBlank(message = "运动类型不能为空")
    @Schema(description = "运动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "跑步")
    private String type;

    @NotNull
    @FutureOrPresent
    @Schema(description = "开始时间（时间戳）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1742054040105")
    private LocalDateTime startTime;

    @NotNull
    @Min(1) @Max(1440)
    @Schema(description = "持续时间（分钟）", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "1440", example = "60")
    private Integer duration;

    @DecimalMin("0.1")
    @Schema(description = "运动距离（千米）", minimum = "0.1", example = "5.5")
    private BigDecimal distance;

    @Min(1)
    @Schema(description = "消耗卡路里（大卡）", minimum = "1", example = "350")
    private Integer calories;
}