package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.OmTagPostDao;
import com.sx.drainage.entity.OmTagPostEntity;
import com.sx.drainage.service.OmTagPostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;


@Service("omTagPostService")
@Transactional
public class OmTagPostServiceImpl extends ServiceImpl<OmTagPostDao, OmTagPostEntity> implements OmTagPostService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String search = (String) params.get("search");
        if(search!=null){
            IPage<OmTagPostEntity> page = this.page(
                    new Query<OmTagPostEntity>().getPage(params),
                    new QueryWrapper<OmTagPostEntity>().eq("del",0).eq("tag_id",search)
            );
            return new PageUtils(page);
        }else{
            IPage<OmTagPostEntity> page = this.page(
                    new Query<OmTagPostEntity>().getPage(params),
                    new QueryWrapper<OmTagPostEntity>().eq("del",0)
            );
            return new PageUtils(page);
        }

    }
    //获取角色所关联的所有岗位
    @Override
    public List<Map<String, Object>> getAllPostByIds(List<String> postId) {
        List<OmTagPostEntity> list = this.list(new QueryWrapper<OmTagPostEntity>().in("sysId", postId).eq("del", 0));
        List<Map<String, Object>> maps=new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("fullname",li.getSysid());
            map.put("description",li.getRemark());
            maps.add(map);
        });
        return maps;
    }
    //在该组织下新增岗位
    @Override
    public void inserPostion(String tag_id, String name, Integer enable, String remark, String sort) {
        OmTagPostEntity entity = new OmTagPostEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setTagId(tag_id);
        entity.setName(name);
        entity.setEnable(enable);
        entity.setRemark(remark);
        entity.setSort(sort);
        entity.setDel(0);
        this.save(entity);
    }
    //在该组织下修改岗位信息
    @Override
    public void updatePostion(String sysId, String name, Integer enable, String remark, String sort) {
        OmTagPostEntity entity = new OmTagPostEntity();
        entity.setSysid(sysId);
        entity.setName(name);
        entity.setEnable(enable);
        entity.setRemark(remark);
        entity.setSort(sort);
        this.updateById(entity);
    }
    //在该组织下删除岗位信息
    @Override
    public void deletePostion(String sysId) {
        this.removeById(sysId);
    }
    //获取用户所在岗位
    @Override
    public String getPostName(String postId) {
        return this.getById(postId)==null?"":this.getById(postId).getName();
    }
    //获取用户所在岗位和id
    @Override
    public Map<String, Object> getPostNameAndId(String postId) {
        OmTagPostEntity entity = this.getById(postId);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",entity.getSysid());
        map.put("name",entity.getName());
        return map;
    }
    //获取组织id
    @Override
    public String getTagId(String post_id) {
        return this.getById(post_id).getTagId();
    }
    //获取岗位名称
    @Override
    public OmTagPostEntity getPostNameByTagIdAndName(String tagId, String substring) {
        List<OmTagPostEntity> list = this.list(new QueryWrapper<OmTagPostEntity>().eq("tag_id", tagId).eq("name", substring).eq("del", 0));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    //根据岗位名称获取岗位
    @Override
    public OmTagPostEntity getPostNameByName(String name) {
        List<OmTagPostEntity> list = this.list(new QueryWrapper<OmTagPostEntity>().eq("name", name).eq("del", 0));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    //修改参建单位时移除岗位下对应用户
    @Override
    public List<String> removeJoinUser(String tagid, String projectId) {
        List<String> id = new ArrayList<>();
        List<OmTagPostEntity> list = this.list(new QueryWrapper<OmTagPostEntity>().eq("tag_id", tagid));
        if(list!=null&&list.size()>0){
            list.forEach(li -> {
                id.add(li.getSysid());
            });
        }
        return id;
    }
}