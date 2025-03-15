package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.HealthCheckConfirmation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体检提醒确认表(HealthCheckConfirmation)表数据库访问层
 */
@Mapper
public interface HealthCheckConfirmationMapper extends BaseMapper<HealthCheckConfirmation> {
}