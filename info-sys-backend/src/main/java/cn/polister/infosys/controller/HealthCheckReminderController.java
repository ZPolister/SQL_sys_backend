package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.entity.HealthCheckReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.HealthCheckReminderDto;
import cn.polister.infosys.service.HealthCheckReminderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@RequestMapping("/health-check-reminder")
@Tag(name = "体检提醒管理", description = "体检提醒相关接口")
public class HealthCheckReminderController {

    @Resource
    private HealthCheckReminderService healthCheckReminderService;

    @PostMapping
    @Operation(summary = "创建体检提醒", description = "为当前登录用户创建新的体检提醒")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<Void> createReminder(
            @Parameter(description = "体检提醒信息", required = true)
            @RequestBody HealthCheckReminderDto checkReminderDto) {
        long userId = StpUtil.getLoginIdAsLong();
        HealthCheckReminder reminder = BeanUtil.toBean(checkReminderDto, HealthCheckReminder.class);
        reminder.setAccountId(userId);
        return healthCheckReminderService.createReminder(reminder);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新体检提醒", description = "更新指定ID的体检提醒信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "提醒不存在或无权限"),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<Void> updateReminder(
            @Parameter(description = "体检提醒信息", required = true)
            @RequestBody HealthCheckReminderDto checkReminderDto,
            @Parameter(description = "提醒ID", required = true, example = "1")
            @PathVariable Long id) {
        HealthCheckReminder reminder = BeanUtil.toBean(checkReminderDto, HealthCheckReminder.class);
        long userId = StpUtil.getLoginIdAsLong();
        reminder.setAccountId(userId);
        reminder.setReminderId(id);
        return healthCheckReminderService.updateReminder(reminder);
    }

    @DeleteMapping("/{reminderId}")
    @Operation(summary = "删除体检提醒", description = "删除指定ID的体检提醒")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "提醒不存在或无权限"),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<Void> deleteReminder(
            @Parameter(description = "提醒ID", required = true, example = "1")
            @PathVariable Long reminderId) {
        return healthCheckReminderService.deleteReminder(reminderId);
    }

    @GetMapping("/list")
    @Operation(summary = "获取体检提醒列表", description = "分页获取当前用户的体检提醒列表，可按日期范围筛选")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(schema = @Schema(implementation = ResponseResult.class))),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<Page<HealthCheckReminder>> getReminders(
            @Parameter(description = "开始日期(yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "结束日期(yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @Parameter(description = "页码，默认1", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量，默认10", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.okResult(healthCheckReminderService.getReminderList(startDate, endDate, pageNum, pageSize));
    }

    @GetMapping
    @Operation(summary = "获取最近一次体检提醒信息", description = "获取当前用户最近一次未完成的体检提醒")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(schema = @Schema(implementation = HealthCheckReminderDto.class))),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<HealthCheckReminderDto> getReminder() {
        HealthCheckReminder reminder = healthCheckReminderService.getOne(new LambdaQueryWrapper<HealthCheckReminder>()
                .eq(HealthCheckReminder::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByAsc(HealthCheckReminder::getScheduledTime)
                .eq(HealthCheckReminder::getCompletionStatus, 0)
                .last("limit 1"));
        return ResponseResult.okResult(BeanUtil.toBean(reminder, HealthCheckReminderDto.class));
    }

    @GetMapping("/confirm")
    @Operation(summary = "确认体检提醒", description = "通过令牌确认完成体检")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "确认成功"),
        @ApiResponse(responseCode = "400", description = "令牌无效或已过期")
    })
    public ResponseResult<Void> confirmReminder(
            @Parameter(description = "确认令牌", required = true, example = "abc123def456")
            @RequestParam String token) {
        return healthCheckReminderService.confirmReminder(token);
    }

    @GetMapping("/test")
    @Operation(summary = "测试处理提醒", description = "触发体检提醒处理逻辑（仅用于测试）")
    @ApiResponse(responseCode = "200", description = "处理成功")
    public ResponseResult<Void> test() {
        healthCheckReminderService.processReminders();
        return ResponseResult.okResult();
    }
}