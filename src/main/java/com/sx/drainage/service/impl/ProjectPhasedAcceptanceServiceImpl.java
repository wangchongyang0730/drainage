package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectPhasedAcceptanceDao;
import com.sx.drainage.entity.ProjectPhasedAcceptanceEntity;
import com.sx.drainage.service.ProjectPhasedAcceptanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/16
 * Time: 13:50
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectPhasedAcceptanceServiceImpl extends ServiceImpl<ProjectPhasedAcceptanceDao, ProjectPhasedAcceptanceEntity> implements ProjectPhasedAcceptanceService {
    /*
    * 获取阶段性验收文件
    * */
    @Override
    public List<ProjectPhasedAcceptanceEntity> getAll(String projectId) {
        return this.list(new QueryWrapper<ProjectPhasedAcceptanceEntity>().eq("projectId",projectId));
    }
    /*
    * 添加阶段性验收文件
    * */
    @Override
    public void addPhasedAcceptanceFile(ProjectPhasedAcceptanceEntity projectPhasedAcceptanceEntity) {
        /*if(StringUtils.isEmpty(projectPhasedAcceptanceEntity.getSysId())){
            projectPhasedAcceptanceEntity.setSysId(CreateUuid.uuid());
            this.save(projectPhasedAcceptanceEntity);
        }else{
            this.updateById(projectPhasedAcceptanceEntity);
        }*/
        List<ProjectPhasedAcceptanceEntity> list = this.list(new QueryWrapper<ProjectPhasedAcceptanceEntity>().eq("projectId", projectPhasedAcceptanceEntity.getProjectId()));
        if(list!=null&&list.size()>0){
            projectPhasedAcceptanceEntity.setSysId(list.get(0).getSysId());
            this.updateById(projectPhasedAcceptanceEntity);
        }else{
            projectPhasedAcceptanceEntity.setSysId(CreateUuid.uuid());
            this.save(projectPhasedAcceptanceEntity);
        }
    }
    /*
    * 获取所有阶段性验收文件
    * */
    @Override
    public List<ProjectPhasedAcceptanceEntity> getAllFile() {
        return this.list();
    }
}
