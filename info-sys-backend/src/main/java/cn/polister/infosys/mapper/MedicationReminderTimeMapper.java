package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.MedicationReminderTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服药时间点表(MedicationReminderTime)表数据库访问层
 */
@Mapper
public interface MedicationReminderTimeMapper extends BaseMapper<MedicationReminderTime> {
}