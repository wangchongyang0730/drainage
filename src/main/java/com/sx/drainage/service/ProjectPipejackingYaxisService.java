package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectPipejackingYaxisEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/30
 * Time: 10:07
 */
public interface ProjectPipejackingYaxisService extends IService<ProjectPipejackingYaxisEntity> {

    /*
    * 设置Y轴
    * */
    void setYAxis(ProjectPipejackingYaxisEntity entity);
    /*
    * 获取Y轴
    * */
    List<ProjectPipejackingYaxisEntity> getYAxis(String initId, String projectId);
}
