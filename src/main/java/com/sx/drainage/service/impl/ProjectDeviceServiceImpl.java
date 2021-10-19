package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.DeviceParams;
import com.sx.drainage.service.ProjectWbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectDeviceDao;
import com.sx.drainage.entity.ProjectDeviceEntity;
import com.sx.drainage.service.ProjectDeviceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectDeviceService")
@Transactional
public class ProjectDeviceServiceImpl extends ServiceImpl<ProjectDeviceDao, ProjectDeviceEntity> implements ProjectDeviceService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private ProjectWbsService projectWbsService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectDeviceEntity> page = this.page(
                new Query<ProjectDeviceEntity>().getPage(params),
                new QueryWrapper<ProjectDeviceEntity>()
        );

        return new PageUtils(page);
    }
    //获取当前施工设备
    @Override
    public List<Map<String, Object>> getProjectDeviceNow(String projectId) {
        List<ProjectDeviceEntity> list = this.list(new QueryWrapper<ProjectDeviceEntity>().eq("inOutMark", "1").eq("projectId", projectId));
        List<Map<String, Object>> data = new ArrayList<>();
        if(list.size()>0){
            list.forEach(li ->{
               ProjectWbsEntity wbs = projectWbsService.getWbs(li.getWbsid());
                Map<String, Object> map = new HashMap<>();
                map.put("sysId",li.getSysid());
                map.put("projectId",li.getProjectid());
                map.put("wbsId",li.getWbsid());
                map.put("name",li.getName());
                map.put("code",li.getCode());
                map.put("model",li.getModel());
                map.put("status",li.getStatus());
                map.put("entryTime",li.getEntrytime());
                map.put("outTime",li.getOuttime());
                map.put("checkTime",li.getChecktime());
                map.put("createDate",li.getCreatedate());
                map.put("createUser",li.getCreateuser());
                map.put("updateDate",li.getUpdatedate());
                map.put("updateUser",li.getUpdateuser());
                map.put("deleteDate",li.getDeletedate());
                map.put("deleteUser",li.getDeleteuser());
                map.put("del",li.getDel());
                map.put("serviceTime",li.getServicetime());
                map.put("Manufacturer",li.getManufacturer());
                map.put("fileupload",li.getFileupload());
                map.put("qualifiedNo",li.getQualifiedno());
                map.put("qualifiedDate",li.getQualifieddate());
                map.put("inOutMark",li.getInoutmark());
                map.put("checkStatus",li.getCheckstatus());
                map.put("checkPerson",li.getCheckperson());
                map.put("approveTime",li.getApprovetime());
                map.put("approvePerson",li.getApproveperson());
                map.put("deviceClassId",li.getDeviceclassid());
                map.put("deviceClass",li.getDeviceclass());
                map.put("useUnit",li.getUseunit());
                map.put("isSpecial",li.getIsspecial());
                map.put("partName",wbs.getPartname());
                data.add(map);
            });
        }
        return data;
    }
    //获取分页列表
    @Override
    public R getPageList(String projectId, Integer page, Integer pageRecord, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",page.toString());
        params.put("limit",pageRecord.toString());
        QueryWrapper<ProjectDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId",projectId).eq("del",0);
        if(!StringUtils.isEmpty(search)&&!search.equals("%22%22")&&!search.equals("''")){
            wrapper.like("name",search).or().like("code",search).or().like("Manufacturer",search);
        }
        IPage<ProjectDeviceEntity> iPage = this.page(
                new Query<ProjectDeviceEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(iPage);
        List<ProjectDeviceEntity> list = (List<ProjectDeviceEntity>) pageUtils.getList();
        List<Map<String, Object>> data = new ArrayList<>();
        if(list.size()>0){
            list.forEach(li ->{
                ProjectWbsEntity wbs = projectWbsService.getWbs(li.getWbsid());
                Map<String, Object> map = new HashMap<>();
                map.put("sysId",li.getSysid());
                map.put("projectId",li.getProjectid());
                map.put("wbsId",li.getWbsid());
                map.put("name",li.getName());
                map.put("code",li.getCode());
                map.put("model",li.getModel());
                map.put("status",li.getStatus());
                map.put("entryTime",li.getEntrytime());
                map.put("outTime",li.getOuttime());
                map.put("checkTime",li.getChecktime());
                map.put("createDate",li.getCreatedate());
                map.put("createUser",li.getCreateuser());
                map.put("updateDate",li.getUpdatedate());
                map.put("updateUser",li.getUpdateuser());
                map.put("deleteDate",li.getDeletedate());
                map.put("deleteUser",li.getDeleteuser());
                map.put("del",li.getDel());
                map.put("serviceTime",li.getServicetime());
                map.put("Manufacturer",li.getManufacturer());
                map.put("fileupload",li.getFileupload());
                map.put("qualifiedNo",li.getQualifiedno());
                map.put("qualifiedDate",li.getQualifieddate());
                map.put("inOutMark",li.getInoutmark());
                map.put("checkStatus",li.getCheckstatus());
                map.put("checkPerson",li.getCheckperson());
                map.put("approveTime",li.getApprovetime());
                map.put("approvePerson",li.getApproveperson());
                map.put("deviceClassId",li.getDeviceclassid());
                map.put("deviceClass",li.getDeviceclass());
                map.put("useUnit",li.getUseunit());
                map.put("isSpecial",li.getIsspecial());
                map.put("partName",wbs.getPartname());
                data.add(map);
            });
        }
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return R.ok(1,"获取成功!",res,true,null);
    }
    //获取详细信息
    @Override
    public R getDetails(String id) {
        ProjectDeviceEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        ProjectWbsEntity wbs = projectWbsService.getWbs(li.getWbsid());
        map.put("sysId",li.getSysid());
        map.put("projectId",li.getProjectid());
        map.put("wbsId",li.getWbsid());
        map.put("name",li.getName());
        map.put("code",li.getCode());
        map.put("model",li.getModel());
        map.put("status",li.getStatus());
        map.put("entryTime",li.getEntrytime());
        map.put("outTime",li.getOuttime());
        map.put("checkTime",li.getChecktime());
        map.put("createDate",li.getCreatedate());
        map.put("createUser",li.getCreateuser());
        map.put("updateDate",li.getUpdatedate());
        map.put("updateUser",li.getUpdateuser());
        map.put("deleteDate",li.getDeletedate());
        map.put("deleteUser",li.getDeleteuser());
        map.put("del",li.getDel());
        map.put("serviceTime",li.getServicetime());
        map.put("Manufacturer",li.getManufacturer());
        map.put("fileupload",li.getFileupload());
        map.put("qualifiedNo",li.getQualifiedno());
        map.put("qualifiedDate",li.getQualifieddate());
        map.put("inOutMark",li.getInoutmark());
        map.put("checkStatus",li.getCheckstatus());
        map.put("checkPerson",li.getCheckperson());
        map.put("approveTime",li.getApprovetime());
        map.put("approvePerson",li.getApproveperson());
        map.put("deviceClassId",li.getDeviceclassid());
        map.put("deviceClass",li.getDeviceclass());
        map.put("useUnit",li.getUseunit());
        map.put("isSpecial",li.getIsspecial());
        map.put("partName",wbs.getPartname());
        return R.ok(1,"获取成功!",map,true,null);
    }
    //添加设备
    @Override
    public void addDevice(DeviceParams deviceParams, String userId) {
        ProjectDeviceEntity entity = new ProjectDeviceEntity();
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setSysid(CreateUuid.uuid());
        entity.setCode(deviceParams.getCode());
        entity.setDeviceclass(deviceParams.getDeviceClass());
        entity.setDeviceclassid(deviceParams.getDeviceClassId());
        entity.setFileupload(deviceParams.getFileupload());
        entity.setIsspecial(Integer.parseInt(deviceParams.getIsSpecial()));
        entity.setManufacturer(deviceParams.getManufacturer());
        entity.setModel(deviceParams.getModel());
        entity.setName(deviceParams.getName());
        entity.setProjectid(deviceParams.getProjectId());
        entity.setQualifieddate(deviceParams.getQualifiedDate());
        entity.setQualifiedno(deviceParams.getQualifiedNo());
        entity.setServicetime(deviceParams.getServiceTime());
        entity.setStatus(deviceParams.getStatus());
        entity.setUseunit(deviceParams.getUseUnit());
        entity.setWbsid(deviceParams.getWbsId());
        entity.setDel(0);
        this.save(entity);
    }
    //修改设备
    @Override
    public void updateDevice(DeviceParams deviceParams, String userId) {
        ProjectDeviceEntity entity = new ProjectDeviceEntity();
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setSysid(deviceParams.getSysId());
        entity.setCode(deviceParams.getCode());
        entity.setDeviceclass(deviceParams.getDeviceClass());
        entity.setDeviceclassid(deviceParams.getDeviceClassId());
        entity.setFileupload(deviceParams.getFileupload());
        entity.setIsspecial(Integer.parseInt(deviceParams.getIsSpecial()));
        entity.setManufacturer(deviceParams.getManufacturer());
        entity.setModel(deviceParams.getModel());
        entity.setName(deviceParams.getName());
        entity.setProjectid(deviceParams.getProjectId());
        entity.setQualifieddate(deviceParams.getQualifiedDate());
        entity.setQualifiedno(deviceParams.getQualifiedNo());
        entity.setServicetime(deviceParams.getServiceTime());
        entity.setStatus(deviceParams.getStatus());
        entity.setUseunit(deviceParams.getUseUnit());
        entity.setWbsid(deviceParams.getWbsId());
        this.updateById(entity);
    }
    //移除设备
    @Override
    public void deleteDevice(String sysId, String userId) {
        ProjectDeviceEntity entity = new ProjectDeviceEntity();
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        entity.setSysid(sysId);
        this.updateById(entity);
    }
    //设备离场
    @Override
    public void deviceLeave(String id, String userId) {
        ProjectDeviceEntity entity = new ProjectDeviceEntity();
        entity.setInoutmark("2");
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setSysid(id);
        this.updateById(entity);
    }

}