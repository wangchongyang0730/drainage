package com.sx.drainage.controller;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectDeviceclashEntity;
import com.sx.drainage.params.AddClashReport;
import com.sx.drainage.params.UpdateDeviceClash;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.ProjectDeviceclashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
 * Date: 2020/8/27
 * Time: 16:57
 */
@Api(value = "/api/ClashReport",description = "获取冲突报告列表")
@RestController
@CrossOrigin
@RequestMapping("/api/ClashReport")
public class ClashReportController {
    @Autowired
    private ProjectDeviceclashService projectDeviceclashService;
    @Autowired
    private OmAccountService omAccountService;
    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取冲突报告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "request.projectId",value = "",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "request.currentPage",value = "",required = false,dataType = "int"),
            @ApiImplicitParam(paramType = "query",name = "request.pageSize",value = "",required = false,dataType = "int"),
            @ApiImplicitParam(paramType = "query",name = "request.sort",value = "",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "request.isAsc",value = "",required = false,dataType = "boolean"),
            @ApiImplicitParam(paramType = "query",name = "request.search",value = "",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "request.queryJson",value = "",required = false,dataType = "String"),
    })
    public R getPageList(@RequestParam("request.projectId") String projectId,@RequestParam(value =  "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,@RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc,@RequestParam(value = "request.search",required = false) String search,
                         @RequestParam(value = "request.queryJson",required = false) String queryJson){
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",currentPage.toString());
        map.put("limit",pageSize.toString());
        map.put("projectId",projectId);
        map.put("search",search);
        map.put("isAsc",isAsc);
        map.put("sort",sort);
        PageUtils pageUtils = projectDeviceclashService.queryPage(map);
        HashMap<String, Object> data = new HashMap<>();
        List<ProjectDeviceclashEntity> list = (List<ProjectDeviceclashEntity>) pageUtils.getList();
        List<Map<String,Object>> maps= new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("sysId",li.getSysid());
            hashMap.put("projectId",li.getProjectid());
            hashMap.put("note",li.getNote());
            hashMap.put("enclosure",li.getEnclosure());
            hashMap.put("createDate",li.getCreatedate());
            hashMap.put("createUser",li.getCreateuser());
            hashMap.put("createeUserName",li.getCreateuser()==null?null:omAccountService.getUser(li.getCreateuser()).getName());
            hashMap.put("updateDate",li.getUpdatedate());
            hashMap.put("updateUser",li.getUpdateuser());
            hashMap.put("deleteDate",li.getDeletedate());
            hashMap.put("deleteUser",li.getDeleteuser());
            hashMap.put("del",li.getDel());
            hashMap.put("pointInfo",li.getPointinfo());
            hashMap.put("flowStatus",1);
            maps.add(hashMap);
        });
        data.put("rows",maps);
        data.put("total",pageUtils.getTotalCount());
        return R.ok(1,"获取成功!",data,true,null);
    }
    @GetMapping("/GetProjectPageList")
    @ApiOperation(value = "获取冲突报告列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "",required = true,dataType = "String")
    })
    public R getProjectPageList(@RequestParam("projectId") String projectId){
        List<ProjectDeviceclashEntity> list=projectDeviceclashService.getProjectPageList(projectId);
        List<Map<String,Object>> maps= new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("sysId",li.getSysid());
            hashMap.put("projectId",li.getProjectid());
            hashMap.put("note",li.getNote());
            hashMap.put("enclosure",li.getEnclosure());
            hashMap.put("createDate",li.getCreatedate());
            hashMap.put("createUser",li.getCreateuser());
            hashMap.put("createeUserName",omAccountService.getUser(li.getCreateuser()).getName());
            hashMap.put("updateDate",li.getUpdatedate());
            hashMap.put("updateUser",li.getUpdateuser());
            hashMap.put("deleteDate",li.getDeletedate());
            hashMap.put("deleteUser",li.getDeleteuser());
            hashMap.put("del",li.getDel());
            hashMap.put("pointInfo",li.getPointinfo());
            hashMap.put("flowStatus",1);
            maps.add(hashMap);
        });
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R updateDeviceclash(@RequestBody UpdateDeviceClash updateDeviceClash){
        projectDeviceclashService.updateProjectDeviceclash(updateDeviceClash);
        return R.ok(1,"修改成功!",true,null,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "",required = true,dataType = "String")
    })
    public R deleteDeviceclash(@RequestParam("sysId") String sysId){
        projectDeviceclashService.deleteDeviceclash(sysId);
        return R.ok(1,"删除成功!",true,null,null);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取冲突报告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "id",value = "sysId主键",required = true,dataType = "String")
    })
    public R getOne(@PathVariable("id") String id){
        ProjectDeviceclashEntity li=projectDeviceclashService.getDeviceclashById(id);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("sysId",li.getSysid());
        hashMap.put("projectId",li.getProjectid());
        hashMap.put("note",li.getNote());
        hashMap.put("enclosure",li.getEnclosure());
        hashMap.put("createDate",li.getCreatedate());
        hashMap.put("createUser",li.getCreateuser());
        hashMap.put("createeUserName",omAccountService.getUser(li.getCreateuser()).getName());
        hashMap.put("updateDate",li.getUpdatedate());
        hashMap.put("updateUser",li.getUpdateuser());
        hashMap.put("deleteDate",li.getDeletedate());
        hashMap.put("deleteUser",li.getDeleteuser());
        hashMap.put("del",li.getDel());
        hashMap.put("pointInfo",li.getPointinfo());
        hashMap.put("flowStatus",1);
        return R.ok(1,"获取成功!",hashMap,true,null);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增并创建报告流程")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addOne(@RequestBody AddClashReport addClashReport, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectDeviceclashService.addDeviceclash(addClashReport,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/UploadReport")
    @ApiOperation(value = "上报冲突报告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "clashSysId",value = "",required = true,dataType = "String")
    })
    public R uploadReport(@RequestParam("clashSysId") String clashSysId){
//        TODO 暂时待定，因后期需要使用工作流引擎
        return R.ok(1,"上传成功!",null,true,null);
    }
}
