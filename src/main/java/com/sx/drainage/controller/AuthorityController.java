package com.sx.drainage.controller;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.params.AddRoleFunction;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.OmRoleService;
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
 * Date: 2020/9/2
 * Time: 9:24
 */
@Api(value = "/api/Authority", description = "权限管理")
@CrossOrigin
@RequestMapping("/api/Authority")
@RestController
public class AuthorityController {
    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmAccountService accountService;

    @GetMapping("/getRoleList")
    @ApiOperation(value = "获取角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "角色名称", required = false, dataType = "String")
    })
    public R getRoleList(@RequestParam(value = "where", required = false) String where) {
        List<Map<String, Object>> maps = omRoleService.getRoleList(where);
        return R.ok(1, "获取成功!", maps, true, null);
    }

    @GetMapping("/getPageRoleList")
    @ApiOperation(value = "获取角色列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int")
    })
    public R getPageRoleList(@RequestParam(value = "where", required = false) String where, @RequestParam(value = "pageRecord", required = false, defaultValue = "10") Integer pageRecord,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", where);
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        PageUtils pageUtils = omRoleService.queryPage(map);
        Map<String, Object> maps = new HashMap<>();
        maps.put("total", pageUtils.getTotalCount());
        List<OmRoleEntity> list = (List<OmRoleEntity>) pageUtils.getList();
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId", li.getSysid());
            entity.put("endTime", li.getEndtime());
            entity.put("startTime", li.getStarttime());
            entity.put("valid", li.getValid());
            entity.put("parent_id", li.getParentId());
            entity.put("fullName", li.getFullname());
            entity.put("enterPriseId", li.getEnterpriseid());
            entity.put("createdDate", li.getCreateddate());
            entity.put("description", li.getDescription());
            entity.put("name", li.getName());
            data.add(entity);
        });
        maps.put("rows", data);
        return R.ok(1, "获取成功!", maps, true, null);
    }

    @PostMapping("/insertRole")
    @ApiOperation(value = "新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "valid", value = "1为有效，0为无效", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "描述", required = false, dataType = "String")
    })
    public R insertRole(@RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "valid", required = false) String valid,
                        @RequestParam(value = "description", required = false) String description) {
        omRoleService.insertRole(name, valid, description);
        return R.ok(1, "添加成功!", null, true, null);
    }

    @PostMapping("/updateRole")
    @ApiOperation(value = "更新角色")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "valid", value = "1为有效，0为无效", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "描述", required = false, dataType = "String")
    })
    public R updateRole(@RequestParam(value = "sysId") String sysId,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "valid", required = false) String valid,
                        @RequestParam(value = "description", required = false) String description) {
        omRoleService.updateRole(sysId, name, valid, description);
        return R.ok(1, "修改成功!", null, true, null);
    }

    @DeleteMapping("/deleteRoleById")
    @ApiOperation(value = "根据角色ID删除信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleId", value = "角色编码", required = true, dataType = "String")
    })
    public R deleteRoleById(@RequestParam(value = "roleId") String roleId) {
        omRoleService.deleteRoleById(roleId);
        return R.ok(1, "删除成功!", null, true, null);
    }

    @GetMapping("/getRoleUserFunctionPost")
    @ApiOperation(value = "获取该角色绑定的用户/功能/岗位列表引用数据表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysid", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "1为用户，2为功能，3为岗位", required = true, dataType = "String")
    })
    public R getRoleUserFunctionPost(@RequestParam(value = "sysid") String sysid, @RequestParam(value = "type") String type) {
        return omRoleService.getRoleUserFunctionPost(sysid, type);
    }

    @GetMapping("/getRoleUserList")
    @ApiOperation(value = "获取角色与所有用户的关系列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "用户名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "用户名称", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "用户名称", required = false, dataType = "int")
    })
    public R getRoleUserList(@RequestParam(value = "sysId") String sysId, @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "pageRecord", required = false, defaultValue = "10") Integer pageRecord,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Map<String, Object> maps = omRoleService.getRoleUserList(sysId, name, pageRecord, page);
        return R.ok(1, "获取成功!", maps, true, null);
    }

    @PostMapping("/insertDeleteRoleUser")
    @ApiOperation(value = "新增、移除角色与用户关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userid", value = "人员编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "action", value = "用户名称", required = true, dataType = "String")
    })
    public R insertDeleteRoleUser(@RequestParam("sysId") String sysId, @RequestParam("userid") String userid, @RequestParam("action") String action) {
        omRoleService.insertDeleteRoleUser(sysId, userid, action);
        return R.ok(1, "操作成功!", null, true, null);
    }

    @GetMapping("/getRoleFunctionList")
    @ApiOperation(value = "获取角色与功能列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleid", value = "角色id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "moduleid", value = "功能id,可以先空", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int")
    })
    public R getRoleFunctionList(@RequestParam("roleid") String roleid,
                                 @RequestParam(value = "moduleid", required = false) String moduleid,
                                 @RequestParam(value = "pageRecord", required = false, defaultValue = "10") Integer pageRecord,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Map<String,Object> map=omRoleService.getRoleFunctionList(roleid);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PostMapping("/saveRoleFunction")
    @ApiOperation(value = "保存角色与功能关系")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R saveRoleFunction(@RequestBody AddRoleFunction addRoleFunction){
        omRoleService.saveRoleFunction(addRoleFunction);
        return R.ok(1,"操作成功!",null,true,null);
    }

}
