package cn.polister.infosys.entity.excel;

import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 睡眠记录表(SleepLog)表实体类
 *
 * @author Polister
 */
@Data
public class SleepLogExcel {
    //入睡时间
    @ExcelProperty("入睡时间")
    private Date sleepStart;
    //醒来时间
    @ExcelProperty("醒来时间")
    private Date sleepEnd;
    //睡眠质量（1-5级）
    @ExcelProperty("睡眠质量（1-5级）")
    private Integer sleepQuality;
}
