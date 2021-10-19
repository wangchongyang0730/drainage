package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectPhaseEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/4
 * Time: 14:11
 */
public interface ProjectPhaseService extends IService<ProjectPhaseEntity> {
    /*
    * 项目阶段文件获取
    * */
    ProjectPhaseEntity getByProjectId(String sysId);
    /*
    * 删除
    * */
    void deleteProjectPhase(String sysId);
    /*
    * 添加数据
    * */
    void addPhase(String sysid);
    /*
    * 项目阶段文件添加
    * */
    void addProjectPhase(ProjectPhaseEntity entity);
    /*
    * 获取所有项目阶段文件
    * */
    List<ProjectPhaseEntity> getAll();
}
