package com.sx.drainage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.*;
import com.sx.drainage.entity.ProjectMonitordailyEntity;
import com.sx.drainage.entity.ProjectMonitorinitEntity;
import com.sx.drainage.entity.ProjectMonitorreportsetEntity;
import com.sx.drainage.params.MonitorDailyParams;
import com.sx.drainage.params.MonitorDataList;
import com.sx.drainage.service.ProjectMonitordailyService;
import com.sx.drainage.service.ProjectMonitorinitService;
import com.sx.drainage.service.ProjectMonitorreportsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.ProjectMonitordataDao;
import com.sx.drainage.entity.ProjectMonitordataEntity;
import com.sx.drainage.service.ProjectMonitordataService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service("projectMonitordataService")
@Transactional
public class ProjectMonitordataServiceImpl extends ServiceImpl<ProjectMonitordataDao, ProjectMonitordataEntity> implements ProjectMonitordataService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ProjectMonitordailyService projectMonitordailyService;
    @Autowired
    private ProjectMonitorreportsetService projectMonitorreportsetService;
    @Autowired
    private ProjectMonitorinitService projectMonitorinitService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectMonitordataEntity> page = this.page(
                new Query<ProjectMonitordataEntity>().getPage(params),
                new QueryWrapper<ProjectMonitordataEntity>()
        );

        return new PageUtils(page);
    }

    //获取报表数据详细信息
    @Override
    public Map<String, Object> getMonitorDailyInfo(String sysId) {
        ProjectMonitordailyEntity pr = projectMonitordailyService.getPr(sysId);
        if (pr != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId", pr.getSysid());
            map.put("code", pr.getCode());
            map.put("monitorDate", pr.getMonitordate() == null ? pr.getMonitordate() : format.format(pr.getMonitordate()));
            map.put("weater", pr.getWeater());
            map.put("remark", pr.getRemark());
            List<ProjectMonitordataEntity> list = this.list(new QueryWrapper<ProjectMonitordataEntity>().eq("del", 0).eq("dailySysId", pr.getSysid()));
            Sort.dataSort(list);
            List<Map<String, Object>> data = new ArrayList<>();
            if (list != null) {
                list.forEach(li -> {
                    Map<String, Object> entity = new HashMap<>();
                    entity.put("sysId", li.getSysid());
                    entity.put("dailySysId", li.getDailysysid());
                    entity.put("pointName", li.getPointname());
                    entity.put("pointD", li.getPointd() == null ? 0 : li.getPointd());
                    entity.put("pointTotalD", li.getPointtotald() == null ? 0 : li.getPointtotald());
                    entity.put("pointZ", li.getPointz() == null ? 0 : li.getPointz());
                    entity.put("currentValueD", li.getCurrentvalued() == null ? 0 : li.getCurrentvalued());
                    entity.put("pointTotalZ", li.getPointtotalz() == null ? 0 : li.getPointtotalz());
                    entity.put("currentValueZ", li.getCurrentvaluez() == null ? 0 : li.getCurrentvaluez());
                    entity.put("remark", li.getRemark());
                    entity.put("createDate", li.getCreatedate() == null ? li.getCreatedate() : format.format(li.getCreatedate()));
                    entity.put("createUser", li.getCreateuser());
                    entity.put("updateDate", li.getUpdatedate() == null ? li.getUpdatedate() : format.format(li.getUpdatedate()));
                    entity.put("updateUser", li.getUpdateuser());
                    entity.put("deleteDate", li.getDeletedate() == null ? li.getDeletedate() : format.format(li.getDeletedate()));
                    entity.put("deleteUser", li.getDeleteuser());
                    entity.put("del", li.getDel());
                    data.add(entity);
                });
            }
            map.put("dataList", data);
            return map;
        }
        return null;
    }

    //获取数据
    @Override
    public List<ProjectMonitordataEntity> getAllByDailyId(String dailyId) {
        return this.list(new QueryWrapper<ProjectMonitordataEntity>().eq("del", 0).eq("dailySysId", dailyId));
    }

    //新增报表数据点位,上传模板信息
    @Override
    public R importMonitorDate(String reportSysId, String dailyId, MultipartFile importFile) {
        String last = importFile.getOriginalFilename().substring(importFile.getOriginalFilename().lastIndexOf("."));
        if (!last.equals(".xlsx")) {
            return R.ok(401, "请检查上传模板!", null, true, null);
        } else {
            ProjectMonitorreportsetEntity entity = projectMonitorreportsetService.getOne(reportSysId);
            List<String> allDailyId = projectMonitordailyService.getAllIdByReportSysId(reportSysId);
            List<String> allDailyIds = projectMonitorinitService.getAllIdByReportSysId(reportSysId);
            if (allDailyIds.size() <= 0) {
                return R.ok(401, "请先初始化!", null, true, null);
            }
            if (dailyId != null) {
                allDailyId.remove(dailyId);
            }
            try {
                InputStream inputStream = importFile.getInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                XSSFRow row = sheet.getRow(0);
                log.error("模板类型：" + entity.getTemplatetype());
                switch (entity.getTemplatetype()) {
                    case "1":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("水平变化值(mm)") &&
                                row.getCell(2).toString().equals("垂直变化值(mm)")) {
                            List<Map<String, Object>> maps = sumAndRes(1, sheet, allDailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                    case "2":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("水平变化值(mm)")) {
                            List<Map<String, Object>> maps = sumAndRes(2, sheet, allDailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                    case "3":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("垂直变化值(mm)")) {
                            List<Map<String, Object>> maps = sumAndRes(3, sheet, allDailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                    case "4":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("当前轴力值（KN）")) {
                            List<Map<String, Object>> maps = sumAndRes(1, sheet, dailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                    case "5":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("当前水位值（mm）")) {
                            List<Map<String, Object>> maps = sumAndRes(2, sheet, dailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                    case "6":
                        if (row.getCell(0).toString().equals("点位名称") &&
                                row.getCell(1).toString().equals("变化值（‰）")) {
                            List<Map<String, Object>> maps = sumAndRes(3, sheet, allDailyId, reportSysId);
                            return R.ok(1, "操作成功!", JSONArray.toJSONString(maps), true, null);
                        } else {
                            return R.ok(401, "请检查上传模板!", null, true, null);
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok(1, "暂未使用!", null, true, null);
    }

    //修改报表数据信息
    @Override
    public void updateMonitorDaily(MonitorDailyParams monitorDailyParams, String userId) {
        ProjectMonitordailyEntity entity = new ProjectMonitordailyEntity();
        entity.setSysid(monitorDailyParams.getSysId());
        try {
            entity.setMonitordate(formats.parse(monitorDailyParams.getMonitorDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entity.setReportsysid(monitorDailyParams.getReportSysId());
        entity.setCode(monitorDailyParams.getCode());
        entity.setRemark(monitorDailyParams.getRemark());
        entity.setWeater(monitorDailyParams.getWeater());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        projectMonitordailyService.updateById(entity);
        ProjectMonitordataEntity entitys = new ProjectMonitordataEntity();
        entitys.setDel(1);
        entitys.setDeleteuser(userId);
        entitys.setDeletedate(new Date());
        this.update(entitys, new UpdateWrapper<ProjectMonitordataEntity>().eq("dailySysId", monitorDailyParams.getSysId()));
        List<MonitorDataList> list = monitorDailyParams.getMonitordataList();
        List<ProjectMonitordataEntity> data = new ArrayList<>();
        list.forEach(li -> {
            ProjectMonitordataEntity child = new ProjectMonitordataEntity();
            child.setSysid(CreateUuid.uuid());
            child.setDailysysid(monitorDailyParams.getSysId());
            child.setDel(0);
            child.setPointname(li.getPointName());
            child.setPointd(new BigDecimal(li.getPointD()));
            child.setPointtotald(new BigDecimal(li.getPointTotalD()));
            child.setCurrentvalued(new BigDecimal(li.getCurrentValueD()));
            child.setPointz(new BigDecimal(li.getPointZ()));
            child.setPointtotalz(new BigDecimal(li.getPointTotalZ()));
            child.setCurrentvaluez(new BigDecimal(li.getCurrentValueZ()));
            child.setRemark(li.getRemark());
            child.setCreatedate(new Date());
            child.setCreateuser(userId);
            data.add(child);
        });
        this.saveBatch(data);
    }

    //新增报表数据信息
    @Override
    public void addMonitorDaily(MonitorDailyParams monitorDailyParams, String userId) {
        ProjectMonitordailyEntity entity = new ProjectMonitordailyEntity();
        String uuid = CreateUuid.uuid();
        entity.setSysid(uuid);
        try {
            entity.setMonitordate(formats.parse(monitorDailyParams.getMonitorDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entity.setReportsysid(monitorDailyParams.getReportSysId());
        entity.setCode(monitorDailyParams.getCode());
        entity.setRemark(monitorDailyParams.getRemark());
        entity.setWeater(monitorDailyParams.getWeater());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setDel(0);
        projectMonitordailyService.addMonitorDaily(entity);
        List<MonitorDataList> list = monitorDailyParams.getMonitordataList();
        List<ProjectMonitordataEntity> data = new ArrayList<>();
        list.forEach(li -> {
            ProjectMonitordataEntity child = new ProjectMonitordataEntity();
            child.setSysid(CreateUuid.uuid());
            child.setDailysysid(uuid);
            child.setDel(0);
            child.setPointname(li.getPointName());
            child.setPointd(new BigDecimal(li.getPointD()));
            child.setPointtotald(new BigDecimal(li.getPointTotalD()));
            child.setCurrentvalued(new BigDecimal(li.getCurrentValueD()));
            child.setPointz(new BigDecimal(li.getPointZ()));
            child.setPointtotalz(new BigDecimal(li.getPointTotalZ()));
            child.setCurrentvaluez(new BigDecimal(li.getCurrentValueZ()));
            child.setRemark(li.getRemark());
            child.setCreatedate(new Date());
            child.setCreateuser(userId);
            data.add(child);
        });
        this.saveBatch(data);
    }

    //删除报表数据信息
    @Override
    public void deleteMonitorDaily(String sysId) {
        ProjectMonitordataEntity entity = new ProjectMonitordataEntity();
        entity.setDel(1);
        this.update(entity, new UpdateWrapper<ProjectMonitordataEntity>().eq("dailySysId", sysId));
    }

    //需累计计算并生成结果
    private List<Map<String, Object>> sumAndRes(int num, XSSFSheet sheet, List<String> allDailyId, String reportSysId) {
        List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
        Sort.initSort(data);
        List<ProjectMonitordataEntity> list = new ArrayList<>();
        if (allDailyId.size()>0){
            if (allDailyId.size() == 1) {
                List<ProjectMonitordataEntity> oneSum = baseMapper.getOneSum(allDailyId.get(0));
                Sort.dataSort(oneSum);
                list.addAll(oneSum);
            } else {
                List<ProjectMonitordataEntity> allSum = baseMapper.getAllSum(allDailyId);
                Sort.dataSort(allSum);
                list.addAll(allSum);
            }
        }
        List<ProjectMonitorinitEntity> two = new ArrayList<>();
        data.forEach(da -> {
            list.forEach(li -> {
                if (li.getPointname().equals(da.getPointname())) {
                    two.add(da);
                }
            });
        });
        data.removeAll(two);
        List<Map<String, Object>> maps = new ArrayList<>();
        log.info("进入计算!!!!!!!!!!");
        log.info("------------ num" + num);
        switch (num) {
            case 1:
                log.info("进入1选择!!!!!!!!");
                log.info("execl表行数" + sheet.getLastRowNum());
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    list.forEach(li -> {
                        if (row.getCell(0).toString().equals(li.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", li.getPointname());
                            map.put("pointD", row.getCell(1).toString());
                            map.put("pointTotalD", li.getPointd() == null ? 0 : li.getPointd().floatValue());
                            map.put("currentValueD", li.getPointd() == null ? row.getCell(1).toString() : li.getPointd().floatValue() + Float.parseFloat(row.getCell(1).toString()));
                            map.put("pointZ", row.getCell(2).toString());
                            map.put("pointTotalZ", li.getPointz() == null ? 0 : li.getPointz().floatValue());
                            map.put("currentValueZ", li.getPointz() == null ? row.getCell(2).toString() : li.getPointz().floatValue() + Float.parseFloat(row.getCell(2).toString()));
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                    data.forEach(di -> {
                        if (row.getCell(0).toString().equals(di.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", di.getPointname());
                            map.put("pointD", row.getCell(1).toString());
                            map.put("pointTotalD", 0);
                            map.put("currentValueD", StringUtils.isEmpty(di.getStandardd()) ? row.getCell(1).toString() : Float.parseFloat(di.getStandardd()) + Float.parseFloat(row.getCell(1).toString()));
                            map.put("pointZ", row.getCell(2).toString());
                            map.put("pointTotalZ", 0);
                            map.put("currentValueZ", di.getStandardz() == null ? row.getCell(2).toString() : Float.parseFloat(di.getStandardz()) + Float.parseFloat(row.getCell(2).toString()));
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                }
                break;
            case 2:
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    list.forEach(li -> {
                        if (row.getCell(0).toString().equals(li.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", li.getPointname());
                            map.put("pointD", row.getCell(1).toString());
                            map.put("pointTotalD", li.getPointd() == null ? 0 : li.getPointz().floatValue());
                            map.put("currentValueD", li.getPointd() == null ? row.getCell(1).toString() : li.getPointd().floatValue() + Float.parseFloat(row.getCell(1).toString()));
                            map.put("pointZ", 0);
                            map.put("pointTotalZ", 0);
                            map.put("currentValueZ", 0);
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                    data.forEach(di -> {
                        if (row.getCell(0).toString().equals(di.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", di.getPointname());
                            map.put("pointD", row.getCell(1).toString());
                            map.put("pointTotalD", 0);
                            map.put("currentValueD", di.getStandardd() == null ? row.getCell(1).toString() : Float.parseFloat(di.getStandardd()) + Float.parseFloat(row.getCell(1).toString()));
                            map.put("pointZ", 0);
                            map.put("pointTotalZ", 0);
                            map.put("currentValueZ", 0);
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                }
                break;
            case 3:
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    list.forEach(li -> {
                        if (row.getCell(0).toString().equals(li.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", li.getPointname());
                            map.put("pointD", 0);
                            map.put("pointTotalD", 0);
                            map.put("currentValueD", 0);
                            map.put("pointZ", row.getCell(1).toString());
                            map.put("pointTotalZ", li.getPointz() == null ? 0 : li.getPointz().floatValue());
                            map.put("currentValueZ", li.getPointz() == null ? row.getCell(1).toString() : li.getPointz().floatValue() + Float.parseFloat(row.getCell(1).toString()));
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                    data.forEach(di -> {
                        if (row.getCell(0).toString().equals(di.getPointname())) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sysId", null);
                            map.put("dailySysId", null);
                            map.put("pointName", di.getPointname());
                            map.put("pointD", 0);
                            map.put("pointTotalD", 0);
                            map.put("currentValueD", 0);
                            map.put("pointZ", row.getCell(1).toString());
                            map.put("pointTotalZ", 0);
                            map.put("currentValueZ", di.getStandardz() == null ? row.getCell(1).toString() : Float.parseFloat(di.getStandardz()) + Float.parseFloat(row.getCell(1).toString()));
                            map.put("remark", null);
                            map.put("createDate", null);
                            map.put("createUser", null);
                            map.put("updateDate", null);
                            map.put("updateUser", null);
                            map.put("deleteDate", null);
                            map.put("deleteUser", null);
                            map.put("del", 0);
                            maps.add(map);
                        }
                    });
                }
                break;
        }
        return maps;
    }

    //无累计计算并生成结果
    private List<Map<String, Object>> sumAndRes(int num, XSSFSheet sheet, String dailyId, String reportSysId) {
        List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
        Sort.initSort(data);//排序 应为模板是有序的
        List<Map<String, Object>> maps = new ArrayList<>();
        switch (num) {
            case 1:
                if (dailyId != null) {
                    String latelyDataId = baseMapper.getLatelyDataId(reportSysId, dailyId);
                    List<ProjectMonitordataEntity> list = this.list(new QueryWrapper<ProjectMonitordataEntity>().eq("del", 0).eq("dailySysId", latelyDataId));
                    List<ProjectMonitorinitEntity> two = new ArrayList<>();
                    data.forEach(da -> {
                        list.forEach(li -> {
                            if (da.getPointname().equals(li.getPointname())) {
                                two.add(da);
                            }
                        });
                    });
                    data.removeAll(two);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        list.forEach(li -> {
                            if (row.getCell(0).toString().equals(li.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", li.getPointname());
                                map.put("pointD", li.getCurrentvalued() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(0).toString()) - li.getCurrentvalued().floatValue());
                                String standardd = null;
                                for (int j = 0; j < two.size(); j++) {
                                    if (two.get(j).getPointname().equals(row.getCell(0).toString())) {
                                        standardd = two.get(j).getStandardd();
                                    }
                                }
                                map.put("pointTotalD", standardd == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) - Float.parseFloat(standardd));
                                map.put("currentValueD", row.getCell(1).toString());
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueZ", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueD", da.getStandardd() == null ? row.getCell(1).toString() : Float.parseFloat(da.getStandardd() + Float.parseFloat(row.getCell(1).toString())));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueZ", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                } else {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueD", da.getStandardd() == null ? row.getCell(1).toString() : Float.parseFloat(da.getStandardd() + Float.parseFloat(row.getCell(1).toString())));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueZ", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                }
                break;
            case 2:
                if (dailyId != null) {
                    String latelyDataId = baseMapper.getLatelyDataId(reportSysId, dailyId);
                    List<ProjectMonitordataEntity> list = this.list(new QueryWrapper<ProjectMonitordataEntity>().eq("del", 0).eq("dailySysId", latelyDataId));
                    List<ProjectMonitorinitEntity> two = new ArrayList<>();
                    data.forEach(da -> {
                        list.forEach(li -> {
                            if (da.getPointname().equals(li.getPointname())) {
                                two.add(da);
                            }
                        });
                    });
                    data.removeAll(two);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        list.forEach(li -> {
                            if (row.getCell(0).toString().equals(li.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", li.getPointname());
                                map.put("pointZ", li.getCurrentvalued() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(0).toString()) - li.getCurrentvaluez().floatValue());
                                String standardz = null;
                                for (int j = 0; j < two.size(); j++) {
                                    if (two.get(j).getPointname().equals(row.getCell(0).toString())) {
                                        standardz = two.get(j).getStandardz();
                                    }
                                }
                                map.put("pointTotalZ", standardz == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) - Float.parseFloat(standardz));
                                map.put("currentValueZ", row.getCell(1).toString());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueZ", da.getStandardz() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + Float.parseFloat(da.getStandardz()));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                } else {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueZ", da.getStandardz() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + Float.parseFloat(da.getStandardz()));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                }
                break;
            case 3:
                if (dailyId != null) {
                    String latelyDataId = baseMapper.getLatelyDataId(reportSysId, dailyId);
                    List<ProjectMonitordataEntity> list = this.list(new QueryWrapper<ProjectMonitordataEntity>().eq("del", 0).eq("dailySysId", latelyDataId));
                    List<ProjectMonitorinitEntity> two = new ArrayList<>();
                    data.forEach(da -> {
                        list.forEach(li -> {
                            if (da.getPointname().equals(li.getPointname())) {
                                two.add(da);
                            }
                        });
                    });
                    data.removeAll(two);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        list.forEach(li -> {
                            if (row.getCell(0).toString().equals(li.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", li.getPointname());
                                map.put("pointZ", row.getCell(1).toString());
                                map.put("pointTotalZ", li.getPointtotalz() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + li.getPointtotalz().floatValue());
                                map.put("currentValueZ", li.getCurrentvaluez() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + li.getCurrentvaluez().floatValue());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueZ", da.getStandardz() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + Float.parseFloat(da.getStandardz()));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                } else {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow row = sheet.getRow(i);
                        data.forEach(da -> {
                            if (row.getCell(0).toString().equals(da.getPointname())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("sysId", null);
                                map.put("dailySysId", null);
                                map.put("pointName", da.getPointname());
                                map.put("pointD", 0);
                                map.put("pointTotalD", 0);
                                map.put("currentValueZ", da.getStandardz() == null ? row.getCell(1).toString() : Float.parseFloat(row.getCell(1).toString()) + Float.parseFloat(da.getStandardz()));
                                map.put("pointZ", 0);
                                map.put("pointTotalZ", 0);
                                map.put("currentValueD", 0);
                                map.put("remark", null);
                                map.put("createDate", null);
                                map.put("createUser", null);
                                map.put("updateDate", null);
                                map.put("updateUser", null);
                                map.put("deleteDate", null);
                                map.put("deleteUser", null);
                                map.put("del", 0);
                                maps.add(map);
                            }
                        });
                    }
                }
                break;
        }
        return maps;
    }
}