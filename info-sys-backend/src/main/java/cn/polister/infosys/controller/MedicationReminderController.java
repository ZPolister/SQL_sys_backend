package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.service.MedicationReminderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/medication-reminder")
@Tag(name = "服药提醒管理", description = "服药提醒相关接口")
public class MedicationReminderController {

    @Resource
    private MedicationReminderService medicationReminderService;

    @PostMapping
    @Operation(summary = "创建服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> createReminder(@RequestBody MedicationReminder reminder) {
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        return medicationReminderService.createReminder(reminder);
    }

    @PutMapping("/{reminderId}")
    @Operation(summary = "更新服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> updateReminder(@PathVariable Long reminderId, @RequestBody MedicationReminder reminder) {
        reminder.setReminderId(reminderId);
        reminder.setAccountId(StpUtil.getLoginIdAsLong());
        return medicationReminderService.updateReminder(reminder);
    }

    @DeleteMapping("/{reminderId}")
    @Operation(summary = "删除服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> deleteReminder(@PathVariable Long reminderId) {
        return medicationReminderService.deleteReminder(reminderId);
    }

    @GetMapping
    @Operation(summary = "获取服药提醒列表")
    @SaCheckLogin
    public ResponseResult<Page<MedicationReminder>> getReminders(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.okResult(medicationReminderService.getReminderList(startDate, endDate, pageNum, pageSize));
    }
}