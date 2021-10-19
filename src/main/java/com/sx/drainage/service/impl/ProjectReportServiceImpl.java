package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.QueryParams;
import com.sx.drainage.params.ReportParams;
import com.sx.drainage.service.ProjectWbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectReportDao;
import com.sx.drainage.entity.ProjectReportEntity;
import com.sx.drainage.service.ProjectReportService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectReportService")
@Transactional
public class ProjectReportServiceImpl extends ServiceImpl<ProjectReportDao, ProjectReportEntity> implements ProjectReportService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ProjectWbsService projectWbsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectReportEntity> page = this.page(
                new Query<ProjectReportEntity>().getPage(params),
                new QueryWrapper<ProjectReportEntity>()
        );
        return new PageUtils(page);
    }
    //获取报告列表
    @Override
    public Map<String, Object> getPageList(Integer currentPage, Integer pageSize, String type, String projectId) {
        QueryParams params = new QueryParams();
        params.setId(projectId);
        params.setSort(type);
        params.setPageIndex(pageSize*(currentPage-1)+1);
        params.setPageSize(pageSize*currentPage);
        List<Map<String, Object>> list = baseMapper.getAllReport(params);
        int size = baseMapper.getCount(params).size();
        Map<String, Object> map = new HashMap<>();
        map.put("rows",list);
        map.put("total",size);
        return map;
    }
    //抛弃老流程后的获取报告列表
    @Override
    public Map<String, Object> getPageList2(Integer currentPage, Integer pageSize, String type, String projectId) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        IPage<ProjectReportEntity> page = this.page(
                new Query<ProjectReportEntity>().getPage(params),
                new QueryWrapper<ProjectReportEntity>().eq("projectId",projectId).eq("type",type).eq("del",0)
        );
        PageUtils pageUtils = new PageUtils(page);
        List<Map<String, Object>> res = new ArrayList<>();
        List<ProjectReportEntity> list = (List<ProjectReportEntity>) pageUtils.getList();
        for(int i=0;i<list.size();i++){
            ProjectWbsEntity wbs = projectWbsService.getWbs(list.get(i).getWbsid());
            Map<String, Object> map = new HashMap<>();
            map.put("ProcState",list.get(i).getEmittime()==null?1:2);
            map.put("rowNum",i+1);
            map.put("createDate",list.get(i).getCreatedate()==null?"":format.format(list.get(i).getCreatedate()));
            map.put("createUser",list.get(i).getCreateuser());
            map.put("del",list.get(i).getDel());
            map.put("filing",list.get(i).getFiling());
            map.put("name",list.get(i).getName());
            map.put("projectId",list.get(i).getProjectid());
            map.put("result",list.get(i).getResult());
            map.put("securityGrade",list.get(i).getSecuritygrade());
            map.put("sysId",list.get(i).getSysid());
            map.put("type",list.get(i).getType());
            map.put("uploadfile",list.get(i).getUploadfile());
            map.put("uploadfile2",list.get(i).getUploadfile2());
            map.put("partName",wbs.getPartname());
            map.put("wbsId",list.get(i).getWbsid());
            map.put("_reporttime",list.get(i).getReporttime()==null?"":format.format(list.get(i).getReporttime()));
            map.put("_emittime",list.get(i).getEmittime()==null?"":format.format(list.get(i).getEmittime()));
            map.put("_plantime",list.get(i).getPlantime()==null?"":format.format(list.get(i).getPlantime()));
            res.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rows",res);
        map.put("total",pageUtils.getTotalCount());
        return map;
    }
    //获取安全和质量信息
    @Override
    public List<ProjectReportEntity> getNoProcessRectification(String projectId, String type) {
        return this.list(new QueryWrapper<ProjectReportEntity>().eq("projectId",projectId).eq("type",type).eq("del",0));
    }

    //获取详细信息
    @Override
    public Map<String, Object> get3(String id) {
        Map<String, Object> map = new HashMap<>();
        ProjectReportEntity entity = this.getById(id);
        if(entity!=null){
            ProjectWbsEntity wbs = projectWbsService.getWbs(entity.getWbsid());
            map.put("name",entity.getName());
            map.put("sysId",entity.getSysid());
            map.put("wbsId",entity.getWbsid());
            map.put("remarks",entity.getRemarks());
            map.put("result",entity.getResult());
            map.put("type",entity.getType());
            map.put("del",entity.getDel());
            map.put("uploadfile",entity.getUploadfile());
            map.put("uploadfile2",entity.getUploadfile2());
            map.put("createDate",entity.getCreatedate());
            map.put("createUser",entity.getCreateuser());
            map.put("projectId",entity.getProjectid());
            map.put("deleteDate",entity.getDeletedate());
            map.put("deleteUser",entity.getDeleteuser());
            map.put("updateDate",entity.getUpdatedate());
            map.put("updateUser",entity.getUpdateuser());
            map.put("reportPerson",entity.getReportperson());
            map.put("schemeType",entity.getSchemetype());
            map.put("_plantime",entity.getPlantime());
            map.put("_emittime",entity.getEmittime());
            map.put("_reporttime",entity.getReporttime());
            map.put("securityGrade",entity.getSecuritygrade());
            map.put("filing",entity.getFiling());
            map.put("partName",wbs.getPartname());
        }
        return map;
    }
    //修改
    @Override
    public void put(ReportParams reportParams,String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(reportParams.getSysId());
        entity.setName(reportParams.getName());
        entity.setProjectid(reportParams.getProjectId());
        entity.setRemarks(reportParams.getRemarks());
        entity.setUploadfile(reportParams.getUploadfile());
        entity.setWbsid(reportParams.getWbsId());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        entity.setFiling(reportParams.getFiling());
        entity.setPlantime(reportParams.getPlanTime()==null?null:reportParams.getPlanTime());
        entity.setEmittime(reportParams.getEmitTime()==null?null:reportParams.getEmitTime());
        this.updateById(entity);
    }
    //添加
    @Override
    public void post1(ReportParams reportParams, String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(reportParams.getName());
        entity.setProjectid(reportParams.getProjectId());
        entity.setRemarks(reportParams.getRemarks());
        entity.setUploadfile(reportParams.getUploadfile());
        entity.setWbsid(reportParams.getWbsId());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setReporttime(new Date());
        entity.setType("1");
        entity.setDel(0);
        entity.setFiling(reportParams.getFiling());
        entity.setSecuritygrade(reportParams.getSecurityGrade());
        entity.setPlantime(reportParams.getPlanTime()==null?null:reportParams.getPlanTime());
        entity.setEmittime(reportParams.getEmitTime()==null?null:reportParams.getEmitTime());
        this.save(entity);
    }
    //添加
    @Override
    public void post2(ReportParams reportParams, String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(reportParams.getName());
        entity.setProjectid(reportParams.getProjectId());
        entity.setRemarks(reportParams.getRemarks());
        entity.setUploadfile(reportParams.getUploadfile());
        entity.setWbsid(reportParams.getWbsId());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setReporttime(new Date());
        entity.setType("2");
        entity.setDel(0);
        entity.setFiling(reportParams.getFiling());
        entity.setSecuritygrade(reportParams.getSecurityGrade());
        entity.setPlantime(reportParams.getPlanTime()==null?null:reportParams.getPlanTime());
        entity.setEmittime(reportParams.getEmitTime()==null?null:reportParams.getEmitTime());
        this.save(entity);
    }
    //添加
    @Override
    public void post3(ReportParams reportParams, String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(reportParams.getName());
        entity.setProjectid(reportParams.getProjectId());
        entity.setRemarks(reportParams.getRemarks());
        entity.setUploadfile(reportParams.getUploadfile());
        entity.setWbsid(reportParams.getWbsId());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setReporttime(new Date());
        entity.setType("3");
        entity.setDel(0);
        entity.setFiling(reportParams.getFiling());
        entity.setSecuritygrade(reportParams.getSecurityGrade());
        entity.setPlantime(reportParams.getPlanTime()==null?null:reportParams.getPlanTime());
        entity.setEmittime(reportParams.getEmitTime()==null?null:reportParams.getEmitTime());
        this.save(entity);
    }
    //添加
    @Override
    public void post5(ReportParams reportParams, String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setName(reportParams.getName());
        entity.setProjectid(reportParams.getProjectId());
        entity.setRemarks(reportParams.getRemarks());
        entity.setUploadfile(reportParams.getUploadfile());
        entity.setWbsid(reportParams.getWbsId());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setReporttime(new Date());
        entity.setType("5");
        entity.setDel(0);
        entity.setFiling(reportParams.getFiling());
        entity.setSecuritygrade(reportParams.getSecurityGrade());
        entity.setPlantime(reportParams.getPlanTime()==null?null:reportParams.getPlanTime());
        entity.setEmittime(reportParams.getEmitTime()==null?null:reportParams.getEmitTime());
        this.save(entity);
    }
    //删除
    @Override
    public void delete(String sysId,String userId) {
        ProjectReportEntity entity = new ProjectReportEntity();
        entity.setSysid(sysId);
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.updateById(entity);
    }
    //获取安全和质量信息
    @Override
    public List<Map<String, Object>> getAllAnQuanOrZhiLiang(String projectId, String wbsId, String type,String partName) {
        List<ProjectReportEntity> list = this.list(new QueryWrapper<ProjectReportEntity>().eq("projectId", projectId).eq("wbsId", wbsId).eq("type", type).eq("del", 0));
        List<Map<String, Object>> res = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("ProcState",li.getEmittime()==null?1:2);
            map.put("createDate",li.getCreatedate()==null?"":format.format(li.getCreatedate()));
            map.put("createUser",li.getCreateuser());
            map.put("del",li.getDel());
            map.put("filing",li.getFiling());
            map.put("name",li.getName());
            map.put("projectId",li.getProjectid());
            map.put("result",li.getResult());
            map.put("securityGrade",li.getSecuritygrade());
            map.put("sysId",li.getSysid());
            map.put("type",li.getType());
            map.put("uploadfile",li.getUploadfile());
            map.put("uploadfile2",li.getUploadfile2());
            map.put("partName",partName);
            map.put("wbsId",li.getWbsid());
            map.put("_reporttime",li.getReporttime()==null?"":format.format(li.getReporttime()));
            map.put("_emittime",li.getEmittime()==null?"":format.format(li.getEmittime()));
            map.put("_plantime",li.getPlantime()==null?"":format.format(li.getPlantime()));
            res.add(map);
        });
        return res;
    }

}