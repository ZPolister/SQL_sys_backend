package cn.polister.infosys.entity.excel;

import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运动记录表(ExerciseLog)表导出类
 *
 * @author Polister
 */
@Data
public class ExerciseLogExcel {

    //运动类型
    @ExcelProperty("运动类型")
    private String exerciseType;
    //开始时间
    @ExcelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date startTimestamp;
    //持续时间(分钟)
    @ExcelProperty("持续时间(分钟)")
    private Integer durationMinutes;
    //距离(km)
    @ExcelProperty("距离(km)")
    private Double distanceKm;
    //消耗卡路里
    @ExcelProperty("消耗卡路里")
    private Integer caloriesBurned;

}
