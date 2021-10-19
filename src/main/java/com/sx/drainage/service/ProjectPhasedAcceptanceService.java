package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectPhasedAcceptanceEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/16
 * Time: 13:49
 */
public interface ProjectPhasedAcceptanceService extends IService<ProjectPhasedAcceptanceEntity> {
    /*
    * 获取阶段性验收文件
    * */
    List<ProjectPhasedAcceptanceEntity> getAll(String projectId);
    /*
    * 添加阶段性验收文件
    * */
    void addPhasedAcceptanceFile(ProjectPhasedAcceptanceEntity projectPhasedAcceptanceEntity);
    /*
    * 获取所有阶段性验收文件
    * */
    List<ProjectPhasedAcceptanceEntity> getAllFile();
}
