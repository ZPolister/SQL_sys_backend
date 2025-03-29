package cn.polister.infosys.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationReminderVo {
    //药品名称
    private String medicationName;
    //服药剂量
    private String medicationDosage;
    //每日服药次数
    private Integer medicationFrequency;
    //服药天数
    private Integer medicationDuration;
}
