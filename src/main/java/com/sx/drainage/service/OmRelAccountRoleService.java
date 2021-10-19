package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmRelAccountRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:33:45
 */
public interface OmRelAccountRoleService extends IService<OmRelAccountRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //获取用户所拥有的的角色Id
    List<String> getAllRoleId(String accountId);
    //删除用户角色关联
    void deleteByUserId(String userId);
    //获取角色所对应的所有用户Id
    List<String> getAllUserId(String sysId);
    //移除角色与用户关系
    void deleteRoleUser(String sysId, String userid);
    //新增角色与用户关系
    void insertRoleUser(String sysId, String userid);
}

