package com.sx.drainage.component;

import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/29
 * Time: 16:20
 */
/*
* 项目启动，检查部分文件夹
* */
@Component
@RequiredArgsConstructor
public class RunTimmerAtApp implements ApplicationRunner {
    @Value("${file.imageUrl}")
    private String imageUrl;
    @Value("${file.pdfUrl}")
    private String pdfUrl;
    private final ProjectProjectService projectProjectService;
    private final ProjectMonthlyReviewService projectMonthlyReviewService;
    private final ProjectPhaseService projectPhaseService;
    private final ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;
    private final ProjectStarRatingService projectStarRatingService;
    private final ProjectMonitoringUnitEvaluationService projectMonitoringUnitEvaluationService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        File image = new File(imageUrl);
        File pdf = new File(pdfUrl);
        if(!image.exists()){
            image.mkdirs();
        }
        if(!pdf.exists()){
            pdf.mkdirs();
        }
        initAppraisal();
    }
    /*
    * 项目启动检查是否初始化考评信息
    * */
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy");
                String s = format.format(new Date());
                List<ProjectMonthlyReviewEntity> appraisal = projectMonthlyReviewService.getAppraisal(t.getSysid(), s);
                if(appraisal==null||appraisal.size()==0){
                    projectMonthlyReviewService.initAppraisal(t.getSysid());
                }
                List<ProjectQuarterlyAssessmentEntity> appraisal1 = projectQuarterlyAssessmentService.getAppraisal(t.getSysid(), s);
                if(appraisal1==null||appraisal1.size()==0){
                    projectQuarterlyAssessmentService.initAppraisal(t.getSysid());

                }
                List<ProjectStarRatingEntity> appraisal2 = projectStarRatingService.getAppraisal(t.getSysid(), s);
                if(appraisal2==null||appraisal2.size()==0){
                    projectStarRatingService.initAppraisal(t.getSysid());
                }
                List<ProjectMonitoringUnitEvaluationEntity> appraisal3 = projectMonitoringUnitEvaluationService.getAppraisal(t.getSysid(), s);
                if(appraisal3==null||appraisal3.size()==0){
                    projectMonitoringUnitEvaluationService.initAppraisal(t.getSysid());
                }
            });
        }
    }
}
