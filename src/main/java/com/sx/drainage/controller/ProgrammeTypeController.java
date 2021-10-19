package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.ProgrammeTypeParams;
import com.sx.drainage.service.ProjectProgrammetypeService;
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
 * Date: 2020/9/24
 * Time: 9:51
 */
@Api(value = "/api/ProgrammeType",description = "项目设置-方案类型")
@CrossOrigin
@RestController
@RequestMapping("/api/ProgrammeType")
public class ProgrammeTypeController {
    @Autowired
    private ProjectProgrammetypeService projectProgrammetypeService;

    @GetMapping("/GetAllProgrammeType")
    @ApiOperation(value = "获取所有方案类型")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAllProgrammeType(){
        List<Map<String,Object>> list = projectProgrammetypeService.GetAllProgrammeType();
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PutMapping("/PutProgrammeType")
    @ApiOperation(value = "修改方案类型信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putProgrammeType(@RequestBody ProgrammeTypeParams programmeTypeParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProgrammetypeService.putProgrammeType(programmeTypeParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/PostProgrammeType")
    @ApiOperation(value = "新增方案类型信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postProgrammeType(@RequestBody ProgrammeTypeParams programmeTypeParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProgrammetypeService.postProgrammeType(programmeTypeParams,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/DeleteProgrammeType")
    @ApiOperation(value = "假删除方案类型信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteProgrammeType(@RequestParam("sysId") String sysId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectProgrammetypeService.deleteProgrammeType(sysId,userId);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @GetMapping("/GetProgrammeType")
    @ApiOperation(value = "获取方案类型信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R getProgrammeType(@RequestParam("sysId") String sysId){
        Map<String,Object> map=projectProgrammetypeService.getProgrammeType(sysId);
        return R.ok(1,"修改成功!",map,true,null);
    }
}
