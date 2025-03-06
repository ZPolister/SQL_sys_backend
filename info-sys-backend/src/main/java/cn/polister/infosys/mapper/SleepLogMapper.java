package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.SleepLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 睡眠记录表(SleepLog)表数据库访问层
 *
 * @author Polister
 * @since 2025-03-02 20:52:25
 */
public interface SleepLogMapper extends BaseMapper<SleepLog> {
    /**
     * 查询最近N天的睡眠记录
     * @param days 最近天数
     */
    @Select("SELECT * FROM sleep_log " +
            "WHERE account_id = #{accountId} " +
            "AND sleep_start >= DATE_SUB(NOW(), INTERVAL #{days} DAY) " +
            "ORDER BY sleep_start DESC")
    List<SleepLog> selectRecentLogs(@Param("accountId") Long accountId,
                                    @Param("days") Integer days);
}
