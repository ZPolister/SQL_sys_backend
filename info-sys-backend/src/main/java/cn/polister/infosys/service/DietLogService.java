package cn.polister.infosys.service;

import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.ResponseResult;
import cn.polister.infosys.entity.dto.DietLogDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;


/**
 * 饮食记录表(DietLog)表服务接口
 *
 * @author Polister
 * @since 2025-03-02 20:46:19
 */
public interface DietLogService extends IService<DietLog> {
    ResponseResult createDietLog(DietLogDto dto);
    ResponseResult deleteDietLog(Long logId);
    Page<DietLog> getDietLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize);

    Double getHotToday();
}
