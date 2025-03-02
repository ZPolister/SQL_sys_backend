package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.mapper.ExerciseLogMapper;
import cn.polister.infosys.service.ExerciseLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 运动记录表(ExerciseLog)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:51:15
 */
@Service("exerciseLogService")
public class ExerciseLogServiceImpl extends ServiceImpl<ExerciseLogMapper, ExerciseLog> implements ExerciseLogService {

}
