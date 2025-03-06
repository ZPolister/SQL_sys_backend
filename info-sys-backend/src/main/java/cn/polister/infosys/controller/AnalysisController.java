package cn.polister.infosys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.polister.infosys.service.AIService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private AIService aiService;
//    @Resource
//    private HealthAnalysisMapper analysisMapper;

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
            // 调用AI生成
            return aiService.streamAnalysis(accountId)
                    .concatWithValues("\n[分析结束]");
        });
    }
}