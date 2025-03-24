package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.constants.GoalStatus;
import cn.polister.infosys.entity.BiometricRecord;
import cn.polister.infosys.entity.HealthGoal;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.HealthGoalDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.mapper.HealthGoalMapper;
import cn.polister.infosys.service.BiometricRecordService;
import cn.polister.infosys.service.ExerciseLogService;
import cn.polister.infosys.service.HealthGoalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 健康目标表(HealthGoal)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:51:46
 */
@Service("healthGoalService")
public class HealthGoalServiceImpl extends ServiceImpl<HealthGoalMapper, HealthGoal>
        implements HealthGoalService {

    @Resource
    private ExerciseLogService exerciseLogService;

    @Resource
    private BiometricRecordService biometricRecordService;

    @Override
    @Transactional
    public ResponseResult<Long> createGoal(HealthGoalDto dto) {
        Long accountId = StpUtil.getLoginIdAsLong();

        // 校验是否存在进行中的目标
        LambdaQueryWrapper<HealthGoal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthGoal::getAccountId, accountId)
                .eq(HealthGoal::getGoalStatus, GoalStatus.IN_PROGRESS.code);
        if (this.count(wrapper) > 0) {
            HealthGoal healthGoal = this.getOne(wrapper);
            healthGoal.setGoalStatus(GoalStatus.FAILED.code);
            this.updateById(healthGoal);
        }

        // 创建新目标
        HealthGoal goal = new HealthGoal();
        goal.setAccountId(accountId);
        goal.setGoalCategory(dto.getGoalCategory());
        goal.setTargetValue(dto.getTargetValue());
        goal.setCurrentValue(0.0);
        goal.setStartDate(new Date());
        goal.setTargetDate(dto.getTargetDate());
        goal.setGoalStatus(checkGoalStatus(goal));
        this.save(goal);
        this.updateCurrentValue(accountId, goal.getGoalCategory());
        return ResponseResult.okResult(goal.getGoalId());
    }

    // 状态检查方法
    private int checkGoalStatus(HealthGoal goal) {
        // 检查目标日期
        if (new Date().after(goal.getTargetDate())) {
            return GoalStatus.FAILED.code;
        }
        return GoalStatus.IN_PROGRESS.code;
    }

    // 更新当前值（根据不同类型）
    @Transactional
    @Async
    public void updateCurrentValue(Long accountId, String goalCategory) {
        LambdaQueryWrapper<HealthGoal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthGoal::getAccountId, accountId)
                .eq(HealthGoal::getGoalCategory, goalCategory)
                .eq(HealthGoal::getGoalStatus, GoalStatus.IN_PROGRESS.code);

        HealthGoal goal = this.getOne(wrapper);
        if (goal == null) return;

        switch (goal.getGoalCategory()) {
            case "WEIGHT_LOSS":
                BiometricRecord latestWeight = biometricRecordService.getLatestBiometricRecord(accountId);
                goal.setCurrentValue(latestWeight != null && latestWeight.getWeightKg() != null
                                        ? latestWeight.getWeightKg() : 0.0);
                break;
            case "BLOOD_LIPID":
                BiometricRecord latestLipid = biometricRecordService.getLatestBiometricRecord(accountId);
                goal.setCurrentValue(latestLipid != null && latestLipid.getBloodLipid() != null
                                        ? latestLipid.getBloodLipid(): 0.0);

                break;
            case "BLOOD_SUGAR":
                BiometricRecord latestSugar = biometricRecordService.getLatestBiometricRecord(accountId);
                goal.setCurrentValue(latestSugar != null && latestSugar.getBloodGlucose() != null
                                        ? latestSugar.getBloodLipid(): 0.0);
                break;
            case "EXERCISE_CALORIES":
                Double calories = exerciseLogService.getTotalCaloriesAfter(accountId, goal.getStartDate());
                goal.setCurrentValue(calories);
                break;
        }

        // 检查是否达成目标
        if (goal.getGoalCategory().equals("EXERCISE_CALORIES")
                && goal.getCurrentValue() >= goal.getTargetValue()) {
            goal.setGoalStatus(GoalStatus.ACHIEVED.code);
        } else if (goal.getCurrentValue() <= goal.getTargetValue()) {
            goal.setGoalStatus(GoalStatus.ACHIEVED.code);
        }else {
            goal.setGoalStatus(checkGoalStatus(goal));
        }

        this.updateById(goal);
    }

    @Override
    public HealthGoal getCurrentGoal() {
        Long accountId = StpUtil.getLoginIdAsLong();

        // 查找进行中的目标
        LambdaQueryWrapper<HealthGoal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthGoal::getAccountId, accountId)
                .eq(HealthGoal::getGoalStatus, GoalStatus.IN_PROGRESS.code)
                .last("LIMIT 1");
        HealthGoal current = this.getOne(wrapper);

        if (current == null) {
            // 查找最近达成的目标
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HealthGoal::getAccountId, accountId)
                    .eq(HealthGoal::getGoalStatus, GoalStatus.ACHIEVED.code)
                    .orderByDesc(HealthGoal::getTargetDate)
                    .orderByDesc(HealthGoal::getGoalId)
                    .last("LIMIT 1");
            current = this.getOne(wrapper);
        }

        return current;
    }
}
