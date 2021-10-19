package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectMonthlyReviewDao;
import com.sx.drainage.entity.ProjectAnnualEvaluationEntity;
import com.sx.drainage.entity.ProjectMonthlyReviewEntity;
import com.sx.drainage.entity.ProjectQuarterlyAssessmentEntity;
import com.sx.drainage.entity.ProjectStarRatingEntity;
import com.sx.drainage.service.ProjectAnnualEvaluationService;
import com.sx.drainage.service.ProjectMonthlyReviewService;
import com.sx.drainage.service.ProjectQuarterlyAssessmentService;
import com.sx.drainage.service.ProjectStarRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 14:41
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectMonthlyReviewServiceImpl extends ServiceImpl<ProjectMonthlyReviewDao, ProjectMonthlyReviewEntity> implements ProjectMonthlyReviewService {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy");
    private final ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;
    private final ProjectAnnualEvaluationService projectAnnualEvaluationService;
    private final ProjectStarRatingService projectStarRatingService;
    /*
    * 初始化月度考评
    * */
    @Override
    public void initAppraisal(String projectId) {
        Date date = new Date();
        String year = format.format(date);
        List<ProjectMonthlyReviewEntity> list = new ArrayList<>();
        String [] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        for(int i=0;i<12;i++){
            ProjectMonthlyReviewEntity entity = new ProjectMonthlyReviewEntity();
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
     * 获取月度考评
     * */
    @Override
    public List<ProjectMonthlyReviewEntity> getAppraisal(String projectId, String year) {
        List<ProjectStarRatingEntity> stars = projectStarRatingService.getAppraisal(projectId, year);
        List<ProjectMonthlyReviewEntity> list = this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", projectId).eq("year", year).orderByAsc("orders"));
        list.forEach(l -> {
            List<ProjectStarRatingEntity> collect = stars.stream().filter(s -> l.getMonth().equals(s.getMonth())).collect(Collectors.toList());
            if(collect.size()>0){
                l.setStar(collect.get(0).getStar());
            }
        });
        return list;
    }
    /*
    * 修改月度考评
    * */
    @Override
    public void updateAppraisal(ProjectMonthlyReviewEntity entity) {
        this.updateById(entity);
        List<String> one = new ArrayList<>();
        List<String> two = new ArrayList<>();
        List<String> three = new ArrayList<>();
        List<String> four = new ArrayList<>();
        one.add("一月");
        one.add("二月");
        one.add("三月");
        two.add("四月");
        two.add("五月");
        two.add("六月");
        three.add("七月");
        three.add("八月");
        three.add("九月");
        four.add("十月");
        four.add("十一月");
        four.add("十二月");
        if(one.contains(entity.getMonth())){
            List<ProjectMonthlyReviewEntity> list = this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", entity.getProjectId()).eq("year", entity.getYear()).in("month", one));
            ProjectQuarterlyAssessmentEntity appraisal = projectQuarterlyAssessmentService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), "第一季度");
            if(appraisal!=null&&appraisal.getQuarterlyScore()!=null){
                boolean res = false;
                float data = 0f;
                for(int i=0;i<list.size();i++){
                    if(list.get(i)!=null&&list.get(i).getEvaluationScore()!=null){
                        data+=list.get(i).getEvaluationScore();
                    }else{
                        res=true;
                    }
                }
                if(!res){
                    appraisal.setTotalScore(appraisal.getQuarterlyScore()*0.4f+(data/3)*0.6f);
                    projectQuarterlyAssessmentService.updateAppraisal(appraisal);
                    ProjectAnnualEvaluationEntity evaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
                    if(evaluationEntity!=null){
                        List<ProjectQuarterlyAssessmentEntity> entities = projectQuarterlyAssessmentService.getAppraisal(entity.getProjectId(), entity.getYear());
                        entities.forEach(a -> {
                            if (a.getQuarterly().equals("第一季度")) {
                                evaluationEntity.setFirstQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第二季度")) {
                                evaluationEntity.setSecondQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第三季度")) {
                                evaluationEntity.setThirdQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第四季度")) {
                                evaluationEntity.setFourthQuarter(a.getTotalScore());
                            }
                        });
                        if(evaluationEntity.getFirstQuarter()!=null&&
                                evaluationEntity.getSecondQuarter()!=null&&
                                evaluationEntity.getThirdQuarter()!=null&&
                                evaluationEntity.getFourthQuarter()!=null&&
                                evaluationEntity.getQualityAccident()!=null&&
                                evaluationEntity.getSafetyIncident()!=null){
                            evaluationEntity.setTotalScore((evaluationEntity.getFirstQuarter() + evaluationEntity.getSecondQuarter() + evaluationEntity.getThirdQuarter() + evaluationEntity.getFourthQuarter()) / 4f - evaluationEntity.getQualityAccident() - evaluationEntity.getSafetyIncident());
                            if (evaluationEntity.getTotalScore() >= 90) {
                                evaluationEntity.setEvaluation("优秀");
                            } else if (evaluationEntity.getTotalScore() >= 80 && evaluationEntity.getTotalScore() < 90) {
                                evaluationEntity.setEvaluation("良");
                            } else if (evaluationEntity.getTotalScore() >= 70 && evaluationEntity.getTotalScore() < 80) {
                                evaluationEntity.setEvaluation("合格");
                            } else {
                                evaluationEntity.setEvaluation("不合格");
                            }
                        }
                        projectAnnualEvaluationService.updateAppraisal(evaluationEntity);
                    }
                }
            }
        }
        if(two.contains(entity.getMonth())){
            List<ProjectMonthlyReviewEntity> list = this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", entity.getProjectId()).eq("year", entity.getYear()).in("month", two));
            ProjectQuarterlyAssessmentEntity appraisal = projectQuarterlyAssessmentService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), "第二季度");
            if(appraisal!=null&&appraisal.getQuarterlyScore()!=null){
                boolean res = false;
                float data = 0f;
                for(int i=0;i<list.size();i++){
                    if(list.get(i)!=null&&list.get(i).getEvaluationScore()!=null){
                        data+=list.get(i).getEvaluationScore();
                    }else{
                        res=true;
                    }
                }
                if(!res){
                    appraisal.setTotalScore(appraisal.getQuarterlyScore()*0.4f+(data/3)*0.6f);
                    projectQuarterlyAssessmentService.updateAppraisal(appraisal);
                    ProjectAnnualEvaluationEntity evaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
                    if(evaluationEntity!=null){
                        List<ProjectQuarterlyAssessmentEntity> entities = projectQuarterlyAssessmentService.getAppraisal(entity.getProjectId(), entity.getYear());
                        entities.forEach(a -> {
                            if (a.getQuarterly().equals("第一季度")) {
                                evaluationEntity.setFirstQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第二季度")) {
                                evaluationEntity.setSecondQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第三季度")) {
                                evaluationEntity.setThirdQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第四季度")) {
                                evaluationEntity.setFourthQuarter(a.getTotalScore());
                            }
                        });
                        if(evaluationEntity.getFirstQuarter()!=null&&
                                evaluationEntity.getSecondQuarter()!=null&&
                                evaluationEntity.getThirdQuarter()!=null&&
                                evaluationEntity.getFourthQuarter()!=null&&
                                evaluationEntity.getQualityAccident()!=null&&
                                evaluationEntity.getSafetyIncident()!=null){
                            evaluationEntity.setTotalScore((evaluationEntity.getFirstQuarter() + evaluationEntity.getSecondQuarter() + evaluationEntity.getThirdQuarter() + evaluationEntity.getFourthQuarter()) / 4f - evaluationEntity.getQualityAccident() - evaluationEntity.getSafetyIncident());
                            if (evaluationEntity.getTotalScore() >= 90) {
                                evaluationEntity.setEvaluation("优秀");
                            } else if (evaluationEntity.getTotalScore() >= 80 && evaluationEntity.getTotalScore() < 90) {
                                evaluationEntity.setEvaluation("良");
                            } else if (evaluationEntity.getTotalScore() >= 70 && evaluationEntity.getTotalScore() < 80) {
                                evaluationEntity.setEvaluation("合格");
                            } else {
                                evaluationEntity.setEvaluation("不合格");
                            }
                        }
                        projectAnnualEvaluationService.updateAppraisal(evaluationEntity);
                    }
                }

            }
        }
        if(three.contains(entity.getMonth())){
            List<ProjectMonthlyReviewEntity> list = this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", entity.getProjectId()).eq("year", entity.getYear()).in("month", three));
            ProjectQuarterlyAssessmentEntity appraisal = projectQuarterlyAssessmentService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), "第三季度");
            if(appraisal!=null&&appraisal.getQuarterlyScore()!=null){
                boolean res = false;
                float data = 0f;
                for(int i=0;i<list.size();i++){
                    if(list.get(i)!=null&&list.get(i).getEvaluationScore()!=null){
                        data+=list.get(i).getEvaluationScore();
                    }else{
                        res=true;
                    }
                }
                if(!res){
                    appraisal.setTotalScore(appraisal.getQuarterlyScore()*0.4f+(data/3)*0.6f);
                    projectQuarterlyAssessmentService.updateAppraisal(appraisal);
                    ProjectAnnualEvaluationEntity evaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
                    if(evaluationEntity!=null){
                        List<ProjectQuarterlyAssessmentEntity> entities = projectQuarterlyAssessmentService.getAppraisal(entity.getProjectId(), entity.getYear());
                        entities.forEach(a -> {
                            if (a.getQuarterly().equals("第一季度")) {
                                evaluationEntity.setFirstQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第二季度")) {
                                evaluationEntity.setSecondQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第三季度")) {
                                evaluationEntity.setThirdQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第四季度")) {
                                evaluationEntity.setFourthQuarter(a.getTotalScore());
                            }
                        });
                        if(evaluationEntity.getFirstQuarter()!=null&&
                                evaluationEntity.getSecondQuarter()!=null&&
                                evaluationEntity.getThirdQuarter()!=null&&
                                evaluationEntity.getFourthQuarter()!=null&&
                                evaluationEntity.getQualityAccident()!=null&&
                                evaluationEntity.getSafetyIncident()!=null){
                            evaluationEntity.setTotalScore((evaluationEntity.getFirstQuarter() + evaluationEntity.getSecondQuarter() + evaluationEntity.getThirdQuarter() + evaluationEntity.getFourthQuarter()) / 4f - evaluationEntity.getQualityAccident() - evaluationEntity.getSafetyIncident());
                            if (evaluationEntity.getTotalScore() >= 90) {
                                evaluationEntity.setEvaluation("优秀");
                            } else if (evaluationEntity.getTotalScore() >= 80 && evaluationEntity.getTotalScore() < 90) {
                                evaluationEntity.setEvaluation("良");
                            } else if (evaluationEntity.getTotalScore() >= 70 && evaluationEntity.getTotalScore() < 80) {
                                evaluationEntity.setEvaluation("合格");
                            } else {
                                evaluationEntity.setEvaluation("不合格");
                            }
                        }
                        projectAnnualEvaluationService.updateAppraisal(evaluationEntity);
                    }
                }
            }
        }
        if(four.contains(entity.getMonth())){
            List<ProjectMonthlyReviewEntity> list = this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", entity.getProjectId()).eq("year", entity.getYear()).in("month", four));
            ProjectQuarterlyAssessmentEntity appraisal = projectQuarterlyAssessmentService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), "第四季度");
            if(appraisal!=null&&appraisal.getQuarterlyScore()!=null){
                boolean res = false;
                float data = 0f;
                for(int i=0;i<list.size();i++){
                    if(list.get(i)!=null&&list.get(i).getEvaluationScore()!=null){
                        data+=list.get(i).getEvaluationScore();
                    }else{
                        res=true;
                    }
                }
                if(!res){
                    appraisal.setTotalScore(appraisal.getQuarterlyScore()*0.4f+(data/3)*0.6f);
                    projectQuarterlyAssessmentService.updateAppraisal(appraisal);
                    ProjectAnnualEvaluationEntity evaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
                    if(evaluationEntity!=null){
                        List<ProjectQuarterlyAssessmentEntity> entities = projectQuarterlyAssessmentService.getAppraisal(entity.getProjectId(), entity.getYear());
                        entities.forEach(a -> {
                            if (a.getQuarterly().equals("第一季度")) {
                                evaluationEntity.setFirstQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第二季度")) {
                                evaluationEntity.setSecondQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第三季度")) {
                                evaluationEntity.setThirdQuarter(a.getTotalScore());
                            }
                            if (a.getQuarterly().equals("第四季度")) {
                                evaluationEntity.setFourthQuarter(a.getTotalScore());
                            }
                        });
                        if(evaluationEntity.getFirstQuarter()!=null&&
                                evaluationEntity.getSecondQuarter()!=null&&
                                evaluationEntity.getThirdQuarter()!=null&&
                                evaluationEntity.getFourthQuarter()!=null&&
                                evaluationEntity.getQualityAccident()!=null&&
                                evaluationEntity.getSafetyIncident()!=null){
                            evaluationEntity.setTotalScore((evaluationEntity.getFirstQuarter() + evaluationEntity.getSecondQuarter() + evaluationEntity.getThirdQuarter() + evaluationEntity.getFourthQuarter()) / 4f - evaluationEntity.getQualityAccident() - evaluationEntity.getSafetyIncident());
                            if (evaluationEntity.getTotalScore() >= 90) {
                                evaluationEntity.setEvaluation("优秀");
                            } else if (evaluationEntity.getTotalScore() >= 80 && evaluationEntity.getTotalScore() < 90) {
                                evaluationEntity.setEvaluation("良");
                            } else if (evaluationEntity.getTotalScore() >= 70 && evaluationEntity.getTotalScore() < 80) {
                                evaluationEntity.setEvaluation("合格");
                            } else {
                                evaluationEntity.setEvaluation("不合格");
                            }
                        }
                        projectAnnualEvaluationService.updateAppraisal(evaluationEntity);
                    }
                }
            }
        }
    }
    /*
    * 获取一季度月度考评
    * */
    @Override
    public List<ProjectMonthlyReviewEntity> getAppraisalByCondition(String projectId, String year, List<String> month) {
        return this.list(new QueryWrapper<ProjectMonthlyReviewEntity>().eq("projectId", projectId).eq("year", year).in("month", month));
    }
}
