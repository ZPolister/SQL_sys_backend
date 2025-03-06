package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.ExerciseLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 运动记录表(ExerciseLog)表数据库访问层
 *
 * @author Polister
 * @since 2025-03-02 20:51:15
 */
public interface ExerciseLogMapper extends BaseMapper<ExerciseLog> {
    @Select("SELECT * FROM exercise_log WHERE account_id = #{accountId} " +
            "AND start_timestamp >= DATE_SUB(NOW(), INTERVAL 7 DAY)")
    List<ExerciseLog> selectLastWeekLogs(@Param("accountId") Long accountId);
}
