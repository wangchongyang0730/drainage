package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.params.ProjectBasicParams;
import com.sx.drainage.params.ProjectParams;
import com.sx.drainage.params.ProjectParticipantsParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 10:52:08
 */
public interface ProjectProjectService extends IService<ProjectProjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取项目信息
    List<Map<String, Object>> getAllProject(String userId);
    //获取项目详细信息
    Map<String,Object> getProjectDetails(String projectId);
    //获取图片
    List<Map<String, Object>> getImage(String projectId);
    //获取项目参与单位及主要人员信息
    List<Map<String, Object>> getProjectJoinPersons(String projectId);
    //获取项目当前作业面信息
    List<Map<String,Object>> getProjectNowSide(String projectId);
    //获取无流程提交风险源信息
    List<ProjectWbsbindgroupsourceriskEntity> getNoProcessSourceRisk(String projectId);
    //获取无流程提交施工设备信息
    List<ProjectWbsbindgroupequipmentEntity> getNoProcessDeviceNow(String projectId);
    //获取项目验工计价和安措费
    List<InspectionpriceEntity> getProjectPrice(String projectId);
    //获取无流程提交质量和安全信息
    Map<String, Object> getNoProcessRectification(String projectId, Integer type);
    //获取项目里程碑节点信息
    Map<String, Object> getProjectMajorNode(String projectId);
    //获取所有二级管理员
    List<Map<String, Object>> getTwoAdmin();
    //获取当前项目的信息
    Map<String, Object> getProjectInfo(String sysId);
    //获取当前项目参建单位信息
    Map<String, Object> getProjectParticipants(String projectId, Integer page, Integer pageRecord);
    //修改当前项目信息
    void putProjectInfo(ProjectParams projectParams);
    //更新当前项目的基本信息
    void putProjectBasicInfo(ProjectBasicParams projectBasicParams);
    //删除项目
    void deleteProject(String sysId);
    //获取设置管理员的用户数据（项目下的参建单位中的用户）
    Map<String, Object> getAllCpmpanyUser(String projectId, Integer page, Integer pageRecord, String where);
    //新增当前项目的参建单位
    void postProjectParticipants(ProjectParticipantsParams projectParticipantsParams);
    //修改当前项目参建单位的信息
    void putProjectParticipants(ProjectParticipantsParams projectParticipantsParams);
    //删除当前项目的参建单位
    void deleteProjectParticipants(String sysId);
    //获取项目
    ProjectProjectEntity getProject(String projectId);
    //新增项目
    String postProject(ProjectParams projectParams, String userId);
    //根据父id标段项目
    List<ProjectProjectEntity> getByParentId(String projectId);
    //项目阶段文件获取(鱼骨图)
    R getProjectPhase(String sysId);
    //获取所有项目
    List<ProjectProjectEntity> getProjects();
    //参建单位初始化
    R getTag(String projectId);
    //获取所有标段项目
    List<ProjectProjectEntity> getAllProject();
    //项目阶段文件添加
    R addProjectPhase(ProjectPhaseEntity entity);
    //项目阶段文件获取
    R getProjectAllPhase(String projectId);
    //获取整改完成情况
    Map<String, Object> getRectificationInformation(String projectId);
    //获取项目各阶段数量
    Map<String, Object> getCategory();
    //排序
    void itemSort(String sortList);
}

