package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.HealthCheckReminder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体检提醒表(HealthCheckReminder)表数据库访问层
 */
@Mapper
public interface HealthCheckReminderMapper extends BaseMapper<HealthCheckReminder> {
}