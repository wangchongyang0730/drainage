package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.MonitorNvrEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 13:49:47
 */
public interface MonitorNvrService extends IService<MonitorNvrEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有的监控设置分类
    List<Map<String, Object>> getAllMonitorNvr();
}

