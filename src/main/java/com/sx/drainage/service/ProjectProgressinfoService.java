package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectProgressinfoEntity;
import com.sx.drainage.params.AddProgressInfoParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:58:17
 */
public interface ProjectProgressinfoService extends IService<ProjectProgressinfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取项目总进度计划信息
    List<Map<String, Object>> getOverAllProgress(String projectId);
    //添加总体进度节点信息,先删除已有proName信息再添加信息
    void addProgressInfo(AddProgressInfoParams addProgressInfoParams);
    //删除总体进度节点信息
    void deleteProgressInfo(String proName, String projectId);
}

