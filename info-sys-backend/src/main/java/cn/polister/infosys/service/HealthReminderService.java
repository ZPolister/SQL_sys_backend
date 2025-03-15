package cn.polister.infosys.service;

import cn.polister.infosys.entity.HealthReminder;
import cn.polister.infosys.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 健康提醒表(HealthReminder)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:52:08
 */
public interface HealthReminderService extends IService<HealthReminder> {
    ResponseResult<Void> createMedicationReminder(HealthReminder reminder);
    ResponseResult<Void> createHealthCheckReminder(HealthReminder reminder);
    void processMedicationReminders();
    void processHealthCheckReminders();
    ResponseResult<Void> confirmReminder(String token);
}
