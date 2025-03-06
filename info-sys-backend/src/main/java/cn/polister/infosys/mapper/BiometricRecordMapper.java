package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.BiometricRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 生物特征记录表(BiometricRecord)表数据库访问层
 *
 * @author Polister
 * @since 2025-03-02 20:44:35
 */
public interface BiometricRecordMapper extends BaseMapper<BiometricRecord> {
    @Select("SELECT * FROM biometric_record WHERE account_id = #{accountId} " +
            "ORDER BY measurement_time DESC LIMIT #{limit}")
    List<BiometricRecord> selectLatestRecords(@Param("accountId") Long accountId,
                                              @Param("limit") int limit);
}
