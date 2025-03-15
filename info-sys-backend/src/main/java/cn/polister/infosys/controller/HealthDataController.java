package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.aspect.UpdateTargetBiometric;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.BiometricRecordDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.service.BiometricRecordService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@Tag(name = "体征数据管理", description = "体征数据相关接口")
@RequestMapping("/api/health")
public class HealthDataController {

    @Resource
    private BiometricRecordService biometricRecordService;

    @PostMapping("/biometric")
    @Operation(summary = "创建生物特征记录", description = "需要登录，参数需符合验证规则")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "记录创建成功"),
            @ApiResponse(responseCode = "400", description = "参数校验失败"),
            @ApiResponse(responseCode = "401", description = "需要登录后操作")
    })
    @UpdateTargetBiometric
    public ResponseResult<Long> recordBiometric(
            @Valid @RequestBody BiometricRecordDto recordDto) {

        // 校验
        if (recordDto.getHeightCm() != null && recordDto.getHeightCm() < 50.0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "身高最小50cm");
        }
        if (recordDto.getWeightKg() != null && recordDto.getWeightKg() < 2.5) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "体重最小2.5kg");
        }
        if (recordDto.getSystolicPressure() != null &&
                (recordDto.getSystolicPressure() < 60 || recordDto.getSystolicPressure() > 250)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "收缩压范围60-250mmHg");
        }
        if (recordDto.getDiastolicPressure() != null &&
                ( recordDto.getDiastolicPressure() < 40 || recordDto.getDiastolicPressure() > 150)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "舒张压范围40-150mmHg");
        }
        if (recordDto.getSystolicPressure() == null || recordDto.getDiastolicPressure() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "必须同时填入收缩压和舒张压");
        }
        if (recordDto.getSystolicPressure() <= recordDto.getDiastolicPressure()) {
            return ResponseResult.errorResult(
                    400,
                    "收缩压必须大于舒张压"
            );
        }
        if (recordDto.getBloodGlucose() != null
                && (recordDto.getBloodGlucose() < 2.0 || recordDto.getBloodGlucose() > 30.0)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "血糖范围2.0-30.0mmol/L");
        }
        if (recordDto.getBloodLipid() != null
                && (recordDto.getBloodLipid() < 0.5 || recordDto.getBloodLipid() > 10.0)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "血脂范围0.5-10.0mmol/L");
        }
        if (recordDto.getMeasurementTime() == null) {
            recordDto.setMeasurementTime(LocalDateTime.now());
        }

        BiometricRecord record = BeanUtil.toBean(recordDto, BiometricRecord.class);
        record.setAccountId(StpUtil.getLoginIdAsLong());

        biometricRecordService.save(record);
        return ResponseResult.okResult(record.getRecordId());
    }

    @DeleteMapping("/biometric/{id}")
    @Operation(summary = "删除生物特征记录", description = "需要登录，id为对应的记录id")
    @UpdateTargetBiometric
    public ResponseResult<Void> deleteBiometric(@PathVariable Long id) {
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

    @Operation(summary = "分页获取生物特征记录", description = "获取当前用户的生物特征记录，支持日期范围筛选")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @GetMapping("/records")
    public ResponseResult<Page<BiometricRecord>> getBiometricRecords(
            @io.swagger.v3.oas.annotations.Parameter(description = "开始日期(yyyy-MM-dd)", example = "2023-01-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,

            @io.swagger.v3.oas.annotations.Parameter(description = "结束日期(yyyy-MM-dd)", example = "2023-12-31")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,

            @io.swagger.v3.oas.annotations.Parameter(description = "页码，默认1", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,

            @io.swagger.v3.oas.annotations.Parameter(description = "每页数量，默认10", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BiometricRecord> page = biometricRecordService.getBiometricRecordsWithDateRange(
                startTime, endTime, pageNum, pageSize
        );
        return ResponseResult.okResult(page);
    }

    @Operation(summary = "获取最新一次生物特征记录", description = "获取当前用户最新的一条生物特征记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "401", description = "未登录访问"),
    })
    @GetMapping("/latest")
    @SaCheckLogin
    public ResponseResult<BiometricRecord> getLatestRecord() {
        return ResponseResult.okResult(biometricRecordService.getLatestBiometricRecord(StpUtil.getLoginIdAsLong()));
    }
}