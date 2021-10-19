package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.dao.ProjectParentProjectDao;
import com.sx.drainage.entity.ProjectInformationEntity;
import com.sx.drainage.entity.ProjectParentProjectEntity;
import com.sx.drainage.entity.ProjectProjectEntity;
import com.sx.drainage.params.ProjectParams;
import com.sx.drainage.service.ProjectInformationService;
import com.sx.drainage.service.ProjectParentProjectService;
import com.sx.drainage.service.ProjectProjectService;
import com.sx.drainage.service.ProjectProjectbasicinfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 13:42
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectParentProjectServiceImpl extends ServiceImpl<ProjectParentProjectDao, ProjectParentProjectEntity> implements ProjectParentProjectService {
    private final ProjectProjectService projectProjectService;
    private final ProjectInformationService projectInformationService;
    private final ProjectProjectbasicinfoService projectProjectbasicinfoService;
    /*
    * 获取所有非段项目
    * */
    @Override
    public List<ProjectParentProjectEntity> getAll() {
        return this.list(new QueryWrapper<ProjectParentProjectEntity>().eq("del",0));
    }
    /*
    * 添加非标段项目
    * */
    @Override
    public void add(String fileId, String name, String projectDescribe, String userId,String type, Boolean isBlock) {
        ProjectParentProjectEntity entity = new ProjectParentProjectEntity();
        entity.setSysId(CreateUuid.uuid());
        entity.setName(name);
        entity.setProjectDescribe(projectDescribe);
        entity.setDel(0);
        entity.setCreateDate(new Date());
        entity.setCreateUser(userId);
        entity.setType(type);
        entity.setFileId(fileId);
        this.save(entity);
        if(isBlock){
            ProjectParams params = new ProjectParams();
            params.setParentId(entity.getSysId());
            params.setName(name);
            params.setFileId(fileId);
            String s = projectProjectService.postProject(params, userId);
            //projectProjectbasicinfoService.putByProjectId(s,projectDescribe);
        }
    }
    /*
    * 非标段项目详情
    * */
    @Override
    public R getDetails(String projectId) {
        ProjectParentProjectEntity entity = this.getById(projectId);
        ProjectInformationEntity files=projectInformationService.getByProjectId(projectId);
        Map<String, Object> map = new HashMap<>();
        map.put("projectName",entity==null?null:entity.getName());
        if(files!=null){
            map.put("projectInformation",files);
        }else{
            map.put("projectInformation",new ProjectInformationEntity());
        }
        return R.ok(1,"获取成功!",map,true,null);
    }
    /*
    * 非标段项目信息修改
    * */
    @Override
    public void updateDetails(ProjectParentProjectEntity projectParentProjectEntity) {
        this.updateById(projectParentProjectEntity);
    }
    /*
    * 非标段项目删除
    * */
    @Override
    public void deleteById(String sysId) {
        ProjectParentProjectEntity entity = new ProjectParentProjectEntity();
        entity.setSysId(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }
}
