package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.mapper.DietLogMapper;
import cn.polister.infosys.mapper.ExerciseLogMapper;
import cn.polister.infosys.mapper.SleepLogMapper;

import java.util.List;

public record AnalysisContext(
        List<BiometricRecord> biometrics,
        List<ExerciseLog> exercises,
        List<DietLog> diets,
        List<SleepLog> sleeps
) {
    public static AnalysisContext of(Long accountId,
                                     BiometricRecordMapper biometricMapper,
                                     ExerciseLogMapper exerciseMapper,
                                     DietLogMapper dietMapper,
                                     SleepLogMapper sleepMapper) {
        return new AnalysisContext(
                biometricMapper.selectLatestRecords(accountId, 5),
                exerciseMapper.selectLastWeekLogs(accountId),
                dietMapper.selectRecentLogs(accountId, 7),
                sleepMapper.selectRecentLogs(accountId, 7)
        );
    }
}
