package com.sx.drainage.controller;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectEarlierreportEntity;
import com.sx.drainage.params.AddEarlierreport;
import com.sx.drainage.params.UpdateEarlierreport;
import com.sx.drainage.service.ProjectEarlierreportService;
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
 * Date: 2020/8/27
 * Time: 11:53
 */
@CrossOrigin
@RestController
@RequestMapping("/api/EarlierReport")
@Api(value = "/api/EarlierReport", description = "证件与审批文件")
public class EarlierReportController {

    @Autowired
    private ProjectEarlierreportService projectEarlierreportService;

    @GetMapping("/GetPageList")
    @ApiOperation(value = "获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "projectId", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "每页数量", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "search", value = "模糊查询批复证书名称、部门、编号", required = false, dataType = "String")
    })
    public R getPageList(@RequestParam("projectId") String projectId, @RequestParam(value = "page", defaultValue = "1", required = false) Integer page, @RequestParam(value = "pageRecord", defaultValue = "10", required = false) Integer pageRecord, @RequestParam(value = "search", required = false) String search) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        map.put("projectId", projectId);
        if(search.equals("%22%22")){
            map.put("search", null);
        }else{
            map.put("search",search);
        }
        PageUtils pageUtils = projectEarlierreportService.queryPage(map);
        List<ProjectEarlierreportEntity> list = (List<ProjectEarlierreportEntity>) pageUtils.getList();
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("sysId", li.getSysid());
            hashMap.put("projectId", li.getProjectid());
            hashMap.put("certificateName", li.getCertificatename());
            hashMap.put("code", li.getCode());
            hashMap.put("ratifyDate", li.getRatifydate());
            hashMap.put("ratifyDept", li.getRatifydept());
            hashMap.put("uploadfile", li.getUploadfile());
            hashMap.put("createDate", li.getCreatedate());
            hashMap.put("createUser", li.getCreateuser());
            hashMap.put("updateDate", li.getUpdatedate());
            hashMap.put("updateUser", li.getUpdateuser());
            hashMap.put("deleteDate", li.getDeletedate());
            hashMap.put("deleteUser", li.getDeleteuser());
            hashMap.put("del", li.getDel());
            hashMap.put("type", li.getType());
            maps.add(hashMap);
        });
        HashMap<String, Object> data = new HashMap<>();
        data.put("rows", maps);
        data.put("total", pageUtils.getTotalCount());
        return R.ok(1, "获取成功!", data, true, null);
    }

    @GetMapping("/GetAllList")
    @ApiOperation(value = "获取所有")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "projectId", value = "projectId", required = true, dataType = "String")
    })
    public R getAllList(@RequestParam("projectId") String projectId) {
        List<ProjectEarlierreportEntity> list = projectEarlierreportService.getAllList(projectId);
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("sysId", li.getSysid());
            hashMap.put("projectId", li.getProjectid());
            hashMap.put("certificateName", li.getCertificatename());
            hashMap.put("code", li.getCode());
            hashMap.put("ratifyDate", li.getRatifydate());
            hashMap.put("ratifyDept", li.getRatifydept());
            hashMap.put("uploadfile", li.getUploadfile());
            hashMap.put("createDate", li.getCreatedate());
            hashMap.put("createUser", li.getCreateuser());
            hashMap.put("updateDate", li.getUpdatedate());
            hashMap.put("updateUser", li.getUpdateuser());
            hashMap.put("deleteDate", li.getDeletedate());
            hashMap.put("deleteUser", li.getDeleteuser());
            hashMap.put("del", li.getDel());
            hashMap.put("type", li.getType());
            maps.add(hashMap);
        });
        return R.ok(1, "获取成功!", maps, true, null);
    }

    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "sysId主键", required = true, dataType = "String")
    })
    public R getOne(@PathVariable("id") String id) {
        ProjectEarlierreportEntity li = projectEarlierreportService.getOne(id);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("sysId", li.getSysid());
        hashMap.put("projectId", li.getProjectid());
        hashMap.put("certificateName", li.getCertificatename());
        hashMap.put("code", li.getCode());
        hashMap.put("ratifyDate", li.getRatifydate());
        hashMap.put("ratifyDept", li.getRatifydept());
        hashMap.put("uploadfile", li.getUploadfile());
        hashMap.put("createDate", li.getCreatedate());
        hashMap.put("createUser", li.getCreateuser());
        hashMap.put("updateDate", li.getUpdatedate());
        hashMap.put("updateUser", li.getUpdateuser());
        hashMap.put("deleteDate", li.getDeletedate());
        hashMap.put("deleteUser", li.getDeleteuser());
        hashMap.put("del", li.getDel());
        hashMap.put("type", li.getType());
        return R.ok(1, "获取成功!", hashMap, true, null);
    }

    @PostMapping("/Post")
    @ApiOperation(value = "新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R addEarlierreport(@RequestBody AddEarlierreport addEarlierreport) {
        projectEarlierreportService.addEarlierreport(addEarlierreport);
        return R.ok(1, "添加成功!", null, true, null);
    }

    @PutMapping("/Put")
    @ApiOperation(value = "修改")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R updateEarlierreport(@RequestBody UpdateEarlierreport updateEarlierreport) {
        projectEarlierreportService.updateEarlierreport(updateEarlierreport);
        return R.ok(1, "修改成功!", null, true, null);
    }

    @DeleteMapping("/Delete")
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "sysId主键", required = true, dataType = "String")
    })
    public R deleteEarlierreport(@RequestParam("sysId") String sysId) {
        projectEarlierreportService.deleteEarlierreport(sysId);
        return R.ok(1, "删除成功!", null, true, null);
    }
}
