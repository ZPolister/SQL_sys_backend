package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.polister.infosys.aspect.UpdateTargetExercise;
import cn.polister.infosys.entity.ExerciseLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.ExerciseLogDto;
import cn.polister.infosys.service.ExerciseLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/exercise")
@Tag(name = "运动记录管理", description = "运动记录相关操作")
public class ExerciseLogController {

    @Resource
    private ExerciseLogService exerciseLogService;

    @Operation(
            summary = "创建运动记录",
            description = "需要登录，参数需满足以下要求：<br>"
                    + "1. 运动类型必填<br>"
                    + "2. 开始时间不能晚于当前时间<br>"
                    + "3. 持续时间至少1分钟<br>"
                    + "4. 卡路里消耗至少1大卡",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ExerciseLogDto.class))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "记录创建成功"),
            @ApiResponse(
                    responseCode = "400",
                    description = "参数校验失败",
                    content = @Content(schema = @Schema(implementation = ResponseResult.class))
            ),
            @ApiResponse(responseCode = "401", description = "未登录访问")
    })
    @SaCheckLogin
    @PostMapping
    @UpdateTargetExercise
    public ResponseResult createLog(@Valid @RequestBody ExerciseLogDto dto) {
        return exerciseLogService.createExerciseLog(dto);
    }

    @Operation(summary = "删除运动记录")
    @SaCheckLogin
    @UpdateTargetExercise
    @DeleteMapping("/{logId}")
    public ResponseResult deleteLog(@PathVariable Long logId) {
        exerciseLogService.deleteExerciseLog(logId);
        return ResponseResult.okResult();
    }

    @Operation(summary = "分页查询运动记录")
    @SaCheckLogin
    @GetMapping
    public ResponseResult getLogs(
            @Parameter(description = "开始时间(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,

            @Parameter(description = "结束时间(yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,

            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,

            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<ExerciseLog> page = exerciseLogService.getExerciseLogs(startDate, endDate, pageNum, pageSize);
        return ResponseResult.okResult(page);
    }
}
