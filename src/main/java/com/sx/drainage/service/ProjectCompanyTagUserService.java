package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectCompanyTagUserEntity;
import com.sx.drainage.params.ProjectParticipantsParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:55:35
 */
public interface ProjectCompanyTagUserService extends IService<ProjectCompanyTagUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取项目参与单位及主要人员信息
    List<ProjectCompanyTagUserEntity> getProjectJoinPersons(String projectId);
    //获取参与单位Id
    String getProjectPositionsList(String tag_id, String projectId);
    //获取当前项目参建单位信息
    Map<String,Object> getProjectParticipants(String projectId, Integer page, Integer pageRecord);
    //新增当前项目的参建单位
    void postProjectParticipants(ProjectParticipantsParams projectParticipantsParams, String companyId);
    //修改当前项目参建单位的信息
    void putProjectParticipants(ProjectParticipantsParams projectParticipantsParams, String companyId);
    //删除当前项目的参建单位
    void deleteProjectParticipants(String sysId);
    //初始化城建单位
    void initCompany(String sysid);
    //获取项目下某单位类别
    ProjectCompanyTagUserEntity getTagByCompanyIdAndProjectId(String companyId, String projectId);
    //获取单位负责人
    ProjectCompanyTagUserEntity getUser(String projectId, String sysId);
    //变更负责人
    void putUser(String sysid, String projectId, String userId);
}

