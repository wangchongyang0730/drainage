package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.params.BindEquipmentParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectWbsbindgroupequipmentDao;
import com.sx.drainage.entity.ProjectWbsbindgroupequipmentEntity;
import com.sx.drainage.service.ProjectWbsbindgroupequipmentService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("projectWbsbindgroupequipmentService")
@Transactional
public class ProjectWbsbindgroupequipmentServiceImpl extends ServiceImpl<ProjectWbsbindgroupequipmentDao, ProjectWbsbindgroupequipmentEntity> implements ProjectWbsbindgroupequipmentService {

    private SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsbindgroupequipmentEntity> page = this.page(
                new Query<ProjectWbsbindgroupequipmentEntity>().getPage(params),
                new QueryWrapper<ProjectWbsbindgroupequipmentEntity>()
        );

        return new PageUtils(page);
    }
    //获取无流程提交施工设备信息
    @Override
    public List<ProjectWbsbindgroupequipmentEntity> getNoProcessDeviceNow(List<String> wbsId) {
        List<ProjectWbsbindgroupequipmentEntity> list=new ArrayList<>();
        wbsId.forEach(id->{
            List<ProjectWbsbindgroupequipmentEntity> equipment = this.list(new QueryWrapper<ProjectWbsbindgroupequipmentEntity>().eq("WbsId", id));
            list.addAll(equipment);
        });
        return list;
    }

    @Override
    public void deleteWbs(String sysid) {
        this.remove(new QueryWrapper<ProjectWbsbindgroupequipmentEntity>().eq("WbsId",sysid));
    }
    //获取wbs绑定模型施工重大设备信息
    @Override
    public Map<String, Object> getEquipment(String wbsId) {
        List<ProjectWbsbindgroupequipmentEntity> list = this.list(new QueryWrapper<ProjectWbsbindgroupequipmentEntity>().eq("WbsId", wbsId));
        if(list.size()>0&&list!=null){
            Map<String, Object> map = new HashMap<>();
            map.put("SysId",list.get(0).getSysid());
            map.put("EntryDate",list.get(0).getEntrydate()==null?"":list.get(0).getEntrydate());
            map.put("DepartureDate",list.get(0).getDeparturedate()==null?"":list.get(0).getDeparturedate());
            map.put("WbsId",list.get(0).getWbsid());
            map.put("EquipmentName",list.get(0).getEquipmentname());
            return map;
        }
        return null;
    }
    //添加wbs绑定模型施工重大设备信息
    @Override
    public void addEquipment(BindEquipmentParams bindEquipmentParams) {
        ProjectWbsbindgroupequipmentEntity entity = new ProjectWbsbindgroupequipmentEntity();
        entity.setWbsid(bindEquipmentParams.getWbsId());
        entity.setDeparturedate(bindEquipmentParams.getDepartureDate());
        entity.setEntrydate(bindEquipmentParams.getEntryDate());
        entity.setEquipmentname(bindEquipmentParams.getEquipmentName());
        entity.setSysid(CreateUuid.uuid());
        this.save(entity);
    }

    @Override
    public void putEquipment(BindEquipmentParams bindEquipmentParams) {
        ProjectWbsbindgroupequipmentEntity entity = new ProjectWbsbindgroupequipmentEntity();
        entity.setWbsid(bindEquipmentParams.getWbsId());
        entity.setDeparturedate(bindEquipmentParams.getDepartureDate());
        entity.setEntrydate(bindEquipmentParams.getEntryDate());
        entity.setEquipmentname(bindEquipmentParams.getEquipmentName());
        entity.setSysid(bindEquipmentParams.getSysId());
        this.updateById(entity);
    }
    //删除wbs绑定模型施工重大设备信息
    @Override
    public void deleteEquipment(String sysId) {
        this.removeById(sysId);
    }

}