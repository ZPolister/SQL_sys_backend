package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "获取分析数据")
    @SaCheckLogin
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamAnalysis(
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