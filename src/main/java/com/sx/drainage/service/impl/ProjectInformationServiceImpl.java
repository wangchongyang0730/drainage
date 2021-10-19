package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectInformationDao;
import com.sx.drainage.entity.ProjectInformationEntity;
import com.sx.drainage.service.ProjectInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 15:49
 */
@Service
@Transactional
public class ProjectInformationServiceImpl extends ServiceImpl<ProjectInformationDao, ProjectInformationEntity> implements ProjectInformationService {

    /*
     * 资料补充
     * */
    @Override
    public void additional(ProjectInformationEntity projectInformationEntity) {
        List<ProjectInformationEntity> list = this.list(new QueryWrapper<ProjectInformationEntity>().eq("projectId", projectInformationEntity.getProjectId()));
        if(list!=null&&list.size()>0){
            projectInformationEntity.setSysId(list.get(0).getSysId());
            this.updateById(projectInformationEntity);
        }else{
            projectInformationEntity.setSysId(CreateUuid.uuid());
            this.save(projectInformationEntity);
        }
    }
    /*
    * 获取项目资料
    * */
    @Override
    public ProjectInformationEntity getByProjectId(String projectId) {
        List<ProjectInformationEntity> list = this.list(new QueryWrapper<ProjectInformationEntity>().eq("projectId", projectId));
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    /*
    * 获取所有项目资料
    * */
    @Override
    public List<ProjectInformationEntity> geAll() {
        return this.list();
    }
}
