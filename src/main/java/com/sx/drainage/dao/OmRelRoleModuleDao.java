package com.sx.drainage.dao;

import com.sx.drainage.entity.OmRelRoleModuleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-02 11:35:38
 */
@Mapper
public interface OmRelRoleModuleDao extends BaseMapper<OmRelRoleModuleEntity> {
    //获取所有角色关联的模块Id
    List<String> getAllModuleId(@Param("roleId") String roleId);
}
