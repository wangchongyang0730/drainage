package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectNvrEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-24 11:23:29
 */
public interface ProjectNvrService extends IService<ProjectNvrEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取模型视频监控信息
    R getModelMonitoring(String projectId);
}

