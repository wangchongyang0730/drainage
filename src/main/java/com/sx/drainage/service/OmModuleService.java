package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmModuleEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-27 11:00:53
 */
public interface OmModuleService extends IService<OmModuleEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取用户权限菜单详细信息
    List<Map<String, Object>> getPermissionList(String userId);
    //获取当前用户菜单
    List<Map<String, Object>> getMenu(String userId);
    //获取当前用户菜单（分项目权限）
    List<Map<String,Object>> getProjectMenu(String userId,String projectId);
    //获取用户权限菜单详细信息（分项目权限）
    List<Map<String,Object>> getProjectPermissionList(String userId,String projectId);
    //获取菜单
    List<OmModuleEntity> getModuleList(String sysId);
    //获取单个菜单或权限详细信息
    OmModuleEntity getModuleBySysId(String sysId);
    //修改模块中的菜单
    void updateModule(String sysId, String parentId, String name, String sequenceNum, String path, String iconCss, Boolean hide);
    //修改模块中的功能
    void updateModuleFunction(String sysId, String parentId, String name, String path, Boolean hide);
    //删除
    void deleteModuleFunction(String sysId);
    //添加
    void insertModule(String parentId, String name, String path, Boolean hide);
    //根据id获取所有模块
    List<Map<String, Object>> getAllModuleByIds(List<String> moduleId);
    //获取所有
    List<OmModuleEntity> getAll();
    //获取用户权限菜单详细信息
    List<Map<String, Object>> getJurisdiction(List<String> modules);
    //获取当前用户菜单
    List<Map<String, Object>> getAllMenu(List<String> modules);
    //如果是超级管理员获取所有权限
    List<Map<String, Object>> getAllJurisdiction();
    //如果是超级管理员获取所有菜单
    List<Map<String, Object>> getAllMenuNotInRoleIds();
}

