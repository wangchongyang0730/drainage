package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.OperativesParams;
import com.sx.drainage.service.ProjectOperativesService;
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
 * Time: 17:28
 */
@Api(value = "/api/DeviceOperatives",description = "设备操作工管理")
@CrossOrigin
@RestController
@RequestMapping("/api/DeviceOperatives")
public class DeviceOperativesController {
    @Autowired
    private ProjectOperativesService projectOperativesService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.deviceSysId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.sort", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.isAsc", value = "", required = false, dataType = "boolean"),
            @ApiImplicitParam(paramType = "query", name = "request.search", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = false, dataType = "int")
    })
    public R getPageList(@RequestParam("request.projectId") String projectId,
                         @RequestParam("request.deviceSysId") String deviceSysId,
                         @RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc,
                         @RequestParam(value = "request.search",required = false) String search,
                         @RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize){
        return projectOperativesService.getPageList(projectId,deviceSysId,currentPage,pageSize,sort,isAsc,search);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键sysId", required = true, dataType = "String")
    })
    public R getDetails(@PathVariable("id") String id){
        return projectOperativesService.getDetails(id);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateOpertaives(@RequestBody OperativesParams operativesParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectOperativesService.updateOpertaives(operativesParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Post")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addOpertaives(@RequestBody OperativesParams operativesParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectOperativesService.addOpertaives(operativesParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/Delete/{id}")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键sysId", required = true, dataType = "String")
    })
    public R deleteOpertaives(@PathVariable("id") String id, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectOperativesService.deleteOpertaives(id,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
