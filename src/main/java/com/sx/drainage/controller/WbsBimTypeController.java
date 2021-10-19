package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.BindBimParams;
import com.sx.drainage.params.WbsBimTypeParams;
import com.sx.drainage.service.ProjectWbsbimtypeService;
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
 * Date: 2020/9/10
 * Time: 10:14
 */
@Api(value = "/api/WbsBimType",description = "项目设置-模型管理 *")
@CrossOrigin
@RestController
@RequestMapping("/api/WbsBimType")
public class WbsBimTypeController {

    @Autowired
    private ProjectWbsbimtypeService projectWbsbimtypeService;

    @GetMapping("/GetAllWbsBimInfo")
    @ApiOperation(value = "获取所有bim类型绑定的bim模型 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getAllWbsBimInfo(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> list = projectWbsbimtypeService.getAllWbsBimInfo(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PostMapping("/BindBim")
    @ApiOperation(value = "绑定bim模型 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R bindBim(@RequestBody BindBimParams bindBimParams,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectWbsbimtypeService.bindBim(bindBimParams,userId);
        return R.ok(1,"绑定成功!",null,true,null);
    }
    @GetMapping("/GetAllWbsbimType")
    @ApiOperation(value = "获取所有模型类别")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAllWbsbimType(){
        List<Map<String,Object>> list = projectWbsbimtypeService.GetAllWbsbimType();
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PostMapping("/PostWbsbimType")
    @ApiOperation(value = "新增模型类别信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postWbsbimType(@RequestBody WbsBimTypeParams wbsBimTypeParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectWbsbimtypeService.postWbsbimType(wbsBimTypeParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/putWbsbimType")
    @ApiOperation(value = "更新模型类别信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putWbsbimType(@RequestBody WbsBimTypeParams wbsBimTypeParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectWbsbimtypeService.putWbsbimType(wbsBimTypeParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/DeleteWbsbimType")
    @ApiOperation(value = "删除模型类别信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteWbsbimType(@RequestParam("sysId") String sysId,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectWbsbimtypeService.deleteWbsbimType(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @DeleteMapping("/deleteWbsBind")
    @ApiOperation(value = "删除模型绑定 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteWbsBind(@RequestParam("sysId") String sysId,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectWbsbimtypeService.deleteWbsBind(sysId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
