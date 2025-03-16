package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.polister.infosys.aspect.UpdateTargetExercise;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.ExerciseLogDto;
import cn.polister.infosys.service.ExerciseLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/exercise")
@Tag(name = "运动记录管理", description = "运动记录相关操作")
public class ExerciseLogController {

    @Operation(
            summary = "获取每日运动消耗热量统计",
            description = "按时间范围获取每日运动消耗热量统计数据，返回格式符合ECharts图表要求"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取统计数据",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                    {
                                      "code": 200,
                                      "msg": "操作成功",
                                      "data": {
                                        "xAxis": ["2024-05-01", "2024-05-02", "2024-05-03", "2024-05-04", "2024-05-05", "2024-05-06", "2024-05-07"],
                                        "series": [120, 150, 0, 200, 180, 250, 300]
                                      }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "无效的时间范围参数",
                    content = @Content(schema = @Schema(implementation = ResponseResult.class))
            ),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/daily-calories-burned")
    public ResponseResult<Map<String, Object>> getDailyCaloriesBurned(
            @Parameter(
                    description = "统计范围类型：WEEK(一周)、MONTH(一月)、THREE_MONTHS(三个月)、HALF_YEAR(半年)",
                    schema = @Schema(
                            type = "string",
                            allowableValues = {"WEEK", "MONTH", "THREE_MONTHS", "HALF_YEAR"}
                    ),
                    example = "WEEK",
                    required = true
            )
            @RequestParam String range) {
        List<Map<String, Object>> dailyData = exerciseLogService.getDailyCaloriesBurned(range);

        // 转换为ECharts格式
        List<String> xAxis = new ArrayList<>();
        List<Integer> series = new ArrayList<>();

        for (Map<String, Object> dayData : dailyData) {
            xAxis.add((String) dayData.get("date"));
            series.add((Integer) dayData.get("value"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("date", xAxis);
        result.put("values", series);

        return ResponseResult.okResult(result);
    }

    @Resource
    private ExerciseLogService exerciseLogService;

    @Operation(
            summary = "创建运动记录",
            description = "需要登录，参数需满足以下要求：<br>"
                    + "1. 运动类型必填<br>"
                    + "2. 开始时间不能晚于当前时间<br>"
                    + "3. 持续时间至少1分钟<br>"
                    + "4. 卡路里消耗至少1大卡",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ExerciseLogDto.class))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "记录创建成功"),
            @ApiResponse(
                    responseCode = "400",
                    description = "参数校验失败",
                    content = @Content(schema = @Schema(implementation = ResponseResult.class))
            ),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @PostMapping
    @UpdateTargetExercise
    public ResponseResult<Long> createLog(@Valid @RequestBody ExerciseLogDto dto) {
        return exerciseLogService.createExerciseLog(dto);
    }

    @Operation(summary = "删除运动记录")
    @SaCheckLogin
    @UpdateTargetExercise
    @DeleteMapping("/{logId}")
    public ResponseResult<Void> deleteLog(@PathVariable Long logId) {
        exerciseLogService.deleteExerciseLog(logId);
        return ResponseResult.okResult();
    }

    @Operation(summary = "分页查询运动记录")
    @SaCheckLogin
    @GetMapping
    public ResponseResult<Page<ExerciseLog>> getLogs(
            @Parameter(description = "开始时间(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,

            @Parameter(description = "结束时间(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,

            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<ExerciseLog> page = exerciseLogService.getExerciseLogs(startDate, endDate, pageNum, pageSize);
        return ResponseResult.okResult(page);
    }

    @Operation(summary = "获取最新的运动记录")
    @SaCheckLogin
    @GetMapping("/latest")
    public ResponseResult<ExerciseLog> getLatestLog() {
        return ResponseResult.okResult(exerciseLogService.getLatestLog());
    }
}
