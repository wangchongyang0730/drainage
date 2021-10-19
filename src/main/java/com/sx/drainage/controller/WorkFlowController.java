package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ActivitiWorkFlowEntity;
import com.sx.drainage.entity.ProjectStopProcessEntity;
import com.sx.drainage.service.ProjectStopProcessService;
import com.sx.drainage.service.WorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/26
 * Time: 16:22
 */
@Api(value = "/api/workflow", description = "工作流")
@CrossOrigin
@RequestMapping("/api/workflow")
@RestController
@RequiredArgsConstructor
public class WorkFlowController {

    private final WorkFlowService workFlowService;
    private final ProjectStopProcessService projectStopProcessService;

    /*
     * 部署流程
     * */
    @PostMapping("/deploymentProcess")
    @ApiOperation(value = "部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "流程名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "file", value = "zip文件", required = true, dataType = "file")
    })
    public R deploymentProcess(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        Map<String, Object> map = workFlowService.deploymentProcess(file, name);
        return R.ok(1, (String) map.get("msg"), null, true, null);
    }
    /*
     * 启动流程
     * */
    @PostMapping("/startProcess")
    @ApiOperation(value = "启动流程")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "body", name = "map", value = "json类型{key:流程启动key值,workType:流程类型,projectId:项目id,num:数量,var:{流程携带信息}}", required = true, dataType = "Map"),
    })
    public R startProcess(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Map<String, Object> process = workFlowService.startProcess(map, userId);
        return R.ok(1, "启动成功!", process, true, null);
    }

    /*
    * 流程添加信息
    * */
    @PostMapping("/addProcessDetails")
    @ApiOperation(value = "流程信息添加")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "processId", value = "流程id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "body", name = "map", value = "键值对形式", required = true, dataType = "Map")
    })
    public R addProcessDetails(@RequestParam("processId") String processId, @RequestBody Map<String, Object> map){
        workFlowService.addProcessDetails(processId,map);
        return R.ok(1,"添加成功!",null,true,null);
    }

    /*
     * 获取所有流程
     * */
    @GetMapping("/getAllProcess")
    @ApiOperation(value = "获取所有流程")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAllProcess() {
        List<Map<String, Object>> list = workFlowService.getAllProcess();
        return R.ok(1, "获取成功!", list, true, null);
    }

    /*
    * 获取流程图
    * */
    @GetMapping("/getProcessImg")
    @ApiOperation(value = "获取流程图")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deploymentId", value = "流程部署id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "img", value = "图片名称", required = true, dataType = "String")
    })
    public void getProcessImg(@RequestParam("deploymentId") String deploymentId, @RequestParam("img") String img, HttpServletRequest request, HttpServletResponse response) {
        workFlowService.getProcessImg(deploymentId, img, request, response);
    }

    /*
     * 获取活动节点
     * */
    @GetMapping("/getCompletionNode")
    @ApiOperation(value = "获取活动节点")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "processInstanceId", value = "流程实例id", required = true, dataType = "String")
    })
    public R getCompletionNode(@RequestParam("processInstanceId") String processInstanceId) {
        List<Map<String, Object>> list = workFlowService.getCompletionNode(processInstanceId);
        return R.ok(1, "获取成功!", list, true, null);
    }

    /*
     * 查询个人任务
     * */
    @GetMapping("/getMyTask")
    @ApiOperation(value = "查询个人任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getMyTask(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Map<String, Object> myTask = workFlowService.getMyTask(userId);
        return R.ok(1, "获取成功!", myTask, true, null);
    }

    /*
     * 完成任务
     * */
    @PostMapping("/completeTask")
    @ApiOperation(value = "完成任务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "body", name = "map", value = "流程完成参数(附带意见之类，键值对形式)", required = true, dataType = "Map")
    })
    public R completeTask(@RequestBody Map<String, Object> map,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        map.put("userId",userId);
        Map<String, Object> task = workFlowService.completeTask(map);
        return R.ok(1, (String) task.get("msg"), null, true, null);
    }

    /*
     * 获取流程跟踪图
     * */
    @GetMapping("/getProcessTrackImg/{processInstanceId}")
    @ApiOperation(value = "获取流程跟踪图")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "processInstanceId", value = "流程实例id", required = true, dataType = "String")
    })
    public void getProcessTrackImg(@PathVariable("processInstanceId") String processInstanceId, HttpServletRequest request, HttpServletResponse response) {
        workFlowService.getProcessTrackImg(processInstanceId, request, response);
    }

    /*
     * 获取流程存档
     * */
    @GetMapping("/getProcessArchive")
    @ApiOperation(value = "获取流程存档")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workType", value = "类型", required = true, dataType = "String")
    })
    public R getProcessArchive(@RequestParam("projectId") String projectId, @RequestParam("workType") String workType) {
        List<ActivitiWorkFlowEntity> list = workFlowService.getProcessArchive(projectId, workType);
        return R.ok(1, "获取成功!", list, true, null);
    }

    /*
     * 删除流程存档
     * */
    @DeleteMapping("/deleteProcessArchive/{sysId}")
    @ApiOperation(value = "删除流程存档")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R deleteProcessArchive(@PathVariable("sysId") String sysId) {
        boolean b = workFlowService.deleteProcessArchive(sysId);
        if(!b){
            return R.error(500,"流程已发起，无法删除!");
        }
        return R.ok(1, "删除成功!", null, true, null);
    }

    /*
     * 任务拾取
     * */
    @GetMapping("/claimTask/{taskId}")
    @ApiOperation(value = "任务拾取")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "taskId", value = "任务id", required = true, dataType = "String")
    })
    public R claimTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        workFlowService.claimTask(userId, taskId);
        return R.ok(1, "拾取成功!", null, true, null);
    }

    /*
     * 任务退回
     * */
    @GetMapping("/rollBackTask/{taskId}")
    @ApiOperation(value = "任务退回")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "taskId", value = "任务id", required = true, dataType = "String")
    })
    public R rollBackTask(@PathVariable("taskId") String taskId) {
        workFlowService.rollBackTask(taskId);
        return R.ok(1, "任务已放弃!", null, true, null);
    }

    /*
    * 获取下一节点待办人
    * */
    @GetMapping("/nextUsers")
    @ApiOperation(value = "获取下一节点待办人")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taskId", value = "任务id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "var", value = "流程走向控制", required = false, dataType = "String"),
    })
    public R nextUsers(@RequestParam("taskId") String taskId,@RequestParam("projectId") String projectId,@RequestParam(value = "var",required = false) String var) {
        return workFlowService.nextUsers(taskId,projectId,var);
    }

    /*
    * 根据任务id详情
    * */
    @GetMapping("/getTaskByTaskId/{taskId}")
    @ApiOperation(value = "根据任务id详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "taskId", value = "任务id", required = true, dataType = "String"),
    })
    public R getTaskByTaskId(@PathVariable("taskId") String taskId){
        Map<String,Object> data = workFlowService.getTaskByTaskId(taskId);
        return R.ok(1,"获取成功!",data,true,null);
    }
    /*
     * 根据流程id详情
     * */
    @GetMapping("/getTaskByProcessId/{processId}")
    @ApiOperation(value = "根据流程id详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "processId", value = "流程id", required = true, dataType = "String"),
    })
    public R getTaskByProcessId(@PathVariable("processId") String processId){
        Map<String,Object> data = workFlowService.getTaskByProcessId(processId);
        return R.ok(1,"获取成功!",data,true,null);
    }
    /*
    * 获取流程携带信息
    * */
    @GetMapping("/getProcessDetails/{processId}")
    @ApiOperation(value = "获取流程携带信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "processId", value = "流程id", required = true, dataType = "String"),
    })
    public R getProcessDetails(@PathVariable("processId") String processId){
        Map<String,Object> data = workFlowService.getProcessDetails(processId);
        return R.ok(1,"获取成功!",data,true,null);
    }

    /*
    * 停用流程存档获取
    * */
    @GetMapping("/getStopProcessArchive")
    @ApiOperation(value ="停用流程存档获取")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "processType", value = "流程类型(施工组织总体设计,施工专项方案,大临验收,监理规划,监理细则,监理(安全)月报)", required = true, dataType = "String")
    })
    public R getStopProcessArchive(@RequestParam("processType") String processType,@RequestParam("projectId") String projectId){
        String[] arr = {"施工组织总体设计","施工专项方案","大临验收","监理规划","监理细则","监理(安全)月报"};
        List<String> asList = Arrays.asList(arr);
        if(!asList.contains(processType)){
            return R.error(500,"请核对流程类型!");
        }
        List<ProjectStopProcessEntity> list = projectStopProcessService.getStopProcessArchive(processType,projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    /*
     * 停用流程存档
     * */
    @PostMapping("/addStopProcessArchive")
    @ApiOperation(value ="停用流程存档")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addStopProcessArchive(@RequestBody ProjectStopProcessEntity entity,HttpServletRequest request){
        String[] arr = {"施工组织总体设计","施工专项方案","大临验收","监理规划","监理细则","监理(安全)月报"};
        List<String> asList = Arrays.asList(arr);
        if(!asList.contains(entity.getProcessType())){
            return R.error(500,"请核对流程类型!");
        }
        String userId = (String) request.getAttribute("userId");
        entity.setCreateUserId(userId);
        projectStopProcessService.addStopProcessArchive(entity);
        return R.ok(1,"添加成功!",null,true,null);
    }
    /*
    * 删除停用流程存档
    * */
    @DeleteMapping("/deleteStopProcessArchive")
    @ApiOperation(value ="删除停用流程存档")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R deleteStopProcessArchive(@RequestParam("sysId") String sysId){
        projectStopProcessService.deleteStopProcessArchive(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
//启动流程
    /*@PostMapping("/startProcess")
    public Map<String,Object> startProcess(@RequestBody(required = false) ProcessParams processParams, @RequestParam("key") String key){
//        List<ContentParams> header = processParams.getHeader();
//        List<ContentParams> contents = processParams.getContents();
//        Map<String, Object> map = new HashMap<>();
//        header.forEach(li -> {
//            List<String> list = new ArrayList<>();
//            contents.forEach(ct -> {
//                if(ct.getFiled().equals(li.getFiled())){
//                    list.add(ct.getContent());
//                }
//            });
//            map.put(li.getFiled(),list);
//        });

        Map<String, Object> res = new HashMap<>();
        res.put("header",processParams.getHeader());
        res.put("contents",processParams.getContents());
        return res;
    }*/
/*
 *  @ApiImplicitParam(paramType = "body",name = "map",value = "json类型(必须包含key字段(启动流程所需字段),workType字段(流程类型),projectId字段(项目id))",required = true,dataType = "Map")
 * */