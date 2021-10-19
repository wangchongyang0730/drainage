package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectPipejackingYaxisDao;
import com.sx.drainage.entity.ProjectPipejackingYaxisEntity;
import com.sx.drainage.service.ProjectPipejackingYaxisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/30
 * Time: 10:08
 */
@Service
@Transactional
public class ProjectPipejackingYaxisServiceImpl extends ServiceImpl<ProjectPipejackingYaxisDao, ProjectPipejackingYaxisEntity> implements ProjectPipejackingYaxisService {

    /*
    * 设置Y轴
    * */
    @Override
    public void setYAxis(ProjectPipejackingYaxisEntity entity) {
        entity.setSysId(CreateUuid.uuid());
        this.save(entity);
    }
    /*
    * 获取Y轴
    * */
    @Override
    public List<ProjectPipejackingYaxisEntity> getYAxis(String initId, String projectId) {
        return this.list(new QueryWrapper<ProjectPipejackingYaxisEntity>().eq("initId",initId).eq("projectId",projectId));
    }
}
