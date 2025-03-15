package cn.polister.infosys.service;

import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * 服药提醒表(MedicationReminder)表服务接口
 */
public interface MedicationReminderService extends IService<MedicationReminder> {
    ResponseResult<Void> createReminder(MedicationReminder reminder);
    ResponseResult<Void> updateReminder(MedicationReminder reminder);
    ResponseResult<Void> deleteReminder(Long reminderId);
    Page<MedicationReminder> getReminderList(Date startDate, Date endDate, Integer pageNum, Integer pageSize);
    void processReminders();
}