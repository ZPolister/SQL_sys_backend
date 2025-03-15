package cn.polister.infosys.entity.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationReminderDto {
    private String medicationName;
    private String medicationDosage;
    private Integer medicationFrequency;
    private Integer medicationDuration;
    private String reminderContent;
    private List<Time> reminderTimes;
}