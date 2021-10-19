package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.DeviceOperationEntity;
import com.sx.drainage.params.DeviceOperationParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-15 15:20:06
 */
public interface DeviceOperationService extends IService<DeviceOperationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表,企业设备库查询
    Map<String, Object> getPageList(String projectId, Integer page, Integer pageRecord, String search);
    //获取详细信息
    Map<String, Object> getDetail(String id);
    //修改
    void putDetail(DeviceOperationParams deviceOperationParams);
    //新增
    void postDevice(DeviceOperationParams deviceOperationParams);
    //删除
    void deleteDevice(String sysId);
    //获取所有设备
    List<Map<String, Object>> getAll(String userId);
    //根据项目id获取设备
    List<Map<String, Object>> getByProjectId(String projectId);
}

