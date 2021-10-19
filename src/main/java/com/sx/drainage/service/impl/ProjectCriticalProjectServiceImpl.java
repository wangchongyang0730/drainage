package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.ProjectCriticalProjectDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.ProjectCriticalProjectEntity;
import com.sx.drainage.service.OmAccountService;
import com.sx.drainage.service.ProjectCriticalProjectService;
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
 * Date: 2020/11/19
 * Time: 16:10
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectCriticalProjectServiceImpl extends ServiceImpl<ProjectCriticalProjectDao, ProjectCriticalProjectEntity> implements ProjectCriticalProjectService {

    private final OmAccountService omAccountService;
    /*
     * 根据项目id获取危大工程
     * , Integer page, Integer limit
     * */
    @Override
    public List<ProjectCriticalProjectEntity> getAllByProjectId(String projectId) {
        /*Map<String, Object> map = new HashMap<>();
        map.put("page", page.toString());
        map.put("limit", limit.toString());
        IPage<ProjectCriticalProjectEntity> iPage = this.page(
                new Query<ProjectCriticalProjectEntity>().getPage(map),
                new QueryWrapper<ProjectCriticalProjectEntity>().eq("projectId",projectId)
        );
        return new PageUtils(iPage);*/
        return this.list(new QueryWrapper<ProjectCriticalProjectEntity>().eq("projectId",projectId));
    }
    /*
     * 添加危大工程
     * */
    @Override
    public void addCriticalProject(ProjectCriticalProjectEntity entity,String userId) {
        entity.setSysId(CreateUuid.uuid());
        entity.setCreateDate(new Date());
        entity.setCreateUserId(userId);
        OmAccountEntity user = omAccountService.getUser(userId);
        entity.setCreateUserName(user.getName());
        this.save(entity);
    }
    /*
     * 删除危大工程
     * */
    @Override
    public void deleteCriticalProject(String sysId) {
        this.removeById(sysId);
    }
    /*
     * 修改危大工程
     * */
    @Override
    public void updateCriticalProject(ProjectCriticalProjectEntity entity) {
        this.updateById(entity);
    }
}
