package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.mapper.DietLogMapper;
import cn.polister.infosys.service.DietLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 饮食记录表(DietLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:46:19
 */
@Service("dietLogService")
public class DietLogServiceImpl extends ServiceImpl<DietLogMapper, DietLog> implements DietLogService {

}
