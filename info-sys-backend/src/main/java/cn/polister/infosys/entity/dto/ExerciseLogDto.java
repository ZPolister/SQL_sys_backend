package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

// 1. 调整后的DTO类（带OpenAPI文档注释）
@Data
@Schema(description = "运动记录请求参数")
public class ExerciseLogDto {
    @Schema(
            description = "运动类型",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "跑步",
            minLength = 1
    )
    private String exerciseType;

    @Schema(
            description = "开始时间（时间戳格式）",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1741096349064"
    )
    private Date startTimestamp;

    @Schema(
            description = "持续时间（分钟）",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1",
            example = "30"
    )
    private Integer durationMinutes;

    @Schema(
            description = "运动距离（千米）",
            minimum = "0.0",
            example = "5.2"
    )
    private Double distanceKm;

    @Schema(
            description = "消耗卡路里（大卡）",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "1",
            example = "300"
    )
    private Integer caloriesBurned;
}