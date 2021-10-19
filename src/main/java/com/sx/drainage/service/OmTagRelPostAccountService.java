package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmTagRelPostAccountEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:30:40
 */
public interface OmTagRelPostAccountService extends IService<OmTagRelPostAccountEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取用户所关联的项目id
    List<String> getAllProjectId(String userId);
    //获取用户所在岗位
    String getPostName(String sysid);
    //获取用户所在岗位和id
    Map<String,Object> getPostNameAndId(String sysid);
    //获取岗位下所有用户id
    List<String> getPostHaveUser(String postId, String projectId);
    //获取fkid
    String getFkId(String sysid, String tag_id,String projectId);
    //新增岗位人员
    void insertPostionUser(String post_id, String fk_id, String projectId, String userId);
    //移除岗位人员
    void updatePostionUser(String post_id, String fk_id, String projectId, String userId);
    //设置总监理工程师
    void insertOne(String sysid, String userId, String projectId, String createUserId);
    //获取总监理工程师
    List<Map<String, Object>> getSupervisoryEngineer(String tagId, String projectId, String postName);
    //修改只能拥有一个人员的岗位
    void updateLeadersInCharge(String sysid, String projectId, String userId,String user);
    //修改参建单位时移除岗位下对应用户
    void removeJoinUser(String tagid, String projectId);
}

