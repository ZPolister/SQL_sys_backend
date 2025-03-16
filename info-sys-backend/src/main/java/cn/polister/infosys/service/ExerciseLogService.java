package cn.polister.infosys.service;

import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.ExerciseLogDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 运动记录表(ExerciseLog)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:51:15
 */
public interface ExerciseLogService extends IService<ExerciseLog> {
    ResponseResult<Long> createExerciseLog(ExerciseLogDto dto);
    void deleteExerciseLog(Long logId);
    Page<ExerciseLog> getExerciseLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize);
    Double getTotalCaloriesAfter(Long accountId, Date startDate);
    ExerciseLog getLatestLog();
    List<Map<String, Object>> getDailyCaloriesBurned(String range);
}
