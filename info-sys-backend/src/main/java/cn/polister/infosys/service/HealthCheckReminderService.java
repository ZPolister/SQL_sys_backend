package cn.polister.infosys.service;

import cn.polister.infosys.entity.HealthCheckReminder;
import cn.polister.infosys.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * 体检提醒表(HealthCheckReminder)表服务接口
 */
public interface HealthCheckReminderService extends IService<HealthCheckReminder> {
    ResponseResult<Void> createReminder(HealthCheckReminder reminder);
    ResponseResult<Void> updateReminder(HealthCheckReminder reminder);
    ResponseResult<Void> deleteReminder(Long reminderId);
    ResponseResult<Void> confirmReminder(String token);
    Page<HealthCheckReminder> getReminderList(Date startDate, Date endDate, Integer pageNum, Integer pageSize);
    void processReminders();
}