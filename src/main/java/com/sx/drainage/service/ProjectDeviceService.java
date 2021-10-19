package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectDeviceEntity;
import com.sx.drainage.params.DeviceParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 14:37:13
 */
public interface ProjectDeviceService extends IService<ProjectDeviceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取当前施工设备
    List<Map<String, Object>> getProjectDeviceNow(String projectId);
    //获取分页列表
    R getPageList(String projectId, Integer page, Integer pageRecord, String search);
    //获取详细信息
    R getDetails(String id);
    //添加设备
    void addDevice(DeviceParams deviceParams, String userId);
    //修改设备
    void updateDevice(DeviceParams deviceParams, String userId);
    //移除设备
    void deleteDevice(String sysId, String userId);
    //设备离场
    void deviceLeave(String id, String userId);
}

