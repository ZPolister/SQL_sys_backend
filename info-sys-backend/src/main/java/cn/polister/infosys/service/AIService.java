package cn.polister.infosys.service;

import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.entity.vo.MedicationReminderVo;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

public interface AIService {
    Flux<String> streamAnalysis(Long accountId);

    List<MedicationReminderVo> getMedicationReminderByPng(MultipartFile pngFile);
}
