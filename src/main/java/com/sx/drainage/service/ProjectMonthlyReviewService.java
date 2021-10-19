package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectMonthlyReviewEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 14:41
 */
public interface ProjectMonthlyReviewService extends IService<ProjectMonthlyReviewEntity> {
    /*
    * 初始化月度考评
    * */
    void initAppraisal(String projectId);
    /*
    * 获取月度考评
    * */
    List<ProjectMonthlyReviewEntity> getAppraisal(String projectId,String year);
    /*
    * 修改月度考评
    * */
    void updateAppraisal(ProjectMonthlyReviewEntity entity);
    /*
    * 获取一季度月度考评
    * */
    List<ProjectMonthlyReviewEntity> getAppraisalByCondition(String projectId,String year,List<String> month);
}
