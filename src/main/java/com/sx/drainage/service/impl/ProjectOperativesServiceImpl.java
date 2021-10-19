package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.params.OperativesParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectOperativesDao;
import com.sx.drainage.entity.ProjectOperativesEntity;
import com.sx.drainage.service.ProjectOperativesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("projectOperativesService")
@Transactional
public class ProjectOperativesServiceImpl extends ServiceImpl<ProjectOperativesDao, ProjectOperativesEntity> implements ProjectOperativesService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectOperativesEntity> page = this.page(
                new Query<ProjectOperativesEntity>().getPage(params),
                new QueryWrapper<ProjectOperativesEntity>()
        );

        return new PageUtils(page);
    }
    //获取分页列表
    @Override
    public R getPageList(String projectId, String deviceSysId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        QueryWrapper<ProjectOperativesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("deviceSysId",deviceSysId).eq("del",0);
        if(!StringUtils.isEmpty(sort)){
            if(isAsc){
                wrapper.orderByAsc(sort);
            }else{
                wrapper.orderByDesc(sort);
            }
        }
        if(!StringUtils.isEmpty(search)&&!search.equals("''")){
            wrapper.like("operativesName",search);
        }
        IPage<ProjectOperativesEntity> page = this.page(
                new Query<ProjectOperativesEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<Map<String,Object>> data = new ArrayList<>();
        List<ProjectOperativesEntity> list = (List<ProjectOperativesEntity>) pageUtils.getList();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("deviceSysId",li.getDevicesysid());
            map.put("operativesName",li.getOperativesname());
            map.put("licenseCode",li.getLicensecode());
            map.put("licenseType",li.getLicensetype());
            map.put("licenseDate",li.getLicensedate());
            map.put("lincenseProject",li.getLincenseproject());
            map.put("remark",li.getRemark());
            map.put("createDate",li.getCreatedate());
            map.put("createUser",li.getCreateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("deleteDate",li.getDeletedate());
            map.put("deleteUser",li.getDeleteuser());
            map.put("del",li.getDel());
            data.add(map);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return R.ok(1,"获取成功!",res,true,null);
    }
    //获取详细信息
    @Override
    public R getDetails(String id) {
        ProjectOperativesEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("deviceSysId",li.getDevicesysid());
        map.put("operativesName",li.getOperativesname());
        map.put("licenseCode",li.getLicensecode());
        map.put("licenseType",li.getLicensetype());
        map.put("licenseDate",li.getLicensedate());
        map.put("lincenseProject",li.getLincenseproject());
        map.put("remark",li.getRemark());
        map.put("createDate",li.getCreatedate());
        map.put("createUser",li.getCreateuser());
        map.put("updateDate",li.getUpdatedate());
        map.put("updateUser",li.getUpdateuser());
        map.put("deleteDate",li.getDeletedate());
        map.put("deleteUser",li.getDeleteuser());
        map.put("del",li.getDel());
        return R.ok(1,"获取成功!",map,true,null);
    }
    //修改信息
    @Override
    public void updateOpertaives(OperativesParams operativesParams, String userId) {
        ProjectOperativesEntity entity = new ProjectOperativesEntity();
        entity.setSysid(operativesParams.getSysId());
        entity.setDevicesysid(operativesParams.getDeviceSysId());
        entity.setLicensecode(operativesParams.getLicenseCode());
        entity.setLicensedate(operativesParams.getLicenseDate());
        entity.setLicensetype(operativesParams.getLicenseType());
        entity.setLincenseproject(operativesParams.getLincenseProject());
        entity.setOperativesname(operativesParams.getOperativesName());
        entity.setRemark(operativesParams.getRemark());
        entity.setUpdatedate(format.format(new Date()));
        entity.setUpdateuser(userId);
        this.updateById(entity);
    }
    //添加
    @Override
    public void addOpertaives(OperativesParams operativesParams, String userId) {
        ProjectOperativesEntity entity = new ProjectOperativesEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setDel(0);
        entity.setDevicesysid(operativesParams.getDeviceSysId());
        entity.setLicensecode(operativesParams.getLicenseCode());
        entity.setLicensedate(operativesParams.getLicenseDate());
        entity.setLicensetype(operativesParams.getLicenseType());
        entity.setLincenseproject(operativesParams.getLincenseProject());
        entity.setOperativesname(operativesParams.getOperativesName());
        entity.setRemark(operativesParams.getRemark());
        entity.setCreatedate(format.format(new Date()));
        entity.setCreateuser(userId);
        this.save(entity);
    }
    //删除
    @Override
    public void deleteOpertaives(String id, String userId) {
        ProjectOperativesEntity entity = new ProjectOperativesEntity();
        entity.setSysid(id);
        entity.setDel(1);
        entity.setDeletedate(format.format(new Date()));
        entity.setDeleteuser(userId);
        this.updateById(entity);
    }

}