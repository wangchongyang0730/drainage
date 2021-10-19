package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.PriceParams;
import com.sx.drainage.service.InspectionpriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 11:05
 */
@Api(value = "/api/ProjectPriceManage",description = "项目验工计价、投资管理(1验工计价2安全施工措施费管理)")
@CrossOrigin
@RestController
@RequestMapping("/api/ProjectPriceManage")
public class ProjectPriceManageController {

    @Autowired
    private InspectionpriceService inspectionpriceService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.type", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.currentPage", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.pageSize", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "request.sort", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.isAsc", value = "", required = false, dataType = "boolean"),
            @ApiImplicitParam(paramType = "query", name = "request.search", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.queryJson", value = "", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam(value = "request.type",required = false) String type,
                         @RequestParam(value = "request.projectId",required = false) String projectId,
                         @RequestParam(value = "request.currentPage",required = false,defaultValue = "1") Integer currentPage,
                         @RequestParam(value = "request.pageSize",required = false,defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "request.sort",required = false) String sort,
                         @RequestParam(value = "request.isAsc",required = false,defaultValue = "false") Boolean isAsc,
                         @RequestParam(value = "request.search",required = false) String search,
                         @RequestParam(value = "request.queryJson",required = false) String queryJson
    ){
        return inspectionpriceService.getPageList(type,projectId,currentPage,pageSize,sort,isAsc,search,queryJson);
    }
    @GetMapping("/GetAllList")
    @ApiOperation(value = "获取所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据类型（1验工计价2安全施工措施费管理）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "search", value = "查询字符串", required = false, dataType = "String")
    })
    public R getAllList(@RequestParam("projectId") String projectId,
                        @RequestParam("type") String type,
                        @RequestParam(value = "search",required = false) String search){
        return inspectionpriceService.getAllList(projectId,type,search);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "sysId主键", required = true, dataType = "String")
    })
    public R getDetails(@PathVariable("id") String id){

        return inspectionpriceService.getDetails(id);
    }
    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addPrice(@RequestBody PriceParams priceParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        inspectionpriceService.addPrice(priceParams,userId);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updatePrice(@RequestBody PriceParams priceParams){
        inspectionpriceService.updatePrice(priceParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/Delete/{id}")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "sysId主键", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据类型（1验工计价2安全施工措施费管理）", required = true, dataType = "String")
    })
    public R delete(@RequestParam("type") String type,@PathVariable("id") String id){
        inspectionpriceService.delete(type,id);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "sysId主键", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "数据类型（1验工计价2安全施工措施费管理）", required = true, dataType = "String")
    })
    public R delete2(@RequestParam("type") String type,@RequestParam("sysId") String id){
        inspectionpriceService.delete(type,id);
        return R.ok(1,"删除成功!",null,true,null);
    }
}
