package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectDeviceclashEntity;
import com.sx.drainage.params.AddClashReport;
import com.sx.drainage.params.UpdateDeviceClash;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-27 16:54:18
 */
public interface ProjectDeviceclashService extends IService<ProjectDeviceclashEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取冲突报告列表
    List<ProjectDeviceclashEntity> getProjectPageList(String projectId);
    //修改
    void updateProjectDeviceclash(UpdateDeviceClash updateDeviceClash);
    //删除
    void deleteDeviceclash(String sysId);
    //获取冲突报告信息
    ProjectDeviceclashEntity getDeviceclashById(String id);
    //新增并创建报告流程
    void addDeviceclash(AddClashReport addClashReport,String userId);
}

