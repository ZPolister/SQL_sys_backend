package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.constants.GoalStatus;
import cn.polister.infosys.entity.HealthGoal;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.HealthGoalDto;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.service.HealthGoalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-goals")
@Tag(name = "健康目标管理", description = "健康目标相关操作")
public class HealthGoalController {

    @Resource
    private HealthGoalService healthGoalService;

    @Operation(summary = "创建健康目标")
    @SaCheckLogin
    @PostMapping
    public ResponseResult<Long> createGoal(@RequestBody HealthGoalDto dto) {
        return healthGoalService.createGoal(dto);
    }

    @Operation(summary = "删除目标")
    @SaCheckLogin
    @DeleteMapping("/{goalId}")
    public ResponseResult<Void> deleteGoal(@PathVariable Long goalId) {
        HealthGoal goal = healthGoalService.getById(goalId);
        if (goal == null || !goal.getAccountId().equals(StpUtil.getLoginIdAsLong())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        healthGoalService.removeById(goalId);
        return ResponseResult.okResult();
    }

    @Operation(summary = "获取当前目标")
    @SaCheckLogin
    @GetMapping("/current")
    public ResponseResult<HealthGoal> getCurrentGoal() {
        return ResponseResult.okResult(healthGoalService.getCurrentGoal());
    }
}