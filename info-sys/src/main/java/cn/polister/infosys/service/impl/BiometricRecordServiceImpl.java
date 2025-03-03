package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.service.BiometricRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 生物特征记录表(BiometricRecord)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:44:36
 */
@Service("biometricRecordService")
public class BiometricRecordServiceImpl extends ServiceImpl<BiometricRecordMapper, BiometricRecord> implements BiometricRecordService {
    @Override
    public Page<BiometricRecord> getRecordsWithDateRange(Date startTime, Date endTime, Integer pageNum, Integer pageSize) {
        Page<BiometricRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BiometricRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(BiometricRecord::getAccountId, StpUtil.getLoginIdAsLong());

        // 构建日期范围查询条件
        if (startTime != null && endTime != null) {
            wrapper.between(BiometricRecord::getMeasurementTime, startTime, endTime);
        } else if (startTime != null) {
            wrapper.ge(BiometricRecord::getMeasurementTime, startTime);
        } else if (endTime != null) {
            wrapper.le(BiometricRecord::getMeasurementTime, endTime);
        }

        return this.page(page, wrapper);
    }
}
