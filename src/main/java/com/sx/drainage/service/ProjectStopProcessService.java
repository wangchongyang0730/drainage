package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectStopProcessEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/3
 * Time: 11:06
 */
public interface ProjectStopProcessService extends IService<ProjectStopProcessEntity> {
    /*
    * 停用流程存档获取
    * */
    List<ProjectStopProcessEntity> getStopProcessArchive(String processType,String projectId);
    /*
    * 停用流程存档
    * */
    void addStopProcessArchive(ProjectStopProcessEntity entity);
    /*
    * 删除停用流程存档
    * */
    void deleteStopProcessArchive(String sysId);
    /*
    * 文档获取
    * */
    List<ProjectStopProcessEntity> getStopProcessFile(String processType);
}
