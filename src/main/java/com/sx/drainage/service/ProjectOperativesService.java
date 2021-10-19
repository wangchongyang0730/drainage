package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectOperativesEntity;
import com.sx.drainage.params.OperativesParams;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-21 17:31:14
 */
public interface ProjectOperativesService extends IService<ProjectOperativesEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表
    R getPageList(String projectId, String deviceSysId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search);
    //获取详细信息
    R getDetails(String id);
    //修改信息
    void updateOpertaives(OperativesParams operativesParams, String userId);
    //添加
    void addOpertaives(OperativesParams operativesParams, String userId);
    //删除
    void deleteOpertaives(String id, String userId);
}

