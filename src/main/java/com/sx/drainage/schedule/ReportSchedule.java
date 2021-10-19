package com.sx.drainage.schedule;

import com.sx.drainage.entity.OmTagEntity;
import com.sx.drainage.entity.ProjectCompanyTagUserEntity;
import com.sx.drainage.entity.ProjectPhaseEntity;
import com.sx.drainage.entity.ProjectProjectEntity;
import com.sx.drainage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 14:15
 * 定时器
 */
@Component
@RequiredArgsConstructor
public class ReportSchedule {
    private final ProjectProjectService projectProjectService;
    private final ProjectMonthlyReviewService projectMonthlyReviewService;
    private final ProjectPhaseService projectPhaseService;
    private final ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;
    private final ProjectStarRatingService projectStarRatingService;
    private final ProjectMonitoringUnitEvaluationService projectMonitoringUnitEvaluationService;
/*  private final ProjectNotificationService projectNotificationService;
    private final ProjectCompanyTagUserService projectCompanyTagUserService;
    private final OmTagService omTagService;*/

    /*
    * 每年一月一号初始化未完成项目的月度考评 季度考评 星级考评信息
    * */
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void initAppraisal(){
        List<ProjectProjectEntity> list = projectProjectService.getAllProject();
        List<ProjectProjectEntity> toDo = new ArrayList<>();
        list.forEach(l -> {
            ProjectPhaseEntity entity = projectPhaseService.getByProjectId(l.getSysid());
            if(entity.getRecord()==null){
                toDo.add(l);
            }
        });
        if(toDo.size()>0){
            toDo.forEach(t -> {
                projectMonthlyReviewService.initAppraisal(t.getSysid());
                projectQuarterlyAssessmentService.initAppraisal(t.getSysid());
                projectStarRatingService.initAppraisal(t.getSysid());
                projectMonitoringUnitEvaluationService.initAppraisal(t.getSysid());
            });
        }
    }
    /*
    * 每月20查询监理月报是否填写，如无则提醒监理负责人
    * 暂时无效
    * */
//    @Scheduled(cron = "0 0 0 20 * ? *")
//    public void remindSupervisor(){
//        OmTagEntity tag = omTagService.getTagByName("监理单位");
//        //获取监理单位负责人
//        ProjectCompanyTagUserEntity user = projectCompanyTagUserService.getUser("", tag.getSysid());
//    }
}
