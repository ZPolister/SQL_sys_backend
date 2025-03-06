package cn.polister.infosys.service;

import reactor.core.publisher.Flux;

public interface AIService {
    Flux<String> streamAnalysis(Long accountId);
}
