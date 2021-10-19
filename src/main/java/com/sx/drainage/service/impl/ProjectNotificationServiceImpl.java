package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectNotificationDao;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/1
 * Time: 9:49
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectNotificationServiceImpl extends ServiceImpl<ProjectNotificationDao, ProjectNotificationEntity> implements ProjectNotificationService {

    private final ProjectProjectService projectProjectService;
    private final OmAccountService omAccountService;
    private final ProjectMonthlyReviewService projectMonthlyReviewService;//月度考评
    private final ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;//季度考评
    private final ProjectStarRatingService projectStarRatingService;//星级考评
    private final ProjectMonitoringUnitEvaluationService projectMonitoringUnitEvaluationService;//监测单位考评
    private final ProjectAnnualEvaluationService projectAnnualEvaluationService;//年度考评
    /*
    * 添加消息
    * */
    @Override
    public void add(ProjectNotificationEntity entity) {
        this.save(entity);
    }
    /*
    * 获取所有消息列表
    * */
    @Override
    public List<Map<String, Object>> getMyMessages(String userId) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<ProjectNotificationEntity> list = this.list(new QueryWrapper<ProjectNotificationEntity>().eq("userId", userId).eq("del",0));
        List<ProjectProjectEntity> allProject = projectProjectService.getAllProject();
        list.forEach(l -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processId",l.getProcessId());
            map.put("workType",l.getProcessType());
            map.put("createUser",l.getCreateUser());
            map.put("createUserId",l.getCreateUserId());
            map.put("states",l.getStates());
            map.put("sysId",l.getSysId());
            map.put("projectId",l.getProjectId());
            map.put("evaluationId",l.getEvaluationId());
            map.put("msg",l.getMsg());
            List<ProjectProjectEntity> collect = allProject.stream().filter(projectProjectEntity -> projectProjectEntity.getSysid().equals(l.getProjectId())).collect(Collectors.toList());
            if(collect!=null&&collect.size()>0){
                map.put("projectName",collect.get(0).getName());
            }else{
                map.put("projectName",null);
            }
            data.add(map);
        });
        return data;
    }
    /*
    * 消息状态变更
    * */
    @Override
    public void read(String sysId) {
        ProjectNotificationEntity entity = new ProjectNotificationEntity();
        entity.setSysId(sysId);
        entity.setStates(1);
        this.updateById(entity);
    }
    /*
     * 批量添加
     * */
    @Override
    public void addByList(List<ProjectNotificationEntity> list) {
        this.saveBatch(list);
    }
    /*
    * 报送
    * */
    @Override
    public void sendMessage(ProjectNotificationEntity projectNotificationEntity, String userId) {
        OmAccountEntity user = omAccountService.getUser(userId);
        projectNotificationEntity.setSysId(CreateUuid.uuid());
        projectNotificationEntity.setStates(0);
        projectNotificationEntity.setCreateUserId(userId);
        projectNotificationEntity.setCreateUser(user.getName());
        projectNotificationEntity.setDel(0);
        switch (projectNotificationEntity.getMsg()){
            case "月度考评":
                ProjectMonthlyReviewEntity entity = projectMonthlyReviewService.getById(projectNotificationEntity.getEvaluationId());
                entity.setStates(2);
                projectMonthlyReviewService.updateAppraisal(entity);
                break;
            case "季度考评":
                ProjectQuarterlyAssessmentEntity assessmentEntity = projectQuarterlyAssessmentService.getById(projectNotificationEntity.getEvaluationId());
                assessmentEntity.setStates(2);
                projectQuarterlyAssessmentService.updateAppraisal(assessmentEntity);
                break;
            case "星级考评":
                ProjectStarRatingEntity starRatingEntity = projectStarRatingService.getById(projectNotificationEntity.getEvaluationId());
                starRatingEntity.setStates(2);
                projectStarRatingService.updateAppraisal(starRatingEntity);
                break;
            case "监测单位考评":
                ProjectMonitoringUnitEvaluationEntity unitEvaluationEntity = projectMonitoringUnitEvaluationService.getById(projectNotificationEntity.getEvaluationId());
                unitEvaluationEntity.setStates(2);
                projectMonitoringUnitEvaluationService.updateAppraisal(unitEvaluationEntity);
                break;
            default:
                ProjectAnnualEvaluationEntity evaluationEntity = projectAnnualEvaluationService.getById(projectNotificationEntity.getEvaluationId());
                evaluationEntity.setStates(2);
                projectAnnualEvaluationService.updateAppraisal(evaluationEntity);
                break;
        }
        this.save(projectNotificationEntity);
    }
}
