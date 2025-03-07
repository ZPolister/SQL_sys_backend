package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.entity.excel.BiometricRecordExcel;
import cn.polister.infosys.entity.excel.DietLogExcel;
import cn.polister.infosys.entity.excel.ExerciseLogExcel;
import cn.polister.infosys.entity.excel.SleepLogExcel;
import cn.polister.infosys.service.BiometricRecordService;
import cn.polister.infosys.service.DietLogService;
import cn.polister.infosys.service.ExerciseLogService;
import cn.polister.infosys.service.SleepLogService;
import cn.polister.infosys.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/export")
@Tag(name = "数据导出", description = "Excel导出数据")
public class DataExportController {

    @Resource
    private FileUtil fileUtil;

    @Resource
    private BiometricRecordService biometricRecordService;

    @Resource
    private DietLogService dietLogService;

    @Resource
    private ExerciseLogService exerciseLogService;

    @Resource
    private SleepLogService sleepLogService;

    /**
     * 导出生物特征数据Excel
     *
     * @throws IOException 导出失败
     */
    @Operation(summary = "导出健康数据")
    @GetMapping("/biometric")
    @SaCheckLogin
    public void exportBiometricData() throws IOException {
        LambdaQueryWrapper<BiometricRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BiometricRecord::getAccountId, StpUtil.getLoginIdAsLong());
        List<BiometricRecord> biometricRecords = biometricRecordService.getBaseMapper().selectList(queryWrapper);
        List<BiometricRecordExcel> biometricRecordExcels = biometricRecords.stream()
                .map(x -> BeanUtil.toBean(x, BiometricRecordExcel.class)).toList();
        fileUtil.outputExcelFile("健康数据导出", biometricRecordExcels, BiometricRecordExcel.class, "导出数据");
    }

    /**
     * 导出饮食数据Excel
     *
     * @throws IOException 导出失败
     */
    @Operation(summary = "导出饮食数据")
    @GetMapping("/diet")
    @SaCheckLogin
    public void exportDietLogData() throws IOException {
        LambdaQueryWrapper<DietLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DietLog::getAccountId, StpUtil.getLoginIdAsLong());
        List<DietLogExcel> dietLogExcels = dietLogService.getBaseMapper().selectList(queryWrapper).stream()
                .map(x -> BeanUtil.toBean(x, DietLogExcel.class)).toList();
        fileUtil.outputExcelFile("饮食数据导出", dietLogExcels, DietLogExcel.class, "导出数据");
    }

    /**
     * 导出运动数据Excel
     *
     * @throws IOException 导出失败
     */
    @Operation(summary = "导出运动数据")
    @GetMapping("/exercise")
    @SaCheckLogin
    public void exportExerciseData() throws IOException {
        LambdaQueryWrapper<ExerciseLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExerciseLog::getAccountId, StpUtil.getLoginIdAsLong());
        List<ExerciseLogExcel> dietLogExcels = exerciseLogService.getBaseMapper().selectList(queryWrapper).stream()
                .map(x -> BeanUtil.toBean(x, ExerciseLogExcel.class)).toList();
        fileUtil.outputExcelFile("运动数据导出", dietLogExcels, ExerciseLogExcel.class, "导出数据");
    }

    /**
     * 导出睡眠数据Excel
     *
     * @throws IOException 导出失败
     */
    @Operation(summary = "导出睡眠数据")
    @GetMapping("/sleep")
    @SaCheckLogin
    public void exportSleepData() throws IOException {
        LambdaQueryWrapper<SleepLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SleepLog::getAccountId, StpUtil.getLoginIdAsLong());
        List<SleepLogExcel> dietLogExcels = sleepLogService.getBaseMapper().selectList(queryWrapper).stream()
                .map(x -> BeanUtil.toBean(x, SleepLogExcel.class)).toList();
        fileUtil.outputExcelFile("睡眠数据导出", dietLogExcels, SleepLogExcel.class, "导出数据");
    }
}
