package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmTagPostEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:32:35
 */
public interface OmTagPostService extends IService<OmTagPostEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取角色所关联的所有岗位
    List<Map<String, Object>> getAllPostByIds(List<String> postId);
    //在该组织下新增岗位
    void inserPostion(String tag_id, String name, Integer enable, String remark, String sort);
    //在该组织下修改岗位信息
    void updatePostion(String sysId, String name, Integer enable, String remark, String sort);
    //在该组织下删除岗位信息
    void deletePostion(String sysId);
    //获取用户所在岗位
    String getPostName(String postId);
    //获取用户所在岗位和id
    Map<String, Object> getPostNameAndId(String postId);
    //获取组织id
    String getTagId(String post_id);
    //获取岗位名称
    OmTagPostEntity getPostNameByTagIdAndName(String tagId, String substring);
    //根据岗位名称获取岗位
    OmTagPostEntity getPostNameByName(String name);
    //修改参建单位时移除岗位下对应用户
    List<String> removeJoinUser(String tagid, String projectId);
}

