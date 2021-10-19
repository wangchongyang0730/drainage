package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectDevicecateDao;
import com.sx.drainage.params.DeviceCateParams;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.entity.ProjectDevicecateEntity;
import com.sx.drainage.service.ProjectDevicecateService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectDevicecateService")
@Transactional
public class ProjectDevicecateServiceImpl extends ServiceImpl<ProjectDevicecateDao, ProjectDevicecateEntity> implements ProjectDevicecateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectDevicecateEntity> page = this.page(
                new Query<ProjectDevicecateEntity>().getPage(params),
                new QueryWrapper<ProjectDevicecateEntity>()
        );

        return new PageUtils(page);
    }
    //获取分页列表
    @Override
    public Map<String, Object> getPageList(String projectId, Integer page, Integer pageRecord, String where) {
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        QueryWrapper<ProjectDevicecateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del",0);
        if(!where.equals("''")&&!StringUtils.isEmpty(where)){
            wrapper.like("name",where);
        }
        IPage<ProjectDevicecateEntity> iPage = this.page(
                new Query<ProjectDevicecateEntity>().getPage(map),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(iPage);
        List<Map<String, Object>> list = new ArrayList<>();
        List<ProjectDevicecateEntity> list1 = (List<ProjectDevicecateEntity>) pageUtils.getList();
        list1.forEach(li ->{
            Map<String, Object> entity=new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("projectId",li.getProjectid());
            entity.put("parentId",li.getParentid());
            entity.put("name",li.getName());
            entity.put("remark",li.getRemark());
            entity.put("createUser",li.getCreateuser());
            entity.put("createDate",li.getCreatedate());
            entity.put("updateUser",li.getUpdateuser());
            entity.put("updateDate",li.getUpdatedate());
            entity.put("deleteUser",li.getDeleteuser());
            entity.put("deleteDate",li.getDeletedate());
            entity.put("del",li.getDel());
            list.add(entity);
        });
        Map<String, Object> data=new HashMap<>();
        data.put("total",pageUtils.getTotalCount());
        data.put("rows",list);
        return data;
    }
    //新增
    @Override
    public void post(DeviceCateParams deviceCateParams, String userId) {
        ProjectDevicecateEntity entity = new ProjectDevicecateEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setName(deviceCateParams.getName());
        entity.setProjectid(deviceCateParams.getProjectId());
        entity.setParentid(deviceCateParams.getParentId());
        entity.setRemark(deviceCateParams.getRemark());
        entity.setDel(0);
        this.save(entity);
    }
    //修改
    @Override
    public void update(DeviceCateParams deviceCateParams, String userId) {
        ProjectDevicecateEntity entity = new ProjectDevicecateEntity();
        entity.setSysid(deviceCateParams.getSysId());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setName(deviceCateParams.getName());
        entity.setProjectid(deviceCateParams.getProjectId());
        entity.setParentid(deviceCateParams.getParentId());
        entity.setRemark(deviceCateParams.getRemark());
        this.updateById(entity);
    }
    //删除
    @Override
    public void delete(String sysId,String userId) {
        ProjectDevicecateEntity entity = new ProjectDevicecateEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.updateById(entity);
    }
    //获取所有设备类别
    @Override
    public List<Map<String, Object>> getAll(String projectId) {
        //eq("projectId", projectId) projectId无用 .net留下的坑
        List<ProjectDevicecateEntity> list = this.list(new QueryWrapper<ProjectDevicecateEntity>().eq("del", 0).orderByDesc("createDate"));
        List<Map<String, Object>> data=new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("projectId",li.getProjectid());
            map.put("parentId",li.getParentid());
            map.put("name",li.getName());
            map.put("remark",li.getRemark());
            map.put("createUser",li.getCreateuser());
            map.put("createDate",li.getCreatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("DeleteUser",li.getDeleteuser());
            map.put("DeleteDate",li.getDeletedate());
            map.put("del",li.getDel());
            data.add(map);
        });
        return data;
    }

}