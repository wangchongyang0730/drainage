package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.Sort;
import com.sx.drainage.entity.ProjectMonitordataEntity;
import com.sx.drainage.entity.ProjectMonitorinitEntity;
import com.sx.drainage.params.AddReportInfo;
import com.sx.drainage.service.ProjectMonitordataService;
import com.sx.drainage.service.ProjectMonitorinitService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectMonitorreportsetDao;
import com.sx.drainage.entity.ProjectMonitorreportsetEntity;
import com.sx.drainage.service.ProjectMonitorreportsetService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


@Service("projectMonitorreportsetService")
@Transactional
public class ProjectMonitorreportsetServiceImpl extends ServiceImpl<ProjectMonitorreportsetDao, ProjectMonitorreportsetEntity> implements ProjectMonitorreportsetService {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Environment env;
    @Autowired
    private ProjectMonitordataService projectMonitordataService;
    @Autowired
    private ProjectMonitorinitService projectMonitorinitService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectMonitorreportsetEntity> page = this.page(
                new Query<ProjectMonitorreportsetEntity>().getPage(params),
                new QueryWrapper<ProjectMonitorreportsetEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, Object> getReportList(String projectId, Integer page, Integer pageRecord) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        IPage<ProjectMonitorreportsetEntity> iPage = this.page(new Query<ProjectMonitorreportsetEntity>().getPage(map),
                new QueryWrapper<ProjectMonitorreportsetEntity>().eq("projectId", projectId).eq("del", 0));
        PageUtils pageUtils = new PageUtils(iPage);
        Map<String, Object> hashMap = new HashMap<>();
        List<ProjectMonitorreportsetEntity> list = (List<ProjectMonitorreportsetEntity>) pageUtils.getList();
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId", li.getSysid());
            entity.put("name", li.getName());
            entity.put("projectId", li.getProjectid());
            entity.put("templateType", li.getTemplatetype());
            entity.put("templateSkinType", li.getTemplateskintype());
            entity.put("templateExcelPath", li.getTemplateexcelpath());
            entity.put("thresholdStartD", li.getThresholdstartd());
            entity.put("thresholdEndD", li.getThresholdendd());
            entity.put("totalThresholdStartD", li.getTotalthresholdstartd());
            entity.put("totalThresholdEndD", li.getTotalthresholdendd());
            entity.put("thresholdStartZ", li.getThresholdstartz());
            entity.put("thresholdEndZ", li.getThresholdendz());
            entity.put("totalThresholdStartZ", li.getTotalthresholdstartz());
            entity.put("totalThresholdEndZ", li.getTotalthresholdendz());
            entity.put("initValueD", li.getInitvalued());
            entity.put("initValueZ", li.getInitvaluez());
            entity.put("picFileId", li.getPicfileid());
            entity.put("createDate", li.getCreatedate() == null ? li.getCreatedate() : format.format(li.getCreatedate()));
            entity.put("createUser", li.getCreateuser());
            entity.put("updateDate", li.getUpdatedate());
            entity.put("updateUser", li.getUpdateuser());
            entity.put("deleteDate", li.getDeletedate());
            entity.put("deleteUser", li.getDeleteuser());
            entity.put("del", li.getDel());
            entity.put("sortValue", li.getSortvalue());
            data.add(entity);
        });
        hashMap.put("total", pageUtils.getTotalCount());
        hashMap.put("rows", data);
        return hashMap;
    }

    @Override
    public Map<String, Object> getReportInfo(String sysId) {
        ProjectMonitorreportsetEntity li = this.getById(sysId);
        Map<String, Object> entity = new HashMap<>();
        entity.put("sysId", li.getSysid());
        entity.put("name", li.getName());
        entity.put("projectId", li.getProjectid());
        entity.put("templateType", li.getTemplatetype());
        entity.put("templateSkinType", li.getTemplateskintype());
        entity.put("templateExcelPath", li.getTemplateexcelpath());
        entity.put("thresholdStartD", li.getThresholdstartd());
        entity.put("thresholdEndD", li.getThresholdendd());
        entity.put("totalThresholdStartD", li.getTotalthresholdstartd());
        entity.put("totalThresholdEndD", li.getTotalthresholdendd());
        entity.put("thresholdStartZ", li.getThresholdstartz());
        entity.put("thresholdEndZ", li.getThresholdendz());
        entity.put("totalThresholdStartZ", li.getTotalthresholdstartz());
        entity.put("totalThresholdEndZ", li.getTotalthresholdendz());
        entity.put("initValueD", li.getInitvalued());
        entity.put("initValueZ", li.getInitvaluez());
        entity.put("picFileId", li.getPicfileid());
        entity.put("createDate", li.getCreatedate() == null ? li.getCreatedate() : format.format(li.getCreatedate()));
        entity.put("createUser", li.getCreateuser());
        entity.put("updateDate", li.getUpdatedate());
        entity.put("updateUser", li.getUpdateuser());
        entity.put("deleteDate", li.getDeletedate());
        entity.put("deleteUser", li.getDeleteuser());
        entity.put("del", li.getDel());
        entity.put("sortValue", li.getSortvalue());
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("data", entity);
        hashMap.put("num", 0);
        return hashMap;
    }

    //新增报表信息
    @Override
    public void addReportInfo(AddReportInfo addReportInfo) {
        ProjectMonitorreportsetEntity entity = new ProjectMonitorreportsetEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(addReportInfo.getName());
        entity.setCreatedate(new Date());
        entity.setProjectid(addReportInfo.getProjectId());
        entity.setPicfileid(addReportInfo.getPicFileId());
        entity.setSortvalue(addReportInfo.getSortValue());
        entity.setTemplatetype(addReportInfo.getTemplateType());
        entity.setThresholdendd(addReportInfo.getThresholdEndD());
        entity.setThresholdendz(addReportInfo.getThresholdEndZ());
        entity.setThresholdstartd(addReportInfo.getThresholdStartD());
        entity.setThresholdstartz(addReportInfo.getThresholdStartZ());
        entity.setTotalthresholdendd(addReportInfo.getTotalThresholdEndD());
        entity.setTotalthresholdendz(addReportInfo.getTotalThresholdEndZ());
        entity.setTotalthresholdstartd(addReportInfo.getTotalThresholdStartD());
        entity.setTotalthresholdstartz(addReportInfo.getTotalThresholdStartZ());
        entity.setDel(0);
        this.save(entity);
    }

    //修改报表信息
    @Override
    public void updatePoint(AddReportInfo addReportInfo) {
        ProjectMonitorreportsetEntity entity = new ProjectMonitorreportsetEntity();
        entity.setSysid(addReportInfo.getSysId());
        entity.setName(addReportInfo.getName());
        entity.setPicfileid(addReportInfo.getPicFileId());
        entity.setProjectid(addReportInfo.getProjectId());
        entity.setSortvalue(addReportInfo.getSortValue());
        entity.setTemplatetype(addReportInfo.getTemplateType());
        entity.setThresholdendd(addReportInfo.getThresholdEndD());
        entity.setThresholdendz(addReportInfo.getThresholdEndZ());
        entity.setThresholdstartd(addReportInfo.getThresholdStartD());
        entity.setThresholdstartz(addReportInfo.getThresholdStartZ());
        entity.setTotalthresholdendd(addReportInfo.getTotalThresholdEndD());
        entity.setTotalthresholdendz(addReportInfo.getTotalThresholdEndZ());
        entity.setTotalthresholdstartd(addReportInfo.getTotalThresholdStartD());
        entity.setTotalthresholdstartz(addReportInfo.getTotalThresholdStartZ());
        this.updateById(entity);
    }

    //删除报表信息
    @Override
    public void deletePoint(String sysId) {
        ProjectMonitorreportsetEntity entity = new ProjectMonitorreportsetEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }

    //下载模板或数据
    @Override
    public void downloadMonitorData(String reportSysId, String dailyId, HttpServletResponse response) {
        ProjectMonitorreportsetEntity entity = this.getById(reportSysId);
        if (entity != null) {
            switch (entity.getTemplatetype()) {
                case "1":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "水平垂直监测数据导入模板.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointz().toString());
                                row.createCell(2).setCellValue(list.get(i).getPointd().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-水平垂直监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=水平垂直监测数据导入模板.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "水平监测数据导入模板.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointd().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-水平监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=水平监测数据导入模板.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "垂直监测数据导入模板 .xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointz().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-垂直监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=垂直监测数据导入模板 .xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "轴力监测导入模板.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointd().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-轴力监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=轴力监测导入模板.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "5":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "水位监测导入模板.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointz().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-水位监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=水位监测导入模板.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "6":
                    try {
                        FileInputStream inputStream = new FileInputStream(new File(env.getProperty("file.download") + "测斜监测导入模板.xlsx"));
                        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                        if (dailyId != null) {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitordataEntity> list = projectMonitordataService.getAllByDailyId(dailyId);
                            /**
                             * 按名字排序
                             */
                            Sort.dataSort(list);
                            for (int i = 0; i < list.size(); i++) {
                                XSSFRow row = sheet.createRow(i + 1);
                                row.createCell(0).setCellValue(list.get(i).getPointname());
                                row.createCell(1).setCellValue(list.get(i).getPointz().toString());
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=" + dailyId + "-测斜监测数据.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                            inputStream.close();
                        } else {
                            XSSFSheet sheet = workbook.getSheetAt(0);
                            List<ProjectMonitorinitEntity> data = projectMonitorinitService.getAllData(reportSysId);
                            /**
                             * 按名字排序
                             */
                            Sort.initSort(data);
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    XSSFRow row = sheet.createRow(i + 1);
                                    row.createCell(0).setCellValue(data.get(i).getPointname());
                                }
                            }
                            //八进制输出流
                            response.setContentType("application/octet-stream");
                            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
                            response.setHeader("Content-Disposition", "attachment;filename=测斜监测导入模板.xlsx");
                            //刷新缓冲
                            response.flushBuffer();
                            //workbook将Excel写入到response的输出流中，供页面下载
                            workbook.write(response.getOutputStream());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    //获取单个
    @Override
    public ProjectMonitorreportsetEntity getOne(String id) {
        return this.getById(id);
    }

}