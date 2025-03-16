package cn.polister.infosys.service;

import cn.polister.infosys.entity.BiometricRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * 生物特征记录表(BiometricRecord)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:44:36
 */
public interface BiometricRecordService extends IService<BiometricRecord> {
    Page<BiometricRecord> getBiometricRecordsWithDateRange(Date startTime, Date endTime, Integer pageNum, Integer pageSize);
    BiometricRecord getLatestBiometricRecord(Long accountId);
    List<BiometricRecord> getBiometricRecordsInRange(Long accountId, Date startTime, Date endTime);
}
