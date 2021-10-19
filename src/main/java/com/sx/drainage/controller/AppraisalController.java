package com.sx.drainage.controller;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.MonthlyException;
import com.sx.drainage.common.QuarterlyException;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 15:38
 */
@Api(value = "/api/appraisal", description = "考评管理 *")
@CrossOrigin
@RestController
@Transactional
@RequestMapping("/api/appraisal")
@RequiredArgsConstructor
public class AppraisalController {
    private final ProjectMonthlyReviewService projectMonthlyReviewService;
    private final OmAccountService omAccountService;
    private final OmTagService omTagService;
    private final ProjectCompanyService projectCompanyService;
    private final ProjectCompanyTagUserService projectCompanyTagUserService;
    private final ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;
    private final ProjectAnnualEvaluationService projectAnnualEvaluationService;
    private final ProjectStarRatingService projectStarRatingService;
    private final ProjectMonitoringUnitEvaluationService projectMonitoringUnitEvaluationService;

    @GetMapping("/getAppraisal")
    @ApiOperation(value = "获取月度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", required = true, dataType = "String")
    })
    public R getAppraisal(@RequestParam("projectId") String projectId, @RequestParam("year") String year) {
        Map<String, Object> data = new HashMap<>();
        List<ProjectMonthlyReviewEntity> list = projectMonthlyReviewService.getAppraisal(projectId, year);
        OmTagEntity tag = omTagService.getTagByName("监理单位");
        ProjectCompanyTagUserEntity companyTagUserEntity = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
        if (companyTagUserEntity != null) {
            ProjectCompanyEntity company = projectCompanyService.getCompany(companyTagUserEntity.getCompanyId());
            data.put("supervision", company.getName());
            data.put("appraisal", list);
            return R.ok(1, "获取成功!", data, true, null);
        } else {
            return R.error(500, "暂无监理单位");
        }
    }

    @GetMapping("/getAppraisalById/{sysId}")
    @ApiOperation(value = "获取单条月度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R getAppraisalById(@PathVariable("sysId") String sysId) {
        ProjectMonthlyReviewEntity entity = projectMonthlyReviewService.getById(sysId);
        return R.ok(1, "获取成功!", entity, true, null);
    }

    @PostMapping("/updateAppraisal")
    @ApiOperation(value = "月度考评信息填写 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateAppraisal(@RequestBody ProjectMonthlyReviewEntity entity, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity user = omAccountService.getUser(userId);
        entity.setAppraisalTime(new Date());
        entity.setUserId(userId);
        entity.setStates(1);
        entity.setUserName(user.getName());
        projectMonthlyReviewService.updateAppraisal(entity);
        return R.ok(1, "保存成功!", null, true, null);
    }

    @GetMapping("/getQuarterlyAssessment")
    @ApiOperation(value = "获取季度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", required = true, dataType = "String")
    })
    public R getQuarterlyAssessment(@RequestParam("projectId") String projectId, @RequestParam("year") String year) {
        Map<String, Object> data = new HashMap<>();
        List<ProjectQuarterlyAssessmentEntity> list = projectQuarterlyAssessmentService.getAppraisal(projectId, year);
        OmTagEntity tag = omTagService.getTagByName("监理单位");
        ProjectCompanyTagUserEntity companyTagUserEntity = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
        if (companyTagUserEntity != null) {
            ProjectCompanyEntity company = projectCompanyService.getCompany(companyTagUserEntity.getCompanyId());
            data.put("supervision", company.getName());
            data.put("appraisal", list);
            return R.ok(1, "获取成功!", data, true, null);
        } else {
            return R.error(500, "暂无监理单位");
        }
    }

    @GetMapping("/getQuarterlyAssessmentById/{sysId}")
    @ApiOperation(value = "获取单条季度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R getQuarterlyAssessmentById(@PathVariable("sysId") String sysId) {
        ProjectQuarterlyAssessmentEntity entity = projectQuarterlyAssessmentService.getById(sysId);
        return R.ok(1, "获取成功!", entity, true, null);
    }

    @PostMapping("/updateQuarterlyAssessment")
    @ApiOperation(value = "季度考评信息填写 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateQuarterlyAssessment(@RequestBody ProjectQuarterlyAssessmentEntity entity, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity user = omAccountService.getUser(userId);
        entity.setSubmitDate(new Date());
        entity.setUserId(userId);
        entity.setStates(1);
        entity.setUserName(user.getName());
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
        boolean res = false;
        float data = 0f;
        ProjectAnnualEvaluationEntity annualEvaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
        if (annualEvaluationEntity == null) {
            List<ProjectQuarterlyAssessmentEntity> appraisal = projectQuarterlyAssessmentService.getAppraisal(entity.getProjectId(), entity.getYear());
            ProjectAnnualEvaluationEntity evaluationEntity = new ProjectAnnualEvaluationEntity();
            evaluationEntity.setSysId(CreateUuid.uuid());
            evaluationEntity.setProjectId(entity.getProjectId());
            evaluationEntity.setYear(entity.getYear());
            evaluationEntity.setStates(0);
            appraisal.forEach(a -> {
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
            projectAnnualEvaluationService.initAppraisal(evaluationEntity);
        }
        annualEvaluationEntity = projectAnnualEvaluationService.getAppraisalByYear(entity.getProjectId(), entity.getYear());
        switch (entity.getQuarterly()) {
            case "第一季度":
                List<ProjectMonthlyReviewEntity> list1 = projectMonthlyReviewService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), one);
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i) != null && list1.get(i).getEvaluationScore() != null) {
                        data += list1.get(i).getEvaluationScore();
                    } else {
                        res = true;
                    }
                }
                if (!res) {
                    entity.setTotalScore(entity.getQuarterlyScore() * 0.4f + (data/3) * 0.6f);
                    annualEvaluationEntity.setFirstQuarter(entity.getTotalScore());
                    if(annualEvaluationEntity.getFirstQuarter()!=null&&
                            annualEvaluationEntity.getSecondQuarter()!=null&&
                            annualEvaluationEntity.getThirdQuarter()!=null&&
                            annualEvaluationEntity.getFourthQuarter()!=null&&
                            annualEvaluationEntity.getQualityAccident()!=null&&
                            annualEvaluationEntity.getSafetyIncident()!=null){
                        annualEvaluationEntity.setTotalScore((annualEvaluationEntity.getFirstQuarter() + annualEvaluationEntity.getSecondQuarter() + annualEvaluationEntity.getThirdQuarter() + annualEvaluationEntity.getFourthQuarter()) / 4f - annualEvaluationEntity.getQualityAccident() - annualEvaluationEntity.getSafetyIncident());
                        if (annualEvaluationEntity.getTotalScore() >= 90) {
                            annualEvaluationEntity.setEvaluation("优秀");
                        } else if (annualEvaluationEntity.getTotalScore() >= 80 && annualEvaluationEntity.getTotalScore() < 90) {
                            annualEvaluationEntity.setEvaluation("良");
                        } else if (annualEvaluationEntity.getTotalScore() >= 70 && annualEvaluationEntity.getTotalScore() < 80) {
                            annualEvaluationEntity.setEvaluation("合格");
                        } else {
                            annualEvaluationEntity.setEvaluation("不合格");
                        }
                    }
                }
                break;
            case "第二季度":
                List<ProjectMonthlyReviewEntity> list2 = projectMonthlyReviewService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), two);
                for (int i = 0; i < list2.size(); i++) {
                    if (list2.get(i) != null && list2.get(i).getEvaluationScore() != null) {
                        data += list2.get(i).getEvaluationScore();
                    } else {
                        res = true;
                    }
                }
                if (!res) {
                    entity.setTotalScore(entity.getQuarterlyScore() * 0.4f + (data/3) * 0.6f);
                    annualEvaluationEntity.setSecondQuarter(entity.getTotalScore());
                    if(annualEvaluationEntity.getFirstQuarter()!=null&&
                            annualEvaluationEntity.getSecondQuarter()!=null&&
                            annualEvaluationEntity.getThirdQuarter()!=null&&
                            annualEvaluationEntity.getFourthQuarter()!=null&&
                            annualEvaluationEntity.getQualityAccident()!=null&&
                            annualEvaluationEntity.getSafetyIncident()!=null){
                        annualEvaluationEntity.setTotalScore((annualEvaluationEntity.getFirstQuarter() + annualEvaluationEntity.getSecondQuarter() + annualEvaluationEntity.getThirdQuarter() + annualEvaluationEntity.getFourthQuarter()) / 4f - annualEvaluationEntity.getQualityAccident() - annualEvaluationEntity.getSafetyIncident());
                        if (annualEvaluationEntity.getTotalScore() >= 90) {
                            annualEvaluationEntity.setEvaluation("优秀");
                        } else if (annualEvaluationEntity.getTotalScore() >= 80 && annualEvaluationEntity.getTotalScore() < 90) {
                            annualEvaluationEntity.setEvaluation("良");
                        } else if (annualEvaluationEntity.getTotalScore() >= 70 && annualEvaluationEntity.getTotalScore() < 80) {
                            annualEvaluationEntity.setEvaluation("合格");
                        } else {
                            annualEvaluationEntity.setEvaluation("不合格");
                        }
                    }
                }
                break;
            case "第三季度":
                List<ProjectMonthlyReviewEntity> list3 = projectMonthlyReviewService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), three);
                for (int i = 0; i < list3.size(); i++) {
                    if (list3.get(i) != null && list3.get(i).getEvaluationScore() != null) {
                        data += list3.get(i).getEvaluationScore();
                    } else {
                        res = true;
                    }
                }
                if (!res) {
                    entity.setTotalScore(entity.getQuarterlyScore() * 0.4f + (data/3) * 0.6f);
                    annualEvaluationEntity.setThirdQuarter(entity.getTotalScore());
                    if(annualEvaluationEntity.getFirstQuarter()!=null&&
                            annualEvaluationEntity.getSecondQuarter()!=null&&
                            annualEvaluationEntity.getThirdQuarter()!=null&&
                            annualEvaluationEntity.getFourthQuarter()!=null&&
                            annualEvaluationEntity.getQualityAccident()!=null&&
                            annualEvaluationEntity.getSafetyIncident()!=null){
                        annualEvaluationEntity.setTotalScore((annualEvaluationEntity.getFirstQuarter() + annualEvaluationEntity.getSecondQuarter() + annualEvaluationEntity.getThirdQuarter() + annualEvaluationEntity.getFourthQuarter()) / 4f - annualEvaluationEntity.getQualityAccident() - annualEvaluationEntity.getSafetyIncident());
                        if (annualEvaluationEntity.getTotalScore() >= 90) {
                            annualEvaluationEntity.setEvaluation("优秀");
                        } else if (annualEvaluationEntity.getTotalScore() >= 80 && annualEvaluationEntity.getTotalScore() < 90) {
                            annualEvaluationEntity.setEvaluation("良");
                        } else if (annualEvaluationEntity.getTotalScore() >= 70 && annualEvaluationEntity.getTotalScore() < 80) {
                            annualEvaluationEntity.setEvaluation("合格");
                        } else {
                            annualEvaluationEntity.setEvaluation("不合格");
                        }
                    }
                }
                break;
            case "第四季度":
                List<ProjectMonthlyReviewEntity> list4 = projectMonthlyReviewService.getAppraisalByCondition(entity.getProjectId(), entity.getYear(), four);
                for (int i = 0; i < list4.size(); i++) {
                    if (list4.get(i) != null && list4.get(i).getEvaluationScore() != null) {
                        data += list4.get(i).getEvaluationScore();
                    } else {
                        res = true;
                    }
                }
                if (!res) {
                    entity.setTotalScore(entity.getQuarterlyScore() * 0.4f + (data/3) * 0.6f);
                    annualEvaluationEntity.setFourthQuarter(entity.getTotalScore());
                    if(annualEvaluationEntity.getFirstQuarter()!=null&&
                            annualEvaluationEntity.getSecondQuarter()!=null&&
                            annualEvaluationEntity.getThirdQuarter()!=null&&
                            annualEvaluationEntity.getFourthQuarter()!=null&&
                            annualEvaluationEntity.getQualityAccident()!=null&&
                            annualEvaluationEntity.getSafetyIncident()!=null){
                        annualEvaluationEntity.setTotalScore((annualEvaluationEntity.getFirstQuarter() + annualEvaluationEntity.getSecondQuarter() + annualEvaluationEntity.getThirdQuarter() + annualEvaluationEntity.getFourthQuarter()) / 4f - annualEvaluationEntity.getQualityAccident() - annualEvaluationEntity.getSafetyIncident());
                        if (annualEvaluationEntity.getTotalScore() >= 90) {
                            annualEvaluationEntity.setEvaluation("优秀");
                        } else if (annualEvaluationEntity.getTotalScore() >= 80 && annualEvaluationEntity.getTotalScore() < 90) {
                            annualEvaluationEntity.setEvaluation("良");
                        } else if (annualEvaluationEntity.getTotalScore() >= 70 && annualEvaluationEntity.getTotalScore() < 80) {
                            annualEvaluationEntity.setEvaluation("合格");
                        } else {
                            annualEvaluationEntity.setEvaluation("不合格");
                        }
                    }
                }
                break;
        }
        if(res){
            throw new MonthlyException();
        }
        projectAnnualEvaluationService.updateAppraisal(annualEvaluationEntity);
        projectQuarterlyAssessmentService.updateAppraisal(entity);
        return R.ok(1, "保存成功!", null, true, null);
    }

    @GetMapping("/getAnnualEvaluation")
    @ApiOperation(value = "获取年度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getAnnualEvaluation(@RequestParam("projectId") String projectId) {
        Map<String, Object> data = new HashMap<>();
        List<ProjectAnnualEvaluationEntity> list = projectAnnualEvaluationService.getAppraisal(projectId);
        OmTagEntity tag = omTagService.getTagByName("监理单位");
        ProjectCompanyTagUserEntity companyTagUserEntity = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
        if (companyTagUserEntity != null) {
            ProjectCompanyEntity company = projectCompanyService.getCompany(companyTagUserEntity.getCompanyId());
            data.put("supervision", company.getName());
            data.put("appraisal", list);
            return R.ok(1, "获取成功!", data, true, null);
        } else {
            return R.error(500, "暂无监理单位");
        }
    }

    @GetMapping("/getAnnualEvaluationById/{sysId}")
    @ApiOperation(value = "获取单条年度考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R getAnnualEvaluationById(@PathVariable("sysId") String sysId) {
        ProjectAnnualEvaluationEntity entity = projectAnnualEvaluationService.getById(sysId);
        return R.ok(1, "获取成功!", entity, true, null);
    }

    @PostMapping("/updateAnnualEvaluation")
    @ApiOperation(value = "年度考评信息填写 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateAnnualEvaluation(@RequestBody ProjectAnnualEvaluationEntity annualEvaluationEntity, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity user = omAccountService.getUser(userId);
        annualEvaluationEntity.setUserId(userId);
        annualEvaluationEntity.setStates(1);
        annualEvaluationEntity.setUserName(user.getName());
        if(annualEvaluationEntity.getFirstQuarter()!=null&&
                annualEvaluationEntity.getSecondQuarter()!=null&&
                annualEvaluationEntity.getThirdQuarter()!=null&&
                annualEvaluationEntity.getFourthQuarter()!=null&&
                annualEvaluationEntity.getQualityAccident()!=null&&
                annualEvaluationEntity.getSafetyIncident()!=null){
            annualEvaluationEntity.setTotalScore((annualEvaluationEntity.getFirstQuarter() + annualEvaluationEntity.getSecondQuarter() + annualEvaluationEntity.getThirdQuarter() + annualEvaluationEntity.getFourthQuarter()) / 4f - annualEvaluationEntity.getQualityAccident() - annualEvaluationEntity.getSafetyIncident());
            if (annualEvaluationEntity.getTotalScore() >= 90) {
                annualEvaluationEntity.setEvaluation("优秀");
            } else if (annualEvaluationEntity.getTotalScore() >= 80 && annualEvaluationEntity.getTotalScore() < 90) {
                annualEvaluationEntity.setEvaluation("良");
            } else if (annualEvaluationEntity.getTotalScore() >= 70 && annualEvaluationEntity.getTotalScore() < 80) {
                annualEvaluationEntity.setEvaluation("合格");
            } else {
                annualEvaluationEntity.setEvaluation("不合格");
            }
        }else{
            throw new QuarterlyException();
        }
        projectAnnualEvaluationService.updateAppraisal(annualEvaluationEntity);
        return R.ok(1, "保存成功!", null, true, null);
    }

    @GetMapping("/getStarRating")
    @ApiOperation(value = "获取星级考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", required = true, dataType = "String")
    })
    public R getStarRating(@RequestParam("projectId") String projectId, @RequestParam("year") String year) {
        Map<String, Object> data = new HashMap<>();
        List<ProjectStarRatingEntity> list = projectStarRatingService.getAppraisal(projectId, year);
        OmTagEntity tag = omTagService.getTagByName("监理单位");
        ProjectCompanyTagUserEntity companyTagUserEntity = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
        if (companyTagUserEntity != null) {
            ProjectCompanyEntity company = projectCompanyService.getCompany(companyTagUserEntity.getCompanyId());
            data.put("supervision", company.getName());
        } else {
            data.put("supervision", null);
        }
        data.put("appraisal", list);
        return R.ok(1, "获取成功!", data, true, null);
    }

    @GetMapping("/getStarRatingById/{sysId}")
    @ApiOperation(value = "获取单条星级考评信息 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R getStarRatingById(@PathVariable("sysId") String sysId) {
        ProjectStarRatingEntity entity = projectStarRatingService.getById(sysId);
        return R.ok(1, "获取成功!", entity, true, null);
    }

    @PostMapping("/updateStarRating")
    @ApiOperation(value = "星级考评信息填写 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateStarRating(@RequestBody ProjectStarRatingEntity entity, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity user = omAccountService.getUser(userId);
        entity.setUserId(userId);
        entity.setStates(1);
        entity.setUserName(user.getName());
        entity.setAppraisalTime(new Date());
        projectStarRatingService.updateAppraisal(entity);
        return R.ok(1, "保存成功!", null, true, null);
    }

    @GetMapping("/getMonitoringUnit")
    @ApiOperation(value = "获取监测单位考评 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "year", value = "年份", required = true, dataType = "String")
    })
    public R getMonitoringUnit(@RequestParam("projectId") String projectId, @RequestParam("year") String year) {
        Map<String, Object> data = new HashMap<>();
        List<ProjectMonitoringUnitEvaluationEntity> list = projectMonitoringUnitEvaluationService.getAppraisal(projectId, year);
        OmTagEntity tag = omTagService.getTagByName("监测单位");
        ProjectCompanyTagUserEntity companyTagUserEntity = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
        if (companyTagUserEntity != null) {
            ProjectCompanyEntity company = projectCompanyService.getCompany(companyTagUserEntity.getCompanyId());
            data.put("supervision", company.getName());
            data.put("appraisal", list);
            return R.ok(1, "获取成功!", data, true, null);
        } else {
            return R.error(500, "暂无监测单位!");
        }
    }

    @GetMapping("/getMonitoringUnitById/{sysId}")
    @ApiOperation(value = "获取单条监测单位考评 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R getMonitoringUnitById(@PathVariable("sysId") String sysId) {
        ProjectMonitoringUnitEvaluationEntity entity = projectMonitoringUnitEvaluationService.getById(sysId);
        return R.ok(1, "获取成功!", entity, true, null);
    }

    @PostMapping("/updateMonitoringUnit")
    @ApiOperation(value = "监测单位考评信息填写 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateMonitoringUnit(@RequestBody ProjectMonitoringUnitEvaluationEntity entity, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity user = omAccountService.getUser(userId);
        entity.setUserId(userId);
        entity.setStates(1);
        entity.setAppraisalTime(new Date());
        entity.setUserName(user.getName());
        projectMonitoringUnitEvaluationService.updateAppraisal(entity);
        return R.ok(1, "保存成功!", null, true, null);
    }

    @GetMapping("/getNumberOfStar/{projectId}")
    @ApiOperation(value = "获取各星级数量根据项目id *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "projectId", value = "项目id", required = false, dataType = "String")
    })
    public R getNumberOfStar(@PathVariable(value = "projectId", required = false) String projectId) {
        Map<String, Object> map = projectStarRatingService.getNumberOfStar(projectId);
        return R.ok(1, "获取成功!", map, true, null);
    }

    @GetMapping("/getNumberOfStar")
    @ApiOperation(value = "获取各星级数量根据项目id *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getNumberOfStars() {
        Map<String, Object> map = projectStarRatingService.getNumberOfStar(null);
        return R.ok(1, "获取成功!", map, true, null);
    }
}
