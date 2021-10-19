package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.entity.ProjectWbsbindgrouprectificationEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:28:08
 */
public interface ProjectWbsbindgrouprectificationService extends IService<ProjectWbsbindgrouprectificationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取无流程提交质量和安全信息
    Map<String, Object> getNoProcessRectification(String projectId, Integer type);
    //删除wbs
    void deleteWbs(String sysid);
    //获取质量或安全信息
    Map<String, Object> getAnQuanOrZhiLiang(ProjectWbsEntity wbsEntity, int i);
}

