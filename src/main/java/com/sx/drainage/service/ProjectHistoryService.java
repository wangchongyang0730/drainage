package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectHistoryEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 9:46
 */
public interface ProjectHistoryService extends IService<ProjectHistoryEntity> {

    /*
    * 添加历史足迹
    * */
    void addHistory(ProjectHistoryEntity historyEntity, String userId);
    /*
    * 获取历史足迹
    * */
    List<ProjectHistoryEntity> getHistory(String userId);
    /*
    * 删除历史足迹
    * */
    void deleteByProjectId(String sysId);
}
