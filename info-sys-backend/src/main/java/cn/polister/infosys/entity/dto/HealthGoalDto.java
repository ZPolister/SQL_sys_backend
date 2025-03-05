package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

// 2. 目标DTO
@Data
@Schema(description = "健康目标请求参数")
public class HealthGoalDto {
    @Schema(description = "目标类别（WEIGHT_LOSS, BLOOD_SUGAR, BLOOD_LIPID, EXERCISE_CALORIES）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goalCategory;

    @Schema(description = "目标值", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double targetValue;

    @Schema(description = "目标日期（yyyy-MM-dd）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date targetDate;
}