package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectParentProjectEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 13:41
 */
public interface ProjectParentProjectService extends IService<ProjectParentProjectEntity> {
    /*
    * 获取所有非段项目
    * */
    List<ProjectParentProjectEntity> getAll();
    /*
    * 添加非标段项目
    * */
    void add(String fileId, String name, String projectDescribe, String userId, String type, Boolean isBlock);
    /*
    * 非标段项目详情
    * */
    R getDetails(String projectId);
    /*
    * 非标段项目信息修改
    * */
    void updateDetails(ProjectParentProjectEntity projectParentProjectEntity);
    /*
    * 非标段项目删除
    * */
    void deleteById(String sysId);
}
