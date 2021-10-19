package com.sx.drainage.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectWbsbindgroupDao;
import com.sx.drainage.entity.ProjectWbsbindgroupEntity;
import com.sx.drainage.service.ProjectWbsbindgroupService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectWbsbindgroupService")
@Transactional
public class ProjectWbsbindgroupServiceImpl extends ServiceImpl<ProjectWbsbindgroupDao, ProjectWbsbindgroupEntity> implements ProjectWbsbindgroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsbindgroupEntity> page = this.page(
                new Query<ProjectWbsbindgroupEntity>().getPage(params),
                new QueryWrapper<ProjectWbsbindgroupEntity>()
        );

        return new PageUtils(page);
    }
    //删除wbs
    @Override
    public void deleteWbs(String sysid) {
        this.remove(new QueryWrapper<ProjectWbsbindgroupEntity>().eq("WbsId",sysid));
    }

    @Override
    public ProjectWbsbindgroupEntity getWbsBind(String wbsId) {
        List<ProjectWbsbindgroupEntity> list = this.list(new QueryWrapper<ProjectWbsbindgroupEntity>().eq("WbsId", wbsId));
        if(list!=null&& list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
    //获取所有已绑定的
    @Override
    public String[] getNotWbsId(String wbsId) {
        List<ProjectWbsbindgroupEntity> list = this.list(new QueryWrapper<ProjectWbsbindgroupEntity>().ne("WbsId", wbsId));
        String treelist=null;
        for (int i = 0; i < list.size(); i++)
        {
            if (i==0)
            {
                treelist= list.get(i).getConstructionid();
            }
            else
            {
                treelist += "," +list.get(i).getConstructionid();
            }
        }
        return treelist.split(",");
    }
    //添加
    @Override
    public void addWbsBind(ProjectWbsbindgroupEntity binds) {
        baseMapper.insert(binds);
    }
    //修改
    @Override
    public void updateWbsBind(ProjectWbsbindgroupEntity wbsBind) {
        this.updateById(wbsBind);
    }
    //解除wbs与树节点关系
    @Override
    public void deleteBindWbsGroup(String wbsId) {
        this.remove(new QueryWrapper<ProjectWbsbindgroupEntity>().eq("WbsId",wbsId));
    }
    //根据构建id获取wbsId
    @Override
    public String getWbsId(String id) {
        List<ProjectWbsbindgroupEntity> list = this.list(new QueryWrapper<ProjectWbsbindgroupEntity>().like("ConstructionId", id));
        if(list!=null&&list.size()>0){
            return list.get(0).getWbsid();
        }
        return null;
    }

}