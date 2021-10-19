package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectMonitorreportsetEntity;
import com.sx.drainage.params.AddReportInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:16:45
 */
public interface ProjectMonitorreportsetService extends IService<ProjectMonitorreportsetEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取报表集合信息
    Map<String, Object> getReportList(String projectId,Integer page,Integer pageRecord);
    //获取报表详细信息
    Map<String, Object> getReportInfo(String sysId);
    //新增报表信息
    void addReportInfo(AddReportInfo addReportInfo);
    //修改报表信息
    void updatePoint(AddReportInfo addReportInfo);
    //删除报表信息
    void deletePoint(String sysId);
    //下载模板或数据
    void downloadMonitorData(String reportSysId, String dailyId, HttpServletResponse response);
    //获取单个
    ProjectMonitorreportsetEntity getOne(String id);
}

