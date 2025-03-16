package cn.polister.infosys.entity.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Schema(description = "生物特征图表数据DTO")
public class BiometricChartDataDto {
    @Schema(description = "日期列表", example = "['2023-01-01', '2023-01-02', ...]")
    private List<String> dates;

    @Schema(description = "体重列表 (kg)", example = "[70.5, 71.0, ...]")
    private List<Double> weights;

    @Schema(description = "收缩压列表 (mmHg)", example = "[120, 118, ...]")
    private List<Integer> systolicPressures;

    @Schema(description = "舒张压列表 (mmHg)", example = "[80, 78, ...]")
    private List<Integer> diastolicPressures;

    @Schema(description = "血糖列表 (mmol/L)", example = "[5.5, 5.6, ...]")
    private List<Double> bloodGlucoses;

    @Schema(description = "血脂列表 (mmol/L)", example = "[4.5, 4.6, ...]")
    private List<Double> bloodLipids;

    @Schema(description = "BMI列表", example = "[22.5, 22.6, ...]")
    private List<Double> bmis;
}