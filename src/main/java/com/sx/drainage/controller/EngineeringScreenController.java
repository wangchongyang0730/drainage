package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectAnnualPlanEntity;
import com.sx.drainage.params.AnnualPlanParams;
import com.sx.drainage.service.EngineeringScreenService;
import com.sx.drainage.service.ProjectAnnualPlanService;
import com.sx.drainage.service.ProjectProjectService;
import com.sx.drainage.service.ProjectWbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/12
 * Time: 16:21
 * @author qianmo
 */
@Api(value = "/api/engineering",description = "工程看板 *")
@CrossOrigin
@RestController
@RequestMapping("/api/engineering")
@RequiredArgsConstructor
public class EngineeringScreenController {

    private final EngineeringScreenService engineeringScreenService;
    private final ProjectAnnualPlanService projectAnnualPlanService;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/getMilestoneNode")
    @ApiOperation(value = "里程碑节点统计 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getMilestoneNode(){
        Map<String, Object> map = engineeringScreenService.getMilestoneNode();
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/getPrice")
    @ApiOperation(value = "年度投资额形象进度表 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getPrice(){
        List<Map<String, Object>> complete = jdbcTemplate.queryForList("select DATE_FORMAT(reportTime,'%Y') date,ROUND(SUM(price),2) complete from InspectionPrice group by date");
        List<ProjectAnnualPlanEntity> plan = projectAnnualPlanService.getAll();
        List<Map<String, Object>> data = new ArrayList<>();
        if(complete.size()>plan.size()){
            complete.forEach(c -> {
                Map<String, Object> map = new HashMap<>();
                String date = (String) c.get("date");
                map.put("date",date);
                map.put("complete",c.get("complete"));
                plan.forEach(p -> {
                    if(p.getDate().equals(date)){
                        map.put("plan",p.getPrice());
                    }
                });
                if(map.size()!=3){
                    map.put("plan",null);
                }
                data.add(map);
            });
        }else{
            plan.forEach(p -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date",p.getDate());
                map.put("plan",p.getPrice());
                complete.forEach(c -> {
                    String date = (String) c.get("date");
                    if(date.equals(p.getDate())){
                        map.put("complete",c.get("complete"));
                    }
                });
                if(map.size()!=3){
                    map.put("complete",null);
                }
                data.add(map);
            });
        }
        return R.ok(1,"获取成功!",data,true,null);
    }
    @PostMapping("/add")
    @ApiOperation(value = "添加年度总计划投资额 *")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
        @ApiImplicitParam(paramType = "query",name = "date",value = "时间",required = true,dataType = "String"),
        @ApiImplicitParam(paramType = "query",name = "price",value = "金额",required = true,dataType = "float")
    })
    public R addOrUpdate(@RequestParam("date") String date,@RequestParam("price") Float priec){
        projectAnnualPlanService.add(date,priec);
        return R.ok(1,"添加成功!",null,true,null);
    }
    @PutMapping("/update")
    @ApiOperation(value = "修改年度总计划投资额 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R update(@RequestBody List<AnnualPlanParams> list){
        projectAnnualPlanService.updateAll(list);
        return R.ok(1,"修改成功!",null,true,null);
    }
    @GetMapping("/getAllProjectSchedule")
    @ApiOperation(value = "获取所有项目进度 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getAllProjectSchedule(){
        List<Map<String, Object>> data = engineeringScreenService.getAllProjectSchedule();
        return R.ok(1,"获取成功!",data,true,null);
    }
}
