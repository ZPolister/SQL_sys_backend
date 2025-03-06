package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.DietLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 饮食记录表(DietLog)表数据库访问层
 *
 * @author Polister
 * @since 2025-03-02 20:46:19
 */
public interface DietLogMapper extends BaseMapper<DietLog> {
    /**
     * 查询最近N天的饮食记录
     * @param days 最近天数
     */
    @Select("SELECT * FROM diet_log " +
            "WHERE account_id = #{accountId} " +
            "AND consumption_time >= DATE_SUB(NOW(), INTERVAL #{days} DAY) " +
            "ORDER BY consumption_time DESC")
    List<DietLog> selectRecentLogs(@Param("accountId") Long accountId,
                                   @Param("days") Integer days);
}
