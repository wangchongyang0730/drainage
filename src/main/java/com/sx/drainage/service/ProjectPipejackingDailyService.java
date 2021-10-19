package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectPipejackingDailyEntity;
import com.sx.drainage.entity.ProjectPipejackingDataEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/1
 * Time: 14:03
 */
public interface ProjectPipejackingDailyService extends IService<ProjectPipejackingDailyEntity> {
    /*
    * 新增日报
    * */
    void addPipejackingDaily(List<ProjectPipejackingDailyEntity> list,String userId);
    /*
    * 获取上次监测数据
    * */
    ProjectPipejackingDailyEntity getLastData(String sysId);
    /*
    * 获取所有日报已填报日期
    * */
    List<Date> getDate(String projectId);
    /*
    * 获取日报详情
    * */
    List<ProjectPipejackingDailyEntity> getPipejackingDaily(String projectId, String reportTime);
    /*
    * 删除日报
    * */
    void deletePipejackingDaily(String projectId, String reportTime);
    /*
    * 统计
    * */
    Map<String, Object> getStatistics(String sysId, Date startDate, Date endDate);
    /*
    * 删除顶管区间
    * */
    void deletePipejacking(String sysId);
}
