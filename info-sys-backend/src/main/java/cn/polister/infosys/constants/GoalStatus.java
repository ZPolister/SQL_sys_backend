package cn.polister.infosys.constants;

// 目标状态枚举
public enum GoalStatus {
    IN_PROGRESS(0),
    ACHIEVED(1),
    FAILED(2);

    public final int code;
    GoalStatus(int code) {
        this.code = code;
    }
}