package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.entity.vo.MedicationReminderVo;
import cn.polister.infosys.service.MedicationReminderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/medication-reminder")
@Tag(name = "服药提醒管理", description = "服药提醒相关接口")
public class MedicationReminderController {

    @Resource
    private MedicationReminderService medicationReminderService;

    @PostMapping
    @Operation(summary = "创建服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> createReminder(@RequestBody MedicationReminderDto reminder) {
        MedicationReminder medicationReminder = BeanUtil.toBean(reminder, MedicationReminder.class);
        medicationReminder.setAccountId(StpUtil.getLoginIdAsLong());
        if (Objects.isNull(medicationReminder.getStartTime())) {
            medicationReminder.setStartTime(new Date());
        }
        return medicationReminderService.createReminder(medicationReminder);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> createReminders(@RequestBody List<MedicationReminderDto> reminders) {
            for (MedicationReminderDto reminderDto : reminders) {
            MedicationReminder medicationReminder = BeanUtil.toBean(reminderDto, MedicationReminder.class);
            medicationReminder.setAccountId(StpUtil.getLoginIdAsLong());
            if (Objects.isNull(medicationReminder.getStartTime())) {
                medicationReminder.setStartTime(new Date());
            }
            medicationReminderService.createReminder(medicationReminder);
        }
        return ResponseResult.okResult(null);
    }

    @PutMapping("/{reminderId}")
    @Operation(summary = "更新服药提醒")
    @SaCheckLogin
    public ResponseResult<Void> updateReminder(@PathVariable Long reminderId, @RequestBody MedicationReminderDto reminderDto) {
        MedicationReminder reminder = BeanUtil.toBean(reminderDto, MedicationReminder.class);
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

    @GetMapping("/next")
    @Operation(summary = "获取下次最近服药时间的提醒")
    @SaCheckLogin
    public ResponseResult<List<MedicationReminder>> getNextReminders() {
        return ResponseResult.okResult(medicationReminderService.getNextReminders(StpUtil.getLoginIdAsLong()));
    }

    @PostMapping("/png")
    @Operation(summary = "通过图片识别服药信息")
    @SaCheckLogin
    public ResponseResult<List<MedicationReminderVo>> getInfoByPng(@RequestParam MultipartFile file) {
        return ResponseResult.okResult(medicationReminderService.getInfoByPng(file));
    }

}