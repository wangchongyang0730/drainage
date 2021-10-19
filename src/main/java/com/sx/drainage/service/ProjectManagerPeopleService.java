package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectManagerPeopleEntity;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/17
 * Time: 16:05
 */
public interface ProjectManagerPeopleService extends IService<ProjectManagerPeopleEntity> {
    /*
    * 获取项目管理人员
    * */
    ProjectManagerPeopleEntity getManagerPeople(String projectId);
    /*
    * 修改项目管理人员
    * */
    void updateManagerPeople(String projectId,String userId,String managerId,String department);
}
