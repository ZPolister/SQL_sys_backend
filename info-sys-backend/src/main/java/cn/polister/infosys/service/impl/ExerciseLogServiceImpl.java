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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
}
