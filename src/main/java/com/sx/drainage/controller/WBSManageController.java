package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.*;
import com.sx.drainage.service.ProjectWbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/10
 * Time: 11:43
 */
@Api(value = "/api/WBSManage",description = "wbs分部分项、wbs进度管理 wbs层级:10工区 30单位工程 50分部工程 70分项工程 90部位")
@CrossOrigin
@RestController
@RequestMapping("/api/WBSManage")
public class WBSManageController {

    @Autowired
    private ProjectWbsService projectWbsService;

    @GetMapping("/GetWbsTree")
    @ApiOperation(value = "查询WBS子节点树")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.parentId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "request.projectId", value = "", required = true, dataType = "String")
    })
    public R getWbsTree(@RequestParam(value = "request.parentId",required = false) String parentId,
                        @RequestParam(value = "request.projectId") String projectId){
        List<Map<String,Object>> map = projectWbsService.getWbsTree(parentId,projectId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "sysId主键", required = true, dataType = "String"),
    })
    public R getDetail(@PathVariable("id") String id){
        Map<String,Object> map=projectWbsService.getDetail(id);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PutMapping("/PutWbs")
    @ApiOperation(value = "更新wbs excel")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putWbs(@RequestBody WbsParams wbsParams){
        projectWbsService.putWbs(wbsParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PostMapping("/PostWbs")
    @ApiOperation(value = "新增wbs")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R postWbs(@RequestBody WbsParams wbsParams){
        projectWbsService.postWbs(wbsParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @DeleteMapping("/DeleteWbs")
    @ApiOperation(value = "删除wbs")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysid", value = "", required = true, dataType = "String")
    })
    public R deleteWbs(@RequestParam("sysid") String sysid){
        projectWbsService.deleteWbs(sysid);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/GetDeriveWBSData")
    @ApiOperation(value = "导出wbs")
    @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    public void GetDeriveWBSData(@RequestParam("projectId") String projectId, HttpServletResponse response){
        projectWbsService.getDeriveWBSData(projectId,response);
    }
    @PostMapping("/ImportWBSData")
    @ApiOperation(value = "导入WBS的工程文件数据到项目（.mpp文件）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bimType", value = "bim类型，默认为空", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "importType", value = "导入类型，默认90（100:部位，90:部位+工序）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "parentId", value = "wbs父id（有在父类id下添加，没有则覆盖）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "importFile", value = "导入文件", required = true, dataType = "__file")
    })
    public R importWBSData(@RequestParam("projectId") String projectId,
                           @RequestParam(value = "bimType",required = false) String bimType,
                           @RequestParam(value = "importType",required = false,defaultValue = "90") Integer importType,
                           @RequestParam(value = "parentId",required = false) String parentId,
                           @RequestParam("importFile") MultipartFile importFile){
        return projectWbsService.importWBSData(projectId,bimType,importType,parentId,importFile);
    }
    @PostMapping("/PostWBSProgress")
    @ApiOperation(value = "WBS进度上报并发起上报流程")
    public R postWBSProgress(@RequestBody WbsParams wbsParams){
        projectWbsService.postWBSProgress(wbsParams);
        return R.ok(1,"上报成功!",null,true,null);
    }
    @GetMapping("/GetBindWbsGroup")
    @ApiOperation(value = "获取wbs与树节点关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wbsId", value = "", required = true, dataType = "String")
    })
    public R getBindWbsGroup(@RequestParam("wbsId") String wbsId){
        return projectWbsService.getBindWbsGroup(wbsId);
    }
    @GetMapping("/GetSourceRisk")
    @ApiOperation(value = "获取wbs绑定模型重大风险源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wbsId", value = "", required = true, dataType = "String")
    })
    public R getSourceRisk(@RequestParam("wbsId") String wbsId){
        return projectWbsService.getSourceRisk(wbsId);
    }
    @GetMapping("/GetEquipment")
    @ApiOperation(value = "获取wbs绑定模型施工重大设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wbsId", value = "", required = true, dataType = "String")
    })
    public R getEquipment(@RequestParam("wbsId") String wbsId){
        return projectWbsService.getEquipment(wbsId);
    }
    @PostMapping("/BindWbsGroupId")
    @ApiOperation(value = "绑定wbs与树节点关系")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R bindWbsGroupId(@RequestBody WbsBindParams wbsBindParams){
        return projectWbsService.bindWbsGroupId(wbsBindParams);
    }
    @DeleteMapping("/DeleteBindWbsGroup")
    @ApiOperation(value = "解除wbs与树节点关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wbsId", value = "", required = true, dataType = "String")
    })
    public R deleteBindWbsGroup(@RequestParam("wbsId") String wbsId){
        projectWbsService.deleteBindWbsGroup(wbsId);
        return R.ok(1,"解除成功!",null,true,null);
    }
    @GetMapping("/GetManagementReportInformation")
    @ApiOperation(value = "获取所有管理报告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wbsId", value = "没有查全部，有查自身和父级的信息", required = false, dataType = "String")
    })
    public R getManagementReportInformation(@RequestParam(value = "projectId",required = false) String projectId,@RequestParam(value = "wbsId",required = false) String wbsId){
        return projectWbsService.getManagementReportInformation(projectId,wbsId);
    }
    @GetMapping("/GetWbsIdManagementReportInfo")
    @ApiOperation(value = "获取构建id管理报告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ConstructionId", value = "", required = false, dataType = "String")
    })
    public R getWbsIdManagementReportInfo(@RequestParam(value = "projectId",required = false) String projectId,@RequestParam(value = "ConstructionId",required = false) String ConstructionId){
        return projectWbsService.getWbsIdManagementReportInfo(projectId,ConstructionId);
    }
    @GetMapping("/GetImageProgress")
    @ApiOperation(value = "形象进度信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planBeginDate", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "planEnDate", value = "", required = false, dataType = "String")
    })
    public R getImageProgress(@RequestParam("projectId") String projectId,
                              @RequestParam(value = "planBeginDate",required = false) String planBeginDate,
                              @RequestParam(value = "planEnDate",required = false) String planEnDate){
        return projectWbsService.getImageProgress(projectId,planBeginDate,planEnDate);
    }
    @PostMapping("/AddSourceRisk")
    @ApiOperation(value = "添加wbs绑定模型重大风险源信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addSourceRisk(@RequestBody BindSourceRiskParams bindSourceRiskParams){
        projectWbsService.addSourceRisk(bindSourceRiskParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/AddEquipment")
    @ApiOperation(value = "添加wbs绑定模型施工重大设备信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addEquipment(@RequestBody BindEquipmentParams bindEquipmentParams){
        projectWbsService.addEquipment(bindEquipmentParams);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/PutSourceRisk")
    @ApiOperation(value = "修改wbs绑定模型重大风险源信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putSourceRisk(@RequestBody BindSourceRiskParams bindSourceRiskParams){
        projectWbsService.putSourceRisk(bindSourceRiskParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @PutMapping("/PutEquipment")
    @ApiOperation(value = "修改wbs绑定模型施工重大设备信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R putEquipment(@RequestBody BindEquipmentParams bindEquipmentParams){
        projectWbsService.putEquipment(bindEquipmentParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/DeleteSourceRisk")
    @ApiOperation(value = "删除wbs绑定模型重大风险源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteSourceRisk(@RequestParam("sysId") String sysId){
        projectWbsService.deleteSourceRisk(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @DeleteMapping("/DeleteEquipment")
    @ApiOperation(value = "删除wbs绑定模型施工重大设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteEquipment(@RequestParam("sysId") String sysId){
        projectWbsService.deleteEquipment(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/GetWeeklyInfo")
    @ApiOperation(value = "获取周报信息和周报的报告管理信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "week", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String")
    })
    public R getWeeklyInfo(@RequestParam("projectId") String projectId,@RequestParam(value = "week",required = false,defaultValue = "0") Integer week){
        Calendar b = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        int day1 = b.get(Calendar.DAY_OF_WEEK) - 1;
        int day2 = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day1 == 0) {
            day1 = 7;
        }
        if (day2 == 0) {
            day2 = 7;
        }
        if(week>0){
            b.add(Calendar.DATE,-day1+week*7+1);
            b.add(Calendar.DATE,-day2+(week+1)*7);
        }else if(week<0){
            b.add(Calendar.DATE, -day1 + (week+1)*7-6);
            c.add(Calendar.DATE, -day2 + (week+1)*7);
        }else {
            b.add(Calendar.DATE, -day1 + 1);
            c.add(Calendar.DATE, -day2 + 7);
        }
        Date startWeek = b.getTime();
        Date endWeek = c.getTime();
        return projectWbsService.getWeeklyInfo(projectId,startWeek,endWeek);
    }
    @PutMapping("/PutJinDu")
    @ApiOperation(value = "修改进度管理信息")
    public R putJinDu(@RequestBody JinDuParams jinDuParams){
        projectWbsService.putJinDu(jinDuParams);
        return R.ok(1,"修改成功!",null,true,null);
    }
}
