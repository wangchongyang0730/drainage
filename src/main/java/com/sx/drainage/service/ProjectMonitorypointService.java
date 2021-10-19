package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectMonitorypointEntity;
import com.sx.drainage.params.CameraInfoParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 13:36:07
 */
public interface ProjectMonitorypointService extends IService<ProjectMonitorypointEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取监控点信息
    List<Map<String, Object>> getMonitoryPoint(String projectId);
    //添加更新监控点信息
    void postMonitoryPoint(CameraInfoParams cameraInfoParams);
    //删除监控点
    void deleteMonitoryPoint(String sysId);
}

