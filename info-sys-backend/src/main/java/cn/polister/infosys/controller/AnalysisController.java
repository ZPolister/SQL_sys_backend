package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "分析数据模块", description = "获取健康情况分析数据")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private AIService aiService;
//    @Resource
//    private HealthAnalysisMapper analysisMapper;

    @Operation(
        summary = "获取分析数据",
        description = "流式获取当前用户的健康数据分析结果，支持实时生成或使用缓存",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "成功返回流式分析数据",
                content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)
            )
        }
    )
    @SaCheckLogin
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamAnalysis(
            @Parameter(description = "是否刷新缓存，true表示强制重新生成分析", schema = @Schema(type = "boolean", defaultValue = "false"))
            @RequestParam(defaultValue = "false") boolean refresh) {

        Long accountId = StpUtil.getLoginIdAsLong();

        return Flux.defer(() -> {
//            // 优先读取缓存
//            if (!refresh) {
//                HealthAnalysis cached = analysisMapper.selectLatest(accountId, type);
//                if (cached != null && cached.getDataVersion().isAfter(getLastUpdateTime(accountId))) {
//                    return Flux.just(cached.getContent());
//                }
//            }
            // 调用大模型生成结果
            return aiService.streamAnalysis(accountId)
                    .concatWithValues("\n[分析结束]");
        });
    }
}