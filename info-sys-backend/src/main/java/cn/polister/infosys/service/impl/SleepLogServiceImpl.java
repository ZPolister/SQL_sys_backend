package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.entity.dto.SleepLogDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.SleepLogMapper;
import cn.polister.infosys.service.SleepLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 睡眠记录表(SleepLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:52:25
 */
@Service("sleepLogService")
public class SleepLogServiceImpl extends ServiceImpl<SleepLogMapper, SleepLog>
        implements SleepLogService {

    @Override
    public ResponseResult<Long> createSleepLog(SleepLogDto dto) {
        // 手动参数校验
        Date now = new Date();

        // 校验入睡时间
        if (dto.getSleepStart() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "入睡时间不能为空");
        }
        if (dto.getSleepStart().after(now)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "入睡时间不能晚于当前时间");
        }

        // 校验醒来时间
        if (dto.getSleepEnd() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "醒来时间不能为空");
        }
        if (dto.getSleepEnd().before(dto.getSleepStart())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "醒来时间不能早于入睡时间");
        }
        if (dto.getSleepEnd().after(now)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "醒来时间不能晚于当前时间");
        }

        // 校验睡眠质量
        if (dto.getSleepQuality() == null || dto.getSleepQuality() < 1 || dto.getSleepQuality() > 5) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "睡眠质量需为1-5级");
        }

        // 保存记录
        SleepLog log = new SleepLog();
        log.setAccountId(StpUtil.getLoginIdAsLong());
        log.setSleepStart(dto.getSleepStart());
        log.setSleepEnd(dto.getSleepEnd());
        log.setSleepQuality(dto.getSleepQuality());
        log.setCreatedAt(now);
        this.save(log);

        return ResponseResult.okResult(log.getLogId());
    }

    @Override
    public ResponseResult<Void> deleteSleepLog(Long logId) {
        SleepLog log = this.getById(logId);
        if (log == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "睡眠记录不存在");
        }
        if (!log.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH, "无权删除该记录");
        }
        this.removeById(logId);
        return ResponseResult.okResult();
    }

    @Override
    public Page<SleepLog> getSleepLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SleepLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SleepLog::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByDesc(SleepLog::getSleepStart);

        // 日期范围筛选（基于入睡时间）
        if (startDate != null && endDate != null) {
            wrapper.between(SleepLog::getSleepStart, startDate, endDate);
        } else if (startDate != null) {
            wrapper.ge(SleepLog::getSleepStart, startDate);
        } else if (endDate != null) {
            wrapper.le(SleepLog::getSleepStart, endDate);
        }

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public SleepLog getLatestRecord() {
        LambdaQueryWrapper<SleepLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SleepLog::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByDesc(SleepLog::getSleepEnd);

        return this.getOne(wrapper);
    }
}
