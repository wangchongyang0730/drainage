package com.sx.drainage.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.OmRelRoleModuleDao;
import com.sx.drainage.entity.OmRelRoleModuleEntity;
import com.sx.drainage.service.OmRelRoleModuleService;
import org.springframework.transaction.annotation.Transactional;


@Service("omRelRoleModuleService")
@Transactional
public class OmRelRoleModuleServiceImpl extends ServiceImpl<OmRelRoleModuleDao, OmRelRoleModuleEntity> implements OmRelRoleModuleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmRelRoleModuleEntity> page = this.page(
                new Query<OmRelRoleModuleEntity>().getPage(params),
                new QueryWrapper<OmRelRoleModuleEntity>()
        );

        return new PageUtils(page);
    }
    //获取所有角色关联的模块Id
    @Override
    public List<String> getAllModuleId(String sysId) {
        return baseMapper.getAllModuleId(sysId);
    }
    //保存角色与功能关系
    @Override
    public void saveRoleFunction(List<OmRelRoleModuleEntity> list) {
        if(list!=null){
            this.remove(new QueryWrapper<OmRelRoleModuleEntity>().eq("role_id",list.get(0).getRoleId()));
            this.saveBatch(list);
        }
    }
    //获取所有模块id
    @Override
    public List<String> getModuleByRoleIds(List<String> roleId) {
        List<OmRelRoleModuleEntity> list = this.list(new QueryWrapper<OmRelRoleModuleEntity>().in("role_id", roleId));
        List<String> res = new ArrayList<>();
        list.forEach(li ->{
            res.add(li.getModuleId());
        });
        return res;
    }

}