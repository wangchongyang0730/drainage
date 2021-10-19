package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectInitPipejackingDao;
import com.sx.drainage.entity.ProjectInitPipejackingEntity;
import com.sx.drainage.service.ProjectInitPipejackingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 9:43
 */
@Service
@Transactional
public class ProjectInitPipejackingServiceImpl extends ServiceImpl<ProjectInitPipejackingDao, ProjectInitPipejackingEntity> implements ProjectInitPipejackingService {

    /*
    * 初始化顶管区间
    * */
    @Override
    public void initPipejacking(ProjectInitPipejackingEntity entity) {
        entity.setDel(0);
        entity.setSysId(CreateUuid.uuid());
        this.save(entity);
    }
    /*
    * 获取顶管区间
    * */
    @Override
    public List<ProjectInitPipejackingEntity> getPipejacking(String projectId) {
        return this.list(new QueryWrapper<ProjectInitPipejackingEntity>().eq("projectId",projectId).eq("del",0));
    }
    /*
    * 删除顶管区间
    * */
    @Override
    public void deletePipejacking(String sysId) {
        ProjectInitPipejackingEntity entity = new ProjectInitPipejackingEntity();
        entity.setSysId(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }
}
