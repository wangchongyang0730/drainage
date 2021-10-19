package com.sx.drainage.controller;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/28
 * Time: 10:55
 */
@Api(value = "/api/ProjectManage",description = "项目管理 *")
@RestController
@RequestMapping("/api/ProjectManage")
@CrossOrigin
public class ProjectManageController {
    @Autowired
    private ProjectProjectService projectProjectService;
    @Autowired
    private ProjectDeviceService projectDeviceService;
    @Autowired
    private ProjectWbsService projectWbsService;
    @Autowired
    private ProjectHistoryService projectHistoryService;
    @Autowired
    private ProjectCriticalProjectService projectCriticalProjectService;
    @Autowired
    private ProjectPhasedAcceptanceService projectPhasedAcceptanceService;
    @Autowired
    private ProjectManagerPeopleService projectManagerPeopleService;
    @Autowired
    private ProjectSupervisorManagementService projectSupervisorManagementService;
    @GetMapping("/GetAllProject")
    @ApiOperation(value = "获取所有项目信息 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getAllProject(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<Map<String, Object>> project=projectProjectService.getAllProject(userId);
        if(project==null||project.size()==0){
            return R.ok(200,"暂无可查看项目，请联系管理员分配!",null,true,null);
        }
        return R.ok(1,"获取成功!",project,true,null);
    }
    @GetMapping("/GetProjectProfile")
    @ApiOperation(value = "获取项目主页项目基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectDetails(@RequestParam("projectId") String projectId){
        Map<String,Object> projectDetails=projectProjectService.getProjectDetails(projectId);
        List<Map<String, Object>> images=projectProjectService.getImage((String) projectDetails.get("profilePic"));
        Map<String, Object> map = new HashMap<>();
        map.put("basicInfo",projectDetails);
        map.put("profilePic",images);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetProjectDeviceNow")
    @ApiOperation(value = "获取项目当前施工设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectDeviceNow(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> map=projectDeviceService.getProjectDeviceNow(projectId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetProjectJoinPersons")
    @ApiOperation(value = "获取项目参与单位及主要人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectJoinPersons(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> data=projectProjectService.getProjectJoinPersons(projectId);
        return R.ok(1,"获取成功!",data,true,null);
    }
    @GetMapping("/GetProjectNowSide")
    @ApiOperation(value = "获取项目当前作业面信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectNowSide(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> wbs=projectProjectService.getProjectNowSide(projectId);
        return R.ok(1,"获取成功!",wbs,true,null);
    }
    @GetMapping("/GetProjectMajorNode")
    @ApiOperation(value = "获取项目里程碑节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectMajorNode(@RequestParam("projectId") String projectId){
        Map<String,Object> wbs=projectProjectService.getProjectMajorNode(projectId);
        return R.ok(1,"获取成功!",wbs,true,null);
    }
    @GetMapping("/GetNoProcessSourceRisk")
    @ApiOperation(value = "获取无流程提交风险源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getNoProcessSourceRisk(@RequestParam("projectId") String projectId){
        List<ProjectWbsbindgroupsourceriskEntity> danger=projectProjectService.getNoProcessSourceRisk(projectId);
        List<Map<String,Object>> res = new ArrayList<>();
        danger.forEach(dr -> {
            Map<String, Object> map = new HashMap<>();
            map.put("factBeginDate",dr.getFactbegindate());
            map.put("factEndDate",dr.getFactenddate());
            map.put("planBeginDate",dr.getPlanbegindate());
            map.put("planEndDate",dr.getPlanenddate());
            map.put("sourceRiskName",dr.getSourceriskname());
            map.put("sysId",dr.getSysid());
            map.put("wbsId",dr.getWbsid());
            res.add(map);
        });
        return R.ok(1,"获取成功!",res,true,null);
    }
    @GetMapping("/GetNoProcessDeviceNow")
    @ApiOperation(value = "获取无流程提交施工设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getNoProcessDeviceNow(@RequestParam("projectId") String projectId){
        List<ProjectWbsbindgroupequipmentEntity> equipment=projectProjectService.getNoProcessDeviceNow(projectId);
        List<Map<String,Object>> res = new ArrayList<>();
        equipment.forEach(et -> {
            Map<String, Object> map = new HashMap<>();
            map.put("DepartureDate",et.getDeparturedate());
            map.put("EntryDate",et.getEntrydate());
            map.put("EquipmentName",et.getEquipmentname());
            map.put("SysId",et.getSysid());
            map.put("WbsId",et.getWbsid());
            map.put("partName",projectWbsService.getWbs(et.getWbsid()).getPartname());
            res.add(map);
        });
        return R.ok(1,"获取成功!",res,true,null);
    }
    @GetMapping("/GetProjectPrice")
    @ApiOperation(value = "获取项目验工计价和安措费")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectPrice(@RequestParam("projectId") String projectId){
        List<InspectionpriceEntity> money=projectProjectService.getProjectPrice(projectId);
        int YGJJ_PriceSum=0;
        int ACF_PriceSum=0;
        if(money.size()>0){
            for(int i=0;i<money.size();i++){
                if(money.get(i).getType().equals("1")){
                    YGJJ_PriceSum+=money.get(i).getPrice();
                }
                if(money.get(i).getType().equals("2")){
                    ACF_PriceSum+=money.get(i).getPrice();
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("YGJJ_PriceSum",YGJJ_PriceSum);
        map.put("ACF_PriceSum",ACF_PriceSum);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetNoProcessRectification")
    @ApiOperation(value = "获取无流程提交质量和安全信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "type",value = "1为质量安全，2为信息安全",required = true,dataType = "int"),
    })
    public R getNoProcessRectification(@RequestParam("projectId") String projectId,@RequestParam("type") Integer type){
        Map<String,Object> map=projectProjectService.getNoProcessRectification(projectId,type);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/getProjectPhase/{projectId}")
    @ApiOperation(value = "项目阶段文件获取(鱼骨图) *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getProjectPhase(@PathVariable("projectId") String projectId){
        return projectProjectService.getProjectPhase(projectId);
    }
    @PostMapping("/addProjectPhase")
    @ApiOperation(value = "项目阶段文件添加 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addProjectPhase(@RequestBody ProjectPhaseEntity entity){
        return projectProjectService.addProjectPhase(entity);
    }
    @GetMapping("/getProjectAllPhase/{projectId}")
    @ApiOperation(value = "项目阶段文件获取 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getProjectAllPhase(@PathVariable("projectId") String projectId){
        return projectProjectService.getProjectAllPhase(projectId);
    }
    @GetMapping("/getTag/{projectId}")
    @ApiOperation(value = "参建单位初始化 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getTag(@PathVariable("projectId") String projectId){
        return projectProjectService.getTag(projectId);
    }
    @PostMapping("/addHistory")
    @ApiOperation(value = "添加历史足迹 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addHistory(@RequestBody ProjectHistoryEntity historyEntity,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectHistoryService.addHistory(historyEntity,userId);
        return R.ok();
    }
    @GetMapping("/getHistory")
    @ApiOperation(value = "获取历史足迹 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getHistory(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<ProjectHistoryEntity> list = projectHistoryService.getHistory(userId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/getCriticalProject/{projectId}")
    @ApiOperation(value = "获取危大工程 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    /*
    * @ApiImplicitParam(paramType = "query",name = "page",value = "页码",required = false,dataType = "int"),
    * @ApiImplicitParam(paramType = "query",name = "limit",value = "数量",required = false,dataType = "int"),
    * @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,@RequestParam(value = "limit",required = false,defaultValue = "10")Integer limit
    * */
    public R getCriticalProject(@PathVariable("projectId") String projectId){
        List<ProjectCriticalProjectEntity> list = projectCriticalProjectService.getAllByProjectId(projectId);
//        Map<String, Object> map = new HashMap<>();
//        map.put("total",iPage.getTotalCount());
//        map.put("rows",iPage.getList());
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PostMapping("/addCriticalProject")
    @ApiOperation(value = "添加危大工程 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addCriticalProject(@RequestBody ProjectCriticalProjectEntity entity,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectCriticalProjectService.addCriticalProject(entity,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/updateCriticalProject")
    @ApiOperation(value = "修改危大工程 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R updateCriticalProject(@RequestBody ProjectCriticalProjectEntity entity){
        projectCriticalProjectService.updateCriticalProject(entity);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @GetMapping("/deleteCriticalProject/{sysId}")
    @ApiOperation(value = "删除危大工程 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "sysId",value = "主键id",required = true,dataType = "String")
    })
    public R deleteCriticalProject(@PathVariable("sysId") String projectId){
        projectCriticalProjectService.deleteCriticalProject(projectId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getRectificationInformation/{projectId}")
    @ApiOperation(value = "获取整改完成情况 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "主键id",required = true,dataType = "String")
    })
    public R getRectificationInformation(@PathVariable("projectId") String projectId){
        Map<String,Object> data = projectProjectService.getRectificationInformation(projectId);
        return R.ok(1,"获取成功!",data,true,null);
    }
    @GetMapping("/getPhasedAcceptance/{projectId}")
    @ApiOperation(value = "获取阶段性验收文件 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "主键id",required = true,dataType = "String")
    })
    public R getPhasedAcceptance(@PathVariable("projectId") String projectId){
        List<ProjectPhasedAcceptanceEntity> list = projectPhasedAcceptanceService.getAll(projectId);
        if(list!=null&&list.size()>0){
            return R.ok(1,"获取成功!",list.get(0),true,null);
        }else{
            return R.ok(1,"获取成功!",new ProjectPhasedAcceptanceEntity(),true,null);
        }

    }
    @PostMapping("/addPhasedAcceptance")
    @ApiOperation(value = "添加阶段性验收文件 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addPhasedAcceptance(@RequestBody ProjectPhasedAcceptanceEntity projectPhasedAcceptanceEntity){
        projectPhasedAcceptanceService.addPhasedAcceptanceFile(projectPhasedAcceptanceEntity);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @GetMapping("/getCategory")
    @ApiOperation(value = "获取项目各阶段数量 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getCategory(){
        Map<String,Object> data = projectProjectService.getCategory();
        return R.ok(1,"获取成功!",data,true,null);
    }
    @GetMapping("/getManagerPeople/{projectId}")
    @ApiOperation(value = "获取项目管理人员 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getManagerPeople(@PathVariable("projectId") String projectId){
        ProjectManagerPeopleEntity managerPeople = projectManagerPeopleService.getManagerPeople(projectId);
        return R.ok(1,"获取成功!",managerPeople,true,null);
    }
    @GetMapping("/getSupervisor/{projectId}")
    @ApiOperation(value = "获取监理管理人员 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getSupervisor(@PathVariable("projectId") String projectId){
        List<Map<String, Object>> list = projectSupervisorManagementService.getSupervisor(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PutMapping("/updateSupervisor")
    @ApiOperation(value = "变更监理管理人员 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R updateSupervisor(@RequestBody ProjectSupervisorManagementEntity entity){
        projectSupervisorManagementService.updateSupervisor(entity);
        return R.ok(1,"变更成功!",null,true,null);
    }
    @PostMapping("/addSupervisor")
    @ApiOperation(value = "添加监理职位 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addSupervisor(@RequestBody ProjectSupervisorManagementEntity entity){
        projectSupervisorManagementService.addSupervisor(entity);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @GetMapping("/itemSort")
    @ApiOperation(value = "排序")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sortList",value = "排序",required = true,dataType = "String")
    })
    public R itemSort(@RequestParam("sortList") String sortList){
        projectProjectService.itemSort(sortList);
        return R.ok(1,"修改成功!",null,true,null);
    }
}
