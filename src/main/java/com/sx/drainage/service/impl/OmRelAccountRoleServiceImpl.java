package com.sx.drainage.service.impl;

import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.OmRelAccountRoleDao;
import com.sx.drainage.entity.OmRelAccountRoleEntity;
import com.sx.drainage.service.OmRelAccountRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;


@Service("omRelAccountRoleService")
@Transactional
public class OmRelAccountRoleServiceImpl extends ServiceImpl<OmRelAccountRoleDao, OmRelAccountRoleEntity> implements OmRelAccountRoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmRelAccountRoleEntity> page = this.page(
                new Query<OmRelAccountRoleEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> getAllRoleId(String accountId) {
        return baseMapper.getAllRoleId(accountId);
    }

    @Override
    public void deleteByUserId(String userId) {
        this.remove(new QueryWrapper<OmRelAccountRoleEntity>().eq("account_id",userId));
    }
    //获取角色所对应的所有用户Id
    @Override
    public List<String> getAllUserId(String sysId) {
        return baseMapper.getAllUserId(sysId);
    }
    //移除角色与用户关系
    @Override
    public void deleteRoleUser(String sysId, String userid) {
        this.remove(new QueryWrapper<OmRelAccountRoleEntity>().eq("account_id",userid).eq("role_id",sysId));
    }
    //新增角色与用户关系
    @Override
    public void insertRoleUser(String sysId, String userid) {
        OmRelAccountRoleEntity entity = new OmRelAccountRoleEntity();
        entity.setAccountId(userid);
        entity.setRoleId(sysId);
        this.save(entity);
    }

}