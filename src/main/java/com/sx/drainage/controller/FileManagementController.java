package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectFileTypeEntity;
import com.sx.drainage.service.ProjectFileTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/19
 * Time: 14:46
 */
@Api(value = "/api/fileManagement",description = "档案管理 *")
@CrossOrigin
@RestController
@RequestMapping("/api/fileManagement")
@RequiredArgsConstructor
public class FileManagementController {

    private final ProjectFileTypeService projectFileTypeService;

    @GetMapping("/getFileTypeTree")
    @ApiOperation(value = "获取档案类型树")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getFileTypeTree(){
        List<ProjectFileTypeEntity> tree = projectFileTypeService.getFileTypeTree();
        return R.ok(1,"获取成功!",tree,true,null);
    }
    @PostMapping("/addFileType")
    @ApiOperation(value = "添加档案类型")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R addFileType(@RequestBody ProjectFileTypeEntity entity){
        projectFileTypeService.addFileType(entity);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @GetMapping("/getFileType")
    @ApiOperation(value = "获取文件类型")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getFileType(){
        List<ProjectFileTypeEntity> list = projectFileTypeService.getFileType();
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/getParentFileType")
    @ApiOperation(value = "获取父级文件类型")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getParentFileType(){
        List<ProjectFileTypeEntity> list = projectFileTypeService.getParentFileType();
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/getFile")
    @ApiOperation(value = "获取文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "文件类型id",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目id",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "fileName",value = "文件名称",required = false,dataType = "String")
    })
    public R getFile(@RequestParam(value = "sysId",required = false) String sysId,
                     @RequestParam(value = "projectId",required = false) String projectId,
                     @RequestParam(value = "fileName",required = false) String fileName){
        List<Map<String, Object>> files = projectFileTypeService.getFiles(sysId, projectId, fileName);
        return R.ok(1,"获取成功!",files,true,null);
    }
}
