package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.CameraInfoParams;
import com.sx.drainage.service.MonitorIpcService;
import com.sx.drainage.service.MonitorNvrService;
import com.sx.drainage.service.ProjectMonitorypointService;
import com.sx.drainage.service.ProjectNvrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/10
 * Time: 13:35
 */
@Api(value = "/api/MonitorNVR",description = "获取监控点信息")
@CrossOrigin
@RestController
@RequestMapping("/api/MonitorNVR")
public class MonitorNVRController {
    @Autowired
    private MonitorNvrService monitorNvrService;
    @Autowired
    private MonitorIpcService monitorIpcService;
    @Autowired
    private ProjectMonitorypointService projectMonitorypointService;
    @Autowired
    private ProjectNvrService projectNvrService;

    @GetMapping("/GetAllMonitorNvr")
    @ApiOperation(value = "获取所有的监控设置分类")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAllMonitorNvr(){
        List<Map<String,Object>> maps=monitorNvrService.getAllMonitorNvr();
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @GetMapping("/GetAllMonitorIpc")
    @ApiOperation(value = "获取分类监控下的摄像机")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "nvrId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "", required = false, dataType = "String")
    })
    public R getAllMonitorIpc(@RequestParam("nvrId") String nvrId,
                              @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                              @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(value = "where",required = false) String where){
        Map<String,Object> map=monitorIpcService.getAllMonitorIpc(nvrId,page,pageRecord,where);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetMonitoryPoint")
    @ApiOperation(value = "获取监控点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getMonitoryPoint(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> map=projectMonitorypointService.getMonitoryPoint(projectId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetModelMonitoring")
    @ApiOperation(value = "获取模型视频监控信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getModelMonitoring(@RequestParam("projectId") String projectId){

        return projectNvrService.getModelMonitoring(projectId);
    }
    @PostMapping("/PostMonitoryPoint")
    @ApiOperation(value = "添加更新监控点信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postMonitoryPoint(@RequestBody CameraInfoParams cameraInfoParams){
        projectMonitorypointService.postMonitoryPoint(cameraInfoParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/DeleteMonitoryPoint")
    @ApiOperation(value = "删除监控点")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R deleteMonitoryPoint(@RequestParam("sysId") String sysId){
        projectMonitorypointService.deleteMonitoryPoint(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
