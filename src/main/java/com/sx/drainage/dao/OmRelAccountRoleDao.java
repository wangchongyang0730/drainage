package com.sx.drainage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.entity.OmRelAccountRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:33:45
 */
@Mapper
public interface OmRelAccountRoleDao extends BaseMapper<OmRelAccountRoleEntity> {
    //获取用户所拥有的的角色Id
    List<String> getAllRoleId(@Param("accountId") String accountId);
    //获取角色所对应的所有用户Id
    List<String> getAllUserId(@Param("roleId") String roleId);
}
