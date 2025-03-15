package cn.polister.infosys.service;

import cn.polister.infosys.entity.HealthGoal;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.HealthGoalDto;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 健康目标表(HealthGoal)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:51:46
 */
public interface HealthGoalService extends IService<HealthGoal> {
    ResponseResult<Long> createGoal(HealthGoalDto dto);
    void updateCurrentValue(Long accountId, String goalCategory);

    HealthGoal getCurrentGoal();
}
