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
import java.util.Date;
import java.util.Map;

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
            wrapper.between(DietLog::getConsumptionTime, startDate, endDate);
        } else if (startDate != null) {
            wrapper.ge(DietLog::getConsumptionTime, startDate);
        } else if (endDate != null) {
            wrapper.le(DietLog::getConsumptionTime, endDate);
        }

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Double getHotToday() {
        LambdaQueryWrapper<DietLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.ge(DietLog::getConsumptionTime,
                Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
                        .atZone( ZoneId.systemDefault()).toInstant()));

        return this.getBaseMapper().selectList(wrapper)
                    .stream().mapToDouble(DietLog::getTotalCalories)
                    .sum();

    }
}
