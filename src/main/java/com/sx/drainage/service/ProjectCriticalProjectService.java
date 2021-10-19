package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectCriticalProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/19
 * Time: 16:10
 */
public interface ProjectCriticalProjectService extends IService<ProjectCriticalProjectEntity> {
    /*
    * 根据项目id获取危大工程
    * , Integer page, Integer limit
    * */
    List<ProjectCriticalProjectEntity> getAllByProjectId(String projectId);
    /*
    * 添加危大工程
    * */
    void addCriticalProject(ProjectCriticalProjectEntity entity,String userId);
    /*
    * 删除危大工程
    * */
    void deleteCriticalProject(String sysId);
    /*
    * 修改危大工程
    * */
    void updateCriticalProject(ProjectCriticalProjectEntity entity);
}
