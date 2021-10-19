package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.ProjectCompanyEntity;
import com.sx.drainage.params.ProjectParticipantsParams;
import com.sx.drainage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

import com.sx.drainage.dao.ProjectCompanyTagUserDao;
import com.sx.drainage.entity.ProjectCompanyTagUserEntity;
import org.springframework.transaction.annotation.Transactional;


@Service("projectCompanyTagUserService")
@Transactional
public class ProjectCompanyTagUserServiceImpl extends ServiceImpl<ProjectCompanyTagUserDao, ProjectCompanyTagUserEntity> implements ProjectCompanyTagUserService {
    @Autowired
    private OmAccountService omAccountService;
    @Autowired
    private ProjectCompanyService projectCompanyService;
    @Autowired
    private OmTagService omTagService;
    @Autowired
    private Environment environment;
    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectCompanyTagUserEntity> page = this.page(
                new Query<ProjectCompanyTagUserEntity>().getPage(params),
                new QueryWrapper<ProjectCompanyTagUserEntity>()
        );

        return new PageUtils(page);
    }
    //获取项目参与单位及主要人员信息
    @Override
    public List<ProjectCompanyTagUserEntity> getProjectJoinPersons(String projectId) {
        return this.list(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id",projectId));
    }
    //获取参与单位Id
    @Override
    public String getProjectPositionsList(String tag_id, String projectId) {
        List<ProjectCompanyTagUserEntity> list = this.list(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id", projectId).eq("tag_id", tag_id));
        if(list.size()>0){
            return list.get(0).getCompanyId();
        }
        return null;
    }

    @Override
    public Map<String,Object> getProjectParticipants(String projectId, Integer page, Integer pageRecord) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",page.toString());
        params.put("limit",pageRecord.toString());
        IPage<ProjectCompanyTagUserEntity> iPage = this.page(
                new Query<ProjectCompanyTagUserEntity>().getPage(params),
                new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id",projectId)
        );
        PageUtils pageUtils = new PageUtils(iPage);
        List<ProjectCompanyTagUserEntity> list = (List<ProjectCompanyTagUserEntity>) pageUtils.getList();
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li->{
            OmAccountEntity user = omAccountService.getUser(li.getUserId());
            ProjectCompanyEntity company = projectCompanyService.getCompany(li.getCompanyId());
            Map<String, Object> map = new HashMap<>();
            map.put("id",li.getId());
            map.put("projectid",li.getProjectId());
            map.put("tagid",li.getTagId());
            map.put("companyId",li.getCompanyId());
            map.put("companyname",company.getName());
            map.put("userid",li.getUserId());
            map.put("userName",user.getName());
            map.put("phone",user.getPhoto());
            map.put("name",omTagService.getById(li.getTagId()).getName());
            data.add(map);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("total",pageUtils.getTotalCount());
        map.put("rows",data);
        return map;
    }
    //新增当前项目的参建单位
    @Override
    public void postProjectParticipants(ProjectParticipantsParams projectParticipantsParams, String companyId) {
        this.remove(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id",projectParticipantsParams.getProjectId()).eq("tag_id",projectParticipantsParams.getTagid()));
        ProjectCompanyTagUserEntity entity = new ProjectCompanyTagUserEntity();
        entity.setId(CreateUuid.uuid());
        entity.setCompanyId(companyId);
        entity.setProjectId(projectParticipantsParams.getProjectId());
        entity.setTagId(projectParticipantsParams.getTagid());
        entity.setUserId(projectParticipantsParams.getAccount_id());
        this.save(entity);
    }
    //修改当前项目的参建单位
    @Override
    public void putProjectParticipants(ProjectParticipantsParams projectParticipantsParams, String companyId) {
        ProjectCompanyTagUserEntity entity = new ProjectCompanyTagUserEntity();
        entity.setId(projectParticipantsParams.getSysId());
        entity.setCompanyId(companyId);
        entity.setProjectId(projectParticipantsParams.getProjectId());
        entity.setTagId(projectParticipantsParams.getTagid());
        entity.setUserId(projectParticipantsParams.getAccount_id());
        this.updateById(entity);
        omTagRelPostAccountService.removeJoinUser(projectParticipantsParams.getTagid(),projectParticipantsParams.getProjectId());
    }
    //删除当前项目的参建单位
    @Override
    public void deleteProjectParticipants(String sysId) {
        this.removeById(sysId);
    }
    //初始化城建单位
    @Override
    public void initCompany(String sysid) {
        ProjectCompanyTagUserEntity entity = new ProjectCompanyTagUserEntity();
        entity.setId(CreateUuid.uuid());
        entity.setProjectId(sysid);
        entity.setTagId(environment.getProperty("init.tagId"));
        entity.setCompanyId(environment.getProperty("init.companyId"));
        this.save(entity);
    }
    //获取项目下某单位类别
    @Override
    public ProjectCompanyTagUserEntity getTagByCompanyIdAndProjectId(String companyId, String projectId) {
        List<ProjectCompanyTagUserEntity> list = this.list(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id", projectId).eq("company_id", companyId));
        if(list.size()>0&&list!=null){
            return list.get(0);
        }
        return null;
    }
    //获取单位负责人
    @Override
    public ProjectCompanyTagUserEntity getUser(String projectId, String sysId) {
        List<ProjectCompanyTagUserEntity> list = this.list(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id", projectId).eq("tag_id", sysId));
        if(list.size()>0&&list!=null){
            return list.get(0);
        }
        return null;
    }
    //变更负责人
    @Override
    public void putUser(String sysid, String projectId, String userId) {
        List<ProjectCompanyTagUserEntity> list = this.list(new QueryWrapper<ProjectCompanyTagUserEntity>().eq("project_id", projectId).eq("tag_id", sysid));
        if(list!=null&&list.size()>0){
            ProjectCompanyTagUserEntity entity = list.get(0);
            entity.setUserId(userId);
            this.updateById(entity);
        }else{
            ProjectCompanyTagUserEntity entity = new ProjectCompanyTagUserEntity();
            OmAccountEntity user = omAccountService.getUser(userId);
            entity.setId(CreateUuid.uuid());
            entity.setTagId(sysid);
            entity.setUserId(userId);
            entity.setCompanyId(user.getCompanyId());
            this.save(entity);
        }
    }
}