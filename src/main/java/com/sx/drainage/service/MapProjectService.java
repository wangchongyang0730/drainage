package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.MapProjectEntity;
import com.sx.drainage.params.MapParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 17:25:02
 */
public interface MapProjectService extends IService<MapProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有地图
    List<MapProjectEntity> getAllMap();
    //根据项目获取地图坐标信息
    List<Map<String, Object>> getProjectMapPoint(String projectId);
    //新增
    void post(MapParams mapParams);
    //修改
    void put(MapParams mapParams);
    //删除
    void delete(String sysId);
}

