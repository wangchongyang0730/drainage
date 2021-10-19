package com.sx.drainage.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.MonitorNvrDao;
import com.sx.drainage.entity.MonitorNvrEntity;
import com.sx.drainage.service.MonitorNvrService;
import org.springframework.transaction.annotation.Transactional;


@Service("monitorNvrService")
@Transactional
public class MonitorNvrServiceImpl extends ServiceImpl<MonitorNvrDao, MonitorNvrEntity> implements MonitorNvrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MonitorNvrEntity> page = this.page(
                new Query<MonitorNvrEntity>().getPage(params),
                new QueryWrapper<MonitorNvrEntity>()
        );

        return new PageUtils(page);
    }
    //获取所有的监控设置分类
    @Override
    public List<Map<String, Object>> getAllMonitorNvr() {
        List<MonitorNvrEntity> list = this.list(new QueryWrapper<MonitorNvrEntity>().eq("del", 0));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("name",li.getName());
            map.put("projectId",li.getProjectid());
            map.put("ipAddress",li.getIpaddress());
            map.put("port",li.getPort());
            map.put("port2",li.getPort2());
            map.put("port3",li.getPort3());
            map.put("user",li.getUser());
            map.put("pwd",li.getPwd());
            map.put("remark",li.getRemark());
            map.put("del",li.getDel());
            data.add(map);
        });
        return data;
    }

}