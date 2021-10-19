package com.sx.drainage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.entity.OmRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:34:36
 */
@Mapper
public interface OmRoleDao extends BaseMapper<OmRoleEntity> {
    //通过角色Id获取角色
	List<OmRoleEntity> getAllMyRole(@Param("roleId") List<String> roleId);
}
