package com.sx.drainage.service;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ActivitiWorkFlowEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/12
 * Time: 10:12
 */
public interface WorkFlowService {
    /*
     *部署流程
     * */
    Map<String, Object> deploymentProcess(MultipartFile file, String name);
    /*
    * 启动流程
    * */
    Map<String, Object> startProcess(Map<String, Object> map, String userId);
    /*
    * 获取所有流程
    * */
    List<Map<String, Object>> getAllProcess();
    /*
    * 获取流程图
    * */
    void getProcessImg(String deploymentId, String img, HttpServletRequest request, HttpServletResponse response);
    /*
    * 获取活动节点
    * */
    List<Map<String, Object>> getCompletionNode(String processInstanceId);
    /*
    * 查询个人任务
    * */
    Map<String, Object> getMyTask(String userId);
    /*
    * 完成任务
    * */
    Map<String, Object> completeTask(Map<String, Object> map);
    /*
    * 获取流程跟踪图
    * */
    void getProcessTrackImg(String processInstanceId, HttpServletRequest request, HttpServletResponse response);
    /*
    * 获取流程存档
    * */
    List<ActivitiWorkFlowEntity> getProcessArchive(String projectId, String workType);
    /*
    * 删除流程存档
    * */
    boolean deleteProcessArchive(String sysId);
    /*
    * 任务拾取
    * */
    void claimTask(String userId, String taskId);
    /*
    * 任务退回
    * */
    void rollBackTask(String taskId);
    /*
    * 获取下一节点待办人
    * */
    R nextUsers(String taskId, String projectId, String var);
    /*
    * 根据任务id详情
    * */
    Map<String, Object> getTaskByTaskId(String taskId);
    /*
     * 根据流程id详情
     * */
    Map<String, Object> getTaskByProcessId(String processId);
    /*
    * 获取流程携带信息
    * */
    Map<String, Object> getProcessDetails(String processId);
    /*
    * 流程信息添加
    * */
    void addProcessDetails(String processId, Map<String, Object> map);
}
