package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.WbsSizeParams;
import com.sx.drainage.service.ProjectWbsSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 9:35
 */
@Api(value = "/api/wbsSize",description = "质量管理")
@CrossOrigin
@RestController
@RequestMapping("/api/wbsSize")
public class WbsSizeController {
    @Autowired
    private ProjectWbsSizeService projectWbsSizeService;

    @PostMapping("/addWbsSize")
    @ApiOperation(value = "添加模型尺寸")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addWbsSize(@RequestBody WbsSizeParams wbsSizeParams){
        projectWbsSizeService.addWbsSize(wbsSizeParams.getWbsId(),wbsSizeParams.getWbsSize(),wbsSizeParams.getFileId());
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/updateWbsSize")
    @ApiOperation(value = "修改模型尺寸")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateWbsSize(@RequestBody WbsSizeParams wbsSizeParams){
        projectWbsSizeService.updateWbsSize(wbsSizeParams.getWbsId(),wbsSizeParams.getWbsSize(),wbsSizeParams.getFileId());
        return R.ok(1,"修改成功!",null,true,null);
    }
    @GetMapping("/queryWbsSize")
    @ApiOperation(value = "获取模型尺寸")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "id", value = "模型id或构建id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1为模型id,2为构建id", required = true, dataType = "String")
    })
    public R queryWbsSize(@RequestParam("id") String wbsId,@RequestParam("type") String type){
        Map<String,Object> map=projectWbsSizeService.queryWbsSize(wbsId,type);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @DeleteMapping("/deleteWbsSize/{wbsId}")
    @ApiOperation(value = "删除模型尺寸")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "wbsId", value = "模型id", required = true, dataType = "String")
    })
    public R deleteWbsSize(@PathVariable("wbsId") String wbsId){
        projectWbsSizeService.deleteWbsSize(wbsId);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
