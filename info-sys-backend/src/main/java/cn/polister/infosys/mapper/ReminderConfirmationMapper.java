package cn.polister.infosys.mapper;

import cn.polister.infosys.entity.ReminderConfirmation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提醒确认记录表(ReminderConfirmation)表数据库访问层
 */
@Mapper
public interface ReminderConfirmationMapper extends BaseMapper<ReminderConfirmation> {
}