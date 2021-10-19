package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectProjectcheckEntity;
import com.sx.drainage.params.ProjectCheckParams;

import java.util.Map;

/**
 * 项目验收
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-22 14:49:56
 */
public interface ProjectProjectcheckService extends IService<ProjectProjectcheckEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表
    R getPageList(String projectId, Integer pageSize, Integer currentPage, String sort, Boolean isAsc);
    //获取详细信息
    R getDetails(String id);
    //添加
    void add(ProjectCheckParams projectCheckParams, String userId);
    //修改
    void put(ProjectCheckParams projectCheckParams, String userId);
    //删除
    void delete(String sysId, String userId);
}

