package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.ReportParams;
import com.sx.drainage.service.ProjectReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/15
 * Time: 13:12
 */
@Api(value = "/api/ReportManage",description = "报告管理，1质量检查（质量报告） 2方案管理（技术报告）3安全检查（安全报告） 5变更管理（变更报告）管理")
@CrossOrigin
@RestController
@RequestMapping("/api/ReportManage")
public class ReportManageController {

    @Autowired
    private ProjectReportService projectReportService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取报告列表,报告类型Type:(2方案管理(技术报告),5变更管理(变更报告))")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.type", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = true, dataType = "String")
    })
    public R getPageList(@RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,
                         @RequestParam("request.type") String type,
                         @RequestParam("request.projectId") String  projectId){
        Map<String,Object> map=projectReportService.getPageList2(currentPage,pageSize,type,projectId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/Get/{type}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "type", value = "报告类型", required = true, dataType = "String")
    })
    public R get3(@PathVariable("type")String type,@RequestParam("id") String id){
        Map<String,Object> map=projectReportService.get3(id);
        return R.ok(1,"获取成功!",map,true,null);
    }
//    @GetMapping("/Get/1")
//    @ApiOperation(value = "获取详细信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "id", value = "", required = true, dataType = "String")
//    })
//    public R get1(@RequestParam("id") String id){
//        Map<String,Object> map=projectReportService.get3(id);
//        return R.ok(1,"获取成功!",map,true,null);
//    }
//    @GetMapping("/Get/5")
//    @ApiOperation(value = "获取详细信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "id", value = "", required = true, dataType = "String")
//    })
//    public R get5(@RequestParam("id") String id){
//        Map<String,Object> map=projectReportService.get3(id);
//        return R.ok(1,"获取成功!",map,true,null);
//    }
    @PutMapping("/Put5")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put5(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.put(reportParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Put3")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put3(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.put(reportParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Put2")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put2(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.put(reportParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Put1")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put1(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.put(reportParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/Post5")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post5(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.post5(reportParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/Post1")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post1(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.post1(reportParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/Post3")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post3(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.post3(reportParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/Post2")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post2(@RequestBody ReportParams reportParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.post2(reportParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "报告类型", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R delete(@RequestParam(value = "type",required = false) String type,
                    @RequestParam(value = "sysId") String sysId,
                    HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectReportService.delete(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
