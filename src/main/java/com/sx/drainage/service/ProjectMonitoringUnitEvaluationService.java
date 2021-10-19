package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectMonitoringUnitEvaluationEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/15
 * Time: 10:46
 */
public interface ProjectMonitoringUnitEvaluationService extends IService<ProjectMonitoringUnitEvaluationEntity> {
    /*
     * 初始化监测单位考评
     * */
    void initAppraisal(String projectId);
    /*
    * 获取监测单位考评
    * */
    List<ProjectMonitoringUnitEvaluationEntity> getAppraisal(String projectId, String year);
    /*
    * 修改检查单位考评
    * */
    void updateAppraisal(ProjectMonitoringUnitEvaluationEntity projectMonitoringUnitEvaluationEntity);
}
