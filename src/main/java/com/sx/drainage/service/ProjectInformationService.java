package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectInformationEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 15:48
 */
public interface ProjectInformationService extends IService<ProjectInformationEntity> {
    /*
    * 资料补充
    * */
    void additional(ProjectInformationEntity projectInformationEntity);
    /*
    * 获取项目资料
    * */
    ProjectInformationEntity getByProjectId(String projectId);
    /*
    * 获取所有项目资料
    * */
    List<ProjectInformationEntity> geAll();
}
