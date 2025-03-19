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

import java.util.*;
import java.text.SimpleDateFormat;

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
                .orderByDesc(SleepLog::getSleepEnd)
                .last("LIMIT 1");

        return this.getOne(wrapper);
    }

    @Override
    public ResponseResult<Map<String, Object>> getSleepDurationByDays(String range) {
        // 获取当前时间
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        // 设置开始时间
        Date startDate;
        switch (range.toLowerCase()) {
            case "week":
                calendar.add(Calendar.DAY_OF_MONTH, -6); // -6是因为要包含今天
                startDate = calendar.getTime();
                break;
            case "month":
                calendar.add(Calendar.MONTH, -1);
                calendar.add(Calendar.DAY_OF_MONTH, 1); // 调整到整月的开始
                startDate = calendar.getTime();
                break;
            case "3months":
                calendar.add(Calendar.MONTH, -3);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
                break;
            case "6months":
                calendar.add(Calendar.MONTH, -6);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
                break;
            default:
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "Invalid range parameter");
        }

        // 查询指定时间范围内的睡眠记录
        LambdaQueryWrapper<SleepLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SleepLog::getAccountId, StpUtil.getLoginIdAsLong())
                .ge(SleepLog::getSleepEnd, startDate)
                .le(SleepLog::getSleepEnd, endDate)
                .orderByAsc(SleepLog::getSleepEnd);

        List<SleepLog> logs = this.list(wrapper);

        // 使用TreeMap按日期排序存储每天的睡眠时长
        Map<String, Double> dailyDurations = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 初始化日期范围内的所有日期，设置睡眠时长为0
        Calendar dateIterator = Calendar.getInstance();
        dateIterator.setTime(startDate);
        while (!dateIterator.getTime().after(endDate)) {
            dailyDurations.put(dateFormat.format(dateIterator.getTime()), 0.0);
            dateIterator.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 计算每天的睡眠时长（小时）
        for (SleepLog log : logs) {
            String dateKey = dateFormat.format(log.getSleepEnd());
            double duration = (log.getSleepEnd().getTime() - log.getSleepStart().getTime()) / (1000.0 * 3600); // 转换为小时
            dailyDurations.merge(dateKey, duration, Double::sum);
        }

        // 准备返回数据
        List<String> dates = new ArrayList<>(dailyDurations.keySet());
        List<Double> durations = new ArrayList<>(dailyDurations.values());

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("durations", durations);

        return ResponseResult.okResult(result);
    }
}
