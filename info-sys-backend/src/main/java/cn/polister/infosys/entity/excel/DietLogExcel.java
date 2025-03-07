package cn.polister.infosys.entity.excel;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DietLogExcel {
    //食物名称
    @ExcelProperty("食物名称")
    private String foodItem;
    //数量(克)
    @ExcelProperty("数量(克)")
    private Double quantityGrams;
    //总热量(kcal)
    @ExcelProperty("总热量(kcal)")
    private Integer totalCalories;
    //食用时间
    @ExcelProperty("食用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date consumptionTime;
}
