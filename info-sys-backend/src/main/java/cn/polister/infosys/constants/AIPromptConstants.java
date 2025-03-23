package cn.polister.infosys.constants;

public class AIPromptConstants {
    public static String ANALYSIS_PROMPT = """
        现在是{nowDate}，作为健康管理专家，请根据以下用户数据进行分析，输出请使用普通文段格式，而不是Markdown：
        
        生物特征记录（最新5条）：
        {biometrics}
        
        运动记录（最近7天）：
        {exercises}
        
        饮食习惯（最近7天）：
        {diets}
        
        睡眠记录（最近7天）：
        {sleeps}
        
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
        """;
}
