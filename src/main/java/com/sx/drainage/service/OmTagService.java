package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmTagEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:45:32
 */
public interface OmTagService extends IService<OmTagEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取组织列表
    List<Map<String, Object>> getOrganizationalList();
    //新增组织
    void insertOrganization(String name, String remark, String tagType, String user_flag, Integer sort);
    //修改组织
    void updateOrganization(String sysid, String name, String remark, String tagType, String user_flag, Integer sort);
    //删除组织
    void deleteOrganization(String sysid);
    //获取该组织下的岗位列表
    Map<String, Object> getPositionsList(String tag_id, Integer page, Integer pageRecord);
    //在该组织下新增岗位
    void inserPostion(String tag_id, String name, Integer enable, String remark, String sort);
    //在该组织下修改岗位信息
    void updatePostion(String sysId, String name, Integer enable, String remark, String sort);
    //在该组织下删除岗位信息
    void deletePostion(String sysId);
    //获取该项目设置组织下的岗位列表
    Map<String, Object> getProjectPositionsList(String tag_id, Integer page, Integer pageRecord, String projectId);
    //获取登录管理员所在的参建单位人员信息列表
    Map<String, Object> getPostionUsersList(String post_id, String where, Integer pages, Integer pageRecord, String projectId);
    //新增岗位人员
    void insertPostionUser(String post_id, String fk_id, String projectId,String userId);
    //移除岗位人员
    void updatePostionUser(String post_id, String fk_id, String projectId, String userId);
    //获取该岗位角色关系列表
    List<Map<String, Object>> getPostionRoleList(String post_id,String projectId);
    //新增岗位与角色关系
    void insertPostionRole(String post_id, String role_id,String userId ,String projectId);
    //移除岗位与角色关系
    void updatePostionRole(String post_id, String role_id, String userId ,String projectId);
    //获取所有参建单位类型
    List<OmTagEntity> getAllTag();
    //根据单位名称获取单位
    OmTagEntity getTagByName(String substring);
    //获取总监理工程师
    List<Map<String, Object>> getSupervisoryEngineer(String projectId, String tagName, String postName);
    //修改项目分管领导
    boolean updateLeadersInCharge(String projectId, String userId, String user, String managerId, String department);
}

