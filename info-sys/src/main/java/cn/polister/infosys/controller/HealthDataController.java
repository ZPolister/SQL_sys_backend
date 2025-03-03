package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.BiometricRecordDto;
import cn.polister.infosys.entity.dto.ExerciseRecordDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.service.BiometricRecordService;
import cn.polister.infosys.service.ExerciseLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/health")
public class HealthDataController {

    @Resource
    private BiometricRecordService biometricRecordService;


    @Resource
    private ExerciseLogService exerciseLogService;

    @PostMapping("/biometric")
    public ResponseResult recordBiometric(
            @Valid @RequestBody BiometricRecordDto dto) {

        // 校验
        if (dto.getSystolic() <= dto.getDiastolic()) {
            return ResponseResult.errorResult(
                    400,
                    "收缩压必须大于舒张压"
            );
        }

        BiometricRecord record = new BiometricRecord();
        BeanUtils.copyProperties(dto, record);
        record.setAccountId(StpUtil.getLoginIdAsLong());

        biometricRecordService.save(record);
        return ResponseResult.okResult(record.getRecordId());
    }

    @DeleteMapping("/biometric/{id}")
    public ResponseResult deleteBiometric(@PathVariable Long id) {
        BiometricRecord record = biometricRecordService.getById(id);

        if (record == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        if (!record.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }

        biometricRecordService.removeById(id);
        return ResponseResult.okResult();
    }

    @PostMapping("/exercise")
    public ResponseResult recordExercise(
            @Valid @RequestBody ExerciseRecordDto dto) {

        ExerciseLog record = new ExerciseLog();
        BeanUtils.copyProperties(dto, record);
        record.setAccountId(StpUtil.getLoginIdAsLong());

        exerciseLogService.save(record);
        return ResponseResult.okResult(record.getLogId());
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseResult deleteExercise(@PathVariable Long id) {
        ExerciseLog record = exerciseLogService.getById(id);

        if(record == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        if(!record.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }

        exerciseLogService.removeById(id);
        return ResponseResult.okResult();
    }

    // 分页获取带日期筛选
    @SaCheckLogin
    @GetMapping("/records")
    public ResponseResult getRecords(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BiometricRecord> page = biometricRecordService.getRecordsWithDateRange(
                startTime, endTime, pageNum, pageSize
        );
        return ResponseResult.okResult(page);
    }
}