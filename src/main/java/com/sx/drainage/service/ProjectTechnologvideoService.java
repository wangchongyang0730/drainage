package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectTechnologvideoEntity;
import com.sx.drainage.params.VideoParams;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-21 10:06:20
 */
public interface ProjectTechnologvideoService extends IService<ProjectTechnologvideoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表
    R getPageList(String type, String projectId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search, String queryJson);
    //新增
    void addVideo(VideoParams videoParams, String userId);
    //获取单个详情
    R get(String id);
    //修改
    void updateVideo(VideoParams videoParams, String userId);
    //删除
    void delete(String sysId, String userId);
}

