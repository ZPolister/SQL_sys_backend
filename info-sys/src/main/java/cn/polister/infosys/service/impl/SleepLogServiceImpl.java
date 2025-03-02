package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.mapper.SleepLogMapper;
import cn.polister.infosys.service.SleepLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 睡眠记录表(SleepLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:52:25
 */
@Service("sleepLogService")
public class SleepLogServiceImpl extends ServiceImpl<SleepLogMapper, SleepLog> implements SleepLogService {

}
