package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.ProjectProjectbasicinfoDao;
import com.sx.drainage.params.ProjectBasicParams;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.entity.ProjectProjectbasicinfoEntity;
import com.sx.drainage.service.ProjectProjectbasicinfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectProjectbasicinfoService")
@Transactional
public class ProjectProjectbasicinfoServiceImpl extends ServiceImpl<ProjectProjectbasicinfoDao, ProjectProjectbasicinfoEntity> implements ProjectProjectbasicinfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectProjectbasicinfoEntity> page = this.page(
                new Query<ProjectProjectbasicinfoEntity>().getPage(params),
                new QueryWrapper<ProjectProjectbasicinfoEntity>()
        );

        return new PageUtils(page);
    }
    //获取项目详细信息
    @Override
    public Map<String,Object> getProjectDetails(String projectId) {
        ProjectProjectbasicinfoEntity entity = this.getOne(new QueryWrapper<ProjectProjectbasicinfoEntity>().eq("projectId", projectId));
        Map<String, Object> map = new HashMap<>();
        if(entity!=null){
            map.put("sysId",entity.getSysid());
            map.put("contractAmount",entity.getContractamount());
            map.put("safetyAmount",entity.getSafetyamount());
            map.put("safetyProportion",entity.getSafetyproportion());
            map.put("projectProfile",entity.getProjectprofile());
            map.put("profilePic",entity.getProfilepic());
            map.put("projectId",entity.getProjectid());
            map.put("createDate",entity.getCreatedate());
            map.put("createUser",entity.getCreateuser());
            map.put("updateDate",entity.getUpdatedate());
            map.put("updateUser",entity.getUpdateuser());
            map.put("deleteDate",entity.getDeletedate());
            map.put("deleteUser",entity.getDeleteuser());
            map.put("del",entity.getDel());
        }
        return map;
    }

    @Override
    public ProjectProjectbasicinfoEntity getProjectEntity(String projectId) {
        ProjectProjectbasicinfoEntity entity = this.getOne(new QueryWrapper<ProjectProjectbasicinfoEntity>().eq("projectId", projectId));
        return entity;
    }

    //更新当前项目的基本信息
    @Override
    public void putProjectBasicInfo(ProjectBasicParams projectBasicParams) {
        ProjectProjectbasicinfoEntity entity = new ProjectProjectbasicinfoEntity();
        entity.setContractamount(projectBasicParams.getContractAmount());
        entity.setSafetyamount(projectBasicParams.getSafetyAmount());
        entity.setSafetyproportion(projectBasicParams.getSafetyProportion());
        entity.setProjectprofile(projectBasicParams.getProjectProfile());
        entity.setProfilepic(projectBasicParams.getProfilePic());
        entity.setProjectid(projectBasicParams.getProjectId());
        this.update(entity,new UpdateWrapper<ProjectProjectbasicinfoEntity>().eq("projectId", projectBasicParams.getProjectId()));
    }
    //删除项目
    @Override
    public void deleteProject(String sysId) {
        ProjectProjectbasicinfoEntity entity = new ProjectProjectbasicinfoEntity();
        entity.setDel(1);
        this.update(entity,new UpdateWrapper<ProjectProjectbasicinfoEntity>().eq("projectId",sysId));
    }
    //新增项目详细信息
    @Override
    public void postProject(String userId, String sysid) {
        ProjectProjectbasicinfoEntity entity = new ProjectProjectbasicinfoEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setDel(0);
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setProjectid(sysid);
        this.save(entity);
    }
    //根据项目id更改信息
    @Override
    public void putByProjectId(String s, String projectDescribe) {
        ProjectProjectbasicinfoEntity entity = new ProjectProjectbasicinfoEntity();
        entity.setProjectprofile(projectDescribe);
        this.update(entity,new UpdateWrapper<ProjectProjectbasicinfoEntity>().eq("projectId", s));
    }

}