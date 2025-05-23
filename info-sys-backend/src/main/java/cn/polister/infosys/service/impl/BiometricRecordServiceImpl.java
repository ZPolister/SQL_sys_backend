package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.service.BiometricRecordService;
import cn.polister.infosys.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 生物特征记录表(BiometricRecord)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:44:36
 */
@Service("biometricRecordService")
public class BiometricRecordServiceImpl extends ServiceImpl<BiometricRecordMapper, BiometricRecord> implements BiometricRecordService {
    @Override
    public Page<BiometricRecord> getBiometricRecordsWithDateRange(Date startTime, Date endTime, Integer pageNum, Integer pageSize) {
        Page<BiometricRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BiometricRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(BiometricRecord::getAccountId, StpUtil.getLoginIdAsLong());

        // 构建日期范围查询条件
        if (startTime != null && endTime != null) {
            endTime = DateUtil.handleDate(endTime);
            wrapper.between(BiometricRecord::getMeasurementTime, startTime, endTime);
        } else if (startTime != null) {
            wrapper.ge(BiometricRecord::getMeasurementTime, startTime);
        } else if (endTime != null) {
            endTime = DateUtil.handleDate(endTime);
            wrapper.le(BiometricRecord::getMeasurementTime, endTime);
        }

        return this.page(page, wrapper);
    }

    @Override
    public BiometricRecord getLatestBiometricRecord(Long accountId) {
        LambdaQueryWrapper<BiometricRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BiometricRecord::getAccountId, accountId)
                .orderByDesc(BiometricRecord::getMeasurementTime)
                .last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<BiometricRecord> getBiometricRecordsInRange(Long accountId, Date startTime, Date endTime) {
        LambdaQueryWrapper<BiometricRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BiometricRecord::getAccountId, accountId);

        // 构建日期范围查询条件
        if (startTime != null && endTime != null) {
            wrapper.between(BiometricRecord::getMeasurementTime, startTime, endTime);
        } else if (startTime != null) {
            wrapper.ge(BiometricRecord::getMeasurementTime, startTime);
        } else if (endTime != null) {
            wrapper.le(BiometricRecord::getMeasurementTime, endTime);
        }

        wrapper.orderByAsc(BiometricRecord::getMeasurementTime);
        return this.list(wrapper);
    }
}
