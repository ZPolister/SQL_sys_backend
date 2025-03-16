package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.ExerciseLogDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.exception.SystemException;
import cn.polister.infosys.mapper.ExerciseLogMapper;
import cn.polister.infosys.service.ExerciseLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 运动记录表(ExerciseLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:51:15
 */
@Service("exerciseLogService")
public class ExerciseLogServiceImpl extends ServiceImpl<ExerciseLogMapper, ExerciseLog>
        implements ExerciseLogService {

    @Override
    public ResponseResult<Long> createExerciseLog(ExerciseLogDto dto) {
        // 校验
        if (StringUtils.isBlank(dto.getExerciseType())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "运动类型不能为空");
        }
        if (dto.getStartTimestamp() == null || dto.getStartTimestamp().after(new Date())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "开始时间不能晚于现在");
        }
        if (dto.getDurationMinutes() == null || dto.getDurationMinutes() < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "持续时间至少1分钟");
        }
        if (dto.getCaloriesBurned() == null || dto.getCaloriesBurned() < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "消耗卡路里至少1大卡");
        }

        ExerciseLog log = new ExerciseLog();
        BeanUtil.copyProperties(dto, log);
        log.setAccountId(StpUtil.getLoginIdAsLong());
        this.save(log);
        return ResponseResult.okResult(log.getLogId());
    }

    @Override
    public void deleteExerciseLog(Long logId) {
        ExerciseLog log = this.getById(logId);
        if (log == null) {
            throw new SystemException(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (!log.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        this.removeById(logId);
    }

    @Override
    public Page<ExerciseLog> getExerciseLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<ExerciseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseLog::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByDesc(ExerciseLog::getStartTimestamp);

        if (startDate != null && endDate != null) {
            wrapper.between(ExerciseLog::getStartTimestamp, startDate, endDate);
        } else if (startDate != null) {
            wrapper.ge(ExerciseLog::getStartTimestamp, startDate);
        } else if (endDate != null) {
            wrapper.le(ExerciseLog::getStartTimestamp, endDate);
        }

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    public Double getTotalCaloriesAfter(Long accountId, Date startDate) {
        LambdaQueryWrapper<ExerciseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseLog::getAccountId, accountId)
                .ge(ExerciseLog::getStartTimestamp, startDate);

        return this.baseMapper.selectList(wrapper)
                 .stream().mapToDouble(ExerciseLog::getCaloriesBurned).sum();
    }

    @Override
    public ExerciseLog getLatestLog() {
        LambdaQueryWrapper<ExerciseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseLog::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByDesc(ExerciseLog::getCreatedAt)
                .last("LIMIT 1");

        return this.getOne(wrapper);
    }

    @Override
    public List<Map<String, Object>> getDailyCaloriesBurned(String range) {
        Date endDate = new Date();
        Date startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);

        switch (range.toUpperCase()) {
            case "WEEK":
                cal.add(Calendar.DAY_OF_YEAR, -6);
                break;
            case "MONTH":
                cal.add(Calendar.MONTH, -1);
                break;
            case "THREE_MONTHS":
                cal.add(Calendar.MONTH, -3);
                break;
            case "HALF_YEAR":
                cal.add(Calendar.MONTH, -6);
                break;
            default:
                throw new IllegalArgumentException("Invalid range: " + range);
        }
        startDate = cal.getTime();

        LambdaQueryWrapper<ExerciseLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExerciseLog::getAccountId, StpUtil.getLoginIdAsLong())
                .between(ExerciseLog::getStartTimestamp, startDate, endDate)
                .orderByAsc(ExerciseLog::getStartTimestamp);

        List<ExerciseLog> logs = this.list(wrapper);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Integer> dailyCalories = new HashMap<>();

        for (ExerciseLog log : logs) {
            String dateStr = sdf.format(log.getStartTimestamp());
            dailyCalories.put(dateStr, dailyCalories.getOrDefault(dateStr, 0) + log.getCaloriesBurned());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        cal.setTime(startDate);
        while (!cal.getTime().after(endDate)) {
            String dateStr = sdf.format(cal.getTime());
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);
            dayData.put("value", dailyCalories.getOrDefault(dateStr, 0));
            result.add(dayData);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        return result;
    }
}
