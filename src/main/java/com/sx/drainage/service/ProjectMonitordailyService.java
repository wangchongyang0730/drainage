package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectMonitordailyEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:14:23
 */
public interface ProjectMonitordailyService extends IService<ProjectMonitordailyEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取报表数据集合信息
    Map<String, Object> getMonitorDailyList(String reportId, Integer page, Integer pageRecord);
    //获取单个
    ProjectMonitordailyEntity getPr(String sysId);
    //根据reportSysId更改状态
    void deleteByReportSysId(String userId, String reportSysId);
    //获取所有id
    List<String> getAllIdByReportSysId(String reportSysId);
    //添加
    void addMonitorDaily(ProjectMonitordailyEntity entity);
    //分页获取点位信息
    R getPagePointList(String reportSysId, String pointStartDate, String pointEndDate, Integer pageSize, Integer pageIndex, String sort, Boolean isAsc);
    //根据点位名称和时间查询点位数据列表
    R getDataList(String reportSysId, String startDate, String endDate, String pointName);
    //删除报表数据信息
    void deleteMonitorDaily(String sysId);
}

