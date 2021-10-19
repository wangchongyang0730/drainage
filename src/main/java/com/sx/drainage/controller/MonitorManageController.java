package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.params.AddInitializationData;
import com.sx.drainage.params.AddReportInfo;
import com.sx.drainage.params.MonitorDailyParams;
import com.sx.drainage.service.ProjectMonitordailyService;
import com.sx.drainage.service.ProjectMonitordataService;
import com.sx.drainage.service.ProjectMonitorinitService;
import com.sx.drainage.service.ProjectMonitorreportsetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/7
 * Time: 10:09
 */
@Slf4j
@Api(value = "/api/MonitorManage",description = "监测管理(监测报表，报表初始化信息，监测数据)")
@CrossOrigin
@RestController
@RequestMapping("/api/MonitorManage")
public class MonitorManageController {
    @Autowired
    private ProjectMonitorreportsetService projectMonitorreportsetService;
    @Autowired
    private ProjectMonitordailyService projectMonitordailyService;
    @Autowired
    private ProjectMonitordataService projectMonitordataService;
    @Autowired
    private ProjectMonitorinitService projectMonitorinitService;

    @GetMapping("/GetReportList")
    @ApiOperation(value = "获取报表集合信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "默认1", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "默认10", required = false, dataType = "int")
    })
    public R getReportList(@RequestParam("projectId") String projectId,
                           @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                           @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord){
        Map<String,Object> map=projectMonitorreportsetService.getReportList(projectId,page,pageRecord);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetReportInfo")
    @ApiOperation(value = "获取报表详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sssId", value = "", required = true, dataType = "String")
    })
    public R getReportInfo(@RequestParam("sysId") String sysId){
        Map<String,Object> map=projectMonitorreportsetService.getReportInfo(sysId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetMonitorDailyList")
    @ApiOperation(value = "获取报表数据集合信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int")
    })
    public R getMonitorDailyList(@RequestParam("reportId") String reportId,
                           @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                           @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord){
        Map<String,Object> map=projectMonitordailyService.getMonitorDailyList(reportId,page,pageRecord);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetMonitorDailyInfo")
    @ApiOperation(value = "获取报表数据详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R getMonitorDailyInfo(@RequestParam("sysId") String sysId){
        Map<String,Object> map=projectMonitordataService.getMonitorDailyInfo(sysId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PostMapping("/AddReportInfo")
    @ApiOperation(value = "新增报表信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addReportInfo(@RequestBody AddReportInfo addReportInfo){
        projectMonitorreportsetService.addReportInfo(addReportInfo);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PostMapping("/UpdatePoint")
    @ApiOperation(value = "修改报表信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updatePoint(@RequestBody AddReportInfo addReportInfo){
        projectMonitorreportsetService.updatePoint(addReportInfo);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @DeleteMapping("/DeletePoint")
    @ApiOperation(value = "删除报表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deletePoint(@RequestParam("sysId") String sysId){
        projectMonitorreportsetService.deletePoint(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/GetInitializationData")
    @ApiOperation(value = "获取初始化数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int")
    })
    public R getInitializationData(@RequestParam("reportSysId") String reportSysId,
                                   @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                   @RequestParam(value = "pageRecord",required = false,defaultValue = "10") Integer pageRecord){
        Map<String,Object> map=projectMonitorinitService.getInitializationData(reportSysId,page,pageRecord);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @PostMapping("/AddInitializationData")
    @ApiOperation(value = "新增初始化数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addInitializationData(@RequestBody AddInitializationData addInitializationData){
        projectMonitorinitService.addInitializationData(addInitializationData);
        return R.ok(1,"添加成功",null,true,null);
    }
    @PostMapping("/UpdateInitializationData")
    @ApiOperation(value = "修改初始化数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateInitializationData(@RequestBody AddInitializationData addInitializationData){
        projectMonitorinitService.updateInitializationData(addInitializationData);
        return R.ok(1,"修改成功",null,true,null);
    }
    @DeleteMapping("/DeleteInitializationData")
    @ApiOperation(value = "删除初始化数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "", required = true, dataType = "String")
    })
    public R deleteInitializationData(@RequestParam("sysId") String sysId){
        projectMonitorinitService.deleteInitializationData(sysId);
        return R.ok(1,"修改成功",null,true,null);
    }
    @PostMapping("/ImportInitializationData")
    @ApiOperation(value = "导入初始化数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "importFile", value = "", required = true, dataType = "__file")
    })
    public R importInitializationData(@RequestParam(value = "reportSysId",required = false) String reportSysId,
                                         @RequestParam("importFile") MultipartFile importFile, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        boolean res=projectMonitorinitService.importInitializationData(userId,reportSysId,importFile);
        if(res){
            projectMonitordailyService.deleteByReportSysId(userId,reportSysId);
            return R.ok(1,"初始化成功!",null,true,null);
        }else {
            return R.ok(1,"初始化失败!请检查上传模板是否符合要求!",null,true,null);
        }
    }
    @GetMapping("/DownloadMonitorData")
    @ApiOperation(value = "下载报表点位信息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "dailyId", value = "", required = false, dataType = "String")
    })
    public void downloadMonitorData(@RequestParam("reportSysId") String reportSysId,
                                    @RequestParam(value = "dailyId",required = false) String dailyId,
                                    HttpServletResponse response){
        projectMonitorreportsetService.downloadMonitorData(reportSysId,dailyId,response);
    }
    @PostMapping("/ImportMonitorDate")
    @ApiOperation(value = "新增报表数据点位,上传模板信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "dailyId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "importFile", value = "", required = true, dataType = "__file")
    })
    public R importMonitorDate(@RequestParam("reportSysId") String reportSysId,
                               @RequestParam(value = "dailyId",required = false) String dailyId,
                               @RequestParam("importFile") MultipartFile importFile){
        return projectMonitordataService.importMonitorDate(reportSysId,dailyId,importFile);
    }
    @PostMapping("/UpdateMonitorDaily")
    @ApiOperation(value = "修改报表数据信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateMonitorDaily(@RequestBody MonitorDailyParams monitorDailyParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectMonitordataService.updateMonitorDaily(monitorDailyParams,userId);
        return R.ok(1,"修改完成!",null,true,null);
    }
    @PostMapping("/AddMonitorDaily")
    @ApiOperation(value = "新增报表数据信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addMonitorDaily(@RequestBody MonitorDailyParams monitorDailyParams, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectMonitordataService.addMonitorDaily(monitorDailyParams,userId);
        return R.ok(1,"添加完成!",null,true,null);
    }
    @GetMapping("/GetPagePointList")
    @ApiOperation(value = "分页获取点位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "报表id（必填）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pointStartDate", value = "查询开始时间", required = true, dataType = "date-time"),
            @ApiImplicitParam(paramType = "query", name = "pointEndDate", value = "查询结束时间", required = true, dataType = "date-time"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "分页大小（默认10）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "当前页（默认1）", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "排序对象（默认pointName）", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isAsc", value = "是否升序", required = false, dataType = "boolean"),
    })
    public R getPagePointList(@RequestParam("reportSysId") String reportSysId,
                              @RequestParam("pointStartDate") String pointStartDate,
                              @RequestParam("pointEndDate") String pointEndDate,
                              @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                              @RequestParam(value = "sort",required = false,defaultValue = "pointName") String sort,
                              @RequestParam(value = "isAsc",required = false,defaultValue = "false") Boolean isAsc){
        return projectMonitordailyService.getPagePointList(reportSysId,pointStartDate,pointEndDate,pageSize,pageIndex,sort,isAsc);
    }
    @GetMapping("/GetDataList")
    @ApiOperation(value = "根据点位名称和时间查询点位数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportSysId", value = "报表id（必填）", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "查询开始时间", required = true, dataType = "date-time"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "查询结束时间", required = true, dataType = "date-time"),
            @ApiImplicitParam(paramType = "query", name = "pointName", value = "点位名称（不传查所有）", required = false, dataType = "String")
    })
    public R getDataList(@RequestParam("reportSysId") String reportSysId,
                              @RequestParam("startDate") String startDate,
                              @RequestParam("endDate") String endDate,
                              @RequestParam(value = "pointName",required = false) String pointName){
        return projectMonitordailyService.getDataList(reportSysId,startDate,endDate,pointName);
    }
    @DeleteMapping("/DeleteMonitorDaily")
    @ApiOperation(value = "删除报表数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "报表id（必填）", required = true, dataType = "String"),
    })
    public R deleteMonitorDaily(@RequestParam("sysId") String sysId){
        projectMonitordailyService.deleteMonitorDaily(sysId);
        return R.ok(1,"刪除成功!",null,true,null);
    }
}
