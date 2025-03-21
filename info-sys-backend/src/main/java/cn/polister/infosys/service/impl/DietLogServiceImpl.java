package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.PageResult;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.DietLogDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.DietLogMapper;
import cn.polister.infosys.service.DietLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.polister.infosys.utils.DateUtil.handleDate;

/**
 * 饮食记录表(DietLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:46:19
 */
@Service("dietLogService")
public class DietLogServiceImpl extends ServiceImpl<DietLogMapper, DietLog>
        implements DietLogService {

    @Override
    public ResponseResult<Long> createDietLog(DietLogDto dto) {
        // 参数校验
        if (StringUtils.isBlank(dto.getFoodItem())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "食物名称不能为空");
        }
        if (dto.getQuantityGrams() == null || dto.getQuantityGrams() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "食用量需大于0克");
        }
        if (dto.getTotalCalories() == null || dto.getTotalCalories() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "总热量需大于0大卡");
        }
        if (dto.getConsumptionTime() != null && dto.getConsumptionTime().after(new Date())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAMETER_INVALID, "食用时间不能是未来时间");
        }

        DietLog log = new DietLog();
        BeanUtils.copyProperties(dto, log);
        log.setAccountId(StpUtil.getLoginIdAsLong());
        if (dto.getConsumptionTime() == null) {
            log.setConsumptionTime(new Date());
        }
        this.save(log);
        return ResponseResult.okResult(log.getLogId());
    }

    @Override
    public ResponseResult<Void> deleteDietLog(Long logId) {
        DietLog log = this.getById(logId);
        if (log == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "记录不存在");
        }
        if (!log.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH, "无权删除该记录");
        }
        this.removeById(logId);
        return ResponseResult.okResult();
    }

    @Override
    public Page<DietLog> getDietLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<DietLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietLog::getAccountId, StpUtil.getLoginIdAsLong())
                .orderByDesc(DietLog::getConsumptionTime);

        if (startDate != null && endDate != null) {
            // 调整 endDate 到当天的最后一刻
            endDate = handleDate(endDate);
            wrapper.between(DietLog::getConsumptionTime, startDate, endDate);
        } else if (startDate != null) {
            wrapper.ge(DietLog::getConsumptionTime, startDate);
        } else if (endDate != null) {
            // 调整 endDate 到当天的最后一刻
            endDate = handleDate(endDate);
            wrapper.le(DietLog::getConsumptionTime, endDate);
        }

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Double getHotToday() {
        LambdaQueryWrapper<DietLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.ge(DietLog::getConsumptionTime,
                Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
                        .atZone(ZoneId.systemDefault()).toInstant()));

        return this.getBaseMapper().selectList(wrapper)
                .stream().mapToDouble(DietLog::getTotalCalories)
                .sum();
    }

    @Override
    public ResponseResult<Map<String, Object>> getDailyCaloriesData(String range) {
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

        // 查询指定时间范围内的饮食记录
        LambdaQueryWrapper<DietLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DietLog::getAccountId, StpUtil.getLoginIdAsLong())
                .ge(DietLog::getConsumptionTime, startDate)
                .le(DietLog::getConsumptionTime, endDate)
                .orderByAsc(DietLog::getConsumptionTime);

        List<DietLog> logs = this.list(wrapper);

        // 使用TreeMap按日期排序存储每天的热量
        Map<String, Double> dailyCalories = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 初始化日期范围内的所有日期，设置热量为0
        Calendar dateIterator = Calendar.getInstance();
        dateIterator.setTime(startDate);
        while (!dateIterator.getTime().after(endDate)) {
            dailyCalories.put(dateFormat.format(dateIterator.getTime()), 0.0);
            dateIterator.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 计算每天的总热量
        for (DietLog log : logs) {
            String dateKey = dateFormat.format(log.getConsumptionTime());
            dailyCalories.merge(dateKey, Double.valueOf(log.getTotalCalories()), Double::sum);
        }

        // 准备返回数据
        List<String> dates = new ArrayList<>(dailyCalories.keySet());
        List<Double> calories = new ArrayList<>(dailyCalories.values());

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("calories", calories);

        return ResponseResult.okResult(result);
    }
}