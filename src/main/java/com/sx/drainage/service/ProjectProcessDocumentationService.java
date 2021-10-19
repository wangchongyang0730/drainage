package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectProcessDocumentationEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/3
 * Time: 11:23
 */
public interface ProjectProcessDocumentationService extends IService<ProjectProcessDocumentationEntity> {
    /*
    * 批量添加
    * */
    void addAll(List<ProjectProcessDocumentationEntity> list);
    /*
    * 根据流程名称获取
    * */
    List<ProjectProcessDocumentationEntity> getFileByProcessName(String processName);
    /*
    * 根据流程名称和项目id获取
    * */
    List<ProjectProcessDocumentationEntity> getFileByProcessNameAndProjectId(String processName, String projectId);
    /*
    * 其他部门文件
    * */
    List<ProjectProcessDocumentationEntity> getOtherDepartmentFile();
    /*
     * 其他部门文件根据项目id查询
     * */
    List<ProjectProcessDocumentationEntity> getOtherDepartmentFileByProjectId(String projectId);
    /*
    * 根据流程名称模糊查询
    * */
    List<ProjectProcessDocumentationEntity> getFileByProcessNameLike(String name);
    /*
     * 根据项目id，流程名称模糊查询
     * */
    List<ProjectProcessDocumentationEntity> getFileByProcessNameLikeAndProjectId(String name, String projectId);
    /*
     * 根据节点名称模糊查询
     * */
    List<ProjectProcessDocumentationEntity> getFileByTaskNameLike(String name);
    /*
     * 根据项目id，根据节点名称模糊查询
     * */
    List<ProjectProcessDocumentationEntity> getFileByTaskNameLikeAndProjectId(String name, String projectId);
    /*
    * 获取所有文件
    * */
    List<ProjectProcessDocumentationEntity> getAllFile();
    /*
    * 根据项目id获取文件
    * */
    List<ProjectProcessDocumentationEntity> getAllFileByProjectId(String projectId);
}
