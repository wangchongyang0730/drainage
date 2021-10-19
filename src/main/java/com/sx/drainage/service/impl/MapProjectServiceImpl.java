package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.params.MapParams;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.MapProjectDao;
import com.sx.drainage.entity.MapProjectEntity;
import com.sx.drainage.service.MapProjectService;
import org.springframework.transaction.annotation.Transactional;

@Service("mapProjectService")
@Transactional
public class MapProjectServiceImpl extends ServiceImpl<MapProjectDao, MapProjectEntity> implements MapProjectService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MapProjectEntity> page = this.page(
                new Query<MapProjectEntity>().getPage(params),
                new QueryWrapper<MapProjectEntity>()
        );

        return new PageUtils(page);
    }

    //获取所有地图
    @Override
    public List<MapProjectEntity> getAllMap() {
        return this.list(new QueryWrapper<MapProjectEntity>().eq("del",0));
    }

    //根据项目获取地图坐标信息
    @Override
    public List<Map<String, Object>> getProjectMapPoint(String projectId) {
        List<MapProjectEntity> list = this.list(new QueryWrapper<MapProjectEntity>().eq("projectId", projectId).eq("del",0));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("CenterPoint", li.getCenterpoint());
            map.put("address", li.getAddress());
            map.put("createtime", li.getCreatetime()==null?null:format.format(li.getCreatetime()));
            map.put("del",li.getDel());
            map.put("deviceId",li.getDeviceid());
            map.put("deviceName",li.getDevicename());
            map.put("images",li.getImages());
            map.put("name",li.getName());
            map.put("projectId",li.getProjectid());
            map.put("remarks",li.getRemarks());
            map.put("status",li.getStatus());
            map.put("sysId",li.getSysid());
            map.put("value",li.getValue());
            res.add(map);
        });
        return res;
    }
    //新增
    @Override
    public void post(MapParams mapParams) {
        MapProjectEntity entity = new MapProjectEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setCreatetime(new Date());
        entity.setProjectid(mapParams.getProjectId());
        entity.setAddress(mapParams.getAddress());
        entity.setName(mapParams.getName());
        entity.setRemarks(mapParams.getRemarks());
        entity.setImages(mapParams.getImages());
        entity.setCenterpoint(mapParams.getCenterPoint());
        entity.setDel(0);
        this.save(entity);
    }

    @Override
    public void put(MapParams mapParams) {
        MapProjectEntity entity = new MapProjectEntity();
        entity.setSysid(mapParams.getSysId());
        entity.setProjectid(mapParams.getProjectId());
        entity.setAddress(mapParams.getAddress());
        entity.setName(mapParams.getName());
        entity.setRemarks(mapParams.getRemarks());
        entity.setImages(mapParams.getImages());
        entity.setCenterpoint(mapParams.getCenterPoint());
        this.updateById(entity);
    }
    //删除
    @Override
    public void delete(String sysId) {
        MapProjectEntity entity = new MapProjectEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }

}