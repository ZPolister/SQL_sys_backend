package cn.polister.infosys.service;

import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.SleepLog;
import cn.polister.infosys.entity.dto.SleepLogDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;


/**
 * 睡眠记录表(SleepLog)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:52:25
 */
public interface SleepLogService extends IService<SleepLog> {
    ResponseResult<Long> createSleepLog(SleepLogDto dto);
    ResponseResult<Void> deleteSleepLog(Long logId);
    Page<SleepLog> getSleepLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize);

    SleepLog getLatestRecord();
}
