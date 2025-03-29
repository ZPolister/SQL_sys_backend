package cn.polister.infosys.service;

import cn.polister.infosys.entity.MedicationReminder;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.MedicationReminderDto;
import cn.polister.infosys.entity.vo.MedicationReminderVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 服药提醒表(MedicationReminder)表服务接口
 */
public interface MedicationReminderService extends IService<MedicationReminder> {
    ResponseResult<Void> createReminder(MedicationReminder reminder);
    ResponseResult<Void> updateReminder(MedicationReminder reminder);
    ResponseResult<Void> deleteReminder(Long reminderId);
    Page<MedicationReminder> getReminderList(Date startDate, Date endDate, Integer pageNum, Integer pageSize);
    void processReminders();
    List<MedicationReminder> getNextReminders(Long accountId);
    List<MedicationReminderVo> getInfoByPng(MultipartFile file);
}