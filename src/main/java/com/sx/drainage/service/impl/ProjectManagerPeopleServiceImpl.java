package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectManagerPeopleDao;
import com.sx.drainage.entity.ProjectManagerPeopleEntity;
import com.sx.drainage.service.ProjectManagerPeopleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/17
 * Time: 16:05
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectManagerPeopleServiceImpl extends ServiceImpl<ProjectManagerPeopleDao, ProjectManagerPeopleEntity> implements ProjectManagerPeopleService {
    /*
     * 获取项目管理人员
     * */
    @Override
    public ProjectManagerPeopleEntity getManagerPeople(String projectId) {
        return this.getOne(new QueryWrapper<ProjectManagerPeopleEntity>().eq("projectId",projectId));
    }
    /*
     * 修改项目管理人员
     * */
    @Override
    public void updateManagerPeople(String projectId, String userId, String managerId, String department) {
        log.error("进来了----------------");
        List<ProjectManagerPeopleEntity> list = this.list(new QueryWrapper<ProjectManagerPeopleEntity>().eq("projectId", projectId));
        if(list!=null&&list.size()>0){
            if(list.size()!=1){
                list.forEach(l -> {
                    this.removeById(l.getSysId());
                });
                ProjectManagerPeopleEntity entity = new ProjectManagerPeopleEntity();
                entity.setSysId(CreateUuid.uuid());
                entity.setDepartment(department);
                entity.setLeadersInCharge(userId);
                entity.setProjectManager(managerId);
                entity.setProjectId(projectId);
                this.save(entity);
            }else{
                ProjectManagerPeopleEntity entity = list.get(0);
                entity.setProjectManager(managerId);
                entity.setLeadersInCharge(userId);
                entity.setDepartment(department);
                this.updateById(entity);
            }
        }else{
            ProjectManagerPeopleEntity entity = new ProjectManagerPeopleEntity();
            entity.setSysId(CreateUuid.uuid());
            entity.setDepartment(department);
            entity.setLeadersInCharge(userId);
            entity.setProjectManager(managerId);
            entity.setProjectId(projectId);
            this.save(entity);
        }
    }
}
