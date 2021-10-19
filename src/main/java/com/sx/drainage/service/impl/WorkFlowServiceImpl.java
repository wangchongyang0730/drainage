package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/12
 * Time: 10:12
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor //构造器注入 lombok注解
public class WorkFlowServiceImpl implements WorkFlowService {

    private SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private final RepositoryService repositoryService;//资源操作
    private final TaskService taskService;//任务管理
    private final RuntimeService runtimeService;//流程管理
    private final HistoryService historyService;//历史信息
    private final ProcessEngine processEngine;//流程引擎对象
    private final ActivitiWorkFlowService activitiWorkFlowService;//流程信息存档
    private final OmAccountService omAccountService;//用户信息
    private final ProjectCompanyTagUserService projectCompanyTagUserService;
    private final OmTagPostService omTagPostService;
    private final OmTagRelPostAccountService omTagRelPostAccountService;
    private final OmTagService omTagService;
    private final ProjectProjectService projectProjectService;
    private final ProjectProcessDocumentationService projectProcessDocumentationService;
    private final ProjectPhaseService projectPhaseService;
    private final RestTemplate restTemplate;
    private final Environment env;

    /*
     *部署流程
     * */
    @Override
    public Map<String, Object> deploymentProcess(MultipartFile file, String name) {
/*        UserTask userTask = (UserTask) repositoryService.getBpmnModel("").getFlowElement("");
        userTask.getFormProperties();*/
        Map<String, Object> map = new HashMap<>();
        try {
            String suffixName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if (!suffixName.equals(".zip")) {
                map.put("msg", "文件格式错误!");
            }
            InputStream inputStream = file.getInputStream();
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).name(name).deploy();
            map.put("msg", "部署成功!");
            map.put("name", deploy.getName());
            map.put("key", deploy.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            map.put("msg", "流程不存在!");
        }
        return map;
    }

    /*
     *启动流程
     * */
    @Override
    public Map<String, Object> startProcess(Map<String, Object> map, String userId) {
        Map<String, Object> res = new HashMap<>();
        String key = (String) map.get("key");
        String workType = (String) map.get("workType");
        String projectId = (String) map.get("projectId");
        String name = (String) map.get("name");
        Map<String, Object> var = (Map<String, Object>) map.get("var");
        Map<String, Object> data = new HashMap<>();
        data.put("username", userId);
        log.error("var" + var);
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        if(list!=null&&list.size()>0){
            Optional<ProcessDefinition> collect = list.stream().collect(Collectors.maxBy(Comparator.comparing(ProcessDefinition::getVersion)));
            String id = collect.get().getId();
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(id, data);
            runtimeService.setVariablesLocal(processInstance.getId(), var);
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId()).singleResult();
            String deploymentName = deployment.getName();
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            OmAccountEntity user = omAccountService.getUser(userId);
            ActivitiWorkFlowEntity entity = new ActivitiWorkFlowEntity();
            entity.setSysId(CreateUuid.uuid());
            entity.setCreateDate(new Date());
            entity.setWorkType(workType);
            if (var != null) {
                Object nums = var.get("data");
                if (nums != null && nums instanceof List) {
                    entity.setNum(((List) nums).size());
                }
            }
            entity.setName(name);
            entity.setUrl(null);
            entity.setProcessName(deploymentName);
            entity.setProcessId(processInstance.getId());
            entity.setDel(0);
            entity.setProjectId(projectId);
            entity.setCreateUserId(userId);
            entity.setSubmitDate(new Date());
            entity.setStates(0);
            entity.setCreateUser(user.getName());
            entity.setTaskId(task.getId());
            entity.setToDoUser(userId);
            activitiWorkFlowService.add(entity);
            res.put("processInstanceId", processInstance.getId());
            res.put("taskId", task.getId());
            return res;
        }else{
            res.put("error","流程不存在!");
            return res;
        }

    }

    /*
     * 获取所有流程
     * */
    @Override
    public List<Map<String, Object>> getAllProcess() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, List<ProcessDefinition>> collect = list.stream().collect(Collectors.groupingBy(ProcessDefinition::getKey));
        collect.forEach((k,v) ->{
            Optional<ProcessDefinition> max = v.stream().collect(Collectors.maxBy(Comparator.comparing(ProcessDefinition::getVersion)));
            if(max.isPresent()){
                ProcessDefinition li = max.get();
                Map<String, Object> map = new HashMap<>();
                map.put("key", li.getKey());
                map.put("name", li.getName());
                map.put("img", li.getDiagramResourceName());
                map.put("deploymentId", li.getDeploymentId());
                res.add(map);
            }
        });
        /*for (ProcessDefinition li : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", li.getKey());
            map.put("name", li.getName());
            map.put("img", li.getDiagramResourceName());
            map.put("deploymentId", li.getDeploymentId());
            res.add(map);
        }*/
        return res;
    }

    /*
     * 获取流程图
     * */
    @Override
    public void getProcessImg(String deploymentId, String img, HttpServletRequest request, HttpServletResponse response) {
        InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, img);
        response.setContentType("image/*");
        try {
            byte[] bytes = new byte[resourceAsStream.available()];
            resourceAsStream.read(bytes);
            resourceAsStream.close();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * 获取活动节点
     * */
    @Override
    public List<Map<String, Object>> getCompletionNode(String processInstanceId) {
        List<HistoricActivityInstance> taskInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<Map<String, Object>> res = new ArrayList<>();
        for (HistoricActivityInstance task : taskInstances) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", task.getAssignee());
            map.put("taskId", task.getTaskId() == null ? null : task.getTaskId());
            map.put("name", task.getActivityName());
            map.put("startTime", task.getStartTime());
            map.put("endTime", task.getEndTime() == null ? null : task.getEndTime());
            map.put("complete", task.getEndTime() == null ? false : true);
            if (task.getTaskId() != null) {
                List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().taskId(task.getTaskId()).list();
                if (list.size() > 0 && list != null) {
                    Map<String, Object> maps = new HashMap<>();
                    for (HistoricVariableInstance variable : list) {
                        maps.put(variable.getVariableName(), variable.getValue());
                    }
                    map.put("variables", maps);
                } else {
                    map.put("variables", null);
                }
            } else {
                map.put("variables", null);
            }
            res.add(map);
        }
        return res;
    }

    /*
     * 查询个人任务
     * */
    @Override
    public Map<String, Object> getMyTask(String userId) {
        List<Task> list = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> toDo = new ArrayList<>();
        List<Map<String, Object>> haveToDo = new ArrayList<>();
        Iterator<HistoricTaskInstance> iterator = historicTaskInstances.iterator();
        while (iterator.hasNext()) {
            HistoricTaskInstance next = iterator.next();
            if (next.getEndTime() == null) {
                iterator.remove();
            }
        }
        for (HistoricTaskInstance his : historicTaskInstances) {
            Map<String, Object> data = new HashMap<>();
            ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(his.getProcessInstanceId());
            if (entity == null) {
                continue;
            }
            if (entity.getSubmitDate() == null && his.getTaskDefinitionKey().equals("usertask1")) {
                continue;
            }
//            Map<String, Object> variables = runtimeService.getVariables(his.getProcessInstanceId());
//            data.put("details", variables);
            ActivitiWorkFlowEntity workFlowEntity = activitiWorkFlowService.selectByProcessId(his.getProcessInstanceId());
            if(workFlowEntity!=null&&workFlowEntity.getName()!=null){
                data.put("taskName", his.getName()+"-"+workFlowEntity.getName());
            }else {
                data.put("taskName", his.getName());
            }
            data.put("taskId", his.getId());
            data.put("processInstanceId", his.getProcessInstanceId());
            data.put("userName", entity.getCreateUser());
            ProjectProjectEntity project = projectProjectService.getProject(entity.getProjectId());
            ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionId(his.getProcessDefinitionId()).singleResult();
            data.put("processKey", result.getKey());
            data.put("processName", entity.getProcessName());
            data.put("projectId", entity.getProjectId());
            data.put("projectName", project == null ? entity.getProjectId() : project.getName());
            data.put("date", his.getStartTime());
            haveToDo.add(data);
        }
        log.error("任务集合长度" + list.size());
        for (Task task : list) {
            log.error(task.getName() + "/////////////////");
            Map<String, Object> data = new HashMap<>();
            ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(task.getProcessInstanceId());
            if (entity == null) {
                continue;
            }
            if (entity.getSubmitDate() == null && task.getTaskDefinitionKey().equals("usertask1")) {
                continue;
            }
//            Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
//            data.put("details", variables);
            if(entity.getName()!=null){
                data.put("taskName", task.getName()+"-"+entity.getName());
            }else {
                data.put("taskName", task.getName());
            }
            data.put("taskId", task.getId());
            data.put("processInstanceId", task.getProcessInstanceId());
            data.put("userName", entity.getCreateUser());
            ProjectProjectEntity project = projectProjectService.getProject(entity.getProjectId());
            ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            data.put("processKey", result.getKey());
            data.put("processName", entity.getProcessName());
            data.put("projectId", entity.getProjectId());
            data.put("projectName", project == null ? entity.getProjectId() : project.getName());
            data.put("date", task.getCreateTime());
            toDo.add(data);
        }
        res.put("toDo", toDo);
        res.put("haveToDo", haveToDo);
        //不需要分组任务
        /*List<Task> group = taskService.createTaskQuery().taskCandidateUser(userId).list();
        Map<String, Object> res = new HashMap<>();
        if (list.size() > 0 && list != null) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            List<Map<String, Object>> claim = new ArrayList<>();
            for (Task task : list) {
                if(task.getClaimTime()!=null){
                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
                    data.put("details", variables);
                    data.put("taskName", task.getName());
                    data.put("taskId", task.getId());
                    data.put("processInstanceId", task.getProcessInstanceId());
                    claim.add(data);
                }else{
                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
                    data.put("details", variables);
                    data.put("taskName", task.getName());
                    data.put("taskId", task.getId());
                    data.put("processInstanceId", task.getProcessInstanceId());
                    mapList.add(data);
                }
            }
            res.put("personal", mapList);
            res.put("claim", claim);
        } else {
            res.put("personal", null);
            res.put("claim", null);
        }
        if (group.size() > 0 && group != null) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (Task task : group) {
                Map<String, Object> data = new HashMap<>();
                Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
                data.put("details", variables);
                data.put("taskName", task.getName());
                data.put("taskId", task.getId());
                data.put("processInstanceId", task.getProcessInstanceId());
                mapList.add(data);
            }
            res.put("group", mapList);
        } else {
            res.put("group", null);
        }*/
        return res;
    }

    /*
     * 完成任务
     * */
    @Override
    public Map<String, Object> completeTask(Map<String, Object> map) {
        String msg = String.valueOf(map.get("msg"));
        String taskId = String.valueOf(map.get("taskId"));
        String userId = String.valueOf(map.get("userId"));
        String userName = String.valueOf(map.get("userName"));
        map.remove("userId");
        map.remove("userName");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (!task.getAssignee().equals(userId)) {
            Map<String, Object> res = new HashMap<>();
            res.put("msg", "当前处理人错误!");
            return res;
        }
        String processInstanceId = task.getProcessInstanceId();
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(processInstanceId);
        if (entity.getSubmitUser() == null) {
            entity.setStates(1);
            entity.setSubmitUser(entity.getCreateUser());
            entity.setToDoUser(userName);
            entity.setSubmitDate(new Date());
            activitiWorkFlowService.update(entity);
        }
        entity.setToDoUser(userName);
        activitiWorkFlowService.update(entity);
        log.error("userName:" + userName);
        log.error("msg:" + msg);
        if (userName == null || userName.equals("null") || userName.equals("")) {
            if (msg == null || msg.equals("") || msg.equals("null")) {
                if (map.size() > 1) {
                    map.remove("taskId");
                    taskService.setVariablesLocal(taskId, map);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("msg", null);
                    taskService.complete(taskId, maps);
                } else {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("msg", null);
                    taskService.complete(taskId, maps);
                }
            } else {
                Map<String, Object> msgs = new HashMap<>();
                msgs.put("msg", msg);
                if (map.size() > 2) {
                    map.remove("taskId");
                    taskService.setVariablesLocal(taskId, map);
                    taskService.complete(taskId, msgs);
                } else {
                    taskService.setVariablesLocal(taskId, map);
                    taskService.complete(taskId, msgs);
                }
            }
        } else {
            log.error("userName" + userName);
            if (msg == null || msg.equals("") || msg.equals("null")) {
                if (map.size() > 1) {
                    map.remove("taskId");
                    taskService.setVariablesLocal(taskId, map);
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", userName);
                    data.put("msg", null);
                    taskService.complete(taskId, data);
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", userName);
                    data.put("msg", null);
                    taskService.complete(taskId, data);
                }
            } else {
                Map<String, Object> msgs = new HashMap<>();
                log.error("userName" + userName);
                msgs.put("msg", msg);
                msgs.put("username", userName);
                if (map.size() > 2) {
                    map.remove("taskId");
                    taskService.setVariablesLocal(taskId, map);
                    taskService.complete(taskId, msgs);
                } else {
                    log.error("userName" + userName);
                    taskService.setVariablesLocal(taskId, map);
                    taskService.complete(taskId, msgs);
                }
            }
        }
        ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (result == null) {
            boolean res=false;
            List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
            try {
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getValue().toString().equals("放弃")){
                        res=true;
                        break;
                    }
                    if(list.get(i).getValue().equals("放弃")){
                        res=true;
                        break;
                    }
                }
            }catch (Exception e){
                log.error("空值跳过");
            }
            if(!res){
                ActivitiWorkFlowEntity flowEntity = activitiWorkFlowService.selectByProcessId(processInstanceId);
                flowEntity.setStates(2);
                activitiWorkFlowService.update(flowEntity);
                processEnd(flowEntity);
            }else{
                ActivitiWorkFlowEntity flowEntity = activitiWorkFlowService.selectByProcessId(processInstanceId);
                flowEntity.setStates(3);
                activitiWorkFlowService.update(flowEntity);
            }
        }
        //微信消息推送
        if(!StringUtils.isEmpty(userName)){
            weChatMessagePush(userName);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "已完成!");
        return res;
    }

    /*
     * 获取流程跟踪图
     * */
    @Override
    public void getProcessTrackImg(String processInstanceId, HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = generateProcessDiagram(processEngine, processInstanceId);
            response.setContentType("image/*");
            response.setHeader("Content-Disposition", "attachment;filename=" + formats.format(new Date()) + ".png");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.error("无此流程，请验证后重试!");
//            response.setContentType("text/html;charset=UTF-8");
//            try {
//                PrintWriter writer = response.getWriter();
//                writer.write("无此流程，请验证后重试!");
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
        }
    }

    /*
     * 获取流程存档
     * */
    @Override
    public List<ActivitiWorkFlowEntity> getProcessArchive(String projectId, String workType) {
        List<ActivitiWorkFlowEntity> list = activitiWorkFlowService.selectByWorkTypeAndProjectId(workType, projectId);
        list.forEach(l -> {
            l.setData(this.getProcessDetails(l.getProcessId()));
        });
        return list;
    }

    /*
     * 删除流程存档
     * */
    @Override
    public boolean deleteProcessArchive(String sysId) {
        return activitiWorkFlowService.deleteBySysId(sysId);
    }

    /*
     * 任务拾取
     * */
    @Override
    public void claimTask(String userId, String taskId) {
        taskService.claim(taskId, userId);
    }

    /*
     * 任务退回
     * */
    @Override
    public void rollBackTask(String taskId) {
        taskService.setAssignee(taskId, null);
    }

    /*
     * 获取下一节点待办人
     * */
    @SneakyThrows
    @Override
    public R nextUsers(String taskId, String projectId, String var) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        FlowElement element = getNextUserFlowElement(task, processEngine, var);
        log.error("当前taskName" + task.getName());
        log.error("当前taskId" + task.getId());
        if (element != null) {
            log.error("taskName" + element.getName());
            log.error("taskId" + element.getId());
            if (element.getId().equals("usertask1")) {
                ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(task.getProcessInstanceId());
                List<Map<String, Object>> data = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("sysId", entity.getCreateUserId());
                map.put("name", entity.getCreateUser());
                data.add(map);
                return R.ok(1, "获取成功!", data, true, null);
            }
            String name = element.getName();
            String substring = name.substring(name.indexOf("(") + 1, name.lastIndexOf(")"));
            log.error("tagName" + substring.substring(0, substring.indexOf("(")));
            OmTagEntity tag = omTagService.getTagByName(substring.substring(0, substring.indexOf("(")));
            if (tag != null) {
                String companyId = projectCompanyTagUserService.getProjectPositionsList(tag.getSysid(), projectId);
                if (!StringUtils.isEmpty(companyId)) {
                    /*if (substring.substring(substring.indexOf("(") + 1, substring.indexOf(")")).equals("总监理工程师")) {
                        ProjectCompanyTagUserEntity user = projectCompanyTagUserService.getUser(projectId, tag.getSysid());
                        if (user != null) {
                            List<Map<String, Object>> data = new ArrayList<>();
                            Map<String, Object> map = new HashMap<>();
                            OmAccountEntity users = omAccountService.getUser(user.getUserId());
                            map.put("sysId", users.getSysid());
                            map.put("name", users.getName());
                            data.add(map);
                            return R.ok(1, "获取成功!", data, true, null);
                        }else{
                            return R.error(500, "请先任命总监理工程师!");
                        }
                    }*/
                    OmTagPostEntity postEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), substring.substring(substring.indexOf("(") + 1, substring.indexOf(")")));
                    if (postEntity != null) {
                        List<String> haveUser = omTagRelPostAccountService.getPostHaveUser(postEntity.getSysid(), projectId);
                        List<Map<String, Object>> data = new ArrayList<>();
                        if (haveUser != null && haveUser.size() > 0) {
                            haveUser.forEach(h -> {
                                Map<String, Object> map = new HashMap<>();
                                OmAccountEntity user = omAccountService.getUser(h);
                                if (companyId.equals(user.getCompanyId())) {
                                    map.put("sysId", user.getSysid());
                                    map.put("name", user.getName());
                                    data.add(map);
                                }
                            });
                            return R.ok(1, "获取成功!", data, true, null);
                        }
                    }
                }
            }
            return R.error(404, "找不到符合条件的处理人!");
        }
        return R.error(400, "无下一任务节点!");
    }

    /*
     * 根据任务id详情
     * */
    @Override
    public Map<String, Object> getTaskByTaskId(String taskId) {
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        Map<String, Object> data = new HashMap<>();
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(task.getProcessInstanceId());
        if (entity == null) {
            return null;
        }
        if(entity.getName()!=null){
            data.put("taskName", task.getName()+"-"+entity.getName());
        }else {
            data.put("taskName", task.getName());
        }
        data.put("taskId", task.getId());
        data.put("processInstanceId", task.getProcessInstanceId());
        data.put("userName", entity.getCreateUser());
        ProjectProjectEntity project = projectProjectService.getProject(entity.getProjectId());
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        data.put("processKey", result.getKey());
        data.put("processName", entity.getProcessName());
        data.put("projectId", entity.getProjectId());
        data.put("projectName", project == null ? entity.getProjectId() : project.getName());
        data.put("date", entity.getSubmitDate());
        return data;
    }

    /*
     * 根据流程id详情
     * */
    @Override
    public Map<String, Object> getTaskByProcessId(String processId) {
        Map<String, Object> data = new HashMap<>();
        ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(processId);
        if (entity == null) {
            return null;
        }
        data.put("userName", entity.getCreateUser());
        ProjectProjectEntity project = projectProjectService.getProject(entity.getProjectId());
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        log.error("是否为空:" + (result == null));
        data.put("processKey", result.getKey());
        data.put("processName", entity.getProcessName());
        data.put("projectId", entity.getProjectId());
        data.put("processInstanceId", processId);
        data.put("projectName", project == null ? entity.getProjectId() : project.getName());
        data.put("date", entity.getSubmitDate());
        return data;
    }

    /*
     * 获取流程携带信息
     * */
    @Override
    public Map<String, Object> getProcessDetails(String processId) {
//        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult().getProcessVariables();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processId).list();
        Map<String, Object> map = new HashMap<>();
        list.forEach(l -> {
            if (StringUtils.isEmpty(l.getTaskId())) {
                map.put(l.getVariableName(), l.getValue());
            }
        });
        //runtimeService.getVariablesLocal(processId);
        return map;
    }

    /*
     * 流程信息添加
     * */
    @Override
    public void addProcessDetails(String processId, Map<String, Object> map) {
        runtimeService.setVariablesLocal(processId, map);
    }

    /*
     * 流程携带表单信息
     * */
    private static List<Map<String, Object>> processFrom(String processDefinitionId, String taskDefinitionKey, RepositoryService repositoryService) {
        UserTask userTask = (UserTask) repositoryService.getBpmnModel(processDefinitionId).getFlowElement(taskDefinitionKey);
        List<FormProperty> properties = userTask.getFormProperties();
        List<Map<String, Object>> list = new ArrayList<>();
        for (FormProperty form : properties) {
            Map<String, Object> map = new HashMap<>();
            map.put("key", form.getId());
            map.put("name", form.getName());
            map.put("type", form.getType());
            list.add(map);
        }
        return list;
    }

    private static InputStream generateProcessDiagram(ProcessEngine processEngine, String processInstanceId) {
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //获取历史流程定义
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                processEngine.getRepositoryService()).getDeployedProcessDefinition(historicProcessInstance
                .getProcessDefinitionId());
        //查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        /*
         * orderByHistoricActivityInstanceId此排序有问题
         * */
        //已执行历史节点
        List<String> executedActivityIdList = new ArrayList<String>();
        historicActivityInstanceList.forEach(historicActivityInstance -> {
            executedActivityIdList.add(historicActivityInstance.getActivityId());
        });
        //当前激活节点
        List<String> allActivityId = new ArrayList<>();
        historicActivityInstanceList.forEach(historicActivityInstance -> {
            if (historicActivityInstance.getEndTime() == null && historicActivityInstance.getAssignee() != null) {
                allActivityId.add(historicActivityInstance.getActivityId());
            }
        });
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processDefinitionEntity.getId());
        //已执行flow的集和
        List<String> executedFlowIdList = executedFlowIdList(bpmnModel, processDefinitionEntity, historicActivityInstanceList);

        com.sx.drainage.config.DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new com.sx.drainage.config.DefaultProcessDiagramGenerator();
        ClassLoader classLoader = processEngine.getProcessEngineConfiguration().getClassLoader();
        InputStream diagram = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList, allActivityId, executedFlowIdList, "黑体", "黑体", "黑体", classLoader, 1.0);

        return diagram;
    }

    private static List<String> executedFlowIdList(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
                                                   List<HistoricActivityInstance> historicActivityInstanceList) {

        List<String> executedFlowIdList = new ArrayList<>();
        log.error("historic长度" + historicActivityInstanceList.size());
        historicActivityInstanceList.forEach(h -> {
            log.error(h.getActivityId());
        });
        for (int i = 0; i < historicActivityInstanceList.size() - 1; i++) {
            HistoricActivityInstance hai = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            log.error("当前循环:" + i);
            log.error(flowNode.getId());
            log.error(flowNode.getName());
            log.error("是否为空" + (sequenceFlows == null ? null : sequenceFlows.size()));
            if (sequenceFlows.size() > 1) {
                HistoricActivityInstance nextHai = historicActivityInstanceList.get(i + 1);
                sequenceFlows.forEach(sequenceFlow -> {
                    if (sequenceFlow.getTargetFlowElement().getId().equals(nextHai.getActivityId())) {
                        executedFlowIdList.add(sequenceFlow.getId());
                    }
                });
            } else {
                executedFlowIdList.add(sequenceFlows.get(0).getId());
            }
        }

        return executedFlowIdList;
    }

    /**
     * 获取当前任务节点的下一个任务节点
     *
     * @param task 当前任务节点
     * @return 下个任务节点
     * @throws Exception
     */
    private static FlowElement getNextUserFlowElement(Task task, ProcessEngine engine, String var) throws Exception {
        // 取得已提交的任务
        HistoricTaskInstance historicTaskInstance = engine.getHistoryService().createHistoricTaskInstanceQuery()
                .taskId(task.getId()).singleResult();
        // 获得流程定义
        ProcessDefinition processDefinition = engine.getRepositoryService().getProcessDefinition(historicTaskInstance.getProcessDefinitionId());
        //获得当前流程的活动ID
        ExecutionQuery executionQuery = engine.getRuntimeService().createExecutionQuery();
        Execution execution = executionQuery.executionId(historicTaskInstance.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        UserTask userTask = null;
        int i = 0;
        while (true) {
            i++;
            //根据活动节点获取当前的组件信息
            FlowNode flowNode = getFlowNode(processDefinition.getId(), activityId, engine);
            log.error("???" + i);
            log.error("???" + flowNode.getOutgoingFlows());
            //获取该节点之后的流向
            List<org.activiti.bpmn.model.SequenceFlow> sequenceFlowListOutGoing = flowNode.getOutgoingFlows();
            // 获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
            if (sequenceFlowListOutGoing.size() > 1) {
                // 如果有1条以上的出线，表示有分支，需要判断分支的条件才能知道走哪个分支
                // 遍历节点的出线得到下个activityId
                activityId = getNextActivityId(execution.getId(),
                        task.getProcessInstanceId(), sequenceFlowListOutGoing, engine, var);
                FlowNode flowNodes = getFlowNode(processDefinition.getId(), activityId, engine);
                log.error(activityId);
                log.error("flowNodes" + flowNodes.getId());
                log.error("flowNodes" + flowNodes.getName());
                if (flowNodes instanceof UserTask) {
                    userTask = (UserTask) flowNodes;
                    return userTask;
                }
            } else if (sequenceFlowListOutGoing.size() == 1) {
                // 只有1条出线,直接取得下个节点
                org.activiti.bpmn.model.SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(0);
                // 下个节点
                FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                if (flowElement instanceof UserTask) {
                    // 下个节点为UserTask时
                    userTask = (UserTask) flowElement;
                    return userTask;
                } else if (flowElement instanceof ExclusiveGateway) {
                    // 下个节点为排它网关时
                    ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                    List<org.activiti.bpmn.model.SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
                    // 遍历网关的出线得到下个activityId
                    activityId = getNextActivityId(execution.getId(), task.getProcessInstanceId(), outgoingFlows, engine, var);
                } else if (flowElement instanceof ParallelGateway) {
                    //TODO 并行
                    return null;
                } else if (flowElement instanceof ServiceTask) {
                    ServiceTask serviceTask = (ServiceTask) flowElement;
                    List<org.activiti.bpmn.model.SequenceFlow> outgoingFlows = serviceTask.getOutgoingFlows();
                    // 遍历网关的出线得到下个activityId
                    activityId = getNextActivityId(execution.getId(), task.getProcessInstanceId(), outgoingFlows, engine, var);
                    FlowNode flowNodes = getFlowNode(processDefinition.getId(), activityId, engine);
                    if (flowNodes instanceof UserTask) {
                        userTask = (UserTask) flowNodes;
                        return userTask;
                    }
                } else {
                    return null;
                }
            } else {
                // 没有出线，则表明是结束节点
                return null;
            }
        }
    }

    /**
     * 根据活动节点和流程定义ID获取该活动节点的组件信息
     */
    private static FlowNode getFlowNode(String processDefinitionId, String flowElementId, ProcessEngine engine) {
        BpmnModel bpmnModel = engine.getRepositoryService().getBpmnModel(processDefinitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(flowElementId);
        return flowNode;
    }

    /**
     * 根据el表达式取得满足条件的下一个activityId
     *
     * @param executionId
     * @param processInstanceId
     * @param outgoingFlows
     * @return
     */
    private static String getNextActivityId(String executionId,
                                            String processInstanceId,
                                            List<org.activiti.bpmn.model.SequenceFlow> outgoingFlows,
                                            ProcessEngine engine,
                                            String var) {
        String activityId = null;
        String defaultActivityId = null;
        // 遍历出线
        // 遍历出线
        if (StringUtils.isEmpty(var)) {
            for (org.activiti.bpmn.model.SequenceFlow outgoingFlow : outgoingFlows) {
                // 取得线上的条件
                String conditionExpression = outgoingFlow.getConditionExpression();
                if (StringUtils.isEmpty(conditionExpression)) {
                    FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                    defaultActivityId = targetFlowElement.getId();
                    continue;
                }
                // 取得所有变量
                Map<String, Object> variables = engine.getRuntimeService().getVariables(executionId);
                String variableName = "";
                // 判断网关条件里是否包含变量名
                for (String s : variables.keySet()) {
                    if (conditionExpression.contains(s)) {
                        // 找到网关条件里的变量名
                        variableName = s;
                    }
                }
                String conditionVal = getVariableValue(variableName, processInstanceId, engine);
                // 判断el表达式是否成立
                if (isCondition(variableName, conditionExpression, conditionVal)) {
                    // 取得目标节点
                    FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                    activityId = targetFlowElement.getId();
                    continue;
                }
            }
            if (StringUtils.isEmpty(activityId)) {
                return defaultActivityId;
            }
            return activityId;
        } else {
            for (org.activiti.bpmn.model.SequenceFlow outgoingFlow : outgoingFlows) {
                // 取得线上的条件
                String conditionExpression = outgoingFlow.getConditionExpression();
                if (StringUtils.isEmpty(conditionExpression)) {
                    FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                    defaultActivityId = targetFlowElement.getId();
                    continue;
                }
                log.error("条件:" + conditionExpression);
                if (conditionExpression.contains("||")) {
                    String[] split = conditionExpression.split("\\|\\|");
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].substring(split[i].indexOf("'") + 1, split[i].lastIndexOf("'")).equals(var)) {
                            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                            activityId = targetFlowElement.getId();
                            log.error("确认后的" + targetFlowElement.getName());
                            log.error("确认后的" + targetFlowElement.getId());
                            continue;
                        }
                    }
                } else if (conditionExpression.contains("&&")) {
                    continue;
                } else {
                    String substring = conditionExpression.substring(conditionExpression.indexOf("'") + 1, conditionExpression.lastIndexOf("'"));
                    if (substring.equals(var)) {
                        FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
                        activityId = targetFlowElement.getId();
                        log.error("确认后的" + targetFlowElement.getName());
                        log.error("确认后的" + targetFlowElement.getId());
                        continue;
                    }
                }
            }
            if (StringUtils.isEmpty(activityId)) {
                return defaultActivityId;
            }
            return activityId;
        }
    }

    /**
     * 取得流程变量的值
     *
     * @param variableName      变量名
     * @param processInstanceId 流程实例Id
     * @return
     */
    private static String getVariableValue(String variableName, String processInstanceId, ProcessEngine engine) {
        Execution execution = engine.getRuntimeService()
                .createExecutionQuery().processInstanceId(processInstanceId).list().get(0);
        Object object = engine.getRuntimeService().getVariable(execution.getId(), variableName);
        return object == null ? "" : object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过
     *
     * @param key   el表达式key
     * @param el    el表达式
     * @param value el表达式传入值
     * @return
     */
    private static boolean isCondition(String key, String el, String value) {
        boolean res;
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        try {
            res = (Boolean) e.getValue(context);
        } catch (PropertyNotFoundException exception) {
            return false;
        }
        return res;
    }

    /*
     * 流程结束需要处理的问题
     * */
    private void processEnd(ActivitiWorkFlowEntity entity) {
        log.error("processId:" + entity.getProcessId());
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(entity.getProcessId()).list();
        List<ProjectProcessDocumentationEntity> data = new ArrayList<>();
        log.error("task长度:" + list.size());
        if (list != null && list.size() > 0) {
            list.forEach(l -> {
                List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().taskId(l.getId()).list();
                variableInstances.forEach(v -> {
                    log.error("字段名1" + v.getVariableName());
                    if (v.getVariableName().contains("fileId") || v.getVariableName().contains("opinionFileId")
                            || v.getVariableName().contains("rectificationId") || v.getVariableName().contains("replyFileId") ||
                            v.getVariableName().contains("meetingFileId") || v.getVariableName().contains("acceptanceFileId") ||
                            v.getVariableName().contains("summaryFileId") || v.getVariableName().contains("filingCertificate")) {
                        ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                        processFile.setSysId(CreateUuid.uuid());
                        processFile.setProcessId(entity.getProcessId());
                        processFile.setProjectId(entity.getProjectId());
                        processFile.setTaskId(l.getId());
                        processFile.setTaskName(l.getName());
                        processFile.setUserId(l.getAssignee());
                        processFile.setFileId(v.getValue().toString());
                        processFile.setWorkType(entity.getWorkType());
                        data.add(processFile);
                        if(v.getVariableName().contains("filingCertificate")){
                            ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                            phaseEntity.setProjectId(entity.getProjectId());
                            phaseEntity.setRecord(v.getValue().toString());
                            phaseEntity.setFilingCertificate(v.getValue().toString());
                            projectPhaseService.addProjectPhase(phaseEntity);
                        }
                    }
                    if (v.getVariableName().equals("data")) {
                        if (v.getValue() instanceof Map) {
                            Map<String, Object> map = (Map<String, Object>) v.getValue();
                            map.forEach((k, s) -> {
                                if (k.contains("fileId") || k.contains("opinionFileId")
                                        || k.contains("rectificationId") || k.contains("replyFileId") ||
                                        k.contains("meetingFileId") || k.contains("acceptanceFileId") ||
                                        k.contains("summaryFileId") || k.contains("filingCertificate")) {
                                    ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                                    processFile.setSysId(CreateUuid.uuid());
                                    processFile.setProcessId(entity.getProcessId());
                                    processFile.setProjectId(entity.getProjectId());
                                    processFile.setTaskId(l.getId());
                                    processFile.setTaskName(l.getName());
                                    processFile.setUserId(l.getAssignee());
                                    processFile.setFileId(s.toString());
                                    processFile.setWorkType(entity.getWorkType());
                                    data.add(processFile);
                                    if(k.contains("filingCertificate")){
                                        ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                                        phaseEntity.setProjectId(entity.getProjectId());
                                        phaseEntity.setRecord(s.toString());
                                        phaseEntity.setFilingCertificate(s.toString());
                                        projectPhaseService.addProjectPhase(phaseEntity);
                                    }
                                }
                                if (k.equals("data")) {
                                    if (s instanceof Map) {
                                        Map<String, Object> maps = (Map<String, Object>) s;
                                        maps.forEach((ks, ss) -> {
                                            if (ks.contains("fileId") || ks.contains("opinionFileId")
                                                    || ks.contains("rectificationId") || ks.contains("replyFileId") ||
                                                    ks.contains("meetingFileId") || ks.contains("acceptanceFileId") ||
                                                    ks.contains("summaryFileId") || ks.contains("filingCertificate")) {
                                                ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                                                processFile.setSysId(CreateUuid.uuid());
                                                processFile.setProcessId(entity.getProcessId());
                                                processFile.setProjectId(entity.getProjectId());
                                                processFile.setFileId(ss.toString());
                                                processFile.setWorkType(entity.getWorkType());
                                                data.add(processFile);
                                                if(ks.contains("filingCertificate")){
                                                    ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                                                    phaseEntity.setProjectId(entity.getProjectId());
                                                    phaseEntity.setRecord(ss.toString());
                                                    phaseEntity.setFilingCertificate(ss.toString());
                                                    projectPhaseService.addProjectPhase(phaseEntity);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            });
        }
        List<HistoricVariableInstance> instanceList = historyService.createHistoricVariableInstanceQuery().processInstanceId(entity.getProcessId()).list();
        log.error("剩余长度:" + instanceList.size());
        if (instanceList != null && instanceList.size() > 0) {
            instanceList.forEach(i -> {
                log.error("字段名2" + i.getVariableName());
                if (StringUtils.isEmpty(i.getTaskId())) {
                    if (i.getVariableName().contains("opinionFileId") || i.getVariableName().contains("fileId")
                            || i.getVariableName().contains("rectificationId") || i.getVariableName().contains("replyFileId") ||
                            i.getVariableName().contains("meetingFileId") || i.getVariableName().contains("acceptanceFileId") ||
                            i.getVariableName().contains("summaryFileId") || i.getVariableName().contains("filingCertificate")) {
                        ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                        processFile.setSysId(CreateUuid.uuid());
                        processFile.setProcessId(entity.getProcessId());
                        processFile.setProjectId(entity.getProjectId());
                        processFile.setFileId(i.getValue().toString());
                        processFile.setWorkType(entity.getWorkType());
                        data.add(processFile);
                        if(i.getVariableName().contains("filingCertificate")){
                            ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                            phaseEntity.setProjectId(entity.getProjectId());
                            phaseEntity.setRecord(i.getValue().toString());
                            phaseEntity.setFilingCertificate(i.getValue().toString());
                            projectPhaseService.addProjectPhase(phaseEntity);
                        }
                    }
                    if (i.getVariableName().equals("data")) {
                        if (i.getValue() instanceof Map) {
                            Map<String, Object> map = (Map<String, Object>) i.getValue();
                            map.forEach((k, s) -> {
                                if (k.contains("fileId") || k.contains("opinionFileId")
                                        || k.contains("rectificationId") || k.contains("replyFileId") ||
                                        k.contains("meetingFileId") || k.contains("acceptanceFileId") ||
                                        k.contains("summaryFileId") || k.contains("filingCertificate")) {
                                    ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                                    processFile.setSysId(CreateUuid.uuid());
                                    processFile.setProcessId(entity.getProcessId());
                                    processFile.setProjectId(entity.getProjectId());
                                    processFile.setFileId(s.toString());
                                    processFile.setWorkType(entity.getWorkType());
                                    data.add(processFile);
                                    if(k.contains("filingCertificate")){
                                        ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                                        phaseEntity.setProjectId(entity.getProjectId());
                                        phaseEntity.setRecord(s.toString());
                                        phaseEntity.setFilingCertificate(s.toString());
                                        projectPhaseService.addProjectPhase(phaseEntity);
                                    }
                                }
                                if (k.equals("data")) {
                                    if (s instanceof Map) {
                                        Map<String, Object> maps = (Map<String, Object>) s;
                                        maps.forEach((ks, ss) -> {
                                            if (ks.contains("fileId") || ks.contains("opinionFileId")
                                                    || ks.contains("rectificationId") || ks.contains("replyFileId") ||
                                                    ks.contains("meetingFileId") || ks.contains("acceptanceFileId") ||
                                                    ks.contains("summaryFileId") || ks.contains("filingCertificate")) {
                                                ProjectProcessDocumentationEntity processFile = new ProjectProcessDocumentationEntity();
                                                processFile.setSysId(CreateUuid.uuid());
                                                processFile.setProcessId(entity.getProcessId());
                                                processFile.setProjectId(entity.getProjectId());
                                                processFile.setFileId(ss.toString());
                                                processFile.setWorkType(entity.getWorkType());
                                                data.add(processFile);
                                                if(ks.contains("filingCertificate")){
                                                    ProjectPhaseEntity phaseEntity = new ProjectPhaseEntity();
                                                    phaseEntity.setProjectId(entity.getProjectId());
                                                    phaseEntity.setRecord(ss.toString());
                                                    phaseEntity.setFilingCertificate(ss.toString());
                                                    projectPhaseService.addProjectPhase(phaseEntity);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        log.error("data长度:" + data.size());
        projectProcessDocumentationService.addAll(data);
        if (entity.getWorkType().equals("监理总监总代管理")) {
            OmTagEntity tag = omTagService.getTagByName("监理单位");
            instanceList.forEach(i -> {
                if (i.getVariableName().equals("data")) {
                    Map<String, Object> map = (Map<String, Object>) i.getValue();
                    String userId = (String) map.get("userId");
                    log.error("监理人员id"+userId);
                    if (!StringUtils.isEmpty(userId)) {
                        OmTagPostEntity omTagPostEntity = omTagPostService.getPostNameByTagIdAndName(tag.getSysid(), "总监理工程师");
                        ProjectCompanyTagUserEntity user = projectCompanyTagUserService.getUser(entity.getProjectId(), tag.getSysid());
                        if (omTagPostEntity != null) {
                            log.error("开始修改总监理工程师--------------------");
                            omTagRelPostAccountService.insertOne(omTagPostEntity.getSysid(), userId, entity.getProjectId(), entity.getCreateUserId());
                        }
                        if(user!=null){
                            log.error("开始变更负责人--------------------");
                            projectCompanyTagUserService.putUser(user.getTagId(),user.getProjectId(),userId);
                        }else{
                            projectCompanyTagUserService.putUser(tag.getSysid(),user.getProjectId(),userId);
                        }
                    }
                }
            });
        }
    }

    /*
     * 获取微信授权码
     * */
    private Map<String,Object> getToken(){
        String corpId = env.getProperty("weixin.corpId");
        String secret = env.getProperty("weixin.secret");
        String url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpId+"&corpsecret="+secret;
        return restTemplate.getForObject(url, Map.class);
    }
    /*
    * 微信消息推送
    * */
    private void weChatMessagePush(String userId){
        OmAccountEntity user = omAccountService.getUser(userId);
        if(user!=null) {
            if (!StringUtils.isEmpty(user.getOpenid())) {
                Map<String, Object> map = getToken();
                if (map.get("errmsg").equals("ok")) {
                    Object access_token = map.get("access_token");
                    String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + access_token;
                    Task task = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list().get(0);
                    ActivitiWorkFlowEntity entity = activitiWorkFlowService.selectByProcessId(task.getProcessInstanceId());
                    ProjectProjectEntity project = projectProjectService.getProject(entity.getProjectId());
                    Map<String, Object> data = new HashMap<>();
                    data.put("touser", user.getOpenid());
                    data.put("msgtype", "text");
                    data.put("agentid", env.getProperty("weixin.agentId"));
                    Map<String, Object> body = new HashMap<>();
                    body.put("content", "您有一个新的待办消息\n来自：" + project.getName() + "\n流程名称：" + entity.getProcessName() + "\n任务名称：" + task.getName() + "\n发起人：" + entity.getCreateUser());
                    data.put("text", body);
                    data.put("enable_id_trans", 0);
                    data.put("enable_duplicate_check", 0);
                    data.put("duplicate_check_interval", 1800);
                    ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, data, Map.class);
                    Map entityBody = responseEntity.getBody();
                    if (entityBody.get("errmsg").equals("ok")) {
                        log.error("推送成功!");
                    } else {
                        log.error("推送失败!");
                        log.error(entityBody.toString());
                    }
                } else {
                    log.error("获取token授权码失败!");
                }
            } else {
                log.error("此用户未绑定微信!");
            }
        }else{
            log.error("此用户不存在!");
        }
    }
}
