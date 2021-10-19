package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmModuleEntity;
import com.sx.drainage.service.OmModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/27
 * Time: 10:57
 */
@Api(value = "/api/ModuleMenu",description = "模块管理")
@CrossOrigin
@RequestMapping("/api/ModuleMenu")
@RestController
public class ModuleMenuController {

    @Autowired
    private OmModuleService omModuleService;

    @GetMapping("/getModuleList")
    @ApiOperation(value = "获取模块列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "模块菜单id,先为空",required = false,dataType = "String")
    })
    public R getModuleList(@RequestParam(value = "sysId",required = false) String sysId){
        if(StringUtils.isEmpty(sysId)||sysId.equals("%22%22")||sysId.equals("''")){
            sysId=null;
        }
        List<OmModuleEntity> list=omModuleService.getModuleList(sysId);
        List<HashMap<String, Object>> maps = new ArrayList<>();
        list.forEach(li ->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("parent_id",li.getParentId());
            map.put("fullName",li.getFullname());
            map.put("moduleType",li.getModuletype());
            map.put("createdDate",li.getCreateddate());
            map.put("description",li.getDescription());
            map.put("name",li.getName());
            map.put("sequenceNum",li.getSequencenum());
            map.put("path",li.getUrl());
            map.put("hide",li.getHide());
            map.put("iconCss",li.getIconcss());
            maps.add(map);
        });
        return R.ok(1,"查询成功!",maps,true,null);
    }
    @GetMapping("/getModuleBySysId")
    @ApiOperation(value = "根据模块id获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "模块id",required = true,dataType = "String")
    })
    public R getModuleBySysId(@RequestParam(value = "sysId",required = false) String sysId){
        OmModuleEntity li=omModuleService.getModuleBySysId(sysId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("parent_id",li.getParentId());
        map.put("fullName",li.getFullname());
        map.put("moduleType",li.getModuletype());
        map.put("createdDate",li.getCreateddate());
        map.put("description",li.getDescription());
        map.put("name",li.getName());
        map.put("sequenceNum",li.getSequencenum());
        map.put("path",li.getUrl());
        map.put("hide",li.getHide());
        map.put("iconCss",li.getIconcss());
        return R.ok(1,"查询成功!",map,true,null);
    }
    @PostMapping("/updateModule")
    @ApiOperation(value = "修改模块中菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "模块id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "parentId",value = "上级id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "名称",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sequenceNum",value = "序号",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "path",value = "",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "iconCss",value = "",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "hide",value = "状态,0为显示,1为隐藏",required = true,dataType = "boolean")
    })
    public R updateModule(@RequestParam("sysId") String sysId,@RequestParam("parentId") String parentId,@RequestParam("name") String name
    ,@RequestParam("sequenceNum") String sequenceNum,@RequestParam("path") String path,@RequestParam("iconCss") String iconCss,@RequestParam("hide") Boolean hide){
        omModuleService.updateModule(sysId,parentId,name,sequenceNum,path,iconCss,hide);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/updateModuleFunction")
    @ApiOperation(value = "修改模块中功能")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "模块id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "parentId",value = "上级id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "名称",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "path",value = "",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "hide",value = "状态,0为显示,1为隐藏",required = true,dataType = "boolean")
    })
    public R updateModuleFunction(@RequestParam("sysId") String sysId,@RequestParam("parentId") String parentId,@RequestParam("name") String name
            ,@RequestParam("path") String path,@RequestParam("hide") Boolean hide){
        omModuleService.updateModuleFunction(sysId,parentId,name,path,hide);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/deleteModuleFunction")
    @ApiOperation(value = "删除模块/功能")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "模块id",required = true,dataType = "String")
    })
    public R deleteModuleFunction(@RequestParam("sysId") String sysId){
        omModuleService.deleteModuleFunction(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @PostMapping("/insertModule")
    @ApiOperation(value = "新增模块/功能")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "parentId",value = "上级id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "名称",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "path",value = "",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "hide",value = "状态,0为显示,1为隐藏",required = true,dataType = "boolean")
    })
    public R insertModule(@RequestParam("parentId") String parentId,@RequestParam("name") String name
            ,@RequestParam("path") String path,@RequestParam("hide") Boolean hide){
        omModuleService.insertModule(parentId,name,path,hide);
        return R.ok(1,"添加成功!",null,true,null);
    }

}
