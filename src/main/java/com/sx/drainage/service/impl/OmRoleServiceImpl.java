package com.sx.drainage.service.impl;

import com.sx.drainage.common.*;
import com.sx.drainage.dao.OmRoleDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.OmModuleEntity;
import com.sx.drainage.entity.OmRelRoleModuleEntity;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.params.AddRoleFunction;
import com.sx.drainage.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("omRoleService")
@Transactional
public class OmRoleServiceImpl extends ServiceImpl<OmRoleDao, OmRoleEntity> implements OmRoleService {

    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private OmAccountService omAccountService;
    @Autowired
    private OmRelRoleModuleService omRelRoleModuleService;
    @Autowired
    private OmModuleService omModuleService;
    @Autowired
    private OmTagRelPostRoleService omTagRelPostRoleService;
    @Autowired
    private OmTagPostService omTagPostService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String search = (String) params.get("search");
        QueryWrapper<OmRoleEntity> wrapper = new QueryWrapper<>();
        if (!search.equals("%22%22") && !search.equals("''") && !StringUtils.isEmpty(search)) {
            wrapper.like("name", search);
        }
        IPage<OmRoleEntity> page = this.page(
                new Query<OmRoleEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<OmRoleEntity> getAllMyRole(List<String> roleId) {
        return baseMapper.getAllMyRole(roleId);
    }

    //获取角色列表
    @Override
    public List<Map<String, Object>> getRoleList(String where) {
        if(where.equals("''") ||where.equals("%22%22")|| StringUtils.isEmpty(where)) {
            List<Map<String, Object>> maps = new ArrayList<>();
            List<OmRoleEntity> list = this.list();
            list.forEach(li -> {
                Map<String, Object> map = new HashMap<>();
                map.put("sysId", li.getSysid());
                map.put("endTime", li.getEndtime());
                map.put("startTime", li.getStarttime());
                map.put("valid", li.getValid());
                map.put("parent_id", li.getParentId());
                map.put("fullName", li.getFullname());
                map.put("enterPriseId", li.getEnterpriseid());
                map.put("createdDate", li.getCreateddate());
                map.put("description", li.getDescription());
                map.put("name", li.getName());
                maps.add(map);
            });
            return maps;
        } else {
            List<Map<String, Object>> maps = new ArrayList<>();
            List<OmRoleEntity> list = this.list(new QueryWrapper<OmRoleEntity>().eq("name", where));
            list.forEach(li -> {
                Map<String, Object> map = new HashMap<>();
                map.put("sysId", li.getSysid());
                map.put("endTime", li.getEndtime());
                map.put("startTime", li.getStarttime());
                map.put("valid", li.getValid());
                map.put("parent_id", li.getParentId());
                map.put("fullName", li.getFullname());
                map.put("enterPriseId", li.getEnterpriseid());
                map.put("createdDate", li.getCreateddate());
                map.put("description", li.getDescription());
                map.put("name", li.getName());
                maps.add(map);
            });
            return maps;
        }
    }

    //获取单个角色
    @Override
    public List<OmRoleEntity> getOneRole(String s) {
        return this.list(new QueryWrapper<OmRoleEntity>().eq("sysId", s));
    }

    //新增角色
    @Override
    public void insertRole(String name, String valid, String description) {
        OmRoleEntity entity = new OmRoleEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(name);
        entity.setValid(Integer.parseInt(valid));
        entity.setDescription(description);
        this.save(entity);
    }

    //更新角色
    @Override
    public void updateRole(String sysId, String name, String valid, String description) {
        OmRoleEntity entity = new OmRoleEntity();
        entity.setSysid(sysId);
        entity.setName(name);
        entity.setValid(Integer.parseInt(valid));
        entity.setDescription(description);
        this.updateById(entity);
    }

    //根据角色ID删除信息
    @Override
    public void deleteRoleById(String sysId) {
        this.removeById(sysId);
    }

    //获取该角色绑定的用户/功能/岗位列表引用数据表
    @Override
    public R getRoleUserFunctionPost(String sysId, String type) {
        if (type.equals("1")) {
            List<String> userId = omRelAccountRoleService.getAllUserId(sysId);
            List<Map<String, Object>> maps = omAccountService.getAllUserByIds(userId);
            return R.ok(1, "获取成功!", maps, true, null);
        }
        if (type.equals("2")) {
            List<String> moduleId = omRelRoleModuleService.getAllModuleId(sysId);
            List<Map<String, Object>> maps = omModuleService.getAllModuleByIds(moduleId);
            return R.ok(1, "获取成功!", maps, true, null);
        }
        if (type.equals("3")) {
            List<String> postId = omTagRelPostRoleService.getAllPostId(sysId);
            List<Map<String, Object>> maps = omTagPostService.getAllPostByIds(postId);
            return R.ok(1, "获取成功!", maps, true, null);
        }
        return R.error(107, "非法参数!");
    }

    //获取角色与所有用户的关系列表
    @Override
    public Map<String, Object> getRoleUserList(String sysid, String name, Integer pageRecord, Integer page) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("pageRecord", pageRecord.toString());
        if (name.equals("''") || name.equals("%22%22") || StringUtils.isEmpty(name)) {
            return getRoleUserListNotByName(sysid,pageRecord,page);
        } else {
            return getRoleUserListByName(name,sysid,pageRecord,page);
        }
    }
    //新增、移除角色与用户关系
    @Override
    public void insertDeleteRoleUser(String sysId, String userid, String action) {
        if(action.equals("D")){
            omRelAccountRoleService.deleteRoleUser(sysId,userid);
        }
        if(action.equals("I")){
            omRelAccountRoleService.insertRoleUser(sysId,userid);
        }
    }
    //获取角色与功能列表
    @Override
    public Map<String, Object> getRoleFunctionList(String roleid) {
        List<String> moduleId = omRelRoleModuleService.getAllModuleId(roleid);
        List<OmModuleEntity> parent=omModuleService.getAll();
        List<Module> list = new ArrayList<>();
        parent.forEach(li ->{
            Module module = new Module(li,moduleId);
            list.add(module);
        });
        List<Module> data = new ArrayList<>();
        list.forEach(li ->{
            if(StringUtils.isEmpty(li.getParentId())){
                data.add(li);
            }
        });
        list.removeAll(data);
        data.forEach(li->{
            List<Module> child = getChild(li.getId(), list);
            li.setChildren(child);
        });
/*        data.forEach(pr->{
            if(pr.getName().equals("菜单")){
                List<Module> child = new ArrayList<>();
                list.forEach(li ->{
                    if(pr.getId().equals(li.getParentId())){
                        child.add(li);
                    }
                });
                list.removeAll(child);
                child.forEach(ch ->{
                    List<Module> childs = new ArrayList<>();
                    list.forEach(li ->{
                        if(ch.getId().equals(li.getParentId())){
                            childs.add(li);
                        }
                    });
                    list.removeAll(childs);
                    ch.setChildren(childs);
                });
                pr.setChildren(child);
            }else{
                List<Module> child = new ArrayList<>();
                list.forEach(li ->{
                    if(pr.getId().equals(li.getParentId())){
                        child.add(li);
                    }
                });
                list.removeAll(child);
                child.forEach(ch ->{
                    List<Module> childs = new ArrayList<>();
                    list.forEach(li ->{
                        if(ch.getId().equals(li.getParentId())){
                            childs.add(li);
                        }
                    });
                    childs.forEach(chs ->{
                        List<Module> child3 = new ArrayList<>();
                        list.forEach(li ->{
                            if(chs.getId().equals(li.getParentId())){
                                child3.add(li);
                            }
                        });
                        list.removeAll(child3);
                        chs.setChildren(child3);
                    });
                    list.removeAll(childs);
                    ch.setChildren(childs);
                });
                pr.setChildren(child);

            }
        });*/
        Map<String, Object> map = new HashMap<>();
        map.put("rows",data);
        map.put("total",0);
        return map;
    }
    //保存角色与功能关系
    @Override
    public void saveRoleFunction(AddRoleFunction addRoleFunction) {
        List<OmRelRoleModuleEntity> list = new ArrayList<>();
        String[] moduleids = addRoleFunction.getModuleids();
        for (String s : moduleids) {
            OmRelRoleModuleEntity entity = new OmRelRoleModuleEntity();
            entity.setRoleId(addRoleFunction.getRoleid());
            entity.setModuleId(s);
            list.add(entity);
        }
        omRelRoleModuleService.saveRoleFunction(list);
    }
    //根据id获取所有角色
    @Override
    public List<OmRoleEntity> getAllRole() {
            return this.list();
    }

    //具备搜索条件的
    private Map<String, Object> getRoleUserListByName(String name,String sysid,Integer pageRecord, Integer page){
        List<String> userId = omRelAccountRoleService.getAllUserId(sysid);
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        map.put("search",name);
        PageUtils queryPage = omAccountService.queryPage(map);
        if(userId.size()>0){
            List<OmAccountEntity> list = omAccountService.getAllUserBySearch(userId, name);
            if(list.size()>(page-1)*pageRecord){
                if((list.size()-(page-1)*pageRecord)>=pageRecord){
                    List<Map<String,Object>> data=new ArrayList<>();
                    for(int i=0;i<pageRecord;i++){
                        Map<String, Object> entity = new HashMap<>();
                        map.put("sysid",list.get((page-1)*pageRecord+i).getSysid());
                        map.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        map.put("name",list.get((page-1)*pageRecord+i).getName());
                        map.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        map.put("state","Y");
                        map.put("operate","N");
                        map.put("sort",list.get((page-1)*pageRecord+i).getSort());
                        data.add(entity);
                    }
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("total",queryPage.getTotalCount());
                    hashMap.put("rows",data);
                    return hashMap;
                }else{
                    List<Map<String,Object>> data=new ArrayList<>();
                    for(int i=0;i<(list.size()-(page-1)*pageRecord);i++){
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("sysid",list.get((page-1)*pageRecord+i).getSysid());
                        entity.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        entity.put("name",list.get((page-1)*pageRecord+i).getName());
                        entity.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        entity.put("state","Y");
                        entity.put("operate","N");
                        entity.put("sort",list.get((page-1)*pageRecord+i).getSort());
                        data.add(entity);
                    }
                    Integer limit=pageRecord-(list.size()-(page-1)*pageRecord);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("page",page.toString());
                    maps.put("limit",limit.toString());
                    maps.put("search",name);
                    maps.put("not",userId);
                    PageUtils pageUtils = omAccountService.queryPage(maps);
                    List<OmAccountEntity> list1 = (List<OmAccountEntity>) pageUtils.getList();
                    list1.forEach(li ->{
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("sysid",li.getSysid());
                        entity.put("accountId",li.getAccountid());
                        entity.put("name",li.getName());
                        entity.put("mobile",li.getMobile());
                        entity.put("state","N");
                        entity.put("operate","Y");
                        entity.put("sort",li.getSort());
                        data.add(entity);
                    });
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("total",queryPage.getTotalCount());
                    hashMap.put("rows",data);
                    return hashMap;
                }
            }else{
                List<OmAccountEntity> list1 = (List<OmAccountEntity>) queryPage.getList();
                List<Map<String,Object>> data=new ArrayList<>();
                list1.forEach(li ->{
                    Map<String, Object> entity = new HashMap<>();
                    entity.put("sysid",li.getSysid());
                    entity.put("accountId",li.getAccountid());
                    entity.put("name",li.getName());
                    entity.put("mobile",li.getMobile());
                    entity.put("state","N");
                    entity.put("operate","Y");
                    entity.put("sort",li.getSort());
                    data.add(entity);
                });
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("total",queryPage.getTotalCount());
                hashMap.put("rows",data);
                return hashMap;
            }
        }else {
            List<OmAccountEntity> list1 = (List<OmAccountEntity>) queryPage.getList();
            List<Map<String,Object>> data=new ArrayList<>();
            list1.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysid",li.getSysid());
                entity.put("accountId",li.getAccountid());
                entity.put("name",li.getName());
                entity.put("mobile",li.getMobile());
                entity.put("state","N");
                entity.put("operate","Y");
                entity.put("sort",li.getSort());
                data.add(entity);
            });
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("total",queryPage.getTotalCount());
            hashMap.put("rows",data);
            return hashMap;
        }
    }
    //不具备搜索条件的
    private Map<String, Object> getRoleUserListNotByName(String sysid, Integer pageRecord, Integer page){
        List<String> userId = omRelAccountRoleService.getAllUserId(sysid);
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        map.put("search",null);
        PageUtils queryPage = omAccountService.queryPage(map);
        if(userId.size()>0){
            List<OmAccountEntity> list = omAccountService.getAllUserNotBySearch(userId);
            if(list.size()>(page-1)*pageRecord){
                if((list.size()-(page-1)*pageRecord)>=pageRecord){
                    List<Map<String,Object>> data=new ArrayList<>();
                    for(int i=0;i<pageRecord;i++){
                        Map<String, Object> entity = new HashMap<>();
                        map.put("sysid",list.get((page-1)*pageRecord+i).getSysid());
                        map.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        map.put("name",list.get((page-1)*pageRecord+i).getName());
                        map.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        map.put("state","Y");
                        map.put("operate","N");
                        map.put("sort",list.get((page-1)*pageRecord+i).getSort());
                        data.add(entity);
                    }
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("total",queryPage.getTotalCount());
                    hashMap.put("rows",data);
                    return hashMap;
                }else{
                    List<Map<String,Object>> data=new ArrayList<>();
                    for(int i=0;i<(list.size()-(page-1)*pageRecord);i++){
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("sysid",list.get((page-1)*pageRecord+i).getSysid());
                        entity.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        entity.put("name",list.get((page-1)*pageRecord+i).getName());
                        entity.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        entity.put("state","Y");
                        entity.put("operate","N");
                        entity.put("sort",list.get((page-1)*pageRecord+i).getSort());
                        data.add(entity);
                    }
                    Integer limit=pageRecord-(list.size()-(page-1)*pageRecord);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("page",page.toString());
                    maps.put("limit",limit.toString());
                    maps.put("search",null);
                    maps.put("not",userId);
                    PageUtils pageUtils = omAccountService.queryPage(maps);
                    List<OmAccountEntity> list1 = (List<OmAccountEntity>) pageUtils.getList();
                    list1.forEach(li ->{
                        Map<String, Object> entity = new HashMap<>();
                        entity.put("sysid",li.getSysid());
                        entity.put("accountId",li.getAccountid());
                        entity.put("name",li.getName());
                        entity.put("mobile",li.getMobile());
                        entity.put("state","N");
                        entity.put("operate","Y");
                        entity.put("sort",li.getSort());
                        data.add(entity);
                    });
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("total",queryPage.getTotalCount());
                    hashMap.put("rows",data);
                    return hashMap;
                }
            }else{
                List<OmAccountEntity> list1 = (List<OmAccountEntity>) queryPage.getList();
                List<Map<String,Object>> data=new ArrayList<>();
                list1.forEach(li ->{
                    Map<String, Object> entity = new HashMap<>();
                    entity.put("sysid",li.getSysid());
                    entity.put("accountId",li.getAccountid());
                    entity.put("name",li.getName());
                    entity.put("mobile",li.getMobile());
                    entity.put("state","N");
                    entity.put("operate","Y");
                    entity.put("sort",li.getSort());
                    data.add(entity);
                });
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("total",queryPage.getTotalCount());
                hashMap.put("rows",data);
                return hashMap;
            }
        }else {
            List<OmAccountEntity> list1 = (List<OmAccountEntity>) queryPage.getList();
            List<Map<String,Object>> data=new ArrayList<>();
            list1.forEach(li ->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("sysid",li.getSysid());
                entity.put("accountId",li.getAccountid());
                entity.put("name",li.getName());
                entity.put("mobile",li.getMobile());
                entity.put("state","N");
                entity.put("operate","Y");
                entity.put("sort",li.getSort());
                data.add(entity);
            });
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("total",queryPage.getTotalCount());
            hashMap.put("rows",data);
            return hashMap;
        }
    }
    //获取子集
    private List<Module> getChild(String sysId,List<Module> list){
        List<Module> childs = new ArrayList<>();
        Iterator<Module> iterator = list.iterator();
        while (iterator.hasNext()){
            Module next = iterator.next();
            if(next.getParentId().equals(sysId)){
                List<Module> child = getChild(next.getId(), list);
                next.setChildren(child);
                childs.add(next);
            }
        }
        return childs;
    }
}