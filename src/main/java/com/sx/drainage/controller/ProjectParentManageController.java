package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectInformationEntity;
import com.sx.drainage.entity.ProjectParentProjectEntity;
import com.sx.drainage.params.ProjectParams;
import com.sx.drainage.service.ProjectInformationService;
import com.sx.drainage.service.ProjectParentProjectService;
import com.sx.drainage.service.ProjectProjectService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 14:19
 */
@Api(value = "/api/ProjectParentManage",description = "非标段项目管理 *")
@RestController
@RequestMapping("/api/ProjectParentManage")
@CrossOrigin
@RequiredArgsConstructor
public class ProjectParentManageController {
    private final ProjectParentProjectService projectParentProjectService;
    private final ProjectInformationService projectInformationService;

    @PostMapping("/add")
    @ApiOperation(value = "添加非标段项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "fileId",value = "文件id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "项目名称",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "type",value = "项目类型",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "isBlock",value = "是否即是项目也是标段",required = true,dataType = "boolean"),
            @ApiImplicitParam(paramType = "query",name = "projectDescribe",value = "项目描述",required = false,dataType = "String")
    })
    public R add(@RequestParam("fileId") String fileId, @RequestParam("name") String name,
                 @RequestParam(value = "type",required = false) String type, @RequestParam("isBlock") Boolean isBlock,
                 @RequestParam(value = "projectDescribe",required = false) String projectDescribe,
                 HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectParentProjectService.add(fileId,name,projectDescribe,userId,type,isBlock);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/additional")
    @ApiOperation(value = "资料补充")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R additional(@RequestBody ProjectInformationEntity projectInformationEntity){
        projectInformationService.additional(projectInformationEntity);
        return R.ok(1,"添加完成!",null,true,null);
    }
    @GetMapping("/getDetails/{projectId}")
    @ApiOperation(value = "非标段项目详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getDetails(@PathVariable("projectId") String projectId){
        return projectParentProjectService.getDetails(projectId);
    }
    @PutMapping("/update")
    @ApiOperation(value = "非标段项目信息修改")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R updateById(@RequestBody @ApiParam(value = "必填(sysId,name)") ProjectParentProjectEntity projectParentProjectEntity){
        if(projectParentProjectEntity.getSysId()==null){
            return R.error(500,"项目id不可为空!");
        }
        if(projectParentProjectEntity.getName()==null){
            return R.error(500,"项目名称不可为空!");
        }
        projectParentProjectService.updateDetails(projectParentProjectEntity);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/delete/{sysId}")
    @ApiOperation(value = "非标段项目删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "sysId",value = "项目id",required = true,dataType = "String")
    })
    public R deleteById(@PathVariable("sysId") String sysId){
        projectParentProjectService.deleteById(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
