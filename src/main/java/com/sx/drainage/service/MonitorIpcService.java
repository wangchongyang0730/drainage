package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.MonitorIpcEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 14:01:42
 */
public interface MonitorIpcService extends IService<MonitorIpcEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分类监控下的摄像机
    Map<String, Object> getAllMonitorIpc(String nvrId, Integer page, Integer pageRecord, String where);
}

