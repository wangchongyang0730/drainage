package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectFileTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/19
 * Time: 14:13
 */
public interface ProjectFileTypeService extends IService<ProjectFileTypeEntity> {
    /*
    * 添加文件类型
    * */
    void addFileType(ProjectFileTypeEntity entity);
    /*
    * 获取文件类型树
    * */
    List<ProjectFileTypeEntity> getFileTypeTree();
    /*
    * 获取文件类型
    * */
    List<ProjectFileTypeEntity> getFileType();
    /*
    * 获取父级文件类型
    * */
    List<ProjectFileTypeEntity> getParentFileType();
    /*
    * 资料获取
    * */
    List<Map<String,Object>> getFiles(String sysId,String projectId,String fileName);
}
