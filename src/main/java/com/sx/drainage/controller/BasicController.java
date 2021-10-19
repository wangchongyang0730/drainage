package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.ProjectBasicParams;
import com.sx.drainage.params.ProjectParams;
import com.sx.drainage.params.ProjectParticipantsParams;
import com.sx.drainage.service.ProjectProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 14:02
 */
@Api(value = "/api/Basic",description = "获取当前项目的信息")
@CrossOrigin
@RestController
@RequestMapping("/api/Basic")
public class BasicController {
    @Autowired
    private ProjectProjectService projectProjectService;

    @GetMapping("/GetProjectInfo")
    @ApiOperation(value = "获取当前项目的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "sysId", required = true, dataType = "String")
    })
    public R getProjectInfo(@RequestParam("sysId") String sysId){
        Map<String, Object> map = projectProjectService.getProjectInfo(sysId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetTwoAdmin")
    @ApiOperation(value = "获取所有二级管理员")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getTwoAdmin(){
        List<Map<String,Object>> map=projectProjectService.getTwoAdmin();
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetProjectBasicInfo")
    @ApiOperation(value = "获取当前项目的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getProjectBasicInfo(@RequestParam("projectId") String projectId){
        Map<String, Object> map = projectProjectService.getProjectDetails(projectId);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetProjectParticipants")
    @ApiOperation(value = "获取当前项目参建单位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int")
    })
    public R getProjectParticipants(@RequestParam("projectId") String projectId,
                                    @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord){
        Map<String, Object> map=projectProjectService.getProjectParticipants(projectId,page,pageRecord);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PutMapping("/PutProjectInfo")
    @ApiOperation(value = "修改当前项目信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putProjectInfo(@RequestBody ProjectParams projectParams){
        projectProjectService.putProjectInfo(projectParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/PutProjectBasicInfo")
    @ApiOperation(value = "更新当前项目的基本信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putProjectBasicInfo(@RequestBody ProjectBasicParams projectBasicParams){
        projectProjectService.putProjectBasicInfo(projectBasicParams);
        return R.ok(1,"更新成功!",null,true,null);
    }
    @DeleteMapping("/DeleteProject")
    @ApiOperation(value = "删除项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteProject(@RequestParam("sysId") String sysId){
        projectProjectService.deleteProject(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/GetAllCpmpanyUser")
    @ApiOperation(value = "获取设置管理员的用户数据（项目下的参建单位中的用户）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "", required = false, dataType = "int")
    })
    public R getAllCpmpanyUser(@RequestParam("projectId") String projectId,
                               @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                               @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                               @RequestParam(value = "where",required = false) String where){
        Map<String,Object> map = projectProjectService.getAllCpmpanyUser(projectId,page,pageRecord,where);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PostMapping("/PostProjectParticipants")
    @ApiOperation(value = "新增当前项目的参建单位")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postProjectParticipants(@RequestBody ProjectParticipantsParams projectParticipantsParams){
        projectProjectService.postProjectParticipants(projectParticipantsParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/PutProjectParticipants")
    @ApiOperation(value = "修改当前项目参建单位的信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putProjectParticipants(@RequestBody ProjectParticipantsParams projectParticipantsParams){
        projectProjectService.putProjectParticipants(projectParticipantsParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/DeleteProjectParticipants")
    @ApiOperation(value = "删除当前项目的参建单位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteProjectParticipants(@RequestParam("sysId") String sysId){
        projectProjectService.deleteProjectParticipants(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @PostMapping("/PostProject")
    @ApiOperation(value = "新增项目")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postProject(@RequestBody ProjectParams projectParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectService.postProject(projectParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
}
