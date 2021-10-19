package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectInitPipejackingEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 9:43
 */
public interface ProjectInitPipejackingService extends IService<ProjectInitPipejackingEntity> {
    /*
    * 初始化顶管区间
    * */
    void initPipejacking(ProjectInitPipejackingEntity entity);
    /*
    * 获取顶管区间
    * */
    List<ProjectInitPipejackingEntity> getPipejacking(String projectId);
    /*
    * 删除顶管区间
    * */
    void deletePipejacking(String sysId);
}
