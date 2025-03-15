package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.PageResult;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.DietLogDto;
import cn.polister.infosys.service.DietLogService;
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

@RestController
@RequestMapping("/diet")
@Tag(name = "饮食记录管理", description = "饮食数据管理接口")
public class DietLogController {

    @Resource
    private DietLogService dietLogService;

    @Operation(summary = "新增饮食记录", description = "需要登录，记录饮食信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "创建成功",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "参数校验失败"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @PostMapping
    public ResponseResult<Long> createLog(@RequestBody DietLogDto dto) {
        return dietLogService.createDietLog(dto);
    }

    @Operation(summary = "删除饮食记录", description = "根据记录ID删除指定记录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "400", description = "记录不存在/无权限"),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @DeleteMapping("/{logId}")
    public ResponseResult<Void> deleteLog(@PathVariable @Parameter(description = "记录ID", example = "123") Long logId) {
        return dietLogService.deleteDietLog(logId);
    }

    @Operation(summary = "分页查询饮食记录", description = "支持按日期范围筛选")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "查询成功",
                    content = @Content(schema = @Schema(implementation = PageResult.class))),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/page")
    public ResponseResult<Page<DietLog>> getLogs(
            @Parameter(description = "开始日期(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,

            @Parameter(description = "结束日期(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

            @Parameter(description = "页码，默认1", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,

            @Parameter(description = "每页数量，默认10", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        return ResponseResult.okResult(dietLogService.getDietLogs(startDate, endDate, pageNum, pageSize));
    }

    @Operation(summary = "获取今天的饮食热量", description = "返回当前用户今天的总饮食热量")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功获取今天的饮食热量",
                    content = @Content(schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/hot_today")
    public ResponseResult<Double> getHotToday() {
        return ResponseResult.okResult(dietLogService.getHotToday());
    }
}