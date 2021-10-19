package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectSupervisorManagementEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/18
 * Time: 9:46
 */
public interface ProjectSupervisorManagementService extends IService<ProjectSupervisorManagementEntity> {
    /*
    * 获取项目监理人员
    * */
    List<Map<String,Object>> getSupervisor(String projectId);
    /*
    * 变更项目监理人员
    * */
    void updateSupervisor(ProjectSupervisorManagementEntity entity);
    /*
    * 添加监理职位
    * */
    void addSupervisor(ProjectSupervisorManagementEntity entity);
    /*
    * 删除监理职位
    * */
    void deleteSupervisor(String sysId);
}
