package cn.polister.infosys;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InfoSysApplicationTests {


    @Autowired
    private ChatModel chatModel;

    @Test
    void contextLoads() {
        System.out.println(chatModel.call("""
                现在是2025-3-20，作为健康管理专家，请根据以下用户数据进行分析，输出请使用普通文段格式，而不是Markdown：
               
                生物特征记录（最新5条）：
                2025-3-7 体重60kg 身高170cm 血压120/75
                2025-3-11 体重50kg 身高169cm 血压120/75
                2025-3-13 体重50kg 身高169cm 血压120/75
                2025-3-16 体重55kg 身高169cm 血压120/75
                2025-3-19 体重57kg 身高169cm 血压120/75
                
                运动记录（最近7天）：
                3小时
                3小时
                3小时
                3小时
                3小时
                3小时
                3小时
                
                饮食习惯（最近7天）：
                健康
                健康
                健康
                健康
                健康
                健康
                健康
                
                睡眠记录（最近7天）：
                4小时
                4小时
                4小时
                4小时
                4小时
                4小时
                4小时
                
                
                请用中文输出包含：
                1. 健康趋势分析（使用图表描述语，所有言语表达格式为一个专家为用户面对面提建议）
                2. 疾病风险评估
                3. 提出一个目标建议，要求：
                - 在减重（WEIGHT_LOSS）、运动热量（EXERCISE_CALORIES）、血糖控制（BLOOD_SUGAR）、血脂控制（BLOOD_LIPID）四个方面四选一
                - 给出理由、目标数值、期望达成日期
                - 最后从建议提取一个json结果，严格按照```{json结果}```格式附加到结果后面
                - 示例格式：
                ```
                {
                    "goalCategory": "WEIGHT_LOSS|BLOOD_SUGAR|BLOOD_LIPID|EXERCISE_CALORIES",
                    "targetValue": 目标数值,
                    "targetDate": "期望达成日期（按照"yyyy-MM-dd"格式字符串输出，如“2025-03-06”）"
                }
                ```
                """));
    }

}
