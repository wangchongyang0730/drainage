package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectMonitordataEntity;
import com.sx.drainage.params.MonitorDailyParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:18:23
 */
public interface ProjectMonitordataService extends IService<ProjectMonitordataEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取报表数据详细信息
    Map<String, Object> getMonitorDailyInfo(String sysId);
    //获取数据
    List<ProjectMonitordataEntity> getAllByDailyId(String dailyId);
    //新增报表数据点位,上传模板信息
    R importMonitorDate(String reportSysId, String dailyId, MultipartFile importFile);
    //修改报表数据信息
    void updateMonitorDaily(MonitorDailyParams monitorDailyParams, String userId);
    //新增报表数据信息
    void addMonitorDaily(MonitorDailyParams monitorDailyParams, String userId);
    //删除报表数据信息
    void deleteMonitorDaily(String sysId);
}

