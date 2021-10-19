package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectProjectbasicinfoEntity;
import com.sx.drainage.params.ProjectBasicParams;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:48:33
 */
public interface ProjectProjectbasicinfoService extends IService<ProjectProjectbasicinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取项目详细信息
    Map<String,Object> getProjectDetails(String projectId);
    //获取项目详细信息
    ProjectProjectbasicinfoEntity getProjectEntity(String projectId);
    //更新当前项目的基本信息
    void putProjectBasicInfo(ProjectBasicParams projectBasicParams);
    //删除项目
    void deleteProject(String sysId);
    //新增项目详细信息
    void postProject(String userId, String sysid);
    //根据项目id更改信息
    void putByProjectId(String s, String projectDescribe);
}

