package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.entity.ProjectProjectEntity;
import com.sx.drainage.params.BindBimParams;
import com.sx.drainage.params.WbsBimTypeParams;
import com.sx.drainage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectWbsbimtypeDao;
import com.sx.drainage.entity.ProjectWbsbimtypeEntity;
import org.springframework.transaction.annotation.Transactional;


@Service("projectWbsbimtypeService")
@Transactional
public class ProjectWbsbimtypeServiceImpl extends ServiceImpl<ProjectWbsbimtypeDao, ProjectWbsbimtypeEntity> implements ProjectWbsbimtypeService {

    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;
    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private ProjectProjectService projectProjectService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsbimtypeEntity> page = this.page(
                new Query<ProjectWbsbimtypeEntity>().getPage(params),
                new QueryWrapper<ProjectWbsbimtypeEntity>()
        );

        return new PageUtils(page);
    }
    //获取所有bim类型绑定的bim模型
    @Override
    public List<Map<String, Object>> getAllWbsBimInfo(String projectId) {
        List<ProjectWbsbimtypeEntity> list = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("projectId", projectId).eq("del", 0).isNotNull("bimType"));
        List<ProjectWbsbimtypeEntity> list1 = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().isNull("bimType").isNull("type").eq("del", 0));
        List<ProjectWbsbimtypeEntity> two = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();
        list1.forEach(li -> {
            list.forEach(ch ->{
                if(ch.getBimtype().equals(li.getId())){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",li.getId());
                    map.put("sysId",ch.getId());
                    map.put("BimUrn",ch.getBimurn());
                    map.put("type",ch.getType());
                    map.put("filePath",ch.getFilepath());
                    map.put("name",li.getName());
                    data.add(map);
                    two.add(li);
                }
            });
        });
//        Iterator<ProjectWbsbimtypeEntity> iterator = list.iterator();
//        Iterator<ProjectWbsbimtypeEntity> iterator1 = list1.iterator();
//        while (iterator1.hasNext()){
//            while (iterator.hasNext()){
//                ProjectWbsbimtypeEntity next = iterator.next();
//                ProjectWbsbimtypeEntity next1 = iterator1.next();
//                if(next.getBimtype().equals(next1.getId())){
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("id",next1.getId());
//                    map.put("BimUrn",next.getBimurn());
//                    map.put("type",next.getType());
//                    map.put("name",next1.getName());
//                    data.add(map);
//                    iterator1.remove();
//                }
//            }
//        }
        list1.removeAll(two);
        list1.forEach(li ->{
                Map<String, Object> map = new HashMap<>();
                map.put("id",li.getId());
                map.put("BimUrn","");
                map.put("type","2");
                map.put("name",li.getName());
                map.put("filePath",null);
                data.add(map);
        });
        return data;
    }
    //绑定bim模型
    @Override
    public void bindBim(BindBimParams bindBimParams, String userId) {
//        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
//        entity.setBimurn(bindBimParams.getBimId());
//        entity.setId(bindBimParams.getBimType());
//        entity.setType(Integer.parseInt(bindBimParams.getEngineType()));
//        entity.setProjectid(bindBimParams.getProjectId());
//        this.updateById(entity);
        this.remove(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("projectId",bindBimParams.getProjectId()).eq("bimType",bindBimParams.getBimType()));
        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
        entity.setBimurn(bindBimParams.getBimId());
        entity.setId(CreateUuid.uuid());
        entity.setName("");
        entity.setBimtype(bindBimParams.getBimType());
        entity.setType(Integer.parseInt(bindBimParams.getEngineType()));
        entity.setProjectid(bindBimParams.getProjectId());
        entity.setDel(0);
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setFilepath(bindBimParams.getFilePath());
        this.save(entity);
    }
    //获取BIM模型的项目Id
    @Override
    public Map<String, Object> getBIMProjectId(String projectId, String bimTypeName) {
        List<ProjectWbsbimtypeEntity> list = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("name", bimTypeName).eq("del", 0).isNull("bimType"));
        Map<String, Object> map = new HashMap<>();
        if(list.size()>0){
            List<ProjectWbsbimtypeEntity> list1 = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("bimType", list.get(0).getId()).eq("projectId", projectId).eq("del", 0));
            if(list1.size()>0){
                map.put("engineType",list1.get(0).getType());
                map.put("GetBIMProjectId",list1.get(0).getBimurn());
            }else{
                map.put("engineType","");
                map.put("GetBIMProjectId","");
            }
        }else{
            map.put("engineType","");
            map.put("GetBIMProjectId","");
        }
        return map;
    }
    //获取所有模型类别
    @Override
    public List<Map<String, Object>> GetAllWbsbimType() {
        List<ProjectWbsbimtypeEntity> list = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("del", 0).isNull("type"));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id",li.getId());
            map.put("name",li.getName());
            map.put("type",li.getType());
            map.put("projectId",li.getProjectid());
            map.put("BimUrn",li.getBimurn());
            map.put("createDate",li.getCreatedate());
            map.put("createUser",li.getCreateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("deleteDate",li.getDeletedate());
            map.put("deleteUser",li.getDeleteuser());
            map.put("del",li.getDel());
            map.put("bimType",li.getBimtype());
            data.add(map);
        });
        return data;
    }
    //新增模型类别信息
    @Override
    public void postWbsbimType(WbsBimTypeParams wbsBimTypeParams, String userId) {
        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
        entity.setId(CreateUuid.uuid());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setDel(0);
        entity.setName(wbsBimTypeParams.getName());
        entity.setProjectid(wbsBimTypeParams.getProjectId());
        this.save(entity);
    }
    //更新模型类别信息
    @Override
    public void putWbsbimType(WbsBimTypeParams wbsBimTypeParams, String userId) {
        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
        entity.setId(wbsBimTypeParams.getId());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setName(wbsBimTypeParams.getName());
        entity.setProjectid(wbsBimTypeParams.getProjectId());
        this.updateById(entity);
    }
    //删除模型类别信息
    @Override
    public void deleteWbsbimType(String sysId, String userId) {
        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
        entity.setId(sysId);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        entity.setDel(1);
        this.updateById(entity);
    }
    //获取所有bim模型
    @Override
    public List<Map<String, Object>> getAllBim(String userId) {
        List<ProjectProjectEntity> project = projectProjectService.getAllProject();
//        //获取用户所拥有的的角色ID
//        List<String> list = omRelAccountRoleService.getAllRoleId(userId);
//        //获取用户所拥有的的所有角色
//        List<OmRoleEntity> roles = omRoleService.getAllMyRole(list);
//        //判断用户是否拥有超级管理员角色
//        boolean role = false;
//        for (int i = 0; i < roles.size(); i++) {
//            if (roles.get(i).getName().equals("超级管理员")) {
//                role = true;
//            }
//        }
//        if(role){
            List<ProjectWbsbimtypeEntity> bim = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("del", 0).isNotNull("bimType"));
            List<ProjectWbsbimtypeEntity> list1 = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().isNull("bimType").isNull("type").eq("del", 0));
            List<Map<String, Object>> data = new ArrayList<>();
            list1.forEach(li -> {
                bim.forEach(ch ->{
                    if(ch.getBimtype().equals(li.getId())){
                        Map<String, Object> map = new HashMap<>();
                        map.put("id",li.getId());
                        map.put("BimUrn",ch.getBimurn());
                        map.put("type",ch.getType());
                        map.put("filePath",ch.getFilepath());
                        map.put("name",li.getName());
                        project.forEach(p -> {
                            if(ch.getProjectid().equals(p.getSysid())){
                                map.put("projectId",p.getSysid());
                                map.put("projectName",p.getName());
                            }
                        });
                        if((map.size()>5)){
                            data.add(map);
                        }
                    }
                });
            });
            return data;
//        }else{
//            List<String> projectId = omTagRelPostAccountService.getAllProjectId(userId);
//            List<ProjectWbsbimtypeEntity> bim = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().in("projectId",projectId).eq("del", 0).isNotNull("bimType"));
//            List<ProjectWbsbimtypeEntity> list1 = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().isNull("bimType").isNull("type").eq("del", 0));
//            List<Map<String, Object>> data = new ArrayList<>();
//            list1.forEach(li -> {
//                bim.forEach(ch ->{
//                    if(ch.getBimtype().equals(li.getId())){
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("id",li.getId());
//                        map.put("BimUrn",ch.getBimurn());
//                        map.put("type",ch.getType());
//                        map.put("filePath",ch.getFilepath());
//                        map.put("name",li.getName());
//                        project.forEach(p -> {
//                            if(ch.getProjectid().equals(p.getSysid())){
//                                map.put("projectId",p.getSysid());
//                                map.put("projectName",p.getName());
//                            }
//                        });
//                        if((map.size()>5)){
//                            data.add(map);
//                        }
//                    }
//                });
//            });
//            return data;
//        }
    }
    //获取绑定的bim模型
    @Override
    public List<Map<String, Object>> getBindBim(String projectId) {
        ProjectProjectEntity project = projectProjectService.getProject(projectId);
        List<ProjectWbsbimtypeEntity> list = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().eq("projectId", projectId).eq("del", 0).isNotNull("bimType"));
        List<ProjectWbsbimtypeEntity> list1 = this.list(new QueryWrapper<ProjectWbsbimtypeEntity>().isNull("bimType").isNull("type").eq("del", 0));
        List<Map<String, Object>> data = new ArrayList<>();
        list1.forEach(li -> {
            list.forEach(ch ->{
                if(ch.getBimtype().equals(li.getId())){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",li.getId());
                    map.put("BimUrn",ch.getBimurn());
                    map.put("type",ch.getType());
                    map.put("filePath",ch.getFilepath());
                    map.put("name",li.getName());
                    map.put("projectId",ch.getProjectid());
                    map.put("projectName",project!=null?null:project.getName());
                    data.add(map);
                }
            });
        });
        return data;
    }
    //删除模型绑定
    @Override
    public void deleteWbsBind(String sysId, String userId) {
        ProjectWbsbimtypeEntity entity = new ProjectWbsbimtypeEntity();
        entity.setId(sysId);
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.updateById(entity);
    }

}