package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.service.OmModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/27
 * Time: 13:36
 */
@Slf4j
@Api(value = "/api/Common",description = "公共接口")
@CrossOrigin
@RestController
@RequestMapping("/api/Common")
public class CommonController {
    @Value("classpath:PermissionConfig.json")
    private Resource permissionConfig;
    @Autowired
    private OmModuleService omModuleService;

    @GetMapping("/GetPermissionList")
    @ApiOperation(value = "获取用户权限菜单详细信息")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getPermissionList(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<Map<String, Object>> list = omModuleService.getPermissionList(userId);
        log.error("进来了----------------------------------");
//        List<Map<String,Object>> map=null;
//        try {
//            String jsonStr = new String(IOUtils.readFully(permissionConfig.getInputStream(), -1,true));
//            map = JSONObject.parseObject(jsonStr, List.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetMenu")
    @ApiOperation(value = "获取当前用户菜单")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getMenu(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<Map<String, Object>> menu = omModuleService.getMenu(userId);
        return R.ok(1,"获取成功!",menu,true,null);
    }
    @GetMapping("/GetProjectMenu")
    @ApiOperation(value = "获取当前用户菜单（分项目权限）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectMenu(HttpServletRequest request, @RequestParam(value = "projectId",required = false,defaultValue = "0") String projectId){
        String userId = (String) request.getAttribute("userId");
        List<Map<String,Object>> list = omModuleService.getMenu(userId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetProjectPermissionList")
    @ApiOperation(value = "获取用户权限菜单详细信息（分项目权限）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "projectId",value = "项目Id",required = true,dataType = "String")
    })
    public R getProjectPermissionList(HttpServletRequest request, @RequestParam(value = "projectId",required = false,defaultValue = "0") String projectId){
        String userId = (String) request.getAttribute("userId");
        List<Map<String,Object>> list = omModuleService.getPermissionList(userId);
//        List<Map<String,Object>> map=null;
//        try {
//            String jsonStr = new String(IOUtils.readFully(permissionConfig.getInputStream(), -1,true));
//            map = JSONObject.parseObject(jsonStr, List.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return R.ok(1,"获取成功!",list,true,null);
    }
}
