package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.service.OmTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/3
 * Time: 14:19
 */
@Api(value = "/api/OrganizationalPositions",description = "组织岗位 *")
@CrossOrigin
@RestController
@RequestMapping("/api/OrganizationalPositions")
public class OrganizationalPositionsController {
    @Autowired
    private OmTagService omTagService;

    @GetMapping("/getOrganizationalList")
    @ApiOperation(value = "获取组织列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getOrganizationalList(){
        List<Map<String,Object>> data=omTagService.getOrganizationalList();
        return R.ok(1,"获取成功!",data,true,null);
    }
    @PostMapping("/insertOrganization")
    @ApiOperation(value = "新增组织")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "组织名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "描述", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tagType", value = "类型", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "use_Flag", value = "Y为启用，N为禁用", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "排序", required = false, dataType = "int")
    })
    public R insertOrganization(@RequestParam("name") String name,
                                @RequestParam(value = "remark",required = false) String remark,
                                @RequestParam("tagType") String tagType,
                                @RequestParam("use_Flag") String user_Flag,
                                @RequestParam(value = "sort",required = false) Integer sort){
        omTagService.insertOrganization(name,remark,tagType,user_Flag,sort);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/updateOrganization")
    @ApiOperation(value = "修改组织")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "组织名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tagType", value = "类型", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "use_Flag", value = "Y为启用，N为禁用", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "排序", required = false, dataType = "int")
    })
    public R updateOrganization(@RequestParam("name") String name,
                                @RequestParam("sysId") String sysId,
                                @RequestParam("remark") String remark,
                                @RequestParam("tagType") String tagType,
                                @RequestParam("use_Flag") String user_Flag,
                                @RequestParam(value = "sort",required = false) Integer sort){
        omTagService.updateOrganization(sysId,name,remark,tagType,user_Flag,sort);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/deleteOrganization")
    @ApiOperation("删除组织")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "组织编码", required = true, dataType = "String")
    })
    public R deleteOrganization(@RequestParam("sysId") String sysId){
        omTagService.deleteOrganization(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getProjectPositionsList")
    @ApiOperation("获取该项目设置组织下的岗位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tag_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "条数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getProjectPositionsList(@RequestParam("tag_id") String tag_id,
                                     @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                                     @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                     @RequestParam("projectId") String projectId){
        Map<String,Object> map=omTagService.getProjectPositionsList(tag_id,page,pageRecord,projectId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/getPositionsList")
    @ApiOperation("获取该组织下的岗位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tag_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "条数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", required = false, dataType = "int")
    })
    public R getPositionsList(@RequestParam("tag_id") String tag_id,
                              @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                              @RequestParam(value = "page",required = false,defaultValue = "1") Integer page){
        Map<String,Object> data=omTagService.getPositionsList(tag_id,page,pageRecord);
        return R.ok(1,"获取成功!",data,true,null);
    }
    @PostMapping("/inserPostion")
    @ApiOperation("在该组织下新增岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tag_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "岗位名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "enable", value = "1为启用,0为禁用", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "排序字段", required = true, dataType = "String")
    })
    public R inserPostion(@RequestParam("tag_id") String tag_id,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "enable") Integer enable,
                          @RequestParam(value = "remark") String remark,
                          @RequestParam(value = "sort") String sort){
        omTagService.inserPostion(tag_id,name,enable,remark,sort);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/updatePostion")
    @ApiOperation("在该组织下修改岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "岗位编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "岗位名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "enable", value = "1为启用,0为禁用", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "排序字段", required = true, dataType = "String")
    })
    public R updatePostion(@RequestParam("sysId") String sysId,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "enable") Integer enable,
                          @RequestParam(value = "remark") String remark,
                          @RequestParam(value = "sort") String sort){
        omTagService.updatePostion(sysId,name,enable,remark,sort);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/deletePostion")
    @ApiOperation("在该组织下删除岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "岗位编码", required = true, dataType = "String")
    })
    public R deletePostion(@RequestParam("sysId") String sysId){
        omTagService.deletePostion(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getPostionUsersList")
    @ApiOperation("获取登录管理员所在的参建单位人员信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "where", value = "姓名或账号查询", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "条数", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getPostionUsersList(@RequestParam("post_id") String post_id,
                                 @RequestParam(value = "where",required = false) String where,
                                 @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                                 @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                 @RequestParam("projectId") String projectId){
        Map<String,Object> data=omTagService.getPostionUsersList(post_id,where,page,pageRecord,projectId);
        return R.ok(1,"获取成功!",data,true,null);
    }
    @PostMapping("/insertPostionUser")
    @ApiOperation("新增岗位人员关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fk_id", value = "姓名或账号查询", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R insertPostionUser(@RequestParam("post_id") String post_id, @RequestParam("fk_id") String fk_id,
                               @RequestParam("projectId") String projectId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        omTagService.insertPostionUser(post_id,fk_id,projectId,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/updatePostionUser")
    @ApiOperation("移除岗位人员关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "组织编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fk_id", value = "姓名或账号查询", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R updatePostionUser(@RequestParam("post_id") String post_id, @RequestParam("fk_id") String fk_id,
                               @RequestParam("projectId") String projectId, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        omTagService.updatePostionUser(post_id,fk_id,projectId,userId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/getPostionRoleList")
    @ApiOperation("获取该岗位角色关系列表 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "岗位编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getPostionRoleList(@RequestParam("post_id") String post_id,@RequestParam("projectId") String projectId){
        List<Map<String,Object>> maps=omTagService.getPostionRoleList(post_id,projectId);
        return R.ok(1,"获取成功!",maps,true,null);
    }
    @PostMapping("/insertPostionRole")
    @ApiOperation("新增岗位与角色关系 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "岗位编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "role_id", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
    })
    public R insertPostionRole(@RequestParam("post_id") String post_id,@RequestParam("role_id") String role_id,HttpServletRequest request,@RequestParam("projectId") String projectId){
        String userId = (String) request.getAttribute("userId");
        omTagService.insertPostionRole(post_id,role_id,userId,projectId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/updatePostionRole")
    @ApiOperation("移除岗位与角色关系 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "post_id", value = "岗位编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "role_id", value = "角色编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R updatePostionRole(@RequestParam("post_id") String post_id,@RequestParam("role_id") String role_id,HttpServletRequest request,@RequestParam("projectId") String projectId){
        String userId = (String) request.getAttribute("userId");
        omTagService.updatePostionRole(post_id,role_id,userId,projectId);
        return R.ok(1,"移除成功!",null,true,null);
    }
    @GetMapping("/getSupervisoryEngineer/{projectId}")
    @ApiOperation(value = "获取总监理工程师 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getSupervisoryEngineer(@PathVariable("projectId") String projectId){
        List<Map<String,Object>> data = omTagService.getSupervisoryEngineer(projectId,"监理单位","总监理工程师");
        return R.ok(1,"获取成功!",data,true,null);
    }
    @GetMapping("/getStaff/{projectId}")
    @ApiOperation(value = "获取工程、项管、计划管理、监理单位信息部人员 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getStaff(@PathVariable("projectId") String projectId){
        List<Map<String,Object>> engineeringDepartment = omTagService.getSupervisoryEngineer(projectId,"建设单位","工程部");
        List<Map<String,Object>> projectManagementDepartment = omTagService.getSupervisoryEngineer(projectId,"建设单位","项管部");
        List<Map<String,Object>> comprehensivePlanningDepartment = omTagService.getSupervisoryEngineer(projectId,"建设单位","综合计划部");
        List<Map<String,Object>> informationDepartment = omTagService.getSupervisoryEngineer(projectId,"监理单位","信息部");
        Map<String, Object> map = new HashMap<>();
        map.put("engineeringDepartment",engineeringDepartment);
        map.put("projectManagementDepartment",projectManagementDepartment);
        map.put("comprehensivePlanningDepartment",comprehensivePlanningDepartment);
        map.put("informationDepartment",informationDepartment);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PutMapping("/updateLeadersInCharge")
    @ApiOperation(value = "修改项目管理人员 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "分管领导人员id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "managerId", value = "项目经理人员id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "department", value = "所属部门", required = true, dataType = "String")
    })
    public R updateLeadersInCharge(@RequestParam("projectId") String projectId,@RequestParam("userId") String userId,
                                   @RequestParam("managerId") String managerId,@RequestParam("department") String department,
                                   HttpServletRequest request){
        String user = (String) request.getAttribute("userId");
        boolean res = omTagService.updateLeadersInCharge(projectId,userId,user,managerId,department);
        if(res){
            return R.ok(1,"修改成功!",null,true,null);
        }else{
            return R.error(1,"请核对人员!");
        }
    }
}
