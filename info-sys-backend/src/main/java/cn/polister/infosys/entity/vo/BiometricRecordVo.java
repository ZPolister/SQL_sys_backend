package cn.polister.infosys.entity.vo;

import cn.polister.infosys.entity.BiometricRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生物特征记录视图对象，包含计算的BMI和各项指标评估")
public class BiometricRecordVo extends BiometricRecord {
    /**
     * BMI值
     */
    @Schema(description = "BMI值", example = "22.5")
    private Double bmi;

    /**
     * BMI分级
     */
    @Schema(description = "BMI分级", example = "正常", allowableValues = {"偏瘦", "正常", "超重", "肥胖"})
    private String bmiLevel;

    /**
     * 血压分级
     */
    @Schema(description = "血压分级", example = "正常",
            allowableValues = {"正常", "正常偏高", "轻度高血压前期", "1级高血压", "2级高血压", "3级高血压"})
    private String bloodPressureLevel;

    /**
     * 血糖状态
     */
    @Schema(description = "血糖状态", example = "正常",
            allowableValues = {"低血糖", "正常", "糖耐量受损", "血糖偏高"})
    private String bloodGlucoseLevel;

    /**
     * 血脂状态
     */
    @Schema(description = "血脂状态", example = "正常",
            allowableValues = {"正常", "边缘升高", "升高"})
    private String bloodLipidLevel;
}