package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectWbsbindgrouprectificationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.params.QueryParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:28:08
 */
@Mapper
public interface ProjectWbsbindgrouprectificationDao extends BaseMapper<ProjectWbsbindgrouprectificationEntity> {

    //获取无流程提交质量和安全信息
	List<Map<String,Object>> getNoProcessRectification(QueryParams queryParams);
}
