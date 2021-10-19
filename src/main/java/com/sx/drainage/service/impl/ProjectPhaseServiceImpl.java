package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectPhaseDao;
import com.sx.drainage.entity.ProjectPhaseEntity;
import com.sx.drainage.service.ProjectPhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/4
 * Time: 14:12
 */
@Service
@Transactional
@RequiredArgsConstructor //构造器注入 lombok注解
public class ProjectPhaseServiceImpl extends ServiceImpl<ProjectPhaseDao, ProjectPhaseEntity> implements ProjectPhaseService {

    /*
    * 项目阶段文件获取
    * */
    @Override
    public ProjectPhaseEntity getByProjectId(String sysId) {
        List<ProjectPhaseEntity> list = this.list(new QueryWrapper<ProjectPhaseEntity>().eq("projectId", sysId));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    /*
    * 删除
    * */
    @Override
    public void deleteProjectPhase(String sysId) {
        this.removeById(sysId);
    }
    /*
    * 添加数据
    * */
    @Override
    public void addPhase(String sysid) {
        ProjectPhaseEntity entity = new ProjectPhaseEntity();
        entity.setSysId(CreateUuid.uuid());
        entity.setProjectId(sysid);
        this.save(entity);
    }
    /*
    * 项目阶段文件添加
    * */
    @Override
    public void addProjectPhase(ProjectPhaseEntity entity) {
        ProjectPhaseEntity phaseEntity = this.getByProjectId(entity.getProjectId());
        if(phaseEntity!=null){
            entity.setSysId(phaseEntity.getSysId());
            this.updateById(entity);
        }else{
            entity.setSysId(CreateUuid.uuid());
            this.save(entity);
        }
    }
    /*
    * 获取所有项目阶段文件
    * */
    @Override
    public List<ProjectPhaseEntity> getAll() {
        return this.list();
    }
}
