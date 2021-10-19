package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectStarRatingEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/15
 * Time: 9:38
 */
public interface ProjectStarRatingService extends IService<ProjectStarRatingEntity> {
    /*
    * 初始化星级考评
    * */
    void initAppraisal(String projectId);
    /*
    * 获取星级考评
    * */
    List<ProjectStarRatingEntity> getAppraisal(String projectId, String year);
    /*
    * 修改星级考评
    * */
    void updateAppraisal(ProjectStarRatingEntity projectStarRatingEntity);
    /*
    * 各星级数量
    * */
    Map<String,Object> getNumberOfStar(String projectId);
}
