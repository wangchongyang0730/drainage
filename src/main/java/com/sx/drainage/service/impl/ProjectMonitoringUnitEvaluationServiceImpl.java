package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectMonitoringUnitEvaluationDao;
import com.sx.drainage.entity.ProjectMonitoringUnitEvaluationEntity;
import com.sx.drainage.service.ProjectMonitoringUnitEvaluationService;
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
 * Date: 2020/12/15
 * Time: 10:46
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMonitoringUnitEvaluationServiceImpl extends ServiceImpl<ProjectMonitoringUnitEvaluationDao, ProjectMonitoringUnitEvaluationEntity> implements ProjectMonitoringUnitEvaluationService {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy");
    /*
     * 初始化监测单位考评
     * */
    @Override
    public void initAppraisal(String projectId) {
        Date date = new Date();
        String year = format.format(date);
        List<ProjectMonitoringUnitEvaluationEntity> list = new ArrayList<>();
        String [] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        for(int i=0;i<12;i++){
            ProjectMonitoringUnitEvaluationEntity entity = new ProjectMonitoringUnitEvaluationEntity();
            entity.setSysId(CreateUuid.uuid());
            entity.setOrders(i);
            entity.setProjectId(projectId);
            entity.setStates(0);
            entity.setMonth(months[i]);
            entity.setStates(0);
            entity.setYear(year);
            list.add(entity);
        }
        this.saveBatch(list);
    }
    /*
     * 获取监测单位考评
     * */
    @Override
    public List<ProjectMonitoringUnitEvaluationEntity> getAppraisal(String projectId, String year) {
        return this.list(new QueryWrapper<ProjectMonitoringUnitEvaluationEntity>().eq("projectId",projectId).eq("year",year).orderByAsc("orders"));
    }
    /*
     * 修改检查单位考评
     * */
    @Override
    public void updateAppraisal(ProjectMonitoringUnitEvaluationEntity projectMonitoringUnitEvaluationEntity) {
        this.updateById(projectMonitoringUnitEvaluationEntity);
    }
}
