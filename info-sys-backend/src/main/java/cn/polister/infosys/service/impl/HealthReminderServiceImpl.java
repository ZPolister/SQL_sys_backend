package cn.polister.infosys.service.impl;

import cn.polister.infosys.entity.HealthReminder;
import cn.polister.infosys.mapper.HealthReminderMapper;
import cn.polister.infosys.service.HealthReminderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 健康提醒表(HealthReminder)表服务实现类
 *
 * @author Polister
 * @since 2025-03-02 20:52:08
 */
@Service("healthReminderService")
public class HealthReminderServiceImpl extends ServiceImpl<HealthReminderMapper, HealthReminder> implements HealthReminderService {

}
