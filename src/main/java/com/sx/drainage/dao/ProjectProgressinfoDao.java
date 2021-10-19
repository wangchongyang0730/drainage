package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectProgressinfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:58:17
 */
@Mapper
public interface ProjectProgressinfoDao extends BaseMapper<ProjectProgressinfoEntity> {
	//获取总名称
    List<String> getAllName(@Param("projectId") String projectId);
}
