package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.DeviceOperationParams;
import com.sx.drainage.service.DeviceOperationService;
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
 * Date: 2020/9/15
 * Time: 15:16
 */
@Api(value = "/api/DeviceOperation",description = "企业设备库查询")
@CrossOrigin
@RestController
@RequestMapping("/api/DeviceOperation")
public class DeviceOperationController {

    @Autowired
    private DeviceOperationService deviceOperationService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表,企业设备库查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "search", value = "", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam("projectId") String projectId,
                         @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                         @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                         @RequestParam(value = "search",required = false) String search){
        Map<String,Object> maps=deviceOperationService.getPageList(projectId,page,pageRecord,search);
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "", required = true, dataType = "String")
    })
    public R getDetail(@PathVariable("id") String id){
        Map<String,Object> maps=deviceOperationService.getDetail(id);
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putDetail(@RequestBody DeviceOperationParams deviceOperationParams){
        deviceOperationService.putDetail(deviceOperationParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postDevice(@RequestBody DeviceOperationParams deviceOperationParams){
        deviceOperationService.postDevice(deviceOperationParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteDevice(@RequestParam("sysId") String sysId){
        deviceOperationService.deleteDevice(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有设备 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAll(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<Map<String,Object>> list = deviceOperationService.getAll(userId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/getByProjectId/{projectId}")
    @ApiOperation(value = "根据项目id获取设备 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getByProjectId(@PathVariable("projectId") String projectId){
        List<Map<String,Object>> list = deviceOperationService.getByProjectId(projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }
}
