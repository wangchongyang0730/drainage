package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectNotificationEntity;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/1
 * Time: 9:49
 */
public interface ProjectNotificationService extends IService<ProjectNotificationEntity> {
    /*
    * 添加消息
    * */
    void add(ProjectNotificationEntity entity);
    /*
    * 获取所有消息列表
    * */
    List<Map<String, Object>> getMyMessages(String userId);
    /*
    * 消息状态变更
    * */
    void read(String sysId);
    /*
    * 批量添加
    * */
    void addByList(List<ProjectNotificationEntity> list);
    /*
    * 报送
    * */
    void sendMessage(ProjectNotificationEntity projectNotificationEntity, String userId);
}
