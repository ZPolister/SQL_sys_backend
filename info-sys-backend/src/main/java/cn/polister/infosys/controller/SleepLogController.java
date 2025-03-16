package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.entity.dto.SleepLogDto;
import cn.polister.infosys.service.SleepLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sleep")
@Tag(name = "睡眠记录管理", description = "睡眠数据管理接口")
public class SleepLogController {

    @Resource
    private SleepLogService sleepLogService;

    @Operation(
            summary = "新增睡眠记录",
            description = "需要登录，参数要求：<br>" +
                    "1. 入睡/醒来时间不能为空且需早于当前时间<br>" +
                    "2. 醒来时间需晚于入睡时间<br>" +
                    "3. 睡眠质量等级1-5",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = SleepLogDto.class))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "记录创建成功"),
            @ApiResponse(responseCode = "400", description = "参数校验失败"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @PostMapping
    public ResponseResult<Long> createLog(@RequestBody SleepLogDto dto) {
        return sleepLogService.createSleepLog(dto);
    }

    @Operation(
            summary = "删除睡眠记录",
            description = "根据记录ID删除指定记录",
            parameters = @Parameter(name = "logId", description = "记录ID", example = "123")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "400", description = "记录不存在/无权限"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @DeleteMapping("/{logId}")
    public ResponseResult<Void> deleteLog(@PathVariable Long logId) {
        return sleepLogService.deleteSleepLog(logId);
    }

    @Operation(
            summary = "分页查询睡眠记录",
            description = "支持按入睡时间范围筛选",
            parameters = {
                    @Parameter(name = "startDate", description = "开始日期(yyyy-MM-dd)", example = "2024-03-01"),
                    @Parameter(name = "endDate", description = "结束日期(yyyy-MM-dd)", example = "2024-03-31"),
                    @Parameter(name = "pageNum", description = "页码（默认1）", example = "1"),
                    @Parameter(name = "pageSize", description = "每页数量（默认10）", example = "20")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/page")
    public ResponseResult<Page<SleepLog>> getLogs(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        return ResponseResult.okResult(sleepLogService.getSleepLogs(startDate, endDate, pageNum, pageSize));
    }

    @SaCheckLogin
    @GetMapping("/latest")
    @Operation(summary = "获取最新一次睡眠数据")
    public ResponseResult<SleepLog> getLatest() {
        return ResponseResult.okResult(sleepLogService.getLatestRecord());
    }

    @Operation(
            summary = "获取每日睡眠时长数据",
            description = "返回指定时间范围内每天的睡眠时长数据，用于生成柱状图，所对应的地址：https://echarts.apache.org/examples/zh/editor.html?c=bar-tick-align",
            parameters = {
                    @Parameter(name = "range", description = "时间范围", example = "week",
                              schema = @Schema(type = "string", allowableValues = {"week", "month", "3months", "6months"}))
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(example = "{\"dates\":[\"2024-05-01\",\"2024-05-02\"],\"durations\":[7.5,8.2]}"))),
            @ApiResponse(responseCode = "400", description = "参数无效"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/duration")
    public ResponseResult<Map<String, Object>> getSleepDurationByDays(
            @RequestParam(defaultValue = "week") String range) {
        return sleepLogService.getSleepDurationByDays(range);
    }
}