package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "睡眠记录请求参数")
public class SleepLogDto {
    @Schema(description = "入睡时间（时间戳格式）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-15T22:00:00")
    private Date sleepStart;

    @Schema(description = "醒来时间（时间戳格式）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-03-16T06:30:00")
    private Date sleepEnd;

    @Schema(description = "睡眠质量（1-5级）", minimum = "1", maximum = "5", example = "4")
    private Integer sleepQuality;
}