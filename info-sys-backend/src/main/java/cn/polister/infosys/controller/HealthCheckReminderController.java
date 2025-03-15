package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.HealthCheckReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.service.HealthCheckReminderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/health-check-reminder")
@Tag(name = "体检提醒管理", description = "体检提醒相关接口")
public class HealthCheckReminderController {

    @Resource
    private HealthCheckReminderService healthCheckReminderService;

    @PostMapping
    @Operation(summary = "创建体检提醒")
    @SaCheckLogin
    public ResponseResult<Void> createReminder(@RequestBody HealthCheckReminder reminder) {
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        return healthCheckReminderService.createReminder(reminder);
    }

    @PutMapping("/{reminderId}")
    @Operation(summary = "更新体检提醒")
    @SaCheckLogin
    public ResponseResult<Void> updateReminder(@PathVariable Long reminderId, @RequestBody HealthCheckReminder reminder) {
        reminder.setReminderId(reminderId);
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        return healthCheckReminderService.updateReminder(reminder);
    }

    @DeleteMapping("/{reminderId}")
    @Operation(summary = "删除体检提醒")
    @SaCheckLogin
    public ResponseResult<Void> deleteReminder(@PathVariable Long reminderId) {
        return healthCheckReminderService.deleteReminder(reminderId);
    }

    @GetMapping
    @Operation(summary = "获取体检提醒列表")
    @SaCheckLogin
    public ResponseResult<Page<HealthCheckReminder>> getReminders(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.okResult(healthCheckReminderService.getReminderList(startDate, endDate, pageNum, pageSize));
    }

    @GetMapping("/confirm")
    @Operation(summary = "确认体检提醒")
    public ResponseResult<Void> confirmReminder(@RequestParam String token) {
        return healthCheckReminderService.confirmReminder(token);
    }
}