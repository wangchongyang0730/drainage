package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.dao.ProjectAnnualEvaluationDao;
import com.sx.drainage.entity.ProjectAnnualEvaluationEntity;
import com.sx.drainage.service.ProjectAnnualEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/14
 * Time: 14:34
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectAnnualEvaluationServiceImpl extends ServiceImpl<ProjectAnnualEvaluationDao, ProjectAnnualEvaluationEntity> implements ProjectAnnualEvaluationService {

    /*
     * 获取年度考评
     * */
    @Override
    public List<ProjectAnnualEvaluationEntity> getAppraisal(String projectId) {
        return this.list(new QueryWrapper<ProjectAnnualEvaluationEntity>().eq("projectId",projectId).orderByAsc("year"));
    }
    /*
    * 修改年度考评
    * */
    @Override
    public void updateAppraisal(ProjectAnnualEvaluationEntity projectAnnualEvaluationEntity) {
        this.updateById(projectAnnualEvaluationEntity);
    }
    /*
    * 初始化年度考评
    * */
    @Override
    public void initAppraisal(ProjectAnnualEvaluationEntity projectAnnualEvaluationEntity) {
        this.save(projectAnnualEvaluationEntity);
    }
    /*
     * 根据条件获取年度考评
     * */
    @Override
    public ProjectAnnualEvaluationEntity getAppraisalByYear(String projectId, String year) {
        return this.getOne(new QueryWrapper<ProjectAnnualEvaluationEntity>().eq("projectId",projectId).eq("year",year));
    }

}
