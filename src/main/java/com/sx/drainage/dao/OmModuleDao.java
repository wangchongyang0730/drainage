package com.sx.drainage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.entity.OmModuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-27 11:00:53
 */
@Mapper
public interface OmModuleDao extends BaseMapper<OmModuleEntity> {

    //获取用户权限菜单详细信息
	List<OmModuleEntity> getPermissionList(@Param("userId") String userId);
	//获取当前用户菜单
    List<OmModuleEntity> getMenu(@Param("userId") String userId);
    //获取当前用户菜单（分项目权限）
    List<OmModuleEntity> getProjectMenu(@Param("userId") String userId,@Param("projectId") String projectId);
    //获取用户权限菜单详细信息（分项目权限）
    List<OmModuleEntity> getProjectPermissionList(@Param("userId") String userId,@Param("projectId") String projectId);
}
