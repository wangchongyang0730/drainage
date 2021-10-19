package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmTagRelPostRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:38:29
 */
public interface OmTagRelPostRoleService extends IService<OmTagRelPostRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取角色关联的所有岗位Id
    List<String> getAllPostId(String sysId);
    //获取岗位绑定的所有角色id
    List<String> getAllRoleId(String post_id,String projectId);
    //新增岗位与角色关系
    void insertPostionRole(String post_id, String role_id,String userId,String projectId);
    //移除岗位与角色关系
    void updatePostionRole(String post_id, String role_id, String userId,String projectId);
}

