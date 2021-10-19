package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectWbsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 14:36:40
 */
@Mapper
public interface ProjectWbsDao extends BaseMapper<ProjectWbsEntity> {
    //获取项目当前作业面信息
    List<Map<String,Object>> getProjectNowSide(@Param("projectId") String projectId);
    //获取所有项目信息
    List<Map<String,Object>> getAllProjectDetails(@Param("projectId") String projectId);
    //获取详细信息
    Map<String ,Object> getDetail(@Param("sysId") String sysId);
}
