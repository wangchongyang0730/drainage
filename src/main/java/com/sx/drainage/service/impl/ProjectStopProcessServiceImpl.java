package com.sx.drainage.service.impl;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/3
 * Time: 11:07
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectStopProcessDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.ProjectStopProcessEntity;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.ProjectStopProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectStopProcessServiceImpl extends ServiceImpl<ProjectStopProcessDao, ProjectStopProcessEntity> implements ProjectStopProcessService {

    private final OmAccountService omAccountService;
    /*
    * 停用流程存档获取
    * */
    @Override
    public List<ProjectStopProcessEntity> getStopProcessArchive(String processType,String projectId) {
        return this.list(new QueryWrapper<ProjectStopProcessEntity>().eq("projectId",projectId).eq("processType",processType).eq("del",0));
    }
    /*
    * 停用流程存档
    * */
    @Override
    public void addStopProcessArchive(ProjectStopProcessEntity entity) {
        OmAccountEntity user = omAccountService.getUser(entity.getCreateUserId());
        if(user!=null){
            entity.setCreateUser(user.getName());
        }
        entity.setCreateTime(new Date());
        entity.setSysId(CreateUuid.uuid());
        entity.setDel(0);
        this.save(entity);
    }
    /*
    * 删除停用流程存档
    * */
    @Override
    public void deleteStopProcessArchive(String sysId) {
        ProjectStopProcessEntity entity = new ProjectStopProcessEntity();
        entity.setSysId(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }
    /*
    * 文档获取
    * */
    @Override
    public List<ProjectStopProcessEntity> getStopProcessFile(String processType) {
        return this.list(new QueryWrapper<ProjectStopProcessEntity>().eq("processType",processType).eq("del",0));
    }
}
