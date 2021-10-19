package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectQuarterlyAssessmentDao;
import com.sx.drainage.entity.ProjectQuarterlyAssessmentEntity;
import com.sx.drainage.service.ProjectQuarterlyAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/14
 * Time: 10:15
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectQuarterlyAssessmentServiceImpl extends ServiceImpl<ProjectQuarterlyAssessmentDao, ProjectQuarterlyAssessmentEntity> implements ProjectQuarterlyAssessmentService {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy");
    /*
     * 初始化季度考评
     * */
    @Override
    public void initAppraisal(String projectId) {
        Date date = new Date();
        String year = format.format(date);
        List<ProjectQuarterlyAssessmentEntity> list = new ArrayList<>();
        ProjectQuarterlyAssessmentEntity firstQuarter = new ProjectQuarterlyAssessmentEntity();
        ProjectQuarterlyAssessmentEntity secondQuarter = new ProjectQuarterlyAssessmentEntity();
        ProjectQuarterlyAssessmentEntity thirdQuarter = new ProjectQuarterlyAssessmentEntity();
        ProjectQuarterlyAssessmentEntity fourthQuarter = new ProjectQuarterlyAssessmentEntity();
        firstQuarter.setSysId(CreateUuid.uuid());
        firstQuarter.setStates(0);
        firstQuarter.setQuarterly("第一季度");
        firstQuarter.setOrders(0);
        firstQuarter.setProjectId(projectId);
        firstQuarter.setYear(year);
        list.add(firstQuarter);
        secondQuarter.setSysId(CreateUuid.uuid());
        secondQuarter.setStates(0);
        secondQuarter.setQuarterly("第二季度");
        secondQuarter.setOrders(1);
        secondQuarter.setYear(year);
        secondQuarter.setProjectId(projectId);
        list.add(secondQuarter);
        thirdQuarter.setSysId(CreateUuid.uuid());
        thirdQuarter.setStates(0);
        thirdQuarter.setQuarterly("第三季度");
        thirdQuarter.setOrders(2);
        thirdQuarter.setYear(year);
        thirdQuarter.setProjectId(projectId);
        list.add(thirdQuarter);
        fourthQuarter.setSysId(CreateUuid.uuid());
        fourthQuarter.setStates(0);
        fourthQuarter.setQuarterly("第四季度");
        fourthQuarter.setOrders(3);
        fourthQuarter.setYear(year);
        fourthQuarter.setProjectId(projectId);
        list.add(fourthQuarter);
        this.saveBatch(list);
    }
    /*
    * 获取季度考评
    * */
    @Override
    public List<ProjectQuarterlyAssessmentEntity> getAppraisal(String projectId, String year) {
        return this.list(new QueryWrapper<ProjectQuarterlyAssessmentEntity>().eq("projectId",projectId).eq("year",year).orderByAsc("orders"));
    }
    /*
    * 修改季度考评
    * */
    @Override
    public void updateAppraisal(ProjectQuarterlyAssessmentEntity assessmentEntity) {
        this.updateById(assessmentEntity);
    }
    /*
    * 根据条件获取单条信息
    * */
    @Override
    public ProjectQuarterlyAssessmentEntity getAppraisalByCondition(String projectId, String year, String quarterly) {
        return this.getOne(new QueryWrapper<ProjectQuarterlyAssessmentEntity>().eq("projectId",projectId).eq("year",year).eq("quarterly",quarterly));
    }
}
