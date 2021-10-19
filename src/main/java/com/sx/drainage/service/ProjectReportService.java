package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectReportEntity;
import com.sx.drainage.params.ReportParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-16 14:11:27
 */
public interface ProjectReportService extends IService<ProjectReportEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取报告列表
    Map<String, Object> getPageList(Integer currentPage, Integer pageSize, String type, String projectId);
    //获取详细信息
    Map<String, Object> get3(String id);
    //修改
    void put(ReportParams reportParams,String userId);
    //添加
    void post1(ReportParams reportParams, String userId);
    //添加
    void post2(ReportParams reportParams, String userId);
    //添加
    void post3(ReportParams reportParams, String userId);
    //添加
    void post5(ReportParams reportParams, String userId);
    //删除
    void delete(String sysId,String userId);
    //获取安全和质量信息
    List<Map<String, Object>> getAllAnQuanOrZhiLiang(String projectId,String wbsId,String type,String partName);
    //抛弃老流程后的获取报告列表
    Map<String, Object> getPageList2(Integer currentPage, Integer pageSize, String type, String projectId);
    //获取安全和质量信息条数
    List<ProjectReportEntity> getNoProcessRectification(String projectId,String type);
}

