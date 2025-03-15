package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.MedicationReminder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服药提醒表(MedicationReminder)表数据库访问层
 */
@Mapper
public interface MedicationReminderMapper extends BaseMapper<MedicationReminder> {
}