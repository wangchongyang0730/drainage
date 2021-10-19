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

import com.sx.drainage.dao.MonitorIpcDao;
import com.sx.drainage.entity.MonitorIpcEntity;
import com.sx.drainage.service.MonitorIpcService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("monitorIpcService")
@Transactional
public class MonitorIpcServiceImpl extends ServiceImpl<MonitorIpcDao, MonitorIpcEntity> implements MonitorIpcService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MonitorIpcEntity> page = this.page(
                new Query<MonitorIpcEntity>().getPage(params),
                new QueryWrapper<MonitorIpcEntity>()
        );

        return new PageUtils(page);
    }
    //获取分类监控下的摄像机
    @Override
    public Map<String, Object> getAllMonitorIpc(String nvrId, Integer page, Integer pageRecord, String where) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",page.toString());
        params.put("limit",pageRecord.toString());
        QueryWrapper<MonitorIpcEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("nvrId",nvrId).eq("del",0);
        if (!where.equals("%22%22") && !where.equals("''") && !StringUtils.isEmpty(where)) {
            wrapper.like("name", where);
        }
        IPage<MonitorIpcEntity> iPage = this.page(
                new Query<MonitorIpcEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(iPage);
        List<Map<String, Object>> data = new ArrayList<>();
        List<MonitorIpcEntity> list = (List<MonitorIpcEntity>) pageUtils.getList();
        list.forEach(li -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("nvrId",li.getNvrid());
            entity.put("name",li.getName());
            entity.put("ipAddress",li.getIpaddress());
            entity.put("port",li.getPort());
            entity.put("port2",li.getPort2());
            entity.put("user",li.getUser());
            entity.put("pwd",li.getPwd());
            entity.put("remark",li.getRemark());
            entity.put("pass",li.getPass());
            entity.put("isenable",li.getIsenable());
            entity.put("del",li.getDel());
            data.add(entity);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return res;
    }

}