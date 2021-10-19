package com.sx.drainage.controller;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectCompanyEntity;
import com.sx.drainage.service.ProjectCompanyService;
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
 * Date: 2020/9/3
 * Time: 13:49
 */
@Api(value = "/api/Company",description = "参建单位管理")
@CrossOrigin
@RestController
@RequestMapping("/api/Company")
public class CompanyController {

    @Autowired
    private ProjectCompanyService projectCompanyService;

    @GetMapping("/GetAllCompany")
    @ApiOperation(value = "获取所有参建单位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "companyName", value = "", required = true, dataType = "String")
    })
    public R GetAllCompany(){
        List<ProjectCompanyEntity> data=projectCompanyService.getAll();
        Map<Object, Object> map = new HashMap<>();
        map.put("data",data);
        map.put("num",0);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetPageCompany")
    @ApiOperation(value = "获取所有参建单位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = true, dataType = "int")
    })
    public R getPageCompany(@RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord,
                            @RequestParam(value = "page",required = false,defaultValue = "1") Integer page){
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        PageUtils pageUtils = projectCompanyService.queryPage(map);
        List<Map<String, Object>> list = new ArrayList<>();
        List<ProjectCompanyEntity> data = (List<ProjectCompanyEntity>) pageUtils.getList();
        data.forEach(da ->{
            Map<String, Object> entity = new HashMap<>();
            entity.put("id",da.getId());
            entity.put("name",da.getName());
            entity.put("remark",da.getRemark());
            list.add(entity);
        });
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("rows",list);
        hashMap.put("total",pageUtils.getTotalCount());
        return R.ok(1,"获取成功!",hashMap,true,null);
    }
    @GetMapping("/GetModel")
    @ApiOperation(value = "获取参建单位详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R getModel(@RequestParam("sysId") String sysId){
        ProjectCompanyEntity entity = projectCompanyService.getCompany(sysId);
        Map<String, Object> data = new HashMap<>();
        data.put("id",entity.getId());
        data.put("name",entity.getName());
        data.put("remark",entity.getRemark());
        return R.ok(1,"获取成功!",data,true,null);
    }
    @PostMapping("/AddCompany")
    @ApiOperation(value = "新建参建单位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "", required = false, dataType = "String")
    })
    public R addCompany(@RequestParam("name") String name,@RequestParam(value = "remark",required = false) String remark){
        projectCompanyService.addCompany(name,remark);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/DeleteCompany")
    @ApiOperation(value = "删除参建单位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteCompany(@RequestParam("sysId") String sysId){
        projectCompanyService.deleteCompany(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @PostMapping("/UpdateCompany")
    @ApiOperation(value = "修改参建单位")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "", required = false, dataType = "String")
    })
    public R updateCompany(@RequestParam("sysId") String sysId,
                           @RequestParam("name") String name,
                           @RequestParam(value = "remark",required = false) String remark){
        projectCompanyService.updateCompany(sysId,name,remark);
        return R.ok(1,"修改成功!",null,true,null);
    }
}
