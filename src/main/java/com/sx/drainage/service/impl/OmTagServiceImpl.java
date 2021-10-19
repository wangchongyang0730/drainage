package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.OmTagDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("omTagService")
@Transactional
public class OmTagServiceImpl extends ServiceImpl<OmTagDao, OmTagEntity> implements OmTagService {

    @Autowired
    private OmTagPostService omTagPostService;
    @Autowired
    private ProjectCompanyTagUserService projectCompanyTagUserService;
    @Autowired
    private OmAccountService omAccountService;
    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;
    @Autowired
    private ProjectCompanyService projectCompanyService;
    @Autowired
    private OmTagRelPostRoleService omTagRelPostRoleService;
    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private ProjectManagerPeopleService projectManagerPeopleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmTagEntity> page = this.page(
                new Query<OmTagEntity>().getPage(params),
                new QueryWrapper<OmTagEntity>()
        );

        return new PageUtils(page);
    }
    //获取组织列表
    @Override
    public List<Map<String, Object>> getOrganizationalList() {
        List<OmTagEntity> list = this.list(new QueryWrapper<OmTagEntity>().eq("del",0).orderByAsc("sort"));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("name",li.getName());
            map.put("remark",li.getRemark());
            map.put("createDate",li.getCreatedate());
            map.put("createUser",li.getCreateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("deleteDate",li.getDeletedate());
            map.put("deleteUser",li.getDeleteuser());
            map.put("del",li.getDel());
            map.put("parentId",li.getParentid());
            map.put("tagType",li.getTagtype());
            map.put("use_Flag",li.getUseFlag());
            map.put("sort",li.getSort());
            data.add(map);
        });
        return data;
    }
    //新增组织
    @Override
    public void insertOrganization(String name, String remark, String tagType, String user_flag, Integer sort) {
        OmTagEntity entity = new OmTagEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setRemark(remark);
        entity.setName(name);
        entity.setTagtype(tagType);
        entity.setUseFlag(user_flag);
        entity.setDel(0);
        if(sort!=null){
            entity.setSort(sort);
        }
        this.save(entity);
    }
    //修改组织
    @Override
    public void updateOrganization(String sysid, String name, String remark, String tagType, String user_flag, Integer sort) {
        OmTagEntity entity = new OmTagEntity();
        entity.setSysid(sysid);
        entity.setName(name);
        entity.setRemark(remark);
        entity.setTagtype(tagType);
        entity.setUseFlag(user_flag);
        if(sort!=null){
            entity.setSort(sort);
        }
        this.updateById(entity);
    }
    //删除组织
    @Override
    public void deleteOrganization(String sysid) {
        OmTagEntity entity = new OmTagEntity();
        entity.setSysid(sysid);
        entity.setDel(1);
        this.updateById(entity);
    }
    //获取该组织下的岗位列表
    @Override
    public Map<String, Object> getPositionsList(String tag_id, Integer page, Integer pageRecord) {
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        map.put("search",tag_id);
        PageUtils pageUtils = omTagPostService.queryPage(map);
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object> > list = new ArrayList<>();
        List<OmTagPostEntity> list1 = (List<OmTagPostEntity>) pageUtils.getList();
        list1.forEach(li->{
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("tag_id",li.getTagId());
            entity.put("name",li.getName());
            entity.put("enable",li.getEnable());
            entity.put("remark",li.getRemark());
            entity.put("createDate",li.getCreatedate());
            entity.put("createUser",li.getCreateuser());
            entity.put("updateDate",li.getUpdatedate());
            entity.put("updateUser",li.getUpdateuser());
            entity.put("deleteDate",li.getDeletedate());
            entity.put("deleteUser",li.getDeleteuser());
            entity.put("del",li.getDel());
            entity.put("deptId",li.getDeptid());
            entity.put("sort",li.getSort());
            list.add(entity);
        });
        data.put("rows",list);
        data.put("total",pageUtils.getTotalCount());
        return data;
    }
    //在该组织下新增岗位
    @Override
    public void inserPostion(String tag_id, String name, Integer enable, String remark, String sort) {
        omTagPostService.inserPostion(tag_id,name,enable,remark,sort);
    }
    //在该组织下修改岗位信息
    @Override
    public void updatePostion(String sysId, String name, Integer enable, String remark, String sort) {
        omTagPostService.updatePostion(sysId,name,enable,remark,sort);
    }
    //在该组织下删除岗位信息
    @Override
    public void deletePostion(String sysId) {
        omTagPostService.deletePostion(sysId);
    }
    //获取该项目设置组织下的岗位列表
    @Override
    public Map<String, Object> getProjectPositionsList(String tag_id, Integer page, Integer pageRecord, String projectId) {
        Map<String, Object> map = new HashMap<>();
        map.put("search",tag_id);
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        PageUtils pageUtils = omTagPostService.queryPage(map);
        String companyId=projectCompanyTagUserService.getProjectPositionsList(tag_id,projectId);
        Map<String, Object> hashMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        List<OmTagPostEntity> data = (List<OmTagPostEntity>) pageUtils.getList();
        if(companyId!=null){
            List<OmAccountEntity> user=omAccountService.getAllUserByCompanyId(companyId);
            data.forEach(da->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("postAccount",getPostHaveName(user,da.getSysid(),projectId));
                entity.put("sysId",da.getSysid());
                entity.put("tag_id",da.getTagId());
                entity.put("name",da.getName());
                entity.put("enable",da.getEnable());
                entity.put("remark",da.getRemark());
                entity.put("createDate",da.getCreatedate());
                entity.put("createUser",da.getCreateuser());
                entity.put("updateDate",da.getUpdatedate());
                entity.put("updateUser",da.getUpdateuser());
                entity.put("deleteDate",da.getDeletedate());
                entity.put("deleteUser",da.getDeleteuser());
                entity.put("del",da.getDel());
                entity.put("deptId",da.getDeptid());
                entity.put("sort",da.getSort());
                list.add(entity);
            });
        }else{
            data.forEach(da->{
                Map<String, Object> entity = new HashMap<>();
                entity.put("postAccount",null);
                entity.put("sysId",da.getSysid());
                entity.put("tag_id",da.getTagId());
                entity.put("name",da.getName());
                entity.put("enable",da.getEnable());
                entity.put("remark",da.getRemark());
                entity.put("createDate",da.getCreatedate());
                entity.put("createUser",da.getCreateuser());
                entity.put("updateDate",da.getUpdatedate());
                entity.put("updateUser",da.getUpdateuser());
                entity.put("deleteDate",da.getDeletedate());
                entity.put("deleteUser",da.getDeleteuser());
                entity.put("del",da.getDel());
                entity.put("deptId",da.getDeptid());
                entity.put("sort",da.getSort());
                list.add(entity);
            });
        }
        hashMap.put("total",pageUtils.getTotalCount());
        hashMap.put("rows",list);
        return hashMap;
    }
    //获取登录管理员所在的参建单位人员信息列表
    @Override
    public Map<String, Object> getPostionUsersList(String post_id, String where, Integer page, Integer pageRecord, String projectId) {
        log.error("页数为："+page);
        String tagId=omTagPostService.getTagId(post_id);
        String companyId=projectCompanyTagUserService.getProjectPositionsList(tagId,projectId);
        Map<String, Object> hashMap = new HashMap<>();
        log.error("单位id"+companyId);
        if(companyId==null){
            hashMap.put("total",null);
            hashMap.put("rows",null);
            return hashMap;
        }
        ProjectCompanyEntity company = projectCompanyService.getCompany(companyId);
        String companyName=company.getName();
        Map<String, Object> parms = new HashMap<>();
        if (where.equals("%22%22")||where.equals("''") || StringUtils.isEmpty(where)) {
            parms.put("search", null);
        } else {
            parms.put("search", where);
        }
        parms.put("id",companyId);
        parms.put("page",page.toString());
        parms.put("limit",pageRecord.toString());
        List<String> userId=omTagRelPostAccountService.getPostHaveUser(post_id,projectId);
        PageUtils user=omAccountService.getPageByCompanyId(parms);
        List<Map<String,Object>> data=new ArrayList<>();
        if(userId!=null){
            Map<String, Object> map = new HashMap<>();
            if (where.equals("%22%22")||where.equals("''") || StringUtils.isEmpty(where)) {
                map.put("search", null);
            } else {
                map.put("search", where);
            }
            map.put("id",companyId);
            map.put("page",page.toString());
            map.put("limit",pageRecord.toString());
            PageUtils users=omAccountService.getBySearch(map,userId,companyId);
            if(users.getTotalCount()>(page-1)*pageRecord){
                if(users.getTotalCount()-(page-1)*pageRecord>=pageRecord){
                    for(int i=0;i<pageRecord;i++){
                        List<OmAccountEntity> list = (List<OmAccountEntity>) users.getList();
                        Map<String,Object> entity=new HashMap<>();
                        log.error("用户id为："+(list.get((page-1)*pageRecord+i)));
                        entity.put("fk_id",omTagRelPostAccountService.getFkId(list.get((page-1)*pageRecord+i).getSysid(),post_id,projectId));
                        entity.put("projectId","");
                        entity.put("name",list.get((page-1)*pageRecord+i).getName());
                        entity.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        entity.put("state","Y");
                        entity.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        entity.put("companyName","");
                        data.add(entity);
                    }
                }else{
                    for(int i=0;i<(users.getTotalCount()-(page-1)*pageRecord);i++){
                        List<OmAccountEntity> list = (List<OmAccountEntity>) users.getList();
                        Map<String,Object> entity=new HashMap<>();
                        log.error("用户id为："+(list.get((page-1)*pageRecord+i)));
                        entity.put("fk_id",omTagRelPostAccountService.getFkId(list.get((page-1)*pageRecord+i).getSysid(),post_id,projectId));
                        entity.put("projectId","");
                        entity.put("name",list.get((page-1)*pageRecord+i).getName());
                        entity.put("mobile",list.get((page-1)*pageRecord+i).getMobile());
                        entity.put("state","Y");
                        entity.put("accountId",list.get((page-1)*pageRecord+i).getAccountid());
                        entity.put("companyName",companyName);
                        data.add(entity);
                    }
                    Map<String, Object> maps = new HashMap<>();
                    if (where.equals("%22%22")||where.equals("''") || StringUtils.isEmpty(where)) {
                        maps.put("search", null);
                    } else {
                        maps.put("search", where);
                    }
                    maps.put("id",companyId);
                    maps.put("page","1");
                    Integer newLimit=pageRecord-(users.getTotalCount()-(page-1)*pageRecord);
                    log.error("id为："+companyId);
                    log.error("page为：1");
                    maps.put("limit",newLimit.toString());
                    log.error("limit为："+newLimit.toString());
                    PageUtils utils = omAccountService.getPageByCompanyIdDis(maps,userId);
                    List<OmAccountEntity> list = (List<OmAccountEntity>) utils.getList();
                    log.error("去除用户的长度："+list.size());
                    log.error("拥有的长度："+userId);
                    list.forEach(li ->{
                        Map<String,Object> entity=new HashMap<>();
                        entity.put("fk_id",li.getSysid());
                        entity.put("projectId","");
                        entity.put("name",li.getName());
                        entity.put("mobile",li.getMobile());
                        entity.put("state","N");
                        entity.put("accountId",li.getAccountid());
                        entity.put("companyName",companyName);
                        data.add(entity);
                    });
                }
            }else{
                int pages=users.getTotalCount()/pageRecord;
                int limits=users.getTotalCount()%pageRecord;
                if(limits!=0){
                    pages=pages+1;
                }
                Map<String, Object> mapr = new HashMap<>();
                if (where.equals("%22%22")||where.equals("''") || StringUtils.isEmpty(where)) {
                    mapr.put("search", null);
                } else {
                    mapr.put("search", where);
                }
                mapr.put("id",companyId);
                mapr.put("page","1");
                Integer newLimit=pageRecord-limits;
                log.error("id为："+companyId);
                log.error("page为：1");
                mapr.put("limit",newLimit.toString());
                log.error("limit为："+newLimit.toString());
                PageUtils utilr = omAccountService.getPageByCompanyIdDis(mapr,userId);
                List<OmAccountEntity> utilrList = (List<OmAccountEntity>) utilr.getList();
                utilrList.forEach(l -> {
                    userId.add(l.getSysid());
                });
                page=page-pages;
                Map<String, Object> maps = new HashMap<>();
                if (where.equals("%22%22")||where.equals("''") || StringUtils.isEmpty(where)) {
                    maps.put("search", null);
                } else {
                    maps.put("search", where);
                }
                maps.put("id",companyId);
                maps.put("page",page.toString());
                log.error("id为："+companyId);
                log.error("page为："+page.toString());
                maps.put("limit",pageRecord.toString());
                PageUtils utils = omAccountService.getPageByCompanyIdDis(maps,userId);
                List<OmAccountEntity> list = (List<OmAccountEntity>) utils.getList();
                list.forEach(li ->{
                    Map<String,Object> entity=new HashMap<>();
                    entity.put("fk_id",li.getSysid());
                    entity.put("projectId","");
                    entity.put("name",li.getName());
                    entity.put("mobile",li.getMobile());
                    entity.put("state","N");
                    entity.put("accountId",li.getAccountid());
                    entity.put("companyName",companyName);
                    data.add(entity);
                });

            }
        }else{
            List<OmAccountEntity> list = (List<OmAccountEntity>) user.getList();
            list.forEach(li ->{
                Map<String,Object> entity=new HashMap<>();
                entity.put("fk_id",li.getSysid());
                entity.put("projectId","");
                entity.put("name",li.getName());
                entity.put("mobile",li.getMobile());
                entity.put("state","N");
                entity.put("accountId",li.getAccountid());
                entity.put("companyName",companyName);
                data.add(entity);
            });

        }
        hashMap.put("total",user.getTotalCount());
        hashMap.put("rows",data);
        return hashMap;
    }
    //新增岗位人员
    @Override
    public void insertPostionUser(String post_id, String fk_id, String projectId,String userId) {
        omTagRelPostAccountService.insertPostionUser(post_id,fk_id,projectId,userId);
        List<String> roleId = omTagRelPostRoleService.getAllRoleId(post_id, projectId);
        List<String> id = omRelAccountRoleService.getAllRoleId(fk_id);
       if(roleId!=null&&roleId.size()>0){
           roleId.forEach(r -> {
               if(!id.contains(r)){
                   omRelAccountRoleService.insertRoleUser(r,fk_id);
               }
           });
       }
    }
    //移除岗位人员
    @Override
    public void updatePostionUser(String post_id, String fk_id, String projectId, String userId) {
        omTagRelPostAccountService.updatePostionUser(post_id,fk_id,projectId,userId);
    }
    //获取该岗位角色关系列表
    @Override
    public List<Map<String, Object>> getPostionRoleList(String post_id,String projectId) {
        List<String> roleId=omTagRelPostRoleService.getAllRoleId(post_id,projectId);
        List<OmRoleEntity> role=omRoleService.getAllRole();
        if(roleId!=null){
            List<Map<String, Object>> data =new ArrayList<>();
            for (OmRoleEntity re:role) {
                boolean res=false;
                for(String id:roleId){
                    if(re.getSysid().equals(id)){
                        Map<String, Object> map = new HashMap<>();
                        map.put("sysId",re.getSysid());
                        map.put("valid",re.getValid());
                        map.put("parent_id",re.getParentId());
                        map.put("fullName",re.getFullname());
                        map.put("description",re.getDescription());
                        map.put("name",re.getName());
                        map.put("state","Y");
                        data.add(map);
                        res=true;
                    }
                }
                if(!res){
                    if(!re.getName().equals("超级管理员")){
                        Map<String, Object> map = new HashMap<>();
                        map.put("sysId",re.getSysid());
                        map.put("valid",re.getValid());
                        map.put("parent_id",re.getParentId());
                        map.put("fullName",re.getFullname());
                        map.put("description",re.getDescription());
                        map.put("name",re.getName());
                        map.put("state","N");
                        data.add(map);
                    }
                }
            }
//            role.forEach(re ->{
//                boolean res=false;
//                roleId.forEach(id ->{
//                    if(re.getSysid().equals(id)){
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("sysId",re.getSysid());
//                        map.put("valid",re.getValid());
//                        map.put("parent_id",re.getParentId());
//                        map.put("fullName",re.getFullname());
//                        map.put("description",re.getDescription());
//                        map.put("name",re.getName());
//                        map.put("state","Y");
//                        data.add(map);
//                    }else{
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("sysId",re.getSysid());
//                        map.put("valid",re.getValid());
//                        map.put("parent_id",re.getParentId());
//                        map.put("fullName",re.getFullname());
//                        map.put("description",re.getDescription());
//                        map.put("name",re.getName());
//                        map.put("state","N");
//                        data.add(map);
//                    }
//                });
//            });
            return data;
        }else{
            List<Map<String, Object>> data =new ArrayList<>();
            role.forEach(re ->{
                if(!re.getName().equals("超级管理员")){
                    Map<String, Object> map = new HashMap<>();
                    map.put("sysId",re.getSysid());
                    map.put("valid",re.getValid());
                    map.put("parent_id",re.getParentId());
                    map.put("fullName",re.getFullname());
                    map.put("description",re.getDescription());
                    map.put("name",re.getName());
                    map.put("state","N");
                    data.add(map);
                }
            });
            return data;
        }
    }
    //新增岗位与角色关系
    @Override
    public void insertPostionRole(String post_id, String role_id,String userId,String projectId) {
        omTagRelPostRoleService.insertPostionRole(post_id,role_id,userId,projectId);
        List<String> user = omTagRelPostAccountService.getPostHaveUser(post_id, projectId);
        if(user!=null&&user.size()>0){
            user.forEach(u -> {
                List<String> list = omRelAccountRoleService.getAllRoleId(u);
                if(!list.contains(role_id)){
                    omRelAccountRoleService.insertRoleUser(role_id,u);
                }
            });
        }
    }
    //移除岗位与角色关系
    @Override
    public void updatePostionRole(String post_id, String role_id, String userId,String projectId) {
        omTagRelPostRoleService.updatePostionRole(post_id,role_id,userId,projectId);
    }
    //获取所有参建单位类型
    @Override
    public List<OmTagEntity> getAllTag() {
        return this.list(new QueryWrapper<OmTagEntity>().eq("del",0).orderByAsc("sort"));
    }
    //根据单位名称获取单位
    @Override
    public OmTagEntity getTagByName(String substring) {
        List<OmTagEntity> list = this.list(new QueryWrapper<OmTagEntity>().eq("name", substring).eq("del", 0));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    //获取总监理工程师
    @Override
    public List<Map<String, Object>> getSupervisoryEngineer(String projectId, String tagName, String postName) {
        OmTagEntity tag = this.getTagByName(tagName);
        if(tag==null){
            return null;
        }
        return omTagRelPostAccountService.getSupervisoryEngineer(tag.getSysid(),projectId,postName);
    }
    //修改项目分管领导
    @Override
    public boolean updateLeadersInCharge(String projectId, String userId, String user, String managerId, String department) {
        OmTagPostEntity postEntity = omTagPostService.getPostNameByName("分管领导");
        OmAccountEntity user1 = omAccountService.getUser(userId);
        OmAccountEntity user2 = omAccountService.getUser(managerId);
        if(user1!=null&&user2!=null){
            if(postEntity!=null){
                omTagRelPostAccountService.updateLeadersInCharge(postEntity.getSysid(),projectId,userId,user);
                List<String> roleId = omTagRelPostRoleService.getAllRoleId(postEntity.getSysid(), projectId);
                List<String> id = omRelAccountRoleService.getAllRoleId(userId);
                if(roleId!=null&&roleId.size()>0){
                    roleId.forEach(r -> {
                        if(!id.contains(r)){
                            omRelAccountRoleService.insertRoleUser(r,userId);
                        }
                    });
                }
            }
            OmTagPostEntity postEntity1 = omTagPostService.getPostNameByName("项目经理");
            if(postEntity1!=null){
                omTagRelPostAccountService.updateLeadersInCharge(postEntity1.getSysid(),projectId,managerId,user);
                List<String> roleId = omTagRelPostRoleService.getAllRoleId(postEntity1.getSysid(), projectId);
                List<String> id = omRelAccountRoleService.getAllRoleId(managerId);
                if(roleId!=null&&roleId.size()>0){
                    roleId.forEach(r -> {
                        if(!id.contains(r)){
                            omRelAccountRoleService.insertRoleUser(r,managerId);
                        }
                    });
                }
            }
            projectManagerPeopleService.updateManagerPeople(projectId,user1.getName(),user2.getName(),department);
            return true;
        }
        else{
            return false;
        }
    }


    //获取岗位拥有员工名称
    private String getPostHaveName(List<OmAccountEntity> list,String postId,String projectId){
        log.error("postId"+postId);
        List<String> userId=omTagRelPostAccountService.getPostHaveUser(postId,projectId);
        if(userId==null||userId.size()==0){
            return null;
        }
        String name=null;
        int j=0;
        log.error("list长度---------"+list.size()+"///////"+list);
        log.error("userId长度--------"+userId.size()+"//////"+userId);
        for(int i=0;i<list.size();i++){
            if(userId.contains(list.get(i).getSysid())){
                log.error("员工名字----------"+list.get(i).getName());
                if(j==0){
                    name= list.get(i).getName();
                    j++;
                }else{
                    name+= (","+list.get(i).getName());
                }
            }
        }
        return name;
    }

}