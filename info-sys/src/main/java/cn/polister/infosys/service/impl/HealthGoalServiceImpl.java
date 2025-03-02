package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.HealthGoal;
import cn.polister.infosys.mapper.HealthGoalMapper;
import cn.polister.infosys.service.HealthGoalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 健康目标表(HealthGoal)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:51:46
 */
@Service("healthGoalService")
public class HealthGoalServiceImpl extends ServiceImpl<HealthGoalMapper, HealthGoal> implements HealthGoalService {

}
