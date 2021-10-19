package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.AddProgressInfoParams;
import com.sx.drainage.service.ProjectProgressinfoService;
import com.sx.drainage.service.ProjectWbsbimtypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/28
 * Time: 16:53
 */
@Api(value = "/api/BIMManage",description = "BIM模型管理")
@RestController
@CrossOrigin
@RequestMapping("/api/BIMManage")
public class BIMManageController {

    @Autowired
    private ProjectProgressinfoService projectProgressinfoService;
    @Autowired
    private ProjectWbsbimtypeService projectWbsbimtypeService;

    @GetMapping("/GetOverAllProgress")
    @ApiOperation(value = "获取项目总进度计划信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R GetOverAllProgress(@RequestParam("projectId") String projectId) {
        List<Map<String,Object>> maps=projectProgressinfoService.getOverAllProgress(projectId);
        Map<String, Object> map = new HashMap<>();
        map.put("data",maps);
        map.put("num",0);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetBIMProjectId")
    @ApiOperation(value = "获取BIM模型的项目Id")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "bimTypeName",value = "模型类型名称(施工BIM,设计BIM,竣工BIM)",required = true,dataType = "String")
    })
    public R getBIMProjectId(@RequestParam("projectId") String projectId,
                             @RequestParam("bimTypeName") String bimTypeName){
        Map<String,Object> map=projectWbsbimtypeService.getBIMProjectId(projectId,bimTypeName);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PostMapping("/AddProgressInfo")
    @ApiOperation(value = "添加总体进度节点信息,先删除已有proName信息再添加信息")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addProgressInfo(@RequestBody AddProgressInfoParams addProgressInfoParams){
        projectProgressinfoService.addProgressInfo(addProgressInfoParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/DeleteProgressInfo")
    @ApiOperation(value = "删除总体进度节点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "proName",value = "名称",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R deleteProgressInfo(@RequestParam("proName") String proName,@RequestParam("projectId") String projectId){
        projectProgressinfoService.deleteProgressInfo(proName,projectId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getAllBim")
    @ApiOperation(value = "获取所有bim模型 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getAllBim(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<Map<String,Object>> list = projectWbsbimtypeService.getAllBim(userId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/getBindBim/{projectId}")
    @ApiOperation(value = "获取绑定的bim模型 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getBindBim(@PathVariable("projectId") String projectId){
        List<Map<String,Object>> list = projectWbsbimtypeService.getBindBim(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
}
