package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.VideoParams;
import com.sx.drainage.service.ProjectTechnologvideoService;
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
 * Time: 10:09
 */
@Api(value = "/api/VideoManage",description = "视频管理（场地漫游，施工方案模拟）")
@CrossOrigin
@RestController
@RequestMapping("/api/VideoManage")
public class VideoManageController {
    @Autowired
    private ProjectTechnologvideoService projectTechnologvideoService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.type", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.sort", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.isAsc", value = "", required = false, dataType = "boolean"),
            @ApiImplicitParam(paramType = "query", name = "request[search]", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.queryJson", value = "", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam("request.type") String type,
                         @RequestParam(value = "request.projectId",required = false) String projectId,
                         @RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc,
                         @RequestParam(value = "request[search]",required = false) String search,
                         @RequestParam(value = "request.queryJson",required = false) String queryJson
                         ){
        return projectTechnologvideoService.getPageList(type,projectId,currentPage,pageSize,sort,isAsc,search,queryJson);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addVideo(@RequestBody VideoParams videoParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectTechnologvideoService.addVideo(videoParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "sysId主键", required = true, dataType = "String")
    })
    public R get(@PathVariable("id") String id){
        return projectTechnologvideoService.get(id);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型(1场地漫游模型，2施工方案模拟视频，用于区分权限)", required = true, dataType = "String")
    })
    public R put(@RequestBody VideoParams videoParams, HttpServletRequest request,@RequestParam("type") String type){
        String userId = (String) request.getAttribute("userId");
        projectTechnologvideoService.updateVideo(videoParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型(1场地漫游模型，2施工方案模拟视频，用于区分权限)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R delete(@RequestParam("sysId") String sysId, HttpServletRequest request,@RequestParam("type") String type){
        String userId = (String) request.getAttribute("userId");
        projectTechnologvideoService.delete(sysId,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }

}
