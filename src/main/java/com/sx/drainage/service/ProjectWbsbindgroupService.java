package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsbindgroupEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 17:00:17
 */
public interface ProjectWbsbindgroupService extends IService<ProjectWbsbindgroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //删除wbs
    void deleteWbs(String sysid);
    //根据wbsId
    ProjectWbsbindgroupEntity getWbsBind(String wbsId);
    //获取所有已绑定的
    String[] getNotWbsId(String wbsId);
    //添加
    void addWbsBind(ProjectWbsbindgroupEntity binds);
    //修改
    void updateWbsBind(ProjectWbsbindgroupEntity wbsBind);
    //解除wbs与树节点关系
    void deleteBindWbsGroup(String wbsId);
    //根据构建id获取wbsId
    String getWbsId(String id);
}

