package cn.polister.infosys.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.entity.HealthGoal;
import cn.polister.infosys.service.HealthGoalService;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

// AOP切面处理数据
@Aspect
@Component
public class HealthDataAspect {

    @Resource
    private HealthGoalService healthGoalService;

    @Pointcut("@annotation(UpdateTargetBiometric)")
    void ptB() {}

    @Pointcut("@annotation(UpdateTargetExercise)")
    void ptE() {}

    // 生物特征记录切入
    @AfterReturning("ptB()")
    public void afterBiometricSave(JoinPoint joinPoint) {
        HealthGoal currentGoal = healthGoalService.getCurrentGoal();
        if (Objects.isNull(currentGoal)) return;
        healthGoalService.updateCurrentValue(StpUtil.getLoginIdAsLong(), currentGoal.getGoalCategory());
    }

    // 运动记录切入
    @AfterReturning("ptE()")
    public void afterExerciseSave(JoinPoint joinPoint) {
        HealthGoal currentGoal = healthGoalService.getCurrentGoal();
        if (Objects.isNull(currentGoal)) return;
        healthGoalService.updateCurrentValue(StpUtil.getLoginIdAsLong(), "EXERCISE_CALORIES");
    }

}