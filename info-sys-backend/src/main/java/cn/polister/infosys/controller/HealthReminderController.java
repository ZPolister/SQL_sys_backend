package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.HealthReminder;
import cn.polister.infosys.entity.MedicationReminderTime;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.HealthCheckReminderDto;
import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.service.HealthReminderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * 健康提醒表(HealthReminder)表控制层
 */
@RestController
@RequestMapping("/reminder")
@Deprecated
@Tag(name = "健康提醒管理", description = "管理用户的健康提醒，包括药品提醒和健康体检提醒")
public class HealthReminderController {

    @Resource
    private HealthReminderService healthReminderService;

    @Operation(summary = "创建药品提醒", description = "创建一个新的药品提醒")
    @ApiResponse(responseCode = "200", description = "提醒创建成功")
    @PostMapping("/medication")
    @SaCheckLogin
    public ResponseResult<Void> createMedicationReminder(
            @RequestBody @Parameter(description = "药品提醒信息", required = true) MedicationReminderDto dto
    ) {
        HealthReminder reminder = new HealthReminder();
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        reminder.setReminderContent(dto.getReminderContent());
        reminder.setMedicationName(dto.getMedicationName());
        reminder.setMedicationDosage(dto.getMedicationDosage());
        reminder.setMedicationFrequency(dto.getMedicationFrequency());
        reminder.setMedicationDuration(dto.getMedicationDuration());

        List<MedicationReminderTime> reminderTimes = new ArrayList<>();
        for (Time time : dto.getReminderTimes()) {
            MedicationReminderTime reminderTime = new MedicationReminderTime();
            reminderTime.setReminderTime(time);
            reminderTimes.add(reminderTime);
        }
        reminder.setReminderTimes(reminderTimes);

        return healthReminderService.createMedicationReminder(reminder);
    }

    @Operation(summary = "创建健康体检提醒", description = "创建一个新的健康体检提醒")
    @ApiResponse(responseCode = "200", description = "提醒创建成功")
    @PostMapping("/health-check")
    @SaCheckLogin
    public ResponseResult<Void> createHealthCheckReminder(
            @RequestBody @Parameter(description = "健康体检提醒信息", required = true) HealthCheckReminderDto dto
    ) {
        HealthReminder reminder = new HealthReminder();
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        reminder.setReminderContent(dto.getReminderContent());
        reminder.setScheduledTime(dto.getScheduledTime());

        return healthReminderService.createHealthCheckReminder(reminder);
    }

    @Operation(summary = "确认提醒", description = "通过提供的token确认提醒")
    @ApiResponse(responseCode = "200", description = "提醒确认成功")
    @GetMapping("/confirm")
    public ResponseResult<Void> confirmReminder(
            @RequestParam @Parameter(description = "确认token", required = true) String token
    ) {
        return healthReminderService.confirmReminder(token);
    }

    @Operation(summary = "获取用户的所有提醒", description = "获取当前登录用户的所有健康提醒")
    @ApiResponse(responseCode = "200", description = "成功获取提醒列表",
            content = @Content(schema = @Schema(implementation = HealthReminder.class)))
    @GetMapping("/list")
    @SaCheckLogin
    public ResponseResult<List<HealthReminder>> getReminders() {
        Long accountId = StpUtil.getLoginIdAsLong();
        return ResponseResult.okResult(healthReminderService.list(
                new LambdaQueryWrapper<HealthReminder>()
                        .eq(HealthReminder::getAccountId, accountId)
                        .orderByDesc(HealthReminder::getCreatedAt)
        ));
    }
}