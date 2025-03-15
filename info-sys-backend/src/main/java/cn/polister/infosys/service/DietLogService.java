package cn.polister.infosys.service;

import cn.polister.infosys.entity.DietLog;
import cn.polister.infosys.entity.PageResult;
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
    /**
     * 创建饮食记录
     *
     * @param dto 饮食记录数据传输对象
     * @return 响应结果，包含创建的记录ID
     */
    ResponseResult<Long> createDietLog(DietLogDto dto);

    /**
     * 删除饮食记录
     *
     * @param logId 记录ID
     * @return 响应结果
     */
    ResponseResult<Void> deleteDietLog(Long logId);

    /**
     * 获取饮食记录列表
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    Page<DietLog> getDietLogs(Date startDate, Date endDate, Integer pageNum, Integer pageSize);

    /**
     * 获取今日摄入的热量总和
     *
     * @return 今日摄入的总热量
     */
    Double getHotToday();
}