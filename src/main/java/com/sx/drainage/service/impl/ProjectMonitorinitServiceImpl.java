package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.Sort;
import com.sx.drainage.params.AddInitializationData;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectMonitorinitDao;
import com.sx.drainage.entity.ProjectMonitorinitEntity;
import com.sx.drainage.service.ProjectMonitorinitService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service("projectMonitorinitService")
@Transactional
public class ProjectMonitorinitServiceImpl extends ServiceImpl<ProjectMonitorinitDao, ProjectMonitorinitEntity> implements ProjectMonitorinitService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectMonitorinitEntity> page = this.page(
                new Query<ProjectMonitorinitEntity>().getPage(params),
                new QueryWrapper<ProjectMonitorinitEntity>()
        );

        return new PageUtils(page);
    }

    //获取初始化数据
    @Override
    public Map<String, Object> getInitializationData(String reportSysId, Integer page, Integer pageRecord) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        IPage<ProjectMonitorinitEntity> iPage = this.page(
                new Query<ProjectMonitorinitEntity>().getPage(map),
                new QueryWrapper<ProjectMonitorinitEntity>().eq("del", 0).eq("reportSysId", reportSysId)
        );
        PageUtils pageUtils = new PageUtils(iPage);
        List<ProjectMonitorinitEntity> list = (List<ProjectMonitorinitEntity>) pageUtils.getList();
        Sort.initSort(list);//排序
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId", li.getSysid());
            entity.put("reportSysId", li.getReportsysid());
            entity.put("pointName", li.getPointname());
            entity.put("standardD", li.getStandardd() == null ? 0 : li.getStandardd());
            entity.put("standardZ", li.getStandardz() == null ? 0 : li.getStandardz());
            entity.put("actualPosition", li.getActualposition() == null ? 0 : li.getActualposition());
            entity.put("relativePosition", li.getRelativeposition() == null ? 0 : li.getRelativeposition());
            entity.put("relativePoint", li.getRelativepoint() == null ? 0 : li.getRelativepoint());
            entity.put("createDate", li.getCreatedate() == null ? li.getCreatedate() : format.format(li.getCreatedate()));
            entity.put("createUser", li.getCreateuser());
            entity.put("updateDate", li.getUpdatedate());
            entity.put("updateUser", li.getCreateuser());
            entity.put("deleteDate", li.getDeletedate());
            entity.put("deleteUser", li.getDeleteuser());
            entity.put("del", li.getDel());
            data.add(entity);
        });
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", pageUtils.getTotalCount());
        hashMap.put("rows", data);
        return hashMap;
    }

    //新增初始化数据
    @Override
    public void addInitializationData(AddInitializationData addInitializationData) {
        ProjectMonitorinitEntity entity = new ProjectMonitorinitEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setReportsysid(addInitializationData.getReportSysId());
        entity.setActualposition(addInitializationData.getActualPosition());
        entity.setPointname(addInitializationData.getPointName());
        entity.setRelativepoint(addInitializationData.getRelativePoint());
        entity.setRelativeposition(addInitializationData.getRelativePosition());
        entity.setStandardd(addInitializationData.getStandardD());
        entity.setStandardz(addInitializationData.getStandardZ());
        entity.setDel(0);
        this.save(entity);
    }

    //修改初始化数据
    @Override
    public void updateInitializationData(AddInitializationData addInitializationData) {
        ProjectMonitorinitEntity entity = new ProjectMonitorinitEntity();
        entity.setSysid(addInitializationData.getSysId());
        entity.setReportsysid(addInitializationData.getReportSysId());
        entity.setActualposition(addInitializationData.getActualPosition());
        entity.setPointname(addInitializationData.getPointName());
        entity.setRelativepoint(addInitializationData.getRelativePoint());
        entity.setRelativeposition(addInitializationData.getRelativePosition());
        entity.setStandardd(addInitializationData.getStandardD());
        entity.setStandardz(addInitializationData.getStandardZ());
        this.updateById(entity);
    }

    //删除初始化数据
    @Override
    public void deleteInitializationData(String sysId) {
        ProjectMonitorinitEntity entity = new ProjectMonitorinitEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }

    //导入初始化数据
    @Override
    public boolean importInitializationData(String userId, String reportSysId, MultipartFile importFile) {
        String last = importFile.getOriginalFilename().substring(importFile.getOriginalFilename().lastIndexOf("."));
        log.info("文件后缀名" + last);
        log.info("请求头" + importFile.getContentType());
        if (last.equals(".xlsx")) {
            log.info("xlsx文件");
            InputStream inputStream = null;
            try {
                inputStream = importFile.getInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                XSSFRow row = sheet.getRow(0);
                if (Short.toUnsignedInt(row.getLastCellNum()) == 6 && row.getCell(0).toString().equals("点位名称") &&
                        row.getCell(1).toString().equals(" 水平基准值（初始值）") && row.getCell(2).toString().equals("垂直基准值（初始值）") &&
                        row.getCell(3).toString().equals("实际位置") && row.getCell(4).toString().equals("相对位置") && row.getCell(5).toString().equals("相对坐标")) {
                    log.info("符合模板条件!");
                    ProjectMonitorinitEntity entity = new ProjectMonitorinitEntity();
                    entity.setDel(1);
                    this.update(entity, new UpdateWrapper<ProjectMonitorinitEntity>().eq("reportSysId", reportSysId));
                    List<ProjectMonitorinitEntity> list = new ArrayList<>();
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        XSSFRow rows = sheet.getRow(i);
                        ProjectMonitorinitEntity data = new ProjectMonitorinitEntity();
                        data.setDel(0);
                        data.setSysid(CreateUuid.uuid());
                        data.setReportsysid(reportSysId);
                        data.setCreateuser(userId);
                        data.setCreatedate(new Date());
                        data.setPointname(StringUtils.isEmpty(rows.getCell(0)) ? null : rows.getCell(0).toString());
                        data.setStandardd(StringUtils.isEmpty(rows.getCell(1)) ? null : rows.getCell(1).toString());
                        data.setStandardz(StringUtils.isEmpty(rows.getCell(2))? null : rows.getCell(2).toString());
                        data.setActualposition(StringUtils.isEmpty(rows.getCell(3)) ? null : rows.getCell(3).toString());
                        data.setRelativeposition(StringUtils.isEmpty(rows.getCell(4)) ? null : rows.getCell(4).toString());
                        data.setRelativepoint(StringUtils.isEmpty(rows.getCell(5)) ? null : rows.getCell(5).toString());
                        list.add(data);
                    }
                    this.saveBatch(list);
                    return true;
                } else {
                    log.error("不符合条件!");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (last.equals(".xls")) {
            log.info("xls文件");
            try {
                InputStream inputStream = importFile.getInputStream();
                POIFSFileSystem fs = new POIFSFileSystem(inputStream);
                HSSFWorkbook wk = new HSSFWorkbook(fs);
                HSSFSheet sheet = wk.getSheetAt(0);
                HSSFRow row = sheet.getRow(0);
                if (Short.toUnsignedInt(row.getLastCellNum()) == 6 && row.getCell(0).toString().equals("点位名称") &&
                        row.getCell(1).toString().equals(" 水平基准值（初始值）") && row.getCell(2).toString().equals("垂直基准值（初始值）") &&
                        row.getCell(3).toString().equals("实际位置") && row.getCell(4).toString().equals("相对位置") && row.getCell(5).toString().equals("相对坐标")) {
                    log.info("符合模板条件!");
                    ProjectMonitorinitEntity entity = new ProjectMonitorinitEntity();
                    entity.setDel(1);
                    entity.setDeletedate(new Date());
                    entity.setDeleteuser(userId);
                    this.update(entity, new UpdateWrapper<ProjectMonitorinitEntity>().eq("reportSysId", reportSysId));
                    List<ProjectMonitorinitEntity> list = new ArrayList<>();
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        HSSFRow rows = sheet.getRow(i);
                        ProjectMonitorinitEntity data = new ProjectMonitorinitEntity();
                        data.setDel(0);
                        data.setSysid(CreateUuid.uuid());
                        data.setReportsysid(reportSysId);
                        data.setCreateuser(userId);
                        data.setCreatedate(new Date());
                        data.setPointname(StringUtils.isEmpty(rows.getCell(0)) ? null : rows.getCell(0).toString());
                        data.setStandardd(StringUtils.isEmpty(rows.getCell(1)) ? null : rows.getCell(1).toString());
                        data.setStandardz(StringUtils.isEmpty(rows.getCell(2)) ? null : rows.getCell(2).toString());
                        data.setActualposition(StringUtils.isEmpty(rows.getCell(3)) ? null : rows.getCell(3).toString());
                        data.setRelativeposition(StringUtils.isEmpty(rows.getCell(4)) ? null : rows.getCell(4).toString());
                        data.setRelativepoint(StringUtils.isEmpty(rows.getCell(5)) ? null : rows.getCell(5).toString());
                        list.add(data);
                    }
                    this.saveBatch(list);
                    return true;
                } else {
                    log.info("不符合条件!");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("文件类型错误!");
            return false;
        }
        return false;
    }
    //获取初始化数据(返回实体类)
    @Override
    public List<ProjectMonitorinitEntity> getAllData(String reportSysId) {
        return this.list(new QueryWrapper<ProjectMonitorinitEntity>().eq("del",0).eq("reportSysId",reportSysId));
    }
    //获取所有id
    @Override
    public List<String> getAllIdByReportSysId(String reportSysId) {
        List<ProjectMonitorinitEntity> list = this.list(new QueryWrapper<ProjectMonitorinitEntity>().eq("reportSysId", reportSysId));
        List<String> id = new ArrayList<>();
        list.forEach(li ->{
            id.add(li.getSysid());
        });
        return id;
    }

}