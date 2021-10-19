package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.dao.ProjectProcessDocumentationDao;
import com.sx.drainage.entity.ProjectProcessDocumentationEntity;
import com.sx.drainage.service.ProjectProcessDocumentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/3
 * Time: 11:23
 */
@Service
@RequiredArgsConstructor
public class ProjectProcessDocumentationServiceImpl extends ServiceImpl<ProjectProcessDocumentationDao, ProjectProcessDocumentationEntity> implements ProjectProcessDocumentationService {

    /*
    * 批量添加
    * */
    @Override
    public void addAll(List<ProjectProcessDocumentationEntity> list) {
        this.saveBatch(list);
    }
    /*
     * 根据流程名称获取
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByProcessName(String processName) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("workType",processName));
    }
    /*
     * 根据流程名称和项目id获取
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByProcessNameAndProjectId(String processName, String projectId) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("workType",processName).eq("projectId",projectId));
    }
    /*
     * 其他部门文件
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getOtherDepartmentFile() {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().isNotNull("taskId").notLike("taskName","工程部").notLike("taskName","顶管部").notLike("taskName","监理").notLike("taskName","施工"));
    }
    /*
     * 其他部门文件根据项目id查询
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getOtherDepartmentFileByProjectId(String projectId) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("projectId",projectId).isNotNull("taskId").notLike("taskName","工程部").notLike("taskName","顶管部").notLike("taskName","监理").notLike("taskName","施工"));
    }
    /*
     * 根据流程名称模糊查询
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByProcessNameLike(String name) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().like("workType",name));
    }
    /*
     * 根据项目id，流程名称模糊查询
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByProcessNameLikeAndProjectId(String name, String projectId) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("projectId",projectId).like("workType",name));
    }
    /*
     * 根据节点名称模糊查询
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByTaskNameLike(String name) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().like("taskName",name));
    }
    /*
     * 根据项目id，根据节点名称模糊查询
     * */
    @Override
    public List<ProjectProcessDocumentationEntity> getFileByTaskNameLikeAndProjectId(String name, String projectId) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("projectId",projectId).like("taskName",name));
    }
    /*
    * 获取所有文件
    * */
    @Override
    public List<ProjectProcessDocumentationEntity> getAllFile() {
        return this.list();
    }
    /*
    * 根据项目id获取文件
    * */
    @Override
    public List<ProjectProcessDocumentationEntity> getAllFileByProjectId(String projectId) {
        return this.list(new QueryWrapper<ProjectProcessDocumentationEntity>().eq("projectId",projectId));
    }
}
