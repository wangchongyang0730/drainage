package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.entity.ProjectProjectEntity;
import com.sx.drainage.params.DeviceOperationParams;
import com.sx.drainage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.sx.drainage.dao.DeviceOperationDao;
import com.sx.drainage.entity.DeviceOperationEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("deviceOperationService")
@Transactional
public class DeviceOperationServiceImpl extends ServiceImpl<DeviceOperationDao, DeviceOperationEntity> implements DeviceOperationService {

    @Autowired
    private ProjectProjectService projectProjectService;
    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;
    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DeviceOperationEntity> page = this.page(
                new Query<DeviceOperationEntity>().getPage(params),
                new QueryWrapper<DeviceOperationEntity>()
        );

        return new PageUtils(page);
    }
    //获取分页列表,企业设备库查询
    @Override
    public Map<String, Object> getPageList(String projectId, Integer page, Integer pageRecord, String search) {
        if(!StringUtils.isEmpty(projectId)&&!projectId.equals("''")&&!projectId.equals("%22%22")){
            ProjectProjectEntity project=projectProjectService.getProject(projectId);
            Map<String, Object> params = new HashMap<>();
            params.put("page",page.toString());
            params.put("limit",pageRecord.toString());
            QueryWrapper<DeviceOperationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("projectId",projectId);
            if(!StringUtils.isEmpty(search)&&!search.equals("''")&&!search.equals("%22%22")){
                wrapper.like("code",search).or().like("name",search).or().like("type",search).or().like("address",search);
            }
            IPage<DeviceOperationEntity> iPage = this.page(
                    new Query<DeviceOperationEntity>().getPage(params),
                    wrapper
            );
            PageUtils pageUtils = new PageUtils(iPage);
            List<DeviceOperationEntity> list = (List<DeviceOperationEntity>) pageUtils.getList();
            List<Map<String, Object>> data = new ArrayList<>();
            list.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysId",li.getSysid());
                entity.put("code",li.getCode());
                entity.put("name",li.getName());
                entity.put("type",li.getType());
                entity.put("parameter",li.getParameter());
                entity.put("properties",li.getProperties());
                entity.put("Manufacturer",li.getManufacturer());
                entity.put("count",li.getCount());
                entity.put("address",li.getAddress());
                entity.put("AppearanceDate",li.getAppearancedate());
                entity.put("GuaranteeDate",li.getGuaranteedate());
                entity.put("MaintenanceMode",li.getMaintenancemode());
                entity.put("remark",li.getRemark());
                entity.put("fileId",li.getFileid());
                entity.put("imgUrl",li.getImgurl());
                entity.put("projectId",projectId);
                entity.put("projectName",project.getName());
                data.add(entity);
            });
            Map<String, Object> res = new HashMap<>();
            res.put("total",pageUtils.getTotalCount());
            res.put("rows",data);
            return res;
        }else{
            Map<String, Object> params = new HashMap<>();
            params.put("page",page.toString());
            params.put("limit",pageRecord.toString());
            IPage<DeviceOperationEntity> iPage = this.page(
                    new Query<DeviceOperationEntity>().getPage(params),
                    new QueryWrapper<DeviceOperationEntity>()
            );
            PageUtils pageUtils = new PageUtils(iPage);
            List<DeviceOperationEntity> list = (List<DeviceOperationEntity>) pageUtils.getList();
            List<Map<String, Object>> data = new ArrayList<>();
            list.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysId",li.getSysid());
                entity.put("code",li.getCode());
                entity.put("name",li.getName());
                entity.put("type",li.getType());
                entity.put("parameter",li.getParameter());
                entity.put("properties",li.getProperties());
                entity.put("Manufacturer",li.getManufacturer());
                entity.put("count",li.getCount());
                entity.put("address",li.getAddress());
                entity.put("AppearanceDate",li.getAppearancedate());
                entity.put("GuaranteeDate",li.getGuaranteedate());
                entity.put("MaintenanceMode",li.getMaintenancemode());
                entity.put("remark",li.getRemark());
                entity.put("fileId",li.getFileid());
                entity.put("imgUrl",li.getImgurl());
                entity.put("projectId",li.getProjectid());
                entity.put("projectName",projectProjectService.getProject(li.getProjectid()).getName());
                data.add(entity);
            });
            Map<String, Object> res = new HashMap<>();
            res.put("total",pageUtils.getTotalCount());
            res.put("rows",data);
            return res;
        }

    }
    //获取详细信息
    @Override
    public Map<String, Object> getDetail(String id) {
        DeviceOperationEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("code",li.getCode());
        map.put("name",li.getName());
        map.put("type",li.getType());
        map.put("parameter",li.getParameter());
        map.put("properties",li.getProperties());
        map.put("Manufacturer",li.getManufacturer());
        map.put("count",li.getCount());
        map.put("address",li.getAddress());
        map.put("AppearanceDate",li.getAppearancedate());
        map.put("GuaranteeDate",li.getGuaranteedate());
        map.put("MaintenanceMode",li.getMaintenancemode());
        map.put("remark",li.getRemark());
        map.put("fileId",li.getFileid());
        map.put("imgUrl",li.getImgurl());
        map.put("projectId",li.getProjectid());
        return map;
    }
    //修改
    @Override
    public void putDetail(DeviceOperationParams deviceOperationParams) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        DeviceOperationEntity entity = new DeviceOperationEntity();
        entity.setSysid(deviceOperationParams.getSysId());
        entity.setCode(deviceOperationParams.getCode());
        entity.setName(deviceOperationParams.getName());
        entity.setType(deviceOperationParams.getType());
        entity.setParameter(deviceOperationParams.getParameter());
        entity.setProperties(deviceOperationParams.getProperties());
        entity.setManufacturer(deviceOperationParams.getManufacturer());
        entity.setCount(deviceOperationParams.getCount());
        entity.setAppearancedate(deviceOperationParams.getAppearanceDate()==null?null:format.format(deviceOperationParams.getAppearanceDate()));
        entity.setAddress(deviceOperationParams.getAddress());
        entity.setGuaranteedate(deviceOperationParams.getGuaranteeDate()==null?null:format.format(deviceOperationParams.getGuaranteeDate()));
        entity.setMaintenancemode(deviceOperationParams.getMaintenanceMode());
        entity.setRemark(deviceOperationParams.getRemark());
        entity.setImgurl(deviceOperationParams.getImgUrl());
        entity.setFileid(deviceOperationParams.getFileId());
        this.updateById(entity);
    }
    //新增
    @Override
    public void postDevice(DeviceOperationParams deviceOperationParams) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        DeviceOperationEntity entity = new DeviceOperationEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setProjectid(deviceOperationParams.getProjectId());
        entity.setCode(deviceOperationParams.getCode());
        entity.setName(deviceOperationParams.getName());
        entity.setType(deviceOperationParams.getType());
        entity.setParameter(deviceOperationParams.getParameter());
        entity.setProperties(deviceOperationParams.getProperties());
        entity.setManufacturer(deviceOperationParams.getManufacturer());
        entity.setCount(deviceOperationParams.getCount());
        entity.setAppearancedate(deviceOperationParams.getAppearanceDate()==null?null:format.format(deviceOperationParams.getAppearanceDate()));
        entity.setAddress(deviceOperationParams.getAddress());
        entity.setGuaranteedate(deviceOperationParams.getGuaranteeDate()==null?null:format.format(deviceOperationParams.getGuaranteeDate()));
        entity.setMaintenancemode(deviceOperationParams.getMaintenanceMode());
        entity.setRemark(deviceOperationParams.getRemark());
        entity.setImgurl(deviceOperationParams.getImgUrl());
        entity.setFileid(deviceOperationParams.getFileId());
        this.save(entity);
    }
    //删除
    @Override
    public void deleteDevice(String sysId) {
        this.removeById(sysId);
    }
    //获取所有设备
    @Override
    public List<Map<String, Object>> getAll(String userId) {
        List<ProjectProjectEntity> project = projectProjectService.getAllProject();
        //获取用户所拥有的的角色ID
        List<String> list = omRelAccountRoleService.getAllRoleId(userId);
        //获取用户所拥有的的所有角色
        List<OmRoleEntity> roles = omRoleService.getAllMyRole(list);
        //判断用户是否拥有超级管理员角色
        boolean role = false;
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getName().equals("超级管理员")) {
                role = true;
            }
        }
        if(role){
            List<DeviceOperationEntity> list1 = this.list();
            List<Map<String, Object>> data = new ArrayList<>();
            list1.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysId",li.getSysid());
                entity.put("code",li.getCode());
                entity.put("name",li.getName());
                entity.put("type",li.getType());
                entity.put("parameter",li.getParameter());
                entity.put("properties",li.getProperties());
                entity.put("Manufacturer",li.getManufacturer());
                entity.put("count",li.getCount());
                entity.put("address",li.getAddress());
                entity.put("AppearanceDate",li.getAppearancedate());
                entity.put("GuaranteeDate",li.getGuaranteedate());
                entity.put("MaintenanceMode",li.getMaintenancemode());
                entity.put("remark",li.getRemark());
                entity.put("fileId",li.getFileid());
                entity.put("imgUrl",li.getImgurl());
                project.forEach(p -> {
                    if(li.getProjectid().equals(p.getSysid())){
                        entity.put("projectId",p.getSysid());
                        entity.put("projectName",p.getName());
                    }
                });
                if(!(entity.size()>15)){
                    entity.put("projectId",li.getProjectid());
                    entity.put("projectName",null);
                }
                data.add(entity);
            });
            return data;
        }else{
            List<String> projectId = omTagRelPostAccountService.getAllProjectId(userId);
            List<DeviceOperationEntity> list1 = this.list(new QueryWrapper<DeviceOperationEntity>().in("projectId",projectId));
            List<Map<String, Object>> data = new ArrayList<>();
            list1.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysId",li.getSysid());
                entity.put("code",li.getCode());
                entity.put("name",li.getName());
                entity.put("type",li.getType());
                entity.put("parameter",li.getParameter());
                entity.put("properties",li.getProperties());
                entity.put("Manufacturer",li.getManufacturer());
                entity.put("count",li.getCount());
                entity.put("address",li.getAddress());
                entity.put("AppearanceDate",li.getAppearancedate());
                entity.put("GuaranteeDate",li.getGuaranteedate());
                entity.put("MaintenanceMode",li.getMaintenancemode());
                entity.put("remark",li.getRemark());
                entity.put("fileId",li.getFileid());
                entity.put("imgUrl",li.getImgurl());
                project.forEach(p -> {
                    if(li.getProjectid().equals(p.getSysid())){
                        entity.put("projectId",p.getSysid());
                        entity.put("projectName",p.getName());
                    }
                });
                if(!(entity.size()>15)){
                    entity.put("projectId",li.getProjectid());
                    entity.put("projectName",null);
                }
                data.add(entity);
            });
            return data;
        }
    }

    @Override
    public List<Map<String, Object>> getByProjectId(String projectId) {
        ProjectProjectEntity project = projectProjectService.getProject(projectId);
        List<DeviceOperationEntity> list = this.list(new QueryWrapper<DeviceOperationEntity>().eq("projectId",projectId));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("code",li.getCode());
            entity.put("name",li.getName());
            entity.put("type",li.getType());
            entity.put("parameter",li.getParameter());
            entity.put("properties",li.getProperties());
            entity.put("Manufacturer",li.getManufacturer());
            entity.put("count",li.getCount());
            entity.put("address",li.getAddress());
            entity.put("AppearanceDate",li.getAppearancedate());
            entity.put("GuaranteeDate",li.getGuaranteedate());
            entity.put("MaintenanceMode",li.getMaintenancemode());
            entity.put("remark",li.getRemark());
            entity.put("fileId",li.getFileid());
            entity.put("imgUrl",li.getImgurl());
            entity.put("projectId",li.getProjectid());
            entity.put("projectName",project==null?null:project.getName());
            data.add(entity);
        });
        return data;
    }

}