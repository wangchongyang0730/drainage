package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectSupervisorManagementDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.ProjectSupervisorManagementEntity;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.ProjectSupervisorManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/18
 * Time: 9:47
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectSupervisorManagementServiceImpl extends ServiceImpl<ProjectSupervisorManagementDao, ProjectSupervisorManagementEntity> implements ProjectSupervisorManagementService {
    private final OmAccountService omAccountService;
    /*
     * 获取项目监理人员
     * */
    @Override
    public List<Map<String,Object>> getSupervisor(String projectId) {
        List<Map<String,Object>> data = new ArrayList<>();
        List<ProjectSupervisorManagementEntity> list = this.list(new QueryWrapper<ProjectSupervisorManagementEntity>().isNull("positionId"));
        List<ProjectSupervisorManagementEntity> list1 = this.list(new QueryWrapper<ProjectSupervisorManagementEntity>().eq("projectId", projectId).isNull("positionId"));
        if(list!=null&&list.size()>0){
            if(list1!=null&&list1.size()>0){
                list.addAll(list1);
            }
            list.forEach(l -> {
                ProjectSupervisorManagementEntity entity = this.getOne(new QueryWrapper<ProjectSupervisorManagementEntity>().eq("projectId", projectId).eq("positionId", l.getSysId()));
                if(entity!=null){
                    OmAccountEntity user = omAccountService.getUser(entity.getUserId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("sysId",l.getSysId());
                    map.put("positionName",l.getPositionName());
                    map.put("userName",user.getName());
                    map.put("phone",user.getMobile());
                    map.put("positionId",l.getSysId());
                    data.add(map);
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("sysId",l.getSysId());
                    map.put("positionName",l.getPositionName());
                    map.put("userName",null);
                    map.put("phone",null);
                    map.put("positionId",null);
                    data.add(map);
                }
            });
        }
        return data;
    }
    /*
     * 变更项目监理人员
     * */
    @Override
    public void updateSupervisor(ProjectSupervisorManagementEntity entity) {
        if(StringUtils.isEmpty(entity.getUserId())||entity.getUserId().equals("")||entity.getUserId().equals("''")){
            ProjectSupervisorManagementEntity entitys = this.getOne(new QueryWrapper<ProjectSupervisorManagementEntity>().eq("projectId", entity.getProjectId()).eq("positionId", entity.getPositionId()));
            if(entitys!=null){
                this.removeById(entitys.getSysId());
            }
        }else{
            ProjectSupervisorManagementEntity entitys = this.getOne(new QueryWrapper<ProjectSupervisorManagementEntity>().eq("projectId", entity.getProjectId()).eq("positionId", entity.getPositionId()));
            if(entitys!=null){
                entitys.setUserId(entity.getUserId());
                this.updateById(entitys);
            }else{
                entity.setSysId(CreateUuid.uuid());
                this.save(entity);
            }
        }
    }
    /*
     * 添加监理职位
     * */
    @Override
    public void addSupervisor(ProjectSupervisorManagementEntity entity) {
        entity.setSysId(CreateUuid.uuid());
        this.save(entity);
    }
    /*
    * 删除监理职位
    * */
    @Override
    public void deleteSupervisor(String sysId) {
        this.removeById(sysId);
        this.remove(new QueryWrapper<ProjectSupervisorManagementEntity>().eq("positionId",sysId));
    }
}
