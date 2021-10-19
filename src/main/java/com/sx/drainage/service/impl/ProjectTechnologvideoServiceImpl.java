package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.params.VideoParams;
import com.sx.drainage.service.OmAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectTechnologvideoDao;
import com.sx.drainage.entity.ProjectTechnologvideoEntity;
import com.sx.drainage.service.ProjectTechnologvideoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectTechnologvideoService")
@Transactional
public class ProjectTechnologvideoServiceImpl extends ServiceImpl<ProjectTechnologvideoDao, ProjectTechnologvideoEntity> implements ProjectTechnologvideoService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private OmAccountService omAccountService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectTechnologvideoEntity> page = this.page(
                new Query<ProjectTechnologvideoEntity>().getPage(params),
                new QueryWrapper<ProjectTechnologvideoEntity>()
        );

        return new PageUtils(page);
    }
    //获取分页列表
    @Override
    public R getPageList(String type, String projectId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search, String queryJson) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        QueryWrapper<ProjectTechnologvideoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("del",0).eq("projectId",projectId).eq("type",type);
        if(!StringUtils.isEmpty(search)){
            wrapper.like("title",search);
        }
        sort=sort.substring(sort.lastIndexOf(",")+1,sort.length()-sort.lastIndexOf(",")-1);
        if(isAsc){
            wrapper.orderByAsc(sort);
        }else{
            wrapper.orderByDesc(sort);
        }
        IPage<ProjectTechnologvideoEntity> page = this.page(
                new Query<ProjectTechnologvideoEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<ProjectTechnologvideoEntity> list = (List<ProjectTechnologvideoEntity>) pageUtils.getList();
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("title",li.getTitle());
            map.put("path",li.getPath());
            map.put("createDate",li.getCreatedate());
            map.put("createUser",li.getCreateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("del",li.getDel());
            map.put("type",li.getType());
            map.put("imgUrl",li.getImgurl());
            map.put("projectId",li.getProjectid());
            data.add(map);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return R.ok(1,"获取成功!",res,true,null);
    }
    //新增
    @Override
    public void addVideo(VideoParams videoParams, String userId) {
        ProjectTechnologvideoEntity entity = new ProjectTechnologvideoEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setImgurl(videoParams.getImgUrl());
        entity.setPath(videoParams.getPath());
        entity.setProjectid(videoParams.getProjectId());
        entity.setTitle(videoParams.getTitle());
        entity.setType(videoParams.getType());
        entity.setDel(0);
        this.save(entity);
    }
    //获取单个详情
    @Override
    public R get(String id) {
        ProjectTechnologvideoEntity entity = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",entity.getSysid());
        map.put("title",entity.getTitle());
        map.put("path",entity.getPath());
        map.put("createDate",entity.getCreatedate()==null?"":format.format(entity.getCreatedate()));
        map.put("createUser",entity.getCreateuser());
        map.put("updateDate",entity.getUpdatedate()==null?"":format.format(entity.getUpdatedate()));
        map.put("updateUser",entity.getUpdateuser());
        map.put("del",entity.getDel());
        map.put("type",entity.getType());
        map.put("imgUrl",entity.getImgurl());
        map.put("projectId",entity.getProjectid());
        map.put("name",entity.getCreateuser()==null?"":omAccountService.getUser(entity.getCreateuser()).getName());
        return R.ok(1,null,map,true,null);
    }
    //修改
    @Override
    public void updateVideo(VideoParams videoParams, String userId) {
        ProjectTechnologvideoEntity entity = new ProjectTechnologvideoEntity();
        entity.setSysid(videoParams.getSysId());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setImgurl(videoParams.getImgUrl());
        entity.setPath(videoParams.getPath());
        entity.setTitle(videoParams.getTitle());
        this.updateById(entity);
    }
    //删除
    @Override
    public void delete(String sysId, String userId) {
        ProjectTechnologvideoEntity entity = new ProjectTechnologvideoEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        this.updateById(entity);
    }

}