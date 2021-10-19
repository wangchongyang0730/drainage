package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.params.AddRoleFunction;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:34:36
 */
public interface OmRoleService extends IService<OmRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //通过角色Id获取角色
    List<OmRoleEntity> getAllMyRole(List<String> roleId);
    //获取角色列表
    List<Map<String, Object>> getRoleList(String where);
    //获取单个角色
    List<OmRoleEntity> getOneRole(String s);
    //新增角色
    void insertRole(String name, String valid, String description);
    //更新角色
    void updateRole(String sysId, String name, String valid, String description);
    //根据角色ID删除信息
    void deleteRoleById(String sysId);
    //获取该角色绑定的用户/功能/岗位列表引用数据表
    R getRoleUserFunctionPost(String sysId, String type);
    //获取角色与所有用户的关系列表
    Map<String, Object> getRoleUserList(String sysid, String name, Integer pageRecord, Integer page);
    //新增、移除角色与用户关系
    void insertDeleteRoleUser(String sysId, String userid, String action);
    //获取角色与功能列表
    Map<String, Object> getRoleFunctionList(String roleid);
    //保存角色与功能关系
    void saveRoleFunction(AddRoleFunction addRoleFunction);
    //根据id获取所有角色
    List<OmRoleEntity> getAllRole();
}

