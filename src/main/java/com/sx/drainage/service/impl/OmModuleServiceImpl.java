package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.OmModuleDao;
import com.sx.drainage.entity.OmModuleEntity;
import com.sx.drainage.service.OmModuleService;
import com.sx.drainage.service.OmRelAccountRoleService;
import com.sx.drainage.service.OmRelRoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("omModuleService")
@Transactional
public class OmModuleServiceImpl extends ServiceImpl<OmModuleDao, OmModuleEntity> implements OmModuleService {
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private OmRelRoleModuleService omRelRoleModuleService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmModuleEntity> page = this.page(
                new Query<OmModuleEntity>().getPage(params),
                new QueryWrapper<OmModuleEntity>()
        );

        return new PageUtils(page);
    }

    //获取用户权限菜单详细信息
    @Override
    public  List<Map<String, Object>> getPermissionList(String userId) {
        List<String> roleId = omRelAccountRoleService.getAllRoleId(userId);
//        boolean is=false;
//        for(int i=0;i<roleId.size();i++){
//            if(roleId.get(i).equals("402880152a491832012a6beac9af1f98")){
//                is=true;
//            }
//        }
//        //如果是超级管理员
//        if(is){
//            List<Map<String,Object>> res = omModuleService.getAllJurisdiction();
//            return res;
//        }
        List<String> modules=omRelRoleModuleService.getModuleByRoleIds(roleId);
        List<Map<String,Object>> res = this.getJurisdiction(modules);
        return res;
    }
    //获取当前用户菜单
    @Override
    public List<Map<String, Object>> getMenu(String userId) {
        List<String> roleId = omRelAccountRoleService.getAllRoleId(userId);
//        boolean is=false;
//        for(int i=0;i<roleId.size();i++){
//            if(roleId.get(i).equals("402880152a491832012a6beac9af1f98")){
//                is=true;
//            }
//        }
//        //如果是超级管理员
//        if(is){
//            List<Map<String,Object>> res = omModuleService.getAllMenuNotInRoleIds();
//           return res;
//        }
        List<String> modules=omRelRoleModuleService.getModuleByRoleIds(roleId);
        List<Map<String,Object>> res = this.getAllMenu(modules);
        return res;
    }
    //获取当前用户菜单（分项目权限）
    @Override
    public List<Map<String,Object>> getProjectMenu(String userId, String projectId) {
        List<OmModuleEntity> list = baseMapper.getProjectMenu(userId, projectId);
        List<Map<String,Object>> model = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("moduleId",li.getSysid());
            map.put("parent_id",li.getParentId());
            map.put("name",li.getName());
            map.put("fullName",li.getFullname());
            map.put("path",li.getUrl());
            map.put("sequenceNum",li.getSequencenum());
            map.put("iconCss",li.getIconcss());
            map.put("isHaveChildNode",li.getIsHaveChildNode());
            model.add(map);
        });
        return model;
    }
    ////获取用户权限菜单详细信息（分项目权限）
    @Override
    public List<Map<String,Object>> getProjectPermissionList(String userId, String projectId) {
        List<OmModuleEntity> list = baseMapper.getProjectPermissionList(userId, projectId);
        List<Map<String,Object>> model = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("route",li.getUrl());
            map.put("permission",li.getFullname());
            map.put("description",li.getDescription());
            model.add(map);
        });
        return model;
    }
    //获取菜单
    @Override
    public List<OmModuleEntity> getModuleList(String sysId) {
        if(sysId==null||sysId==""|| StringUtils.isEmpty(sysId)){
            return this.list(new QueryWrapper<OmModuleEntity>().isNull("parent_id").orderByAsc("sequenceNum"));
        }else{
            return this.list(new QueryWrapper<OmModuleEntity>().eq("parent_id",sysId).orderByAsc("sequenceNum"));
        }
    }
    //获取单个菜单或权限详细信息
    @Override
    public OmModuleEntity getModuleBySysId(String sysId) {
        return this.getById(sysId);
    }
    //修改模块中的菜单
    @Override
    public void updateModule(String sysId, String parentId, String name, String sequenceNum, String path, String iconCss, Boolean hide) {
        OmModuleEntity entity = new OmModuleEntity();
        entity.setSysid(sysId);
        if(StringUtils.isEmpty(parentId)||parentId.equals("")||parentId.equals("''")){
            entity.setParentId(null);
        }else{
            entity.setParentId(parentId);
        }
        entity.setName(name);
        entity.setSequencenum(sequenceNum);
        entity.setUrl(path);
        entity.setIconcss(iconCss);
        entity.setHide(hide);
        this.updateById(entity);
    }
    //修改模块中的功能
    @Override
    public void updateModuleFunction(String sysId, String parentId, String name, String path, Boolean hide) {
        OmModuleEntity entity = new OmModuleEntity();
        entity.setSysid(sysId);
        entity.setParentId(parentId);
        entity.setName(name);
        if(path!=null&&!path.equals("''")){
            entity.setUrl(path);
        }
        entity.setHide(hide);
        this.updateById(entity);
    }
    //删除
    @Override
    public void deleteModuleFunction(String sysId) {
        this.removeById(sysId);
    }
    //添加
    @Override
    public void insertModule(String parentId, String name, String path, Boolean hide) {
        OmModuleEntity module = this.getById(parentId);
        OmModuleEntity entity = new OmModuleEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setFullname(module.getFullname()+"/"+name);
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setUrl(path);
        entity.setHide(hide);
        entity.setCreateddate(new Date());
        entity.setModuletype("common");
        this.save(entity);
    }
    //根据id获取所有模块
    @Override
    public List<Map<String, Object>> getAllModuleByIds(List<String> moduleId) {
        List<OmModuleEntity> list = this.list(new QueryWrapper<OmModuleEntity>().in("sysId", moduleId));
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("fullname",li.getFullname());
            map.put("description",li.getDescription());
            maps.add(map);
        });
        return maps;
    }
    //获取所有
    @Override
    public List<OmModuleEntity> getAll() {
        return this.list();
    }
    //获取权限
    @Override
    public List<Map<String, Object>> getJurisdiction(List<String> modules) {
        List<OmModuleEntity> list = this.list(new QueryWrapper<OmModuleEntity>().in("sysId", modules).likeRight("fullName", "功能权限"));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("route",li.getUrl());
            map.put("permission",li.getFullname());
            map.put("description",li.getDescription());
            res.add(map);
        });
        return res;
    }
    //获取当前用户菜单
    @Override
    public List<Map<String, Object>> getAllMenu(List<String> modules) {
        List<OmModuleEntity> list = this.list(new QueryWrapper<OmModuleEntity>().in("sysId", modules).likeRight("fullName", "菜单").eq("hide",0).orderByAsc("sequenceNum"));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("moduleId",li.getSysid());
            map.put("parent_id",li.getParentId());
            map.put("name",li.getName());
            boolean contains = li.getFullname().contains("//");
            if(contains){
                map.put("fullName",li.getFullname().replaceAll("//","/"));
            }else{
                map.put("fullName",li.getFullname());
            }
            map.put("path",li.getUrl());
            map.put("sequenceNum",li.getSequencenum());
            map.put("iconCss",li.getIconcss());
            map.put("isHaveChildNode",li.getIsHaveChildNode());
            res.add(map);
        });
        return res;
    }
    //如果是超级管理员获取所有权限
    @Override
    public List<Map<String, Object>> getAllJurisdiction() {
        List<OmModuleEntity> list = this.list(new QueryWrapper<OmModuleEntity>().likeRight("fullName", "功能权限"));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("route",li.getUrl());
            map.put("permission",li.getFullname());
            map.put("description",li.getDescription());
            res.add(map);
        });
        return res;
    }
    //如果是超级管理员获取所有菜单
    @Override
    public List<Map<String, Object>> getAllMenuNotInRoleIds() {
        List<OmModuleEntity> list = this.list(new QueryWrapper<OmModuleEntity>().likeRight("fullName", "菜单").eq("hide",0).orderByAsc("sequenceNum"));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("moduleId",li.getSysid());
            map.put("parent_id",li.getParentId());
            map.put("name",li.getName());
            boolean contains = li.getFullname().contains("//");
            if(contains){
                map.put("fullName",li.getFullname().replaceAll("//","/"));
            }else{
                map.put("fullName",li.getFullname());
            }
            map.put("path",li.getUrl());
            map.put("sequenceNum",li.getSequencenum());
            map.put("iconCss",li.getIconcss());
            map.put("isHaveChildNode",li.getIsHaveChildNode());
            res.add(map);
        });
        return res;
    }

}