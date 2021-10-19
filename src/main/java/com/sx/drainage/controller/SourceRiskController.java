package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.SourceriskParams;
import com.sx.drainage.service.SourceriskService;
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
 * Date: 2020/9/22
 * Time: 13:34
 */
@Api(value = "/api/SourceRisk",description = "危险源管理")
@CrossOrigin
@RestController
@RequestMapping("/api/SourceRisk")
public class SourceRiskController {

    @Autowired
    private SourceriskService sourceriskService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.search", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.sort", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.isAsc", value = "", required = false, dataType = "boolean")
    })
    public R getPageList(@RequestParam("request.projectId") String projectId,
                         @RequestParam(value = "request.search",required = false) String search,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc){
        return sourceriskService.getPageList(projectId,pageSize,currentPage,sort,isAsc,search);
    }
    @GetMapping("/Get/")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "主键sysID", required = true, dataType = "String")
    })
    public R getDetails(@RequestParam("id") String id){
        return sourceriskService.getDetails(id);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put(@RequestBody SourceriskParams sourceriskParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        sourceriskService.put(sourceriskParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R post(@RequestBody SourceriskParams sourceriskParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        sourceriskService.post(sourceriskParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键sysId", required = true, dataType = "String")
    })
    public R delete(@RequestParam("sysId") String sysId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        sourceriskService.delete(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }

}
