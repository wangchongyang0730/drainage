package com.sx.drainage.service.activiti;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/30
 * Time: 15:18
 */
@Slf4j
@Service("noticeService")
@Transactional
@RequiredArgsConstructor
public class NoticeService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ActivitiWorkFlowService activitiWorkFlowService;
    private final ProjectNotificationService projectNotificationService;
    private final ProjectCompanyTagUserService projectCompanyTagUserService;
    private final OmTagService omTagService;
    private final OmTagPostService omTagPostService;
    private final OmTagRelPostAccountService omTagRelPostAccountService;

    /*
    * 安全或监理整改系统通知
    * */
    public void safetyRectification(DelegateExecution execution){
        log.info("安全整改||监理整改");
        OmTagEntity tag = omTagService.getTagByName("施工单位");
        OmTagEntity tags = omTagService.getTagByName("建设单位");
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(execution.getProcessInstanceId());
        ProjectCompanyTagUserEntity tagUser = projectCompanyTagUserService.getUser(entity.getProjectId(),tag.getSysid());
        ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
        if(tagUser!=null) {
            notificationEntity.setUserId(tagUser.getUserId());
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
        }else{
            ProjectCompanyTagUserEntity tagUsers = projectCompanyTagUserService.getUser(entity.getProjectId(),tags.getSysid());
            notificationEntity.setUserId(tagUsers.getUserId());
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg("找不到施工单位负责人!");
        }
        projectNotificationService.add(notificationEntity);
    }

    /*
    * 监理整改通知项管和工程部
    * */
    public void supervisionAndRectification(DelegateExecution execution){
        log.error("监理整改通知项管和工程部");
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(execution.getProcessInstanceId());
        OmTagEntity tag = omTagService.getTagByName("建设单位");
        Map<String, Object> variables = execution.getVariables();
        variables.forEach((k,v) -> {
            log.error("取参数:   key:"+k+"    values:"+v);
        });
        boolean noticeType1 = (boolean) variables.get("noticeType1");
        boolean noticeType2 = (boolean) variables.get("noticeType2");
        List<ProjectNotificationEntity> list = new ArrayList<>();
        if(noticeType1){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "项管部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup1"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        if(noticeType2){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "工程部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup2"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        projectNotificationService.addByList(list);
    }
    /*
    * 监理月报 通知相关部门
    * */
    public void supervisionMonthlyReport(DelegateExecution execution){
        log.error("监理月报 通知相关部门");
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(execution.getProcessInstanceId());
        OmTagEntity tag = omTagService.getTagByName("建设单位");
        OmTagEntity tags = omTagService.getTagByName("监理单位");
        Map<String, Object> variables = execution.getVariables();
        variables.forEach((k,v) -> {
            log.error("取参数:   key:"+k+"    values:"+v);
        });
        boolean noticeType1 = (boolean) variables.get("noticeType1");
        boolean noticeType2 = (boolean) variables.get("noticeType2");
        boolean noticeType3 = (boolean) variables.get("noticeType3");
        boolean noticeType4 = (boolean) variables.get("noticeType4");
        List<ProjectNotificationEntity> list = new ArrayList<>();
        if(noticeType1){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "项管部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup1"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        if(noticeType2){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "工程部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup2"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        if(noticeType3){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "综合计划部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup3"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        if(noticeType4){
            OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tags.getSysid(), "信息部");
            List<String> postHaveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), entity.getProjectId());
            if(postHaveUser!=null){
                postHaveUser.forEach(u -> {
                    ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
                    notificationEntity.setUserId(u);
                    notificationEntity.setSysId(CreateUuid.uuid());
                    notificationEntity.setStates(0);
                    notificationEntity.setDel(0);
                    notificationEntity.setProcessId(entity.getProcessId());
                    notificationEntity.setProjectId(entity.getProjectId());
                    notificationEntity.setCreateUser(entity.getCreateUser());
                    notificationEntity.setCreateUserId(entity.getCreateUserId());
                    notificationEntity.setProcessType(entity.getWorkType());
                    notificationEntity.setMsg(entity.getWorkType());
                    list.add(notificationEntity);
                });
            }
        }else{
            ProjectNotificationEntity notificationEntity = new ProjectNotificationEntity();
            notificationEntity.setUserId((String) variables.get("userGroup4"));
            notificationEntity.setSysId(CreateUuid.uuid());
            notificationEntity.setStates(0);
            notificationEntity.setDel(0);
            notificationEntity.setProcessId(entity.getProcessId());
            notificationEntity.setProjectId(entity.getProjectId());
            notificationEntity.setCreateUser(entity.getCreateUser());
            notificationEntity.setCreateUserId(entity.getCreateUserId());
            notificationEntity.setProcessType(entity.getWorkType());
            notificationEntity.setMsg(entity.getWorkType());
            list.add(notificationEntity);
        }
        projectNotificationService.addByList(list);
    }
}
