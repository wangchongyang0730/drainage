package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.OmTagRelPostRoleDao;
import com.sx.drainage.entity.OmTagRelPostRoleEntity;
import com.sx.drainage.service.OmTagRelPostRoleService;
import org.springframework.transaction.annotation.Transactional;


@Service("omTagRelPostRoleService")
@Transactional
public class OmTagRelPostRoleServiceImpl extends ServiceImpl<OmTagRelPostRoleDao, OmTagRelPostRoleEntity> implements OmTagRelPostRoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmTagRelPostRoleEntity> page = this.page(
                new Query<OmTagRelPostRoleEntity>().getPage(params),
                new QueryWrapper<OmTagRelPostRoleEntity>()
        );

        return new PageUtils(page);
    }
    //获取角色关联的所有岗位Id
    @Override
    public List<String> getAllPostId(String sysId) {
        return baseMapper.getAllPostId(sysId);
    }
    //获取岗位绑定的所有角色id
    @Override
    public List<String> getAllRoleId(String post_id,String projectId) {
        List<OmTagRelPostRoleEntity> list = this.list(new QueryWrapper<OmTagRelPostRoleEntity>().eq("del", 0).eq("post_id", post_id).eq("projectId",projectId));
        if(list.size()>0){
            List<String> list1 = new ArrayList<>();
            list.forEach(li ->{
                list1.add(li.getRoleId());
            });
            return list1;
        }
        return null;
    }
    //新增岗位与角色关系
    @Override
    public void insertPostionRole(String post_id, String role_id,String userId,String projectId) {
        OmTagRelPostRoleEntity entity = new OmTagRelPostRoleEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setRoleId(role_id);
        entity.setPostId(post_id);
        entity.setDel(0);
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setProjectId(projectId);
        this.save(entity);
    }
    //移除岗位与角色关系
    @Override
    public void updatePostionRole(String post_id, String role_id, String userId,String projectId) {
        OmTagRelPostRoleEntity entity = new OmTagRelPostRoleEntity();
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.update(entity,new QueryWrapper<OmTagRelPostRoleEntity>().eq("role_id",role_id).eq("post_id",post_id).eq("projectId",projectId));
    }

}