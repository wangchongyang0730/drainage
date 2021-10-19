package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.ProjectCheckParams;
import com.sx.drainage.service.ProjectProjectcheckService;
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
 * Time: 14:51
 */
@Api(value = "/api/ProjectCheck",description = "竣工验收")
@CrossOrigin
@RestController
@RequestMapping("/api/ProjectCheck")
public class ProjectCheckController {

    @Autowired
    private ProjectProjectcheckService projectProjectcheckService;
    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.sort", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.isAsc", value = "", required = false, dataType = "boolean")
    })
    public R getPageList(@RequestParam("request.projectId") String projectId,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc){
        return projectProjectcheckService.getPageList(projectId,pageSize,currentPage,sort,isAsc);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "主键sysId", required = true, dataType = "String"),
    })
    public R getDetails(@PathVariable("id") String id){
        return projectProjectcheckService.getDetails(id);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "添加")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R add(@RequestBody ProjectCheckParams projectCheckParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectcheckService.add(projectCheckParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/Put1")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put1(@RequestBody ProjectCheckParams projectCheckParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectcheckService.put(projectCheckParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Put2")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put2(@RequestBody ProjectCheckParams projectCheckParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectcheckService.put(projectCheckParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/Put3")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R put3(@RequestBody ProjectCheckParams projectCheckParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectcheckService.put(projectCheckParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键sysId", required = true, dataType = "String")
    })
    public R delete(@RequestParam("sysId") String sysId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProjectcheckService.delete(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }

}
