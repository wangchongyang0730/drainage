package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.params.ProjectCheckParams;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectProjectcheckDao;
import com.sx.drainage.entity.ProjectProjectcheckEntity;
import com.sx.drainage.service.ProjectProjectcheckService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectProjectcheckService")
@Transactional
public class ProjectProjectcheckServiceImpl extends ServiceImpl<ProjectProjectcheckDao, ProjectProjectcheckEntity> implements ProjectProjectcheckService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectProjectcheckEntity> page = this.page(
                new Query<ProjectProjectcheckEntity>().getPage(params),
                new QueryWrapper<ProjectProjectcheckEntity>()
        );

        return new PageUtils(page);
    }
    //获取分页列表
    @Override
    public R getPageList(String projectId, Integer pageSize, Integer currentPage, String sort, Boolean isAsc) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        QueryWrapper<ProjectProjectcheckEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId",projectId).eq("del",0);
        if(!StringUtils.isEmpty(sort)&&!sort.equals("''")){
            if(isAsc){
                wrapper.orderByAsc(sort);
            }else{
                wrapper.orderByDesc(sort);
            }
        }
        IPage<ProjectProjectcheckEntity> page = this.page(
                new Query<ProjectProjectcheckEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<ProjectProjectcheckEntity> list = (List<ProjectProjectcheckEntity>) pageUtils.getList();
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("type",li.getType());
            entity.put("wbsId",li.getWbsid());
            entity.put("wbsName",li.getWbsname());
            entity.put("constructWarranty",li.getConstructwarranty());
            entity.put("constructRemark",li.getConstructionremark());
            entity.put("constructFile",li.getConstructfile());
            entity.put("constructionRemark",li.getConstructionremark());
            entity.put("constructionFile",li.getConstructionfile());
            entity.put("supervisorRemark",li.getSupervisorremark());
            entity.put("supervisorFile",li.getSupervisorfile());
            entity.put("projectId",li.getProjectid());
            entity.put("checkStatus",li.getCheckstatus());
            entity.put("del",li.getDel());
            entity.put("createUser",li.getCreateuser());
            entity.put("createDate",li.getCreatedate());
            entity.put("updateUser",li.getUpdateuser());
            entity.put("updateDate",li.getUpdatedate());
            entity.put("deleteUser",li.getDeleteuser());
            entity.put("deleteDate",li.getDeletedate());
            entity.put("shbuploadfile",li.getShbuploadfile());
            data.add(entity);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("total",pageUtils.getTotalCount());
        map.put("rows",data);
        return R.ok(1,"获取成功!",map,true,null);
    }
    //获取详细信息
    @Override
    public R getDetails(String id) {
        ProjectProjectcheckEntity li = this.getById(id);
        Map<String, Object> entity = new HashMap<>();
        entity.put("sysId",li.getSysid());
        entity.put("type",li.getType());
        entity.put("wbsId",li.getWbsid());
        entity.put("wbsName",li.getWbsname());
        entity.put("constructWarranty",li.getConstructwarranty());
        entity.put("constructRemark",li.getConstructionremark());
        entity.put("constructFile",li.getConstructfile());
        entity.put("constructionRemark",li.getConstructionremark());
        entity.put("constructionFile",li.getConstructionfile());
        entity.put("supervisorRemark",li.getSupervisorremark());
        entity.put("supervisorFile",li.getSupervisorfile());
        entity.put("projectId",li.getProjectid());
        entity.put("checkStatus",li.getCheckstatus());
        entity.put("del",li.getDel());
        entity.put("createUser",li.getCreateuser());
        entity.put("createDate",li.getCreatedate());
        entity.put("updateUser",li.getUpdateuser());
        entity.put("updateDate",li.getUpdatedate());
        entity.put("deleteUser",li.getDeleteuser());
        entity.put("deleteDate",li.getDeletedate());
        entity.put("shbuploadfile",li.getShbuploadfile());
        return R.ok(1,"获取成功!",entity,true,null);
    }
    //添加
    @Override
    public void add(ProjectCheckParams projectCheckParams, String userId) {
        ProjectProjectcheckEntity entity = new ProjectProjectcheckEntity();
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setSysid(CreateUuid.uuid());
        entity.setConstructionfile(projectCheckParams.getConstructionFile());
        entity.setConstructionremark(projectCheckParams.getConstructionRemark());
        entity.setProjectid(projectCheckParams.getProjectId());
        entity.setType(Integer.parseInt(projectCheckParams.getType()));
        this.save(entity);
    }
    //修改
    @Override
    public void put(ProjectCheckParams projectCheckParams, String userId) {
        ProjectProjectcheckEntity entity = new ProjectProjectcheckEntity();
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setSysid(projectCheckParams.getSysId());
        entity.setConstructionfile(projectCheckParams.getConstructionFile());
        entity.setConstructionremark(projectCheckParams.getConstructionRemark());
        this.updateById(entity);
    }
    //删除
    @Override
    public void delete(String sysId, String userId) {
        ProjectProjectcheckEntity entity = new ProjectProjectcheckEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.updateById(entity);
    }

}