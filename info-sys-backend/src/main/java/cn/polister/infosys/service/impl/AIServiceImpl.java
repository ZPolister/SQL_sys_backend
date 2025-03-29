package cn.polister.infosys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import cn.polister.infosys.constants.AIPromptConstants;
import cn.polister.infosys.entity.*;
import cn.polister.infosys.entity.dto.HealthGoalDto;
import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.entity.vo.MedicationReminderVo;
import cn.polister.infosys.enums.AppHttpCodeEnum;
import cn.polister.infosys.exception.SystemException;
import cn.polister.infosys.mapper.BiometricRecordMapper;
import cn.polister.infosys.mapper.DietLogMapper;
import cn.polister.infosys.mapper.ExerciseLogMapper;
import cn.polister.infosys.mapper.SleepLogMapper;
import cn.polister.infosys.service.AIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {
    @Resource
    private StreamingChatModel streamingChatModel;
    @Resource
    private ChatModel chatModel;
    @Resource
    private BiometricRecordMapper biometricMapper;
    @Resource
    private ExerciseLogMapper exerciseMapper;
    @Resource
    private DietLogMapper dietLogMapper;
    @Resource
    private  SleepLogMapper sleepLogMapper;

    public Flux<String> streamAnalysis(Long accountId) {
        // 获取上下文数据
        AnalysisContext context = AnalysisContext.of(StpUtil.getLoginIdAsLong(),
                biometricMapper, exerciseMapper, dietLogMapper, sleepLogMapper);

        // 获取时间字符串
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);

        // 构建提示词
        Prompt prompt = new Prompt(AIPromptConstants.ANALYSIS_PROMPT
                .replace("{biometrics}", formatBiometrics(context.biometrics()))
                .replace("{exercises}", formatExercises(context.exercises()))
                .replace("{nowDate}", dateString)
                .replace("{diets}", formatDiets(context.diets()))
                .replace("{sleeps}", formatSleeps(context.sleeps())));

        // 流式响应处理
        return streamingChatModel.stream(prompt)
                .map(response -> response.getResult().getOutput().getText());
//                .transform(this::extractGoals); // 提取目标建议
    }

    @Override
    public List<MedicationReminderVo> getMedicationReminderByPng(MultipartFile pngFile) {
        var userMessage = new UserMessage(AIPromptConstants.REMINDER_PROMPT,
                new Media(MimeTypeUtils.IMAGE_PNG, pngFile.getResource()));

        ChatResponse response = chatModel.call(new Prompt(userMessage,
                OpenAiChatOptions.builder()
                        .model("Qwen/Qwen2.5-VL-32B-Instruct")
                        .build()));

        String aiResponse = response.getResult().getOutput().getText();

        // 使用正则表达式匹配```json和```之间的内容
        Pattern pattern = Pattern.compile("(?<=```json)([\\s\\S]*?)(?=```)");
        Matcher matcher = pattern.matcher(aiResponse);

        if (matcher.find()) {
            String jsonContent = matcher.group(1).trim();
            return JSONUtil.toList(jsonContent, MedicationReminderVo.class);
        }

        pattern = Pattern.compile("(?<=```)([\\s\\S]*?)(?=```)");
        matcher = pattern.matcher(aiResponse);
        if (matcher.find()) {
            String jsonContent = matcher.group(1).trim();
            return JSONUtil.toList(jsonContent, MedicationReminderVo.class);
        }

        // 如果没有找到匹配的JSON格式，则尝试直接解析整个响应
        return JSONUtil.toList(aiResponse, MedicationReminderVo.class);
    }

    // 结构化目标提取
    private Flux<String> extractGoals(Flux<String> flux) {
        final StringBuilder buffer = new StringBuilder();

        return flux.concatWithValues("\n[ANALYSIS_END]")
                .handle((content, sink) -> {
                    if (content.contains("{")) buffer.append(content);
                    if (content.contains("}") && !buffer.isEmpty()) {
                        processGoal(buffer.toString(), sink);
                        buffer.setLength(0);
                    } else {
                        sink.next(content.replaceAll("\\{.*", ""));
                    }
                });
    }

    private void processGoal(String json, SynchronousSink<String> sink) {
        try {
            JsonNode node = new ObjectMapper().readTree(json);
            node.get("goals").forEach(goal -> {
                String category = goal.get("category").asText();
                String reason = goal.get("reason").asText();
                double value = goal.get("targetValue").asDouble();
                String timeFrame = goal.get("timeFrame").asText();

                // 发送目标建议事件
                sink.next(String.format(
                        "\n[建议目标]%s：%.1f（%s）, 建议达成时间: %s",
                        category, value, reason, timeFrame
                ));


                // 生成快速创建数据json回传前端
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    HealthGoalDto healthGoalDto = new HealthGoalDto(category, value, formatter.parse(timeFrame));
                    String quickCreate = JSONUtil.toJsonStr(healthGoalDto);
                    sink.next("\n[快速创建]" + quickCreate);
                } catch (ParseException e) {
                    throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                }


            });
        } catch (Exception e) {
            sink.error(new Exception("目标解析失败"));
        }


    }

    private String formatBiometrics(List<BiometricRecord> records) {
        return records.stream()
                .map(r -> String.format("%s | 身高: %.1fcm | 体重: %.1fkg | 血压: %d/%d",
                        r.getMeasurementTime().format(DateTimeFormatter.ISO_DATE),
                        r.getHeightCm(),
                        r.getWeightKg(),
                        r.getSystolicPressure(),
                        r.getDiastolicPressure()))
                .collect(Collectors.joining("\n"));
    }

    private String formatExercises(List<ExerciseLog> logs) {
        return logs.stream()
                .map(l -> String.format("%s %s | 时长:%d分钟 | 消耗:%dkcal",
                        l.getStartTimestamp().toString(),
                        l.getExerciseType(),
                        l.getDurationMinutes(),
                        l.getCaloriesBurned()))
                .collect(Collectors.joining("\n"));
    }

    private String formatDiets(List<DietLog> diets) {
        return diets.stream()
                .map(d -> String.format("%s 食用 %s %.1fg（%dkcal）",
                        d.getConsumptionTime().toString(),
                        d.getFoodItem(),
                        d.getQuantityGrams(),
                        d.getTotalCalories()))
                .collect(Collectors.joining("\n"));
    }

    private String formatSleeps(List<SleepLog> sleeps) {
        return sleeps.stream()
                .map(s -> {
                    Duration duration = Duration.between(s.getSleepStart().toInstant(), s.getSleepEnd().toInstant());
                    return String.format("%s 睡眠：%s-%s（%.1f小时）质量%d级",
                            s.getSleepStart().toString(),
                            s.getSleepStart().toString(),
                            s.getSleepEnd().toString(),
                            duration.toMinutes() / 60.0,
                            s.getSleepQuality());
                })
                .collect(Collectors.joining("\n"));
    }
}
