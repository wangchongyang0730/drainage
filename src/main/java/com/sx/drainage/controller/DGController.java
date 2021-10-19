package com.sx.drainage.controller;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.ProjectInitPipejackingService;
import com.sx.drainage.service.ProjectPipejackingDailyService;
import com.sx.drainage.service.ProjectPipejackingYaxisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 16:41
 */
@Api(value = "/api/DG", description = "顶管")
@CrossOrigin
@RestController
@RequestMapping("/api/DG")
public class DGController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProjectInitPipejackingService projectInitPipejackingService;
    @Autowired
    private ProjectPipejackingDailyService projectPipejackingDailyService;
    @Autowired
    private ProjectPipejackingYaxisService projectPipejackingYaxisService;

    private SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");

    @GetMapping("/GetDGListByProjectId")
    @ApiOperation(value = "顶管机状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R GetDGListByProjectId(@RequestParam("projectId") String projectId) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select convert(varchar(50),dg.id) dingguanji_id,dg.name dingguanji_name,t.value dingguanji_value,t.datetime Datetime from dingguanji dg left join (select t.* from (select dingguanji_value.*,row_number() over (partition by dingguanji_id order by datetime desc) rn from dingguanji_value) t where rn=1)t on dg.id=t.dingguanji_id where dg.project_id='" + projectId + "'");
        Map<String, Object> map = new HashMap<>();
        map.put("dingguanji", list);
        map.put("projectId", projectId);
        return R.ok(1, "获取成功!", map, true, null);
    }

    @PostMapping("/initPipejacking")
    @ApiOperation(value = "初始化顶管区间 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R initPipejacking(@RequestBody ProjectInitPipejackingEntity entity) {
        projectInitPipejackingService.initPipejacking(entity);
        return R.ok(1, "添加成功!", null, true, null);
    }

    @GetMapping("/getPipejacking")
    @ApiOperation(value = "获取顶管区间 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getPipejacking(@RequestParam("projectId") String projectId) {
        List<ProjectInitPipejackingEntity> list = projectInitPipejackingService.getPipejacking(projectId);
        return R.ok(1, "获取成功!", list, true, null);
    }

    @PostMapping("/addPipejackingDaily")
    @ApiOperation(value = "新增日报 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addPipejackingDaily(@RequestBody List<ProjectPipejackingDailyEntity> list, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        projectPipejackingDailyService.addPipejackingDaily(list, userId);
        return R.ok(1, "添加成功!", null, true, null);
    }

    @GetMapping("/getLastData")
    @ApiOperation(value = "获取上次监测数据 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "顶管区间主键id", required = true, dataType = "String")
    })
    public R getLastData(@RequestParam("sysId") String sysId) {
        ProjectPipejackingDailyEntity data = projectPipejackingDailyService.getLastData(sysId);
        return R.ok(1, "获取成功!", data, true, null);
    }

    @GetMapping("/getDate")
    @ApiOperation(value = "获取所有日报已填报日期 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getDate(@RequestParam("projectId") String projectId) {
        List<Date> list = projectPipejackingDailyService.getDate(projectId);
        return R.ok(1, "获取成功!", list, true, null);
    }

    @GetMapping("/getPipejackingDaily")
    @ApiOperation(value = "获取日报详情 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportTime", value = "监测时间", required = true, dataType = "String")
    })
    public R getPipejackingDaily(@RequestParam("projectId") String projectId, @RequestParam(value = "reportTime") String reportTime) {
        if (!StringUtils.isEmpty(reportTime)) {
            List<ProjectPipejackingDailyEntity> list = projectPipejackingDailyService.getPipejackingDaily(projectId, reportTime);
            return R.ok(1, "获取成功!", list, true, null);
        } else {
            return R.error(500, "请传入正确时间!");
        }
    }

    @DeleteMapping("/deletePipejackingDaily")
    @ApiOperation(value = "删除日报 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportTime", value = "监测时间", required = true, dataType = "String")
    })
    public R deletePipejackingDaily(@RequestParam("projectId") String projectId, @RequestParam(value = "reportTime") String reportTime) {
        if (!StringUtils.isEmpty(reportTime)) {
            projectPipejackingDailyService.deletePipejackingDaily(projectId, reportTime);
            return R.ok(1, "删除成功!", null, true, null);

        } else {
            return R.error(500, "请传入正确时间!");
        }
    }

    @GetMapping("/getStatistics")
    @ApiOperation(value = "统计 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "顶管区间主键id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "开始时间", required = false, dataType = "date"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束时间", required = false, dataType = "date")
    })
    public R getStatistics(@RequestParam("sysId") String sysId, @RequestParam(value = "startDate", required = false) Date startDate, @RequestParam(value = "endDate", required = false) Date endDate) {
        Map<String, Object> map = projectPipejackingDailyService.getStatistics(sysId, startDate, endDate);
        return R.ok(1, "获取成功!", map, true, null);
    }
    @DeleteMapping("/deletePipejacking")
    @ApiOperation(value = "删除顶管区间 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "主键id", required = true, dataType = "String")
    })
    public R deletePipejacking(@RequestParam("sysId") String sysId){
        projectPipejackingDailyService.deletePipejacking(sysId);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @PostMapping("/setYAxis")
    @ApiOperation(value = "设置Y轴 *")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R setYAxis(@RequestBody ProjectPipejackingYaxisEntity entity){
        projectPipejackingYaxisService.setYAxis(entity);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @GetMapping("/getYAxis")
    @ApiOperation(value = "获取Y轴 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "initId", value = "日报初始化id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "项目id", required = true, dataType = "String")
    })
    public R getYAxis(@RequestParam("initId") String initId,@RequestParam("projectId") String projectId){
        List<ProjectPipejackingYaxisEntity> list=projectPipejackingYaxisService.getYAxis(initId,projectId);
        return R.ok(1,"获取成功!",list,true,null);
    }

}
