package cn.polister.infosys.entity.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthCheckReminderDto {
    private Integer checkFrequencyDays;
    private Date scheduledTime;
    private String reminderContent;
}