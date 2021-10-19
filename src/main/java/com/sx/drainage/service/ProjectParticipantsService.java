package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectParticipantsEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:53:39
 */
public interface ProjectParticipantsService extends IService<ProjectParticipantsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

