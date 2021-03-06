package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.params.ProjectBasicParams;
import com.sx.drainage.params.ProjectParams;
import com.sx.drainage.params.ProjectParticipantsParams;
import com.sx.drainage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.ProjectProjectDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectProjectService")
@Transactional
public class ProjectProjectServiceImpl extends ServiceImpl<ProjectProjectDao, ProjectProjectEntity> implements ProjectProjectService {

    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmAccountService omAccountService;
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;
    @Autowired
    private ProjectProjectbasicinfoService projectProjectbasicinfoService;
    @Autowired
    private ChengjianFileuploadService chengjianFileuploadService;
    @Autowired
    private ProjectCompanyTagUserService projectCompanyTagUserService;
    @Autowired
    private ProjectCompanyService projectCompanyService;
    @Autowired
    private ProjectWbsService projectWbsService;
    @Autowired
    private ProjectWbsbindgroupsourceriskService projectWbsbindgroupsourceriskService;
    @Autowired
    private ProjectWbsbindgroupequipmentService projectWbsbindgroupequipmentService;
    @Autowired
    private InspectionpriceService inspectionpriceService;
    @Autowired
    private ProjectWbsbindgrouprectificationService projectWbsbindgrouprectificationService;
    @Autowired
    private ProjectReportService projectReportService;
    @Autowired
    private OmTagService omTagService;
    @Autowired
    private ProjectParentProjectService projectParentProjectService;
    @Autowired
    private ProjectPhaseService projectPhaseService;
    @Autowired
    private ProjectInformationService projectInformationService;
    @Autowired
    private ProjectHistoryService historyService;
    @Autowired
    private ProjectMonthlyReviewService projectMonthlyReviewService;
    @Autowired
    private ActivitiWorkFlowService activitiWorkFlowService;
    @Autowired
    private ProjectQuarterlyAssessmentService projectQuarterlyAssessmentService;
    @Autowired
    private ProjectStarRatingService projectStarRatingService;
    @Autowired
    private ProjectMonitoringUnitEvaluationService projectMonitoringUnitEvaluationService;
    @Autowired
    private ProjectProcessDocumentationService projectProcessDocumentationService;
    @Autowired
    private ProjectStopProcessService projectStopProcessService;
    @Autowired
    private Environment env;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectProjectEntity> page = this.page(
                new Query<ProjectProjectEntity>().getPage(params),
                new QueryWrapper<ProjectProjectEntity>()
        );

        return new PageUtils(page);
    }

    //??????????????????
    @Override
    public List<Map<String, Object>> getAllProject(String userId) {
        //?????????????????????????????????ID
        List<String> list = omRelAccountRoleService.getAllRoleId(userId);
        //???????????????????????????????????????
        List<OmRoleEntity> roles = omRoleService.getAllMyRole(list);
        //?????????????????????????????????????????????
        boolean role = false;
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getName().equals("???????????????")) {
                role = true;
            }
        }
        if (role) {
            //?????????????????????????????????????????????????????????
            List<ProjectProjectEntity> project = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).orderByAsc("sort"));
//            List<ProjectProjectEntity> parent = new ArrayList<>();
//            project.forEach(li -> {
//                if(li.getParentid()==null){
//                    parent.add(li);
//                }
//            });
//            project.removeAll(parent);
            List<Map<String, Object>> map = new ArrayList<>();
            List<ProjectParentProjectEntity> parents = projectParentProjectService.getAll();
            parents.forEach(li -> {
                List<Map<String, Object>> childs = new ArrayList<>();
                List<String> list1 = new ArrayList<>();
                project.forEach(pr -> {
                    if (li.getSysId().equals(pr.getParentid())) {
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", childs.size() + 1);
                        hashMap.put("sysId", pr.getSysid());
                        hashMap.put("parentId", pr.getParentid());
                        hashMap.put("type", pr.getType());
                        hashMap.put("parentName", li.getName());
                        hashMap.put("code", pr.getCode());
                        hashMap.put("name", pr.getName());
                        hashMap.put("shortName", pr.getShortname());
                        hashMap.put("managerId", pr.getManagerid());
                        hashMap.put("telephone", pr.getTelephone());
                        hashMap.put("actualStartDate", pr.getActualstartdate());
                        hashMap.put("planStartDate", pr.getPlanstartdate());
                        hashMap.put("planEndDate", pr.getPlanenddate());
                        hashMap.put("status", pr.getStatus());
                        hashMap.put("province", pr.getProvince());
                        hashMap.put("city", pr.getCity());
                        hashMap.put("address", pr.getAddress());
                        hashMap.put("weekDay", pr.getWeekday());
                        hashMap.put("monthDay", pr.getMonthday());
                        hashMap.put("del", pr.getDel());
                        hashMap.put("creatorId", pr.getCreatorid());
                        hashMap.put("createDate", pr.getCreatedate());
                        hashMap.put("bimpicid", pr.getBimpicid());
                        hashMap.put("constructionUnit", pr.getConstructionunit());
                        hashMap.put("supervisionUnit", pr.getSupervisionunit());
                        hashMap.put("designUnit", pr.getDesignunit());
                        hashMap.put("bimpicid2", pr.getBimpicid2());
                        hashMap.put("bimpicid3", pr.getBimpicid3());
                        hashMap.put("ApproveState", pr.getApprovestate());
                        hashMap.put("PMSCode", pr.getPmscode());
                        hashMap.put("PMSSysId", pr.getPmssysid());
                        hashMap.put("IsSyncDate", pr.getIssyncdate());
                        hashMap.put("IsSyncData", pr.getIssyncdata());
                        hashMap.put("fileId", pr.getFileid());
                        Map<String, Object> details = projectProjectbasicinfoService.getProjectDetails(pr.getSysid());
                        hashMap.put("creatorName", details.get("createUser"));
                        hashMap.put("_createDate", details.get("createDate"));
                        List<ProjectWbsEntity> wbs = projectWbsService.getWbsDetails(pr.getSysid());
                        hashMap.put("_actualStartDate", "");
                        if (wbs.size() == 0) {
                            hashMap.put("_planStartDate", "");
                            hashMap.put("_planEndDate", "");
                        } else {
                            hashMap.put("_planStartDate", wbs.get(0).getPlanbegindate());
                            hashMap.put("_planEndDate", wbs.get(0).getPlanenddate());
                        }
                        hashMap.put("state", "");
                        ProjectProjectbasicinfoEntity projectEntity = projectProjectbasicinfoService.getProjectEntity(pr.getSysid());
                        if (projectEntity != null) {
                            hashMap.put("price", projectEntity.getContractamount() != null ? String.format("%.2f", Double.valueOf(projectEntity.getContractamount())) : 0);
                            if(projectEntity.getContractamount()==null){
                                list1.add("0.00");
                            }else{
                                list1.add(String.format("%.2f", Double.valueOf(projectEntity.getContractamount())));
                            }
                        }
                        childs.add(hashMap);
                    }
                });
                Map<String, Object> hash = new HashMap<>();
                hash.put("id", map.size() + 1);
                hash.put("sysId", li.getSysId());
                hash.put("name", li.getName());
                hash.put("type", li.getType());
                hash.put("fileId", li.getFileId());
                hash.put("projectDescribe", li.getProjectDescribe());
                hash.put("childs", childs);
                if (list1.size() > 0) {
                    double totalPrice = 0;
                    for (int i = 0; i < list1.size(); i++) {
                        totalPrice += Double.valueOf(list1.get(i));
                    }
                    hash.put("price", String.format("%.2f", totalPrice));
                } else {
                    hash.put("price", 0);
                }
                List<ProjectProjectEntity> end = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByDesc("planEndDate"));
                List<ProjectProjectEntity> start = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByAsc("planStartDate"));
                if (start != null && start.size() > 0) {
                    hash.put("planStartDate", start.get(0).getPlanstartdate());
                } else {
                    hash.put("planStartDate", null);
                }
                if (end != null && end.size() > 0) {
                    hash.put("planEndDate", end.get(0).getPlanenddate());
                } else {
                    hash.put("planEndDate", null);
                }
                map.add(hash);
            });
           /* parent.forEach(pr -> {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", map.size() + 1);
                hashMap.put("sysId", pr.getSysid());
                hashMap.put("parentId", pr.getParentid());
                hashMap.put("code", pr.getCode());
                hashMap.put("name", pr.getName());
                hashMap.put("shortName", pr.getShortname());
                hashMap.put("managerId", pr.getManagerid());
                hashMap.put("telephone", pr.getTelephone());
                hashMap.put("actualStartDate", pr.getActualstartdate());
                hashMap.put("planStartDate", pr.getPlanstartdate());
                hashMap.put("planEndDate", pr.getPlanenddate());
                hashMap.put("status", pr.getStatus());
                hashMap.put("province", pr.getProvince());
                hashMap.put("city", pr.getCity());
                hashMap.put("address", pr.getAddress());
                hashMap.put("weekDay", pr.getWeekday());
                hashMap.put("monthDay", pr.getMonthday());
                hashMap.put("del", pr.getDel());
                hashMap.put("creatorId", pr.getCreatorid());
                hashMap.put("createDate", pr.getCreatedate());
                hashMap.put("bimpicid", pr.getBimpicid());
                hashMap.put("constructionUnit", pr.getConstructionunit());
                hashMap.put("supervisionUnit", pr.getSupervisionunit());
                hashMap.put("designUnit", pr.getDesignunit());
                hashMap.put("bimpicid2", pr.getBimpicid2());
                hashMap.put("bimpicid3", pr.getBimpicid3());
                hashMap.put("ApproveState", pr.getApprovestate());
                hashMap.put("PMSCode", pr.getPmscode());
                hashMap.put("PMSSysId", pr.getPmssysid());
                hashMap.put("IsSyncDate", pr.getIssyncdate());
                hashMap.put("IsSyncData", pr.getIssyncdata());
                Map<String,Object> details = projectProjectbasicinfoService.getProjectDetails(pr.getSysid());
                hashMap.put("creatorName", details.get("createUser"));
                hashMap.put("_createDate", details.get("createDate"));
                List<ProjectWbsEntity> wbs=projectWbsService.getWbsDetails(pr.getSysid());
                hashMap.put("_actualStartDate", "");
                if(wbs.size()==0){
                    hashMap.put("_planStartDate", "");
                    hashMap.put("_planEndDate", "");
                }else{
                    hashMap.put("_planStartDate", wbs.get(0).getPlanbegindate());
                    hashMap.put("_planEndDate", wbs.get(0).getPlanenddate());
                }
                hashMap.put("state", "");
                hashMap.put("isParent",false);
                hashMap.put("childs",null);
                map.add(hashMap);
            });*/
            return map;
        }
        OmAccountEntity user = omAccountService.getUser(userId);
        if(user!=null&&user.getCompanyId().equals(env.getProperty("init.companyId"))){
            List<ProjectProjectEntity> project = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).orderByAsc("sort"));
//            List<ProjectProjectEntity> parent = new ArrayList<>();
//            project.forEach(li -> {
//                if(li.getParentid()==null){
//                    parent.add(li);
//                }
//            });
//            project.removeAll(parent);
            List<Map<String, Object>> map = new ArrayList<>();
            List<ProjectParentProjectEntity> parents = projectParentProjectService.getAll();
            parents.forEach(li -> {
                List<Map<String, Object>> childs = new ArrayList<>();
                List<String> list1 = new ArrayList<>();
                project.forEach(pr -> {
                    if (li.getSysId().equals(pr.getParentid())) {
                        Map<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", childs.size() + 1);
                        hashMap.put("sysId", pr.getSysid());
                        hashMap.put("parentId", pr.getParentid());
                        hashMap.put("type", pr.getType());
                        hashMap.put("parentName", li.getName());
                        hashMap.put("code", pr.getCode());
                        hashMap.put("name", pr.getName());
                        hashMap.put("shortName", pr.getShortname());
                        hashMap.put("managerId", pr.getManagerid());
                        hashMap.put("telephone", pr.getTelephone());
                        hashMap.put("actualStartDate", pr.getActualstartdate());
                        hashMap.put("planStartDate", pr.getPlanstartdate());
                        hashMap.put("planEndDate", pr.getPlanenddate());
                        hashMap.put("status", pr.getStatus());
                        hashMap.put("province", pr.getProvince());
                        hashMap.put("city", pr.getCity());
                        hashMap.put("address", pr.getAddress());
                        hashMap.put("weekDay", pr.getWeekday());
                        hashMap.put("monthDay", pr.getMonthday());
                        hashMap.put("del", pr.getDel());
                        hashMap.put("creatorId", pr.getCreatorid());
                        hashMap.put("createDate", pr.getCreatedate());
                        hashMap.put("bimpicid", pr.getBimpicid());
                        hashMap.put("constructionUnit", pr.getConstructionunit());
                        hashMap.put("supervisionUnit", pr.getSupervisionunit());
                        hashMap.put("designUnit", pr.getDesignunit());
                        hashMap.put("bimpicid2", pr.getBimpicid2());
                        hashMap.put("bimpicid3", pr.getBimpicid3());
                        hashMap.put("ApproveState", pr.getApprovestate());
                        hashMap.put("PMSCode", pr.getPmscode());
                        hashMap.put("PMSSysId", pr.getPmssysid());
                        hashMap.put("IsSyncDate", pr.getIssyncdate());
                        hashMap.put("IsSyncData", pr.getIssyncdata());
                        hashMap.put("fileId", pr.getFileid());
                        Map<String, Object> details = projectProjectbasicinfoService.getProjectDetails(pr.getSysid());
                        hashMap.put("creatorName", details.get("createUser"));
                        hashMap.put("_createDate", details.get("createDate"));
                        List<ProjectWbsEntity> wbs = projectWbsService.getWbsDetails(pr.getSysid());
                        hashMap.put("_actualStartDate", "");
                        if (wbs.size() == 0) {
                            hashMap.put("_planStartDate", "");
                            hashMap.put("_planEndDate", "");
                        } else {
                            hashMap.put("_planStartDate", wbs.get(0).getPlanbegindate());
                            hashMap.put("_planEndDate", wbs.get(0).getPlanenddate());
                        }
                        hashMap.put("state", "");
                        ProjectProjectbasicinfoEntity projectEntity = projectProjectbasicinfoService.getProjectEntity(pr.getSysid());
                        if (projectEntity != null) {
                            hashMap.put("price", projectEntity.getContractamount() != null ? String.format("%.2f", Double.valueOf(projectEntity.getContractamount())) : 0);
                            if(projectEntity.getContractamount()==null){
                                list1.add("0.00");
                            }else{
                                list1.add(String.format("%.2f", Double.valueOf(projectEntity.getContractamount())));
                            }
                        }
                        childs.add(hashMap);
                    }
                });
                Map<String, Object> hash = new HashMap<>();
                hash.put("id", map.size() + 1);
                hash.put("sysId", li.getSysId());
                hash.put("name", li.getName());
                hash.put("type", li.getType());
                hash.put("fileId", li.getFileId());
                hash.put("projectDescribe", li.getProjectDescribe());
                hash.put("childs", childs);
                if (list1.size() > 0) {
                    double totalPrice = 0;
                    for (int i = 0; i < list1.size(); i++) {
                        totalPrice += Double.valueOf(list1.get(i));
                    }
                    hash.put("price", String.format("%.2f", totalPrice));
                } else {
                    hash.put("price", 0);
                }
                List<ProjectProjectEntity> end = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByDesc("planEndDate"));
                List<ProjectProjectEntity> start = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByAsc("planStartDate"));
                if (start != null && start.size() > 0) {
                    hash.put("planStartDate", start.get(0).getPlanstartdate());
                } else {
                    hash.put("planStartDate", null);
                }
                if (end != null && end.size() > 0) {
                    hash.put("planEndDate", end.get(0).getPlanenddate());
                } else {
                    hash.put("planEndDate", null);
                }
                map.add(hash);
            });
            return map;
        }
        //?????????????????????????????????????????????????????????????????????
        //1.??????????????????????????????id
        List<String> projectId = omTagRelPostAccountService.getAllProjectId(userId);
        List<ProjectProjectEntity> list2 = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("creatorId", userId));
        List<ProjectProjectEntity> project = null;
        if(projectId==null||projectId.size()==0){
            project = new ArrayList<>();
        }else {
            project = this.list(new QueryWrapper<ProjectProjectEntity>().in("sysId", projectId).orderByAsc("sort"));
        }
        if(list2!=null&&list2.size()>0){
            project.addAll(list2);
            project=project.stream().filter(distinctByKey(ProjectProjectEntity::getSysid)).collect(Collectors.toList());
        }
        List<ProjectProjectEntity> projects=project;
        List<ProjectParentProjectEntity> parents = projectParentProjectService.getAll();
        //List<ProjectProjectEntity> parent = new ArrayList<>();
/*        project.forEach(li -> {
            if(li.getParentid()==null){
                parent.add(li);
            }
        });
        project.removeAll(parent);*/
        List<Map<String, Object>> map = new ArrayList<>();
        parents.forEach(li -> {
            List<Map<String, Object>> childs = new ArrayList<>();
            List<String> list1 = new ArrayList<>();
            projects.forEach(pr -> {
                if (li.getSysId().equals(pr.getParentid())) {
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", childs.size() + 1);
                    hashMap.put("sysId", pr.getSysid());
                    hashMap.put("parentId", pr.getParentid());
                    hashMap.put("type", pr.getType());
                    hashMap.put("parentName", li.getName());
                    hashMap.put("code", pr.getCode());
                    hashMap.put("name", pr.getName());
                    hashMap.put("shortName", pr.getShortname());
                    hashMap.put("managerId", pr.getManagerid());
                    hashMap.put("telephone", pr.getTelephone());
                    hashMap.put("actualStartDate", pr.getActualstartdate());
                    hashMap.put("planStartDate", pr.getPlanstartdate());
                    hashMap.put("planEndDate", pr.getPlanenddate());
                    hashMap.put("status", pr.getStatus());
                    hashMap.put("province", pr.getProvince());
                    hashMap.put("city", pr.getCity());
                    hashMap.put("address", pr.getAddress());
                    hashMap.put("weekDay", pr.getWeekday());
                    hashMap.put("monthDay", pr.getMonthday());
                    hashMap.put("del", pr.getDel());
                    hashMap.put("creatorId", pr.getCreatorid());
                    hashMap.put("createDate", pr.getCreatedate());
                    hashMap.put("bimpicid", pr.getBimpicid());
                    hashMap.put("constructionUnit", pr.getConstructionunit());
                    hashMap.put("supervisionUnit", pr.getSupervisionunit());
                    hashMap.put("designUnit", pr.getDesignunit());
                    hashMap.put("bimpicid2", pr.getBimpicid2());
                    hashMap.put("bimpicid3", pr.getBimpicid3());
                    hashMap.put("ApproveState", pr.getApprovestate());
                    hashMap.put("PMSCode", pr.getPmscode());
                    hashMap.put("PMSSysId", pr.getPmssysid());
                    hashMap.put("IsSyncDate", pr.getIssyncdate());
                    hashMap.put("IsSyncData", pr.getIssyncdata());
                    hashMap.put("fileId", pr.getFileid());
                    Map<String, Object> details = projectProjectbasicinfoService.getProjectDetails(pr.getSysid());
                    hashMap.put("creatorName", details.get("createUser"));
                    hashMap.put("_createDate", details.get("createDate"));
                    List<ProjectWbsEntity> wbs = projectWbsService.getWbsDetails(pr.getSysid());
                    hashMap.put("_actualStartDate", "");
                    if (wbs.size() == 0) {
                        hashMap.put("_planStartDate", "");
                        hashMap.put("_planEndDate", "");
                    } else {
                        hashMap.put("_planStartDate", wbs.get(0).getPlanbegindate());
                        hashMap.put("_planEndDate", wbs.get(0).getPlanenddate());
                    }
                    hashMap.put("state", "");
                    ProjectProjectbasicinfoEntity projectEntity = projectProjectbasicinfoService.getProjectEntity(pr.getSysid());
                    if (projectEntity != null) {
                        hashMap.put("price", projectEntity.getContractamount() != null ? String.format("%.2f", Double.valueOf(projectEntity.getContractamount())) : 0);
                        if(projectEntity.getContractamount()==null){
                            list1.add("0.00");
                        }else{
                            list1.add(String.format("%.2f", Double.valueOf(projectEntity.getContractamount())));
                        }
                    }
                    childs.add(hashMap);
                }
            });
            if (childs.size() > 0 || li.getCreateUser().equals(userId)) {
                Map<String, Object> hash = new HashMap<>();
                hash.put("id", map.size() + 1);
                hash.put("sysId", li.getSysId());
                hash.put("name", li.getName());
                hash.put("type", li.getType());
                hash.put("fileId", li.getFileId());
                hash.put("projectDescribe", li.getProjectDescribe());
                hash.put("childs", childs);
                if (list1.size() > 0) {
                    double totalPrice = 0;
                    for (int i = 0; i < list1.size(); i++) {
                        totalPrice += Double.valueOf(list1.get(i));
                    }
                    hash.put("price", String.format("%.2f", totalPrice));
                } else {
                    hash.put("price", 0);
                }
                List<ProjectProjectEntity> end = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByDesc("planEndDate"));
                List<ProjectProjectEntity> start = this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", li.getSysId()).orderByAsc("planStartDate"));
                if (start != null && start.size() > 0) {
                    hash.put("planStartDate", start.get(0).getPlanstartdate());
                } else {
                    hash.put("planStartDate", null);
                }
                if (end != null && end.size() > 0) {
                    hash.put("planEndDate", end.get(0).getPlanenddate());
                } else {
                    hash.put("planEndDate", null);
                }
                map.add(hash);
            }
        });
        /*parent.forEach(pr -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", map.size() + 1);
            hashMap.put("sysId", pr.getSysid());
            hashMap.put("parentId", pr.getParentid());
            hashMap.put("code", pr.getCode());
            hashMap.put("name", pr.getName());
            hashMap.put("shortName", pr.getShortname());
            hashMap.put("managerId", pr.getManagerid());
            hashMap.put("telephone", pr.getTelephone());
            hashMap.put("actualStartDate", pr.getActualstartdate());
            hashMap.put("planStartDate", pr.getPlanstartdate());
            hashMap.put("planEndDate", pr.getPlanenddate());
            hashMap.put("status", pr.getStatus());
            hashMap.put("province", pr.getProvince());
            hashMap.put("city", pr.getCity());
            hashMap.put("address", pr.getAddress());
            hashMap.put("weekDay", pr.getWeekday());
            hashMap.put("monthDay", pr.getMonthday());
            hashMap.put("del", pr.getDel());
            hashMap.put("creatorId", pr.getCreatorid());
            hashMap.put("createDate", pr.getCreatedate());
            hashMap.put("bimpicid", pr.getBimpicid());
            hashMap.put("constructionUnit", pr.getConstructionunit());
            hashMap.put("supervisionUnit", pr.getSupervisionunit());
            hashMap.put("designUnit", pr.getDesignunit());
            hashMap.put("bimpicid2", pr.getBimpicid2());
            hashMap.put("bimpicid3", pr.getBimpicid3());
            hashMap.put("ApproveState", pr.getApprovestate());
            hashMap.put("PMSCode", pr.getPmscode());
            hashMap.put("PMSSysId", pr.getPmssysid());
            hashMap.put("IsSyncDate", pr.getIssyncdate());
            hashMap.put("IsSyncData", pr.getIssyncdata());
            Map<String,Object> details = projectProjectbasicinfoService.getProjectDetails(pr.getSysid());
            hashMap.put("creatorName", details.get("createUser"));
            hashMap.put("_createDate", details.get("createDate"));
            List<ProjectWbsEntity> wbs=projectWbsService.getWbsDetails(pr.getSysid());
            hashMap.put("_actualStartDate", "");
            if(wbs.size()==0){
                hashMap.put("_planStartDate", "");
                hashMap.put("_planEndDate", "");
            }else{
                hashMap.put("_planStartDate", wbs.get(0).getPlanbegindate());
                hashMap.put("_planEndDate", wbs.get(0).getPlanenddate());
            }
            hashMap.put("state", "");
            hashMap.put("isParent",false);
            hashMap.put("childs",null);
            map.add(hashMap);
        });*/
        return map;
    }

    //????????????????????????
    @Override
    public Map<String, Object> getProjectDetails(String projectId) {
        return projectProjectbasicinfoService.getProjectDetails(projectId);
    }

    //????????????
    @Override
    public List<Map<String, Object>> getImage(String projectId) {
        return chengjianFileuploadService.getImage(projectId);
    }

    //?????????????????????????????????????????????
    @Override
    public List<Map<String, Object>> getProjectJoinPersons(String projectId) {
        List<ProjectCompanyTagUserEntity> details = projectCompanyTagUserService.getProjectJoinPersons(projectId);
        List<Map<String, Object>> list = new ArrayList<>();
        details.forEach(li -> {
            ProjectCompanyEntity company = projectCompanyService.getCompany(li.getCompanyId());
            OmAccountEntity user = omAccountService.getUser(li.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("account_id", user.getSysid());
            map.put("sysId", li.getId());
            map.put("projectId", projectId);
            map.put("department", company.getName());
            map.put("userName", user.getName());
            map.put("phone", user.getMobile());
            map.put("name", omTagService.getById(li.getTagId()).getName());
            list.add(map);
        });
        return list;
    }

    //?????????????????????????????????
    @Override
    public List<Map<String, Object>> getProjectNowSide(String projectId) {
        return projectWbsService.getProjectNowSide(projectId);
    }

    //????????????????????????????????????
    @Override
    public List<ProjectWbsbindgroupsourceriskEntity> getNoProcessSourceRisk(String projectId) {
        List<String> wbsId = projectWbsService.getWbsId(projectId);
        return projectWbsbindgroupsourceriskService.getNoProcessSourceRisk(wbsId);
    }

    //???????????????????????????????????????
    @Override
    public List<ProjectWbsbindgroupequipmentEntity> getNoProcessDeviceNow(String projectId) {
        List<String> wbsId = projectWbsService.getWbsId(projectId);
        return projectWbsbindgroupequipmentService.getNoProcessDeviceNow(wbsId);
    }

    //????????????????????????????????????
    @Override
    public List<InspectionpriceEntity> getProjectPrice(String projectId) {
        return inspectionpriceService.getProjectPrice(projectId);
    }

    //??????????????????????????????????????????
    @Override
    public Map<String, Object> getNoProcessRectification(String projectId, Integer type) {
        Map<String, Object> map = new HashMap<>();
        if (type == 1) {
            List<ProjectReportEntity> noProcessRectification = projectReportService.getNoProcessRectification(projectId, "1");
            map.put("allCount", noProcessRectification.size());
            map.put("completeCount", 0);
        }
        if (type == 2) {
            List<ProjectReportEntity> noProcessRectification = projectReportService.getNoProcessRectification(projectId, "3");
            map.put("allCount", noProcessRectification.size());
            map.put("completeCount", 0);
        }
        return map;

    }

    //?????????????????????????????????
    @Override
    public Map<String, Object> getProjectMajorNode(String projectId) {
        return projectWbsService.getProjectMajorNode(projectId);
    }

    //???????????????????????????
    @Override
    public List<Map<String, Object>> getTwoAdmin() {
        List<String> userId = omRelAccountRoleService.getAllUserId("aefc867c36bd4f36aaef31cb37238590");
        List<Map<String, Object>> list = omAccountService.getTwoUser(userId);
        return list;
    }

    //???????????????????????????
    @Override
    public Map<String, Object> getProjectInfo(String sysId) {
        ProjectProjectEntity entity = this.getById(sysId);
        Map<String, Object> map = new HashMap<>();
        if (entity != null) {
            map.put("sysId", entity.getSysid());
            map.put("parentId", entity.getParentid());
            ProjectParentProjectEntity parent = projectParentProjectService.getById(entity.getParentid());
            map.put("parentName", parent == null ? null : parent.getName());
            map.put("code", entity.getCode());
            map.put("name", entity.getName());
            map.put("shortName", entity.getShortname());
            map.put("managerId", entity.getManagerid());
            map.put("telephone", entity.getTelephone());
            map.put("actualStartDate", entity.getActualstartdate());
            map.put("planStartDate", entity.getPlanstartdate());
            map.put("planEndDate", entity.getPlanenddate());
            map.put("status", entity.getStatus());
            map.put("province", entity.getProvince());
            map.put("city", entity.getCity());
            map.put("address", entity.getAddress());
            map.put("weekDay", entity.getWeekday());
            map.put("monthDay", entity.getMonthday());
            map.put("del", entity.getDel());
            map.put("creatorId", entity.getCreatorid());
            map.put("createDate", entity.getCreatedate());
            map.put("bimpicid", entity.getBimpicid());
            map.put("constructionUnit", entity.getConstructionunit());
            map.put("supervisionUnit", entity.getSupervisionunit());
            map.put("designUnit", entity.getDesignunit());
            map.put("bimpicid2", entity.getBimpicid2());
            map.put("bimpicid3", entity.getBimpicid3());
            map.put("ApproveState", entity.getApprovestate());
            map.put("PMSCode", entity.getPmscode());
            map.put("PMSSysId", entity.getPmssysid());
            map.put("IsSyncDate", entity.getIssyncdate());
            map.put("IsSyncData", entity.getIssyncdata());
        }
        return map;
    }

    //????????????????????????????????????
    @Override
    public Map<String, Object> getProjectParticipants(String projectId, Integer page, Integer pageRecord) {
        return projectCompanyTagUserService.getProjectParticipants(projectId, page, pageRecord);
    }

    //????????????????????????
    @Override
    public void putProjectInfo(ProjectParams projectParams) {
        ProjectProjectEntity entity = new ProjectProjectEntity();
        entity.setSysid(projectParams.getSysId());
        try {
            entity.setPlanstartdate(projectParams.getPlanStartDate() == null ? null : format.parse(projectParams.getPlanStartDate()));
            entity.setPlanenddate(projectParams.getPlanEndDate() == null ? null : format.parse(projectParams.getPlanEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entity.setType(projectParams.getType());
        entity.setAddress(projectParams.getAddress());
        entity.setCode(projectParams.getCode());
        entity.setManagerid(projectParams.getManagerId());
        entity.setMonthday(projectParams.getMonthDay());
        entity.setName(projectParams.getName());
        entity.setShortname(projectParams.getShortName());
        entity.setWeekday(projectParams.getWeekDay());
        this.updateById(entity);
    }

    //?????????????????????????????????
    @Override
    public void putProjectBasicInfo(ProjectBasicParams projectBasicParams) {
        projectProjectbasicinfoService.putProjectBasicInfo(projectBasicParams);
    }

    //????????????
    @Override
    public void deleteProject(String sysId) {
        ProjectProjectEntity entity = new ProjectProjectEntity();
        entity.setSysid(sysId);
        entity.setDel(true);
        projectProjectbasicinfoService.deleteProject(sysId);
        historyService.deleteByProjectId(sysId);
        List<ProjectProcessDocumentationEntity> fileIds = projectProcessDocumentationService.getAllFileByProjectId(sysId);
        List<String> fileIdsList = new ArrayList<>();
        if(fileIds!=null&&fileIds.size()>0){
            fileIds.forEach(f -> {
                fileIdsList.add(f.getFileId());
            });
        }
        fileIdsList.add(sysId);
        this.updateById(entity);
        chengjianFileuploadService.deleteFiles(fileIdsList);
    }

    //??????????????????????????????????????????????????????????????????????????????
    @Override
    public Map<String, Object> getAllCpmpanyUser(String projectId, Integer page, Integer pageRecord, String where) {
        Map<String, Object> map = new HashMap<>();
        if (where.equals("%22%22") || where.equals("''") || StringUtils.isEmpty(where)) {
            map.put("search", null);
        } else {
            map.put("search", where);
        }
        map.put("operateUserId", null);
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        Map<String, Object> maps = new HashMap<>();
        PageUtils pageUtils = omAccountService.queryPage(map);
        List<OmAccountEntity> list = (List<OmAccountEntity>) pageUtils.getList();
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            String roleName = omAccountService.getRoleName(li.getSysid());
            String companyName = omAccountService.getCompanyName(li.getCompanyId());
            String postName = omAccountService.getPostName(li.getSysid());
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("postName", postName);
            hashMap.put("roleName", roleName);
            hashMap.put("companyName", companyName);
            hashMap.put("sysId", li.getSysid());
            hashMap.put("accountId", li.getAccountid());
            hashMap.put("empId", li.getEmpid());
            hashMap.put("expire", li.getExpire());
            hashMap.put("human_id", li.getHumanId());
            hashMap.put("inact", li.getInact());
            hashMap.put("lastchg", li.getLastchg());
            hashMap.put("max", li.getMax());
            hashMap.put("min", li.getMin());
            hashMap.put("password", li.getPassword());
            hashMap.put("removed", li.getRemoved());
            hashMap.put("valid", li.getValid());
            hashMap.put("warn", li.getWarn());
            hashMap.put("accountType", li.getAccounttype());
            hashMap.put("email", li.getEmail());
            hashMap.put("msn", li.getMsn());
            hashMap.put("wechartCode", li.getWechartcode());
            hashMap.put("address", li.getAddress());
            hashMap.put("address2", li.getAddress2());
            hashMap.put("postalcode", li.getPostalcode());
            hashMap.put("postalcode2", li.getPostalcode2());
            hashMap.put("workTelphone", li.getWorktelphone());
            hashMap.put("workTelphone2", li.getWorktelphone2());
            hashMap.put("mobile", li.getMobile());
            hashMap.put("mobile2", li.getMobile2());
            hashMap.put("faxphone", li.getFaxphone());
            hashMap.put("faxphone2", li.getFaxphone2());
            hashMap.put("pinYin", li.getPinyin());
            hashMap.put("del", li.getDel());
            hashMap.put("logonDate", li.getLogondate());
            hashMap.put("logonTimes", li.getLogontimes());
            hashMap.put("enterPriseId", li.getEnterpriseid());
            hashMap.put("mobileValid", li.getMobilevalid());
            hashMap.put("mobileConfirmCode", li.getMobileconfirmcode());
            hashMap.put("synchroId", li.getSynchroid());
            hashMap.put("createdDate", li.getCreateddate());
            hashMap.put("description", li.getDescription());
            hashMap.put("name", li.getName());
            hashMap.put("keyId", li.getKeyid());
            hashMap.put("sex", li.getSex());
            hashMap.put("position", li.getPosition());
            hashMap.put("duty", li.getDuty());
            hashMap.put("identityCard", li.getIdentitycard());
            hashMap.put("edBackground", li.getEdbackground());
            hashMap.put("officeHolder", li.getOfficeholder());
            hashMap.put("qualificationCert", li.getQualificationcert());
            hashMap.put("postCert", li.getPostcert());
            hashMap.put("photo", li.getPhoto());
            hashMap.put("file1", li.getFile1());
            hashMap.put("file2", li.getFile2());
            hashMap.put("file3", li.getFile3());
            hashMap.put("file4", li.getFile4());
            hashMap.put("file5", li.getFile5());
            hashMap.put("fileName1", li.getFilename1());
            hashMap.put("fileName2", li.getFilename2());
            hashMap.put("fileName3", li.getFilename3());
            hashMap.put("fileName4", li.getFilename4());
            hashMap.put("fileName5", li.getFilename5());
            hashMap.put("updateContentDate", li.getUpdatecontentdate());
            hashMap.put("dynamicPassword", li.getDynamicpassword());
            hashMap.put("dynamicPwdDate", li.getDynamicpwddate());
            hashMap.put("sort", li.getSort());
            hashMap.put("emailaccount", li.getEmailaccount());
            hashMap.put("taskCount", li.getTaskcount());
            hashMap.put("wechartUserId", li.getWechartuserid());
            hashMap.put("fixedTelephone", li.getFixedtelephone());
            hashMap.put("officeAddress", li.getOfficeaddress());
            hashMap.put("officeRoomNo", li.getOfficeroomno());
            hashMap.put("conEnterprise", li.getConenterprise());
            hashMap.put("wechartMobile", li.getWechartmobile());
            hashMap.put("wechartEmail", li.getWechartemail());
            hashMap.put("recMsgType", li.getRecmsgtype());
            hashMap.put("accountState", li.getAccountstate());
            hashMap.put("openid", li.getOpenid());
            hashMap.put("company_id", li.getCompanyId());
            hashMap.put("unionid", li.getUnionid());
            data.add(hashMap);
        });
        maps.put("rows", data);
        maps.put("total", pageUtils.getTotalCount());
        return maps;
    }

    //?????????????????????????????????
    @Override
    public void postProjectParticipants(ProjectParticipantsParams projectParticipantsParams) {
        String companyId = projectCompanyService.getCompanyByName(projectParticipantsParams.getDepartment());
        projectCompanyTagUserService.postProjectParticipants(projectParticipantsParams, companyId);
    }

    //???????????????????????????????????????
    @Override
    public void putProjectParticipants(ProjectParticipantsParams projectParticipantsParams) {
        String companyId = projectCompanyService.getCompanyByName(projectParticipantsParams.getDepartment());
        projectCompanyTagUserService.putProjectParticipants(projectParticipantsParams, companyId);
    }

    //?????????????????????????????????
    @Override
    public void deleteProjectParticipants(String sysId) {
        projectCompanyTagUserService.deleteProjectParticipants(sysId);
    }

    //????????????
    @Override
    public ProjectProjectEntity getProject(String projectId) {
        return this.getById(projectId);
    }

    //????????????
    @Override
    public String postProject(ProjectParams projectParams, String userId) {
        ProjectProjectEntity entity = new ProjectProjectEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setDel(false);
        try {
            entity.setPlanenddate(projectParams.getPlanEndDate() == null ? null : format.parse(projectParams.getPlanEndDate()));
            entity.setPlanstartdate(projectParams.getPlanStartDate() == null ? null : format.parse(projectParams.getPlanStartDate()));
        } catch (ParseException e) {
        }
        entity.setType(projectParams.getType());
        entity.setAddress(projectParams.getAddress());
        entity.setCode(projectParams.getCode());
        entity.setManagerid(projectParams.getManagerId());
        entity.setMonthday(projectParams.getMonthDay());
        entity.setName(projectParams.getName());
        entity.setShortname(projectParams.getShortName());
        entity.setWeekday(projectParams.getWeekDay());
        entity.setCreatorid(userId);
        entity.setCreatedate(new Date());
        entity.setParentid(projectParams.getParentId());
        entity.setFileid(projectParams.getFileId());
        log.error("???????????????--------------------------------");
        log.error("?????????????????????-----------------------------");
        projectCompanyTagUserService.initCompany(entity.getSysid());
        log.error("?????????????????????-----------------------------");
        projectProjectbasicinfoService.postProject(userId, entity.getSysid());
        log.error("?????????????????????-----------------------------");
        projectPhaseService.addPhase(entity.getSysid());
        log.error("?????????????????????-----------------------------");
        projectMonthlyReviewService.initAppraisal(entity.getSysid());
        log.error("?????????????????????-----------------------------");
        projectQuarterlyAssessmentService.initAppraisal(entity.getSysid());
        log.error("?????????????????????-----------------------------");
        projectStarRatingService.initAppraisal(entity.getSysid());
        log.error("???????????????????????????--------------------------");
        projectMonitoringUnitEvaluationService.initAppraisal(entity.getSysid());
        this.save(entity);
        return entity.getSysid();
    }

    //?????????id????????????
    @Override
    public List<ProjectProjectEntity> getByParentId(String projectId) {
        return this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).eq("parentId", projectId));
    }

    //???????????????????????????????????????
    @Override
    public R getProjectPhase(String sysId) {
        ProjectProjectEntity entity = this.getById(sysId);
        if (entity == null) {
            return R.error(400, "???????????????!??????????????????!");
        }
        ProjectInformationEntity projectInformationEntity = projectInformationService.getByProjectId(entity.getParentid());
        ProjectPhaseEntity projectPhaseEntity = projectPhaseService.getByProjectId(sysId);
        Map<String, Object> map = new HashMap<>();
        if (projectInformationEntity != null) {
            map.put("proposals", projectInformationEntity.getProposals() != null ? true : false);//???????????????
            map.put("feasibilityReport", projectInformationEntity.getFeasibilityReport() != null ? true : false);//???????????????
            map.put("designReport", projectInformationEntity.getDesignReport() != null ? true : false);//??????????????????
        } else {
            map.put("proposals", false);
            map.put("feasibilityReport", false);
            map.put("designReport", false);
        }
        if (projectPhaseEntity != null) {
            map.put("planningPermit", projectPhaseEntity.getPlanningPermit() != null ? true : false);//???????????????
            map.put("constructionPermit", projectPhaseEntity.getConstructionPermit() != null ? true : false);//???????????????
            map.put("supervision", projectPhaseEntity.getSupervision() != null ? true : false);//????????????
            map.put("scenarioConstruction", projectPhaseEntity.getScenarioConstruction() != null ? true : false);//??????????????????-????????????
            map.put("designModel", projectPhaseEntity.getDesignModel() != null ? true : false);//????????????
            map.put("startWork", projectPhaseEntity.getStartWork() != null ? true : false);//?????????
            //map.put("projectSchedule", projectPhaseEntity.getProjectSchedule() != null ? true : false);
            map.put("constructionModel", projectPhaseEntity.getConstructionModel() != null ? true : false);
            map.put("videoSurveillance", projectPhaseEntity.getVideoSurveillance() != null ? true : false);
            map.put("completedModel", projectPhaseEntity.getCompletedModel() != null ? true : false);
            if (projectPhaseEntity.getConstructionBidding() == null || projectPhaseEntity.getSupervisionBidding() == null || projectPhaseEntity.getMonitorBidding() == null) {
                map.put("tripartiteDocument", false);
            } else {
                map.put("tripartiteDocument", true);
            }
        } else {
            map.put("constructionPermit", false);
            map.put("supervision", false);
            map.put("scenarioConstruction", false);
            map.put("designModel", false);
            map.put("startWork", false);
            map.put("projectSchedule", false);
            map.put("constructionModel", false);
            map.put("videoSurveillance", false);
            map.put("completedModel", false);
            map.put("tripartiteDocument", false);
            map.put("planningPermit", false);
        }
        List<ProjectWbsEntity> list = projectWbsService.getWbsDetails(sysId);
        for(ProjectWbsEntity pw:list){
            map.put("projectSchedule",false);
            if(pw.getMpplevel()==1&&pw.getFactenddate()!=null){
                map.put("projectSchedule",true);//????????????
                break;
            }
            if(pw.getFactbegindate()!=null){
                map.put("projectSchedule",null);
            }
        }
        List<ProjectStopProcessEntity> organizationalDesign = projectStopProcessService.getStopProcessArchive("????????????????????????", sysId);
        List<ProjectStopProcessEntity> specialPrograms = projectStopProcessService.getStopProcessArchive("??????????????????", sysId);
        List<ProjectStopProcessEntity> acceptance = projectStopProcessService.getStopProcessArchive("????????????", sysId);
        List<ProjectStopProcessEntity> supervisionDetails = projectStopProcessService.getStopProcessArchive("????????????", sysId);
        List<ProjectStopProcessEntity> supervisedPlanning = projectStopProcessService.getStopProcessArchive("????????????", sysId);
        if(organizationalDesign!=null&&organizationalDesign.size()>0){
            map.put("organizationalDesign",true);
        }else{
            map.put("organizationalDesign",false);
        }
        if(specialPrograms!=null&&specialPrograms.size()>0){
            map.put("specialPrograms",true);
        }else{
            map.put("specialPrograms",false);
        }
        if(supervisionDetails!=null&&supervisionDetails.size()>0){
            map.put("supervisionDetails",true);
        }else{
            map.put("supervisionDetails",false);
        }
        if(acceptance!=null&&acceptance.size()>0){
            map.put("acceptance",true);
        }else{
            map.put("acceptance",false);
        }
        if(supervisedPlanning!=null&&supervisedPlanning.size()>0){
            map.put("supervisedPlanning",true);
        }else{
            map.put("supervisedPlanning",false);
        }
        //TODO:????????????????????????????????????
        /*Boolean one = activitiWorkFlowService.checkFile(sysId, "??????????????????");
        //if (one) {
            map.put("specialPrograms", one);
        *//*} else {
            map.put("specialPrograms", false);
        }*//*
        Boolean organizationalDesign = activitiWorkFlowService.checkFile(sysId, "??????????????????");
        //????????????????????????
        //if (organizationalDesign) {
            map.put("organizationalDesign", organizationalDesign);
        *//*} else {
            map.put("organizationalDesign", false);
        }*//*
        Boolean supervisedPlanning = activitiWorkFlowService.checkFile(sysId, "????????????");
        //????????????
        //if (supervisedPlanning) {
            map.put("supervisedPlanning", supervisedPlanning);
//        } else {
//            map.put("supervisedPlanning", false);
//        }
        Boolean acceptance = activitiWorkFlowService.checkFile(sysId, "??????????????????");
        //??????????????????-??????
        //if (acceptance) {
            map.put("acceptance", acceptance);
       *//* } else {
            map.put("acceptance", false);
        }*//*
        Boolean supervisionDetails = activitiWorkFlowService.checkFile(sysId, "????????????");
        //????????????
        //if (supervisionDetails) {
            map.put("supervisionDetails", supervisionDetails);
        *//*} else {
            map.put("supervisionDetails", false);
        }*/
        //????????????
        Boolean completionAcceptance = activitiWorkFlowService.checkFile(sysId, "????????????");
        //if (completionAcceptance) {
            map.put("completionAcceptance", completionAcceptance);
//        } else {
//            map.put("completionAcceptance", false);
//        }
        Boolean completionAcceptance2 = activitiWorkFlowService.checkFile(sysId, "????????????");
        //if (completionAcceptance2) {
            map.put("record", completionAcceptance2);
            map.put("filingCertificate", completionAcceptance2);
       /* } else {
            map.put("record", false);
            map.put("filingCertificate", false);
        }*/
        //???????????????
        Boolean quexian = activitiWorkFlowService.checkFile(sysId, "???????????????");
        //if (quexian) {
            map.put("responsibility", quexian);
        /*} else {
            map.put("responsibility", false);
        }*/
        return R.ok(1, "????????????!", map, true, null);
    }

    //??????????????????
    @Override
    public List<ProjectProjectEntity> getProjects() {
        return this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0));
    }

    //?????????????????????
    @Override
    public R getTag(String projectId) {
        List<OmTagEntity> tag = omTagService.getAllTag();
        List<OmTagEntity> rm = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<ProjectCompanyTagUserEntity> persons = projectCompanyTagUserService.getProjectJoinPersons(projectId);
        if (persons != null && persons.size() > 0) {
            Iterator<OmTagEntity> iterator = tag.iterator();
            while (iterator.hasNext()) {
                OmTagEntity t = iterator.next();
                Map<String, Object> map = new HashMap<>();
                map.put("tag", t.getName());
                for (int i = 0; i < persons.size(); i++) {
                    if (t.getSysid().equals(persons.get(i).getTagId())) {
                        ProjectCompanyEntity company = projectCompanyService.getCompany(persons.get(i).getCompanyId());
                        map.put("sysId", persons.get(i).getId());
                        map.put("companyId", company.getId());
                        map.put("company", company != null ? company.getName() : null);
                        map.put("tagId", t.getSysid());
                        if (persons.get(i).getUserId() == null) {
                            map.put("user", null);
                            map.put("phone", null);
                        } else {
                            OmAccountEntity user = omAccountService.getUser(persons.get(i).getUserId());
                            map.put("user", user != null ? user.getName() : null);
                            map.put("phone", user != null ? user.getMobile() : null);
                        }
                        list.add(map);
                        rm.add(t);
                    }
                }
            }
            tag.removeAll(rm);
            if (tag.size() > 0) {
                tag.forEach(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tag", t.getName());
                    map.put("companyId", null);
                    map.put("sysId", null);
                    map.put("tagId", t.getSysid());
                    map.put("company", null);
                    map.put("user", null);
                    map.put("phone", null);
                    list.add(map);
                });
            }
        } else {
            tag.forEach(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("sysId", null);
                map.put("companyId", null);
                map.put("tag", t.getName());
                map.put("tagId", t.getSysid());
                map.put("company", null);
                map.put("user", null);
                map.put("phone", null);
                list.add(map);
            });
        }
        log.error("list??????:" + list.size());
        return R.ok(1, "????????????!", list, true, null);
    }

    //????????????????????????
    @Override
    public List<ProjectProjectEntity> getAllProject() {
        return this.list(new QueryWrapper<ProjectProjectEntity>().eq("del", 0).isNotNull("parentId").orderByAsc("sort"));
    }

    //????????????????????????
    @Override
    public R addProjectPhase(ProjectPhaseEntity entity) {
        ProjectPhaseEntity phaseEntity = projectPhaseService.getByProjectId(entity.getProjectId());
        if (phaseEntity != null) {
            if (StringUtils.isEmpty(entity.getSpecialPrograms())) {
                phaseEntity.setSpecialPrograms(phaseEntity.getSpecialPrograms());
            } else {
                phaseEntity.setSpecialPrograms(entity.getSpecialPrograms());
                if (entity.getSpecialPrograms().equals("false")) {
                    phaseEntity.setSpecialPrograms(null);
                }
            }
            if (StringUtils.isEmpty(entity.getMonitorBidding())) {
                phaseEntity.setMonitorBidding(phaseEntity.getMonitorBidding());
            } else {
                phaseEntity.setMonitorBidding(entity.getMonitorBidding());
                if (entity.getMonitorBidding().equals("false")) {
                    phaseEntity.setMonitorBidding(null);
                }
            }
            if(StringUtils.isEmpty(entity.getSupervision())){
                phaseEntity.setSupervision(phaseEntity.getSupervision());
            }else{
                phaseEntity.setSupervision(entity.getSupervision());
                if(entity.getSupervision().equals("false")){
                    phaseEntity.setSupervision(null);
                }
            }
            if (StringUtils.isEmpty(entity.getSupervisionBidding())) {
                phaseEntity.setSupervisionBidding(phaseEntity.getSupervisionBidding());
            } else {
                phaseEntity.setSupervisionBidding(entity.getSupervisionBidding());
                if (entity.getSupervisionBidding().equals("false")) {
                    phaseEntity.setSupervisionBidding(null);
                }
            }


            if (StringUtils.isEmpty(entity.getConstructionBidding())) {
                phaseEntity.setConstructionBidding(phaseEntity.getConstructionBidding());
            } else {
                phaseEntity.setConstructionBidding(entity.getConstructionBidding());
                if (entity.getConstructionBidding().equals("false")) {
                    phaseEntity.setConstructionBidding(null);
                }
            }


            if (StringUtils.isEmpty(entity.getRecord())) {
                phaseEntity.setRecord(phaseEntity.getRecord());
            } else {
                phaseEntity.setRecord(entity.getRecord());
                if (entity.getRecord().equals("false")) {
                    phaseEntity.setRecord(null);
                }
            }


            if (StringUtils.isEmpty(entity.getConstructionPermit())) {
                phaseEntity.setConstructionPermit(phaseEntity.getConstructionPermit());
            } else {
                phaseEntity.setConstructionPermit(entity.getConstructionPermit());
                if (entity.getConstructionPermit().equals("false")) {
                    phaseEntity.setConstructionPermit(null);
                }
            }

            if (StringUtils.isEmpty(entity.getStartWork())) {
                phaseEntity.setStartWork(phaseEntity.getStartWork());
            } else {
                phaseEntity.setStartWork(entity.getStartWork());
                if (entity.getStartWork().equals("false")) {
                    phaseEntity.setStartWork(null);
                }
            }

            if (StringUtils.isEmpty(entity.getAcceptanceReport())) {
                phaseEntity.setAcceptanceReport(phaseEntity.getAcceptanceReport());
            } else {
                phaseEntity.setAcceptanceReport(entity.getAcceptanceReport());
                if (entity.getAcceptanceReport().equals("false")) {
                    phaseEntity.setAcceptanceReport(null);
                }
            }
            if (StringUtils.isEmpty(entity.getCreateAplan())) {
                phaseEntity.setCreateAplan(phaseEntity.getCreateAplan());
            } else {
                phaseEntity.setCreateAplan(entity.getCreateAplan());
                if (entity.getCreateAplan().equals("false")) {
                    phaseEntity.setCreateAplan(null);
                }
            }
            if (StringUtils.isEmpty(entity.getFloodControlDocument())) {
                phaseEntity.setFloodControlDocument(phaseEntity.getFloodControlDocument());
            } else {
                phaseEntity.setFloodControlDocument(entity.getFloodControlDocument());
                if (entity.getFloodControlDocument().equals("false")) {
                    phaseEntity.setFloodControlDocument(null);
                }
            }

            if (StringUtils.isEmpty(entity.getMaintenanceReport())) {
                phaseEntity.setMaintenanceReport(phaseEntity.getMaintenanceReport());
            } else {
                phaseEntity.setMaintenanceReport(entity.getMaintenanceReport());
                if (entity.getMaintenanceReport().equals("false")) {
                    phaseEntity.setMaintenanceReport(null);
                }
            }

            if (StringUtils.isEmpty(entity.getTrafficOrganizationPlan())) {
                phaseEntity.setTrafficOrganizationPlan(phaseEntity.getTrafficOrganizationPlan());
            } else {
                phaseEntity.setTrafficOrganizationPlan(entity.getTrafficOrganizationPlan());
                if (entity.getTrafficOrganizationPlan().equals("false")) {
                    phaseEntity.setTrafficOrganizationPlan(null);
                }
            }

            if (StringUtils.isEmpty(entity.getSpecialSubmission())) {
                phaseEntity.setSpecialSubmission(phaseEntity.getSpecialSubmission());
            } else {
                phaseEntity.setSpecialSubmission(entity.getSpecialSubmission());
                if (entity.getSpecialSubmission().equals("false")) {
                    phaseEntity.setSpecialSubmission(null);
                }
            }

            if (StringUtils.isEmpty(entity.getPlanningPermit())) {
                phaseEntity.setPlanningPermit(phaseEntity.getPlanningPermit());
            } else {
                phaseEntity.setPlanningPermit(entity.getPlanningPermit());
                if (entity.getPlanningPermit().equals("false")) {
                    phaseEntity.setPlanningPermit(null);
                }
            }

            if (StringUtils.isEmpty(entity.getScenarioConstruction())) {
                phaseEntity.setScenarioConstruction(phaseEntity.getScenarioConstruction());
            } else {
                phaseEntity.setScenarioConstruction(entity.getScenarioConstruction());
                if (entity.getScenarioConstruction().equals("false")) {
                    phaseEntity.setScenarioConstruction(null);
                }
            }

            if (StringUtils.isEmpty(entity.getWorkTicket())) {
                phaseEntity.setWorkTicket(phaseEntity.getWorkTicket());
            } else {
                phaseEntity.setWorkTicket(entity.getWorkTicket());
                if (entity.getWorkTicket().equals("false")) {
                    phaseEntity.setWorkTicket(null);
                }
            }

            if (StringUtils.isEmpty(entity.getCompletedModel())) {
                phaseEntity.setCompletedModel(phaseEntity.getCompletedModel());
            } else {
                phaseEntity.setCompletedModel(entity.getCompletedModel());
                if (entity.getCompletedModel().equals("false")) {
                    phaseEntity.setCompletedModel(null);
                }
            }
            if (StringUtils.isEmpty(entity.getConstructionModel())) {
                phaseEntity.setConstructionModel(phaseEntity.getConstructionModel());
            } else {
                phaseEntity.setConstructionModel(entity.getConstructionModel());
                if (entity.getConstructionModel().equals("false")) {
                    phaseEntity.setConstructionModel(null);
                }
            }
                if (StringUtils.isEmpty(entity.getDesignModel())) {
                    phaseEntity.setDesignModel(phaseEntity.getDesignModel());
                } else {
                    phaseEntity.setDesignModel(entity.getDesignModel());
                    if (entity.getDesignModel().equals("false")) {
                        phaseEntity.setDesignModel(null);
                    }
                }
            if (StringUtils.isEmpty(entity.getProjectSchedule())) {
                phaseEntity.setProjectSchedule(phaseEntity.getProjectSchedule());
            } else {
                phaseEntity.setProjectSchedule(entity.getProjectSchedule());
                if (entity.getProjectSchedule().equals("false")) {
                    phaseEntity.setProjectSchedule(null);
                }
            }
            if (StringUtils.isEmpty(entity.getResponsibility())) {
                phaseEntity.setResponsibility(phaseEntity.getResponsibility());
            } else {
                phaseEntity.setResponsibility(entity.getResponsibility());
                if (entity.getResponsibility().equals("false")) {
                    phaseEntity.setResponsibility(null);
                }
            }

            if (StringUtils.isEmpty(entity.getVideoSurveillance())) {
                phaseEntity.setVideoSurveillance(phaseEntity.getVideoSurveillance());
            } else {
                phaseEntity.setVideoSurveillance(entity.getVideoSurveillance());
                if (entity.getVideoSurveillance().equals("false")) {
                    phaseEntity.setVideoSurveillance(null);
                }
            }

            if (StringUtils.isEmpty(entity.getCompletionAcceptance())) {
                phaseEntity.setCompletionAcceptance(phaseEntity.getCompletionAcceptance());
            } else {
                phaseEntity.setCompletionAcceptance(entity.getCompletionAcceptance());
                if (entity.getCompletionAcceptance().equals("false")) {
                    phaseEntity.setCompletionAcceptance(null);
                }
            }

            if (StringUtils.isEmpty(entity.getFilingCertificate())) {
                phaseEntity.setFilingCertificate(phaseEntity.getFilingCertificate());
            } else {
                phaseEntity.setFilingCertificate(entity.getFilingCertificate());
                if (entity.getFilingCertificate().equals("false")) {
                    phaseEntity.setFilingCertificate(null);
                }
            }

            if (StringUtils.isEmpty(entity.getOrganizationalDesign())) {
                phaseEntity.setOrganizationalDesign(phaseEntity.getOrganizationalDesign());
            } else {
                phaseEntity.setOrganizationalDesign(entity.getOrganizationalDesign());
                if (entity.getOrganizationalDesign().equals("false")) {
                    phaseEntity.setOrganizationalDesign(null);
                }
            }

            if (StringUtils.isEmpty(entity.getSupervisedPlanning())) {
                phaseEntity.setSupervisedPlanning(phaseEntity.getSupervisedPlanning());
            } else {
                phaseEntity.setSupervisedPlanning(entity.getSupervisedPlanning());
                if (entity.getSupervisedPlanning().equals("false")) {
                    phaseEntity.setSupervisedPlanning(null);
                }
            }
            if (StringUtils.isEmpty(entity.getSupervisionDetails())) {
                phaseEntity.setSupervisionDetails(phaseEntity.getSupervisionDetails());
            } else {
                phaseEntity.setSupervisionDetails(entity.getSupervisionDetails());
                if (entity.getSupervisionDetails().equals("false")) {
                    phaseEntity.setSupervisionDetails(null);
                }
            }
            projectPhaseService.addProjectPhase(phaseEntity);
        } else {
            entity.setSysId(CreateUuid.uuid());
            projectPhaseService.addProjectPhase(entity);
        }
        return R.ok(1, "????????????!", null, true, null);
    }

    //????????????????????????
    @Override
    public R getProjectAllPhase(String projectId) {
        ProjectPhaseEntity entity = projectPhaseService.getByProjectId(projectId);
        return R.ok(1, "????????????!", entity, true, null);
    }

    //????????????????????????
    @Override
    public Map<String, Object> getRectificationInformation(String projectId) {
        return activitiWorkFlowService.getRectificationInformation(projectId);
    }

    //???????????????????????????
    @Override
    public Map<String, Object> getCategory() {
        Map<String, Object> data = new HashMap<>();
        //?????????????????????
        List<ProjectParentProjectEntity> all = projectParentProjectService.getAll();
        //????????????
        data.put("allItems", all == null ? 0 : all.size());
        if (all != null && all.size() > 0) {
            List<ProjectParentProjectEntity> earlyStageOfTheProject = new ArrayList<>();
            List<ProjectParentProjectEntity> projectImplement = new ArrayList<>();
            List<ProjectParentProjectEntity> endOfTheProject = new ArrayList<>();
            List<ProjectParentProjectEntity> projectAcceptance = new ArrayList<>();
            all.forEach(a -> {//????????????????????????id
                List<ProjectProjectEntity> list = this.getByParentId(a.getSysId());//?????????id??????????????????
                if (list != null && list.size() > 0) {
                    int stage = stage(list);//??????????????????
                    if (stage == 0) {
                        earlyStageOfTheProject.add(a);
                    } else if (stage == 1) {
                        projectImplement.add(a);
                    } else if (stage==2){//????????????  ????????????+1
                        endOfTheProject.add(a);
                    }else if (stage==3){
                        projectAcceptance.add(a);
                    }
                } else {
                    earlyStageOfTheProject.add(a);
                }
            });
            data.put("earlyStageOfTheProject", earlyStageOfTheProject.size());//????????????
            data.put("projectImplement", projectImplement.size());//????????????
            data.put("endOfTheProject", endOfTheProject.size());//????????????
            data.put("projectAcceptance",projectAcceptance.size());//?????????????????????????????????
        } else {
            data.put("earlyStageOfTheProject", 0);
            data.put("projectImplement", 0);
            data.put("endOfTheProject", 0);
            data.put("projectAcceptance",0);
        }
        return data;
    }

    //??????
    @Override
    public void itemSort(String sortList) {
        String[] split = sortList.split(",");
        List<ProjectProjectEntity> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            ProjectProjectEntity entity = new ProjectProjectEntity();
            entity.setSysid(split[i]);
            entity.setSort(i);
            list.add(entity);
        }
        this.updateBatchById(list);
    }

    //??????????????????
    private int stage(List<ProjectProjectEntity> list) {
        int end = 0;
        int implement = 0;
        int check=0;
        for (int i = 0; i < list.size(); i++) {
            R r = this.getProjectPhase(list.get(i).getSysid());
            boolean success = (boolean) r.get("success");
            if (success) {
                Map<String, Object> data = (Map<String, Object>) r.get("data");
                try {
                    boolean responsibility = (Boolean) data.get("responsibility");//???????????????
                    if (responsibility) {
                        end++;
                        continue;
                    }
                    boolean organizationalDesign = (Boolean) data.get("organizationalDesign") == null ? false : (Boolean) data.get("organizationalDesign");//????????????????????????
                    boolean supervisedPlanning = (Boolean) data.get("supervisedPlanning") == null ? false : (Boolean) data.get("supervisedPlanning");//????????????
                    boolean scenarioConstruction = (Boolean) data.get("scenarioConstruction") == null ? false : (Boolean) data.get("scenarioConstruction");//??????????????????-????????????
                    boolean acceptance = (Boolean) data.get("acceptance") == null ? false : (Boolean) data.get("acceptance");//??????????????????-??????
                    boolean designModel = (Boolean) data.get("designModel") == null ? false : (Boolean) data.get("designModel");//????????????
                    boolean startWork = (Boolean) data.get("startWork") == null ? false : (Boolean) data.get("startWork");//?????????
//---------         //8???25?????????
                    boolean specialPrograms = (Boolean) data.get("specialPrograms") == null ? false : (Boolean) data.get("specialPrograms");//??????????????????
                    boolean supervisionDetails = (Boolean) data.get("supervisionDetails") == null ? false : (Boolean) data.get("supervisionDetails");//????????????
                    boolean projectSchedule = (Boolean) data.get("projectSchedule") == null ? false : (Boolean) data.get("projectSchedule");//????????????
                    boolean constructionModel = (Boolean) data.get("constructionModel") == null ? false : (Boolean) data.get("constructionModel");//????????????
                    boolean videoSurveillance = (Boolean) data.get("videoSurveillance") == null ? false : (Boolean) data.get("videoSurveillance");//????????????
//--------------------
                    if (organizationalDesign && supervisedPlanning && scenarioConstruction && acceptance &&
                            designModel && startWork) {
                        //????????????????????? ?????????????????????????????? ?????????????????? ????????????
                        if (specialPrograms&&supervisionDetails&&projectSchedule&&constructionModel&&videoSurveillance){
                            check++;
                            continue;
                        }
                        implement++;
                        continue;
                    }
                }catch (NullPointerException e){
                    boolean organizationalDesign = (Boolean) data.get("organizationalDesign") == null ? false : (Boolean) data.get("organizationalDesign");
                    boolean supervisedPlanning = (Boolean) data.get("supervisedPlanning") == null ? false : (Boolean) data.get("supervisedPlanning");
                    boolean scenarioConstruction = (Boolean) data.get("scenarioConstruction") == null ? false : (Boolean) data.get("scenarioConstruction");
                    boolean acceptance = (Boolean) data.get("acceptance") == null ? false : (Boolean) data.get("acceptance");
                    boolean designModel = (Boolean) data.get("designModel") == null ? false : (Boolean) data.get("designModel");
                    boolean startWork = (Boolean) data.get("startWork") == null ? false : (Boolean) data.get("startWork");
                    if (organizationalDesign && supervisedPlanning && scenarioConstruction && acceptance &&
                            designModel && startWork) {
                        implement++;
                        continue;
                    }
                }
            }
        }
        if (check==list.size()){
            return 3;
        }
        if (end == list.size()) {
            return 2;
        }
        if (implement == list.size()) {
            return 1;
        }
        return 0;//0????????????  1????????????  2???????????? 3????????????
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}