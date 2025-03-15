package cn.polister.infosys.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服药提醒数据传输对象")
public class MedicationReminderDto {
    @Schema(description = "药品名称", example = "阿司匹林", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medicationName;

    @Schema(description = "药品剂量", example = "100mg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medicationDosage;

    @Schema(description = "每日服药次数", example = "3", minimum = "1", maximum = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer medicationFrequency;

    @Schema(description = "服药持续天数", example = "7", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer medicationDuration;

    @Schema(description = "提醒内容", example = "请记得按时服用药物，注意饭后服用")
    private String reminderContent;

    @Schema(description = "提醒时间（格式：[HH:mm]）", example = "[08:00, 20:00]", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reminderTime;

    @Schema(description = "开始服药时间", example = "2023-07-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startTime;
}