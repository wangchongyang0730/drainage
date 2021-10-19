package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectQuarterlyAssessmentEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/14
 * Time: 10:15
 */
public interface ProjectQuarterlyAssessmentService extends IService<ProjectQuarterlyAssessmentEntity> {

    /*
    * 初始化季度考评
    * */
    void initAppraisal(String projectId);
    /*
    * 获取季度考评
    * */
    List<ProjectQuarterlyAssessmentEntity> getAppraisal(String projectId,String year);
    /*
    * 修改季度考评
    * */
    void updateAppraisal(ProjectQuarterlyAssessmentEntity assessmentEntity);
    /*
    * 根据条件获取单条信息
    * */
    ProjectQuarterlyAssessmentEntity getAppraisalByCondition(String projectId,String year,String quarterly);
}
