package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.DeviceParams;
import com.sx.drainage.service.ProjectDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 17:12
 */
@Api(value = "/api/DeviceManage",description = "设备管理（施工设备管理）")
@CrossOrigin
@RestController
@RequestMapping("/api/DeviceManage")
public class DeviceManageController {
    @Autowired
    private ProjectDeviceService projectDeviceService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "search", value = "", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam("projectId") String projectId,
                         @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                         @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                         @RequestParam(value = "search",required = false,defaultValue = "1") String search){
        return projectDeviceService.getPageList(projectId,page,pageRecord,search);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键sysId", required = true, dataType = "String")
    })
    public R getDetails(@PathVariable("id") String id){
        return projectDeviceService.getDetails(id);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "添加设备")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addDevice(@RequestBody DeviceParams deviceParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDeviceService.addDevice(deviceParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改设备")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateDevice(@RequestBody DeviceParams deviceParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDeviceService.updateDevice(deviceParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "移除设备")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R deleteDevice(@RequestParam("sysId") String sysId,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDeviceService.deleteDevice(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @PostMapping("/DeviceLeave/{id}")
    @ApiOperation(value = "设备离场")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键sysId", required = true, dataType = "String")
    })
    public R addDevice(@PathVariable("id") String id, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDeviceService.deviceLeave(id,userId);
        return R.ok(1,"操作成功!",null,true,null);
    }
}
