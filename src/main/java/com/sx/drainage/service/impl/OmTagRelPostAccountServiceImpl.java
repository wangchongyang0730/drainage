package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.OmTagRelPostAccountDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.OmTagPostEntity;
import com.sx.drainage.entity.OmTagRelPostAccountEntity;
import com.sx.drainage.entity.ProjectCompanyEntity;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.OmTagPostService;
import com.sx.drainage.service.OmTagRelPostAccountService;
import com.sx.drainage.service.ProjectCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service("omTagRelPostAccountService")
@Transactional
public class OmTagRelPostAccountServiceImpl extends ServiceImpl<OmTagRelPostAccountDao, OmTagRelPostAccountEntity> implements OmTagRelPostAccountService {

    @Autowired
    private OmTagPostService omTagPostService;
    @Autowired
    private OmAccountService omAccountService;
    @Autowired
    private ProjectCompanyService projectCompanyService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmTagRelPostAccountEntity> page = this.page(
                new Query<OmTagRelPostAccountEntity>().getPage(params),
                new QueryWrapper<OmTagRelPostAccountEntity>()
        );

        return new PageUtils(page);
    }
    //获取用户所关联的项目id
    @Override
    public List<String> getAllProjectId(String userId) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("account_id", userId).eq("del",0));
        List<String> projectId = new ArrayList<>();
        list.forEach(li ->{
            projectId.add(li.getProjectid());
        });
        return projectId;
    }
    //获取用户所在岗位
    @Override
    public String getPostName(String sysid) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("account_id", sysid));
        if(list.size()>0){
            String postName = omTagPostService.getPostName(list.get(0).getPostId());
            return postName;
        }
        return null;
    }
    //获取用户所在岗位和id
    @Override
    public Map<String,Object> getPostNameAndId(String sysid) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("account_id", sysid));
        if(list.size()>0){
            return omTagPostService.getPostNameAndId(list.get(0).getPostId());
        }
        return null;
    }
    //获取岗位下所有用户id
    @Override
    public List<String> getPostHaveUser(String postId, String projectId) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("del", 0).eq("post_id", postId).eq("projectId", projectId));
        if(list.size()>0){
            List<String> data = new ArrayList<>();
            list.forEach(li ->{
                data.add(li.getAccountId());
            });
            return data;
        }
        return null;
    }
    //获取fkid
    @Override
    public String getFkId(String sysid, String tag_id,String projectId) {
        return baseMapper.getFkId(sysid,tag_id,projectId);
    }
    //新增岗位人员
    @Override
    public void insertPostionUser(String post_id, String fk_id, String projectId, String userId) {
        OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
        entity.setFkId(CreateUuid.uuid());
        entity.setAccountId(fk_id);
        entity.setPostId(post_id);
        entity.setProjectid(projectId);
        entity.setCreateuser(userId);
        entity.setDel(0);
        this.save(entity);
    }
    //移除岗位人员
    @Override
    public void updatePostionUser(String post_id, String fk_id, String projectId, String userId) {
        OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
        entity.setFkId(fk_id);
        entity.setDeleteuser(userId);
        entity.setDel(1);
        this.updateById(entity);
    }
    //设置总监理工程师
    @Override
    public void insertOne(String sysid, String userId, String projectId, String createUserId) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("del", 0).eq("projectId", projectId).eq("post_id", sysid));
        if(list!=null&&list.size()>0){
            OmTagRelPostAccountEntity entity = list.get(0);
            entity.setAccountId(userId);
            entity.setUpdatedate(new Date());
            entity.setUpdateuser(createUserId);
            this.updateById(entity);
        }else{
            OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
            entity.setFkId(CreateUuid.uuid());
            entity.setPostId(sysid);
            entity.setAccountId(userId);
            entity.setCreatedate(new Date());
            entity.setCreateuser(createUserId);
            entity.setProjectid(projectId);
            entity.setDel(0);
            this.save(entity);
        }
    }
    //获取总监理工程师
    @Override
    public List<Map<String, Object>> getSupervisoryEngineer(String tagId, String projectId, String postName) {
        OmTagPostEntity entity = omTagPostService.getPostNameByTagIdAndName(tagId, postName);
        if(entity!=null){
            List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("del", 0).eq("projectId", projectId).eq("post_id", entity.getSysid()));
            if(list!=null&&list.size()>0){
                List<Map<String, Object>> data = new ArrayList<>();
                list.forEach(l -> {
                    OmAccountEntity user = omAccountService.getUser(l.getAccountId());
                    if(user!=null){
                        Map<String, Object> map = new HashMap<>();
                        map.put("sysId",user.getSysid());
                        map.put("name",user.getName());
                        ProjectCompanyEntity company = projectCompanyService.getCompany(user.getCompanyId());
                        map.put("company",company==null?null:company.getName());
                        data.add(map);
                    }
                });
                return data;
            }
        }
        return null;
    }
    //修改只能拥有一个人员的岗位
    @Override
    public void updateLeadersInCharge(String sysid, String projectId, String userId, String user) {
        List<OmTagRelPostAccountEntity> list = this.list(new QueryWrapper<OmTagRelPostAccountEntity>().eq("del", 0).eq("projectId", projectId).eq("post_id", sysid));
        if(list!=null&&list.size()>0){
            if(list.size()!=1){
                list.forEach(l -> {
                    this.removeById(l.getFkId());
                });
                OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
                entity.setFkId(CreateUuid.uuid());
                entity.setDel(0);
                entity.setPostId(sysid);
                entity.setProjectid(projectId);
                entity.setCreatedate(new Date());
                entity.setCreateuser(user);
                entity.setAccountId(userId);
                this.save(entity);
            }else{
                OmTagRelPostAccountEntity entity = list.get(0);
                entity.setAccountId(userId);
                entity.setUpdateuser(user);
                entity.setUpdatedate(new Date());
                this.updateById(entity);
            }
        }else{
            OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
            entity.setFkId(CreateUuid.uuid());
            entity.setDel(0);
            entity.setPostId(sysid);
            entity.setProjectid(projectId);
            entity.setCreatedate(new Date());
            entity.setCreateuser(user);
            entity.setAccountId(userId);
            this.save(entity);
        }
    }
    //修改参建单位时移除岗位下对应用户
    @Override
    public void removeJoinUser(String tagid, String projectId) {
        List<String> id = omTagPostService.removeJoinUser(tagid, projectId);
        OmTagRelPostAccountEntity entity = new OmTagRelPostAccountEntity();
        entity.setDel(1);
        this.update(entity,new QueryWrapper<OmTagRelPostAccountEntity>().in("post_id",id).eq("projectId",projectId));
    }

}