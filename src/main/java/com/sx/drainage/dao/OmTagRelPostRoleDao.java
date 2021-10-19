package com.sx.drainage.dao;

import com.sx.drainage.entity.OmTagRelPostRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:38:29
 */
@Mapper
public interface OmTagRelPostRoleDao extends BaseMapper<OmTagRelPostRoleEntity> {
    //获取角色关联的所有岗位Id
    List<String> getAllPostId(@Param("roleId") String roleId);
}
