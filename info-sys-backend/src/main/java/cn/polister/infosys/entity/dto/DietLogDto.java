package cn.polister.infosys.entity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "饮食记录请求参数")
public class DietLogDto {
    @Schema(description = "食物名称", requiredMode = REQUIRED, example = "苹果")
    private String foodItem;

    @Schema(description = "食用量（克）", minimum = "1", example = "150.0")
    private Double quantityGrams;

    @Schema(description = "总热量（大卡）", requiredMode = REQUIRED, minimum = "1", example = "80")
    private Integer totalCalories;

    @Schema(description = "时间戳格式", example = "1741096349064")
    private Date consumptionTime;
}