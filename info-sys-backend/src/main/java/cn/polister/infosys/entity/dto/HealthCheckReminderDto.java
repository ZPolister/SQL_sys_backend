package cn.polister.infosys.entity.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 体检提醒数据传输对象
 * <p>
 * 该类用于在前端和后端之间传输体检提醒相关的数据。
 * 包含了体检频率、计划时间和提醒内容等信息。
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "体检提醒DTO")
public class HealthCheckReminderDto {

    /**
     * 体检频率，以天为单位
     * <p>
     * 表示多少天进行一次体检。例如，180表示每半年进行一次体检。
     * </p>
     */
    @Schema(description = "体检频率（天）", example = "180")
    private Integer checkFrequencyDays;

    /**
     * 计划的体检时间
     * <p>
     * 表示下一次体检的预定日期。
     * </p>
     */
    @Schema(description = "体检时间", example = "2023-07-01")
    private Date scheduledTime;

    /**
     * 体检提醒的具体内容
     * <p>
     * 包含发送给用户的提醒消息。可以包括体检项目、注意事项等信息。
     * </p>
     */
    @Schema(description = "提醒内容", example = "请记得进行半年一次的常规体检")
    private String reminderContent;
}