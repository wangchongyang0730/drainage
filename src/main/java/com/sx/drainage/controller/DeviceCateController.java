package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.DeviceCateParams;
import com.sx.drainage.service.ProjectDevicecateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 12:00
 */
@Api(value = "/api/DeviceCate",description = "设备分类管理")
@CrossOrigin
@RestController
@RequestMapping("/api/DeviceCate")
public class DeviceCateController {
    @Autowired
    private ProjectDevicecateService projectDevicecateService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam("projectId") String projectId,
                         @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                         @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                         @RequestParam(value = "where",required = false) String where){
        Map<String,Object> maps=projectDevicecateService.getPageList(projectId,page,pageRecord,where);
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post(@RequestBody DeviceCateParams deviceCateParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDevicecateService.post(deviceCateParams,userId);
        return R.ok(1,"添加完成!",null,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R update(@RequestBody DeviceCateParams deviceCateParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDevicecateService.update(deviceCateParams,userId);
        return R.ok(1,"修改完成!",null,true,null);
    }
    @DeleteMapping("Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R delete(@RequestParam("sysId") String sysId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDevicecateService.delete(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/GetAllList")
    @ApiOperation(value = "获取所有设备类别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getAllList(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> list=projectDevicecateService.getAll(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
}
