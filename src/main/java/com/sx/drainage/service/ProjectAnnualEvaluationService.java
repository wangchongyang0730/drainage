package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectAnnualEvaluationEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/14
 * Time: 14:34
 */
public interface ProjectAnnualEvaluationService extends IService<ProjectAnnualEvaluationEntity> {
    /*
    * 获取年度考评
    * */
    List<ProjectAnnualEvaluationEntity> getAppraisal(String projectId);
    /*
    * 修改年度考评
    * */
    void updateAppraisal(ProjectAnnualEvaluationEntity projectAnnualEvaluationEntity);
    /*
    * 初始化年度考评
    * */
    void initAppraisal(ProjectAnnualEvaluationEntity projectAnnualEvaluationEntity);
    /*
    * 根据条件获取年度考评
    * */
    ProjectAnnualEvaluationEntity getAppraisalByYear(String projectId, String year);
}
