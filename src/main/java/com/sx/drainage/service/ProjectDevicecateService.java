package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectDevicecateEntity;
import com.sx.drainage.params.DeviceCateParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-09 12:03:20
 */
public interface ProjectDevicecateService extends IService<ProjectDevicecateEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表
    Map<String, Object> getPageList(String projectId, Integer page, Integer pageRecord, String where);
    //新增
    void post(DeviceCateParams deviceCateParams, String userId);
    //修改
    void update(DeviceCateParams deviceCateParams, String userId);
    //删除
    void delete(String sysId, String userId);
    //获取所有设备类别
    List<Map<String, Object>> getAll(String projectId);
}

