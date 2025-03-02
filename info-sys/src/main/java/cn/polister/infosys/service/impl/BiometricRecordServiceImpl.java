package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.service.BiometricRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 生物特征记录表(BiometricRecord)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:44:36
 */
@Service("biometricRecordService")
public class BiometricRecordServiceImpl extends ServiceImpl<BiometricRecordMapper, BiometricRecord> implements BiometricRecordService {

}
