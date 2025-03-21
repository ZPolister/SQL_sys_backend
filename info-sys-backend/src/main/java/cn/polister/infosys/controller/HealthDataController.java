package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.aspect.UpdateTargetBiometric;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.BiometricChartDataDto;
import cn.polister.infosys.entity.dto.BiometricRecordDto;
import cn.polister.infosys.entity.vo.BiometricRecordVo;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "体征数据管理", description = "体征数据相关接口")
@RequestMapping("/health")
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
    public ResponseResult<Page<BiometricRecordVo>> getBiometricRecords(
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
        // 转换为VO并计算额外信息
        Page<BiometricRecordVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(this::convertToVo).toList());
        return ResponseResult.okResult(voPage);
    }

    @Operation(summary = "获取最新一次生物特征记录", description = "获取当前用户最新的一条生物特征记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "401", description = "未登录访问"),
    })
    @GetMapping("/latest")
    @SaCheckLogin
    public ResponseResult<BiometricRecordVo> getLatestRecord() {
        BiometricRecord record = biometricRecordService.getLatestBiometricRecord(StpUtil.getLoginIdAsLong());
        return ResponseResult.okResult(record != null ? convertToVo(record) : null);
    }

    /**
     * 将BiometricRecord转换为BiometricRecordVo，并计算额外信息
     */
    private BiometricRecordVo convertToVo(BiometricRecord record) {
        if (record == null) {
            return null;
        }

        BiometricRecordVo vo = BeanUtil.toBean(record, BiometricRecordVo.class);

        // 计算BMI
        if (record.getHeightCm() != null && record.getWeightKg() != null && record.getHeightCm() > 0) {
            double heightInMeters = record.getHeightCm() / 100.0;
            vo.setBmi(record.getWeightKg() / (heightInMeters * heightInMeters));
            vo.setBmiLevel(getBmiLevel(vo.getBmi()));
        }

        // 评估血压
        if (record.getSystolicPressure() != null && record.getDiastolicPressure() != null) {
            vo.setBloodPressureLevel(getBloodPressureLevel(
                record.getSystolicPressure(), record.getDiastolicPressure()));
        }

        // 评估血糖
        if (record.getBloodGlucose() != null) {
            vo.setBloodGlucoseLevel(getBloodGlucoseLevel(record.getBloodGlucose()));
        }

        // 评估血脂
        if (record.getBloodLipid() != null) {
            vo.setBloodLipidLevel(getBloodLipidLevel(record.getBloodLipid()));
        }

        return vo;
    }

    /**
     * 获取BMI等级
     */
    private String getBmiLevel(double bmi) {
        if (bmi < 18.5) {
            return "偏瘦";
        } else if (bmi < 24.0) {
            return "正常";
        } else if (bmi < 28.0) {
            return "超重";
        } else {
            return "肥胖";
        }
    }

    /**
     * 获取血压等级
     */
    private String getBloodPressureLevel(int systolic, int diastolic) {
        if (systolic < 120 && diastolic < 80) {
            return "正常";
        } else if (systolic < 130 && diastolic < 80) {
            return "正常偏高";
        } else if (systolic < 140 || diastolic < 90) {
            return "轻度高血压前期";
        } else if (systolic < 160 || diastolic < 100) {
            return "1级高血压";
        } else if (systolic < 180 || diastolic < 110) {
            return "2级高血压";
        } else {
            return "3级高血压";
        }
    }

    /**
     * 获取血糖等级（空腹血糖标准）
     */
    private String getBloodGlucoseLevel(double glucose) {
        if (glucose < 3.9) {
            return "低血糖";
        } else if (glucose <= 6.1) {
            return "正常";
        } else if (glucose < 7.0) {
            return "糖耐量受损";
        } else {
            return "血糖偏高";
        }
    }

    /**
     * 获取血脂等级（以总胆固醇为例）
     */
    private String getBloodLipidLevel(double lipid) {
        if (lipid < 5.2) {
            return "正常";
        } else if (lipid < 6.2) {
            return "边缘升高";
        } else {
            return "升高";
        }
    }

    @GetMapping("/chart")
    @Operation(
        summary = "获取生物特征图表数据",
        description = "获取指定时间范围内的生物特征数据用于图表展示。支持七天、一个月、三个月和半年的数据范围。" +
                "返回的数据包括体重、血压、血糖、血脂和BMI等指标的时间序列数据，可直接用于前端图表展示。"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "查询成功",
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResponseResult.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "无效的时间范围参数"),
        @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    public ResponseResult<BiometricChartDataDto> getBiometricChartData(
            @io.swagger.v3.oas.annotations.Parameter(description = "时间范围: 7d-七天, 1m-一个月, 3m-三个月, 6m-半年", example = "7d")
            @RequestParam(defaultValue = "7d") String timeRange) {

        // 计算时间范围
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime;

        switch (timeRange) {
            case "7d" -> startTime = endTime.minusDays(7);
            case "1m" -> startTime = endTime.minusMonths(1);
            case "3m" -> startTime = endTime.minusMonths(3);
            case "6m" -> startTime = endTime.minusMonths(6);
            default -> {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "无效的时间范围");
            }
        }

        // 获取数据
        List<BiometricRecord> records = biometricRecordService.getBiometricRecordsInRange(
                StpUtil.getLoginIdAsLong(),
                Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant())
        );

        // 转换数据格式
        BiometricChartDataDto chartData = new BiometricChartDataDto();
        if (records.isEmpty()) {
            return ResponseResult.okResult(chartData);
        }

        // 按测量时间排序
        records.sort(Comparator.comparing(BiometricRecord::getMeasurementTime));

        // 提取数据
        chartData.setDates(records.stream()
                .map(r -> r.getMeasurementTime().toLocalDate().toString())
                .collect(Collectors.toList()));

        chartData.setWeights(records.stream()
                .map(BiometricRecord::getWeightKg)
                .collect(Collectors.toList()));

        chartData.setSystolicPressures(records.stream()
                .map(BiometricRecord::getSystolicPressure)
                .collect(Collectors.toList()));

        chartData.setDiastolicPressures(records.stream()
                .map(BiometricRecord::getDiastolicPressure)
                .collect(Collectors.toList()));

        chartData.setBloodGlucoses(records.stream()
                .map(BiometricRecord::getBloodGlucose)
                .collect(Collectors.toList()));

        chartData.setBloodLipids(records.stream()
                .map(BiometricRecord::getBloodLipid)
                .collect(Collectors.toList()));

        // 计算BMI
        List<Double> bmis = new ArrayList<>();
        for (BiometricRecord record : records) {
            if (record.getHeightCm() != null && record.getWeightKg() != null && record.getHeightCm() > 0) {
                double heightInMeters = record.getHeightCm() / 100.0;
                bmis.add(record.getWeightKg() / (heightInMeters * heightInMeters));
            } else {
                bmis.add(null);
            }
        }
        chartData.setBmis(bmis);

        return ResponseResult.okResult(chartData);
    }
}