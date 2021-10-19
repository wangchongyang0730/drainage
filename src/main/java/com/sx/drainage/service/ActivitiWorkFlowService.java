package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ActivitiWorkFlowEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/27
 * Time: 9:50
 */
public interface ActivitiWorkFlowService extends IService<ActivitiWorkFlowEntity> {

    /*
     * 添加流程存档
     * */
    void add(ActivitiWorkFlowEntity entity);
    /*
    * 根据类型和项目id查询流程存档
    * */
    List<ActivitiWorkFlowEntity> selectByWorkTypeAndProjectId(String workType,String projectId);
    /*
    * 根据流程实例id查询流程存档
    * */
    ActivitiWorkFlowEntity selectByProcessId(String processId);
    /*
    * 根据主键id删除流程存档
    * */
    boolean deleteBySysId(String sysId);
    /*
    * 修改流程存档
    * */
    void update(ActivitiWorkFlowEntity entity);
    /*
    * 判断文件是否存在
    * */
    Boolean checkFile(String sysId, String workType);
    /*
    * 获取整改完成情况
    * */
    Map<String, Object> getRectificationInformation(String projectId);
}
