package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.MapProjectEntity;
import com.sx.drainage.params.MapParams;
import com.sx.drainage.service.MapProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/28
 * Time: 17:27
 */
@Api(value = "/api/MapManage",description = "地图信息")
@CrossOrigin
@RestController
@RequestMapping("/api/MapManage")
public class MapManageController {

    @Autowired
    private MapProjectService mapProjectService;

    @GetMapping("/GetAllProjectMapPoint")
    @ApiOperation(value = "获取地图所有项目坐标信息")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getAllProjectMapPoint(){
        List<MapProjectEntity> map=mapProjectService.getAllMap();
        List<Map<String,Object>> list = new ArrayList<>();
        map.forEach(li ->{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("CenterPoint",li.getCenterpoint());
            hashMap.put("address",li.getAddress());
            hashMap.put("createtime",li.getCreatetime());
            hashMap.put("del",li.getDel());
            hashMap.put("deviceId",li.getDeviceid());
            hashMap.put("deviceName",li.getDevicename());
            hashMap.put("images",li.getImages());
            hashMap.put("name",li.getName());
            hashMap.put("projectId",li.getProjectid());
            hashMap.put("remarks",li.getRemarks());
            hashMap.put("status",li.getStatus());
            hashMap.put("sysId",li.getSysid());
            hashMap.put("value",li.getValue());
            list.add(hashMap);
        });
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetProjectMapPoint")
    @ApiOperation(value = "根据项目获取地图坐标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目id",required = true,dataType = "String")
    })
    public R getProjectMapPoint(@RequestParam("projectId") String projectId){
        List<Map<String,Object>> list = mapProjectService.getProjectMapPoint(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R post(@RequestBody MapParams mapParams){
        mapProjectService.post(mapParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R put(@RequestBody MapParams mapParams){
        mapProjectService.put(mapParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sysId",value = "主键id",required = true,dataType = "String")
    })
    public R delete(@RequestParam("sysId") String sysId){
        mapProjectService.delete(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
