package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.R;
import com.sx.drainage.common.Sort;
import com.sx.drainage.params.QueryParams;
import com.sx.drainage.params.dsParams;
import com.sx.drainage.service.ProjectMonitordataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectMonitordailyDao;
import com.sx.drainage.entity.ProjectMonitordailyEntity;
import com.sx.drainage.service.ProjectMonitordailyService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectMonitordailyService")
@Transactional
public class ProjectMonitordailyServiceImpl extends ServiceImpl<ProjectMonitordailyDao, ProjectMonitordailyEntity> implements ProjectMonitordailyService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ProjectMonitordataService projectMonitordataService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectMonitordailyEntity> page = this.page(
                new Query<ProjectMonitordailyEntity>().getPage(params),
                new QueryWrapper<ProjectMonitordailyEntity>()
        );

        return new PageUtils(page);
    }
    //获取报表数据集合信息
    @Override
    public Map<String, Object> getMonitorDailyList(String reportId, Integer page, Integer pageRecord) {
        Map<String, Object> map = new HashMap<>();
        map.put("page",page.toString());
        map.put("limit",pageRecord.toString());
        IPage<ProjectMonitordailyEntity> iPage = this.page(new Query<ProjectMonitordailyEntity>().getPage(map),
                new QueryWrapper<ProjectMonitordailyEntity>().eq("del",0).eq("reportSysId", reportId));
        PageUtils pageUtils = new PageUtils(iPage);
        Map<String, Object> hashMap = new HashMap<>();
        List<ProjectMonitordailyEntity> list = (List<ProjectMonitordailyEntity>) pageUtils.getList();
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId",li.getSysid());
            entity.put("reportSysId",li.getReportsysid());
            entity.put("code",li.getCode());
            entity.put("monitorDate",li.getMonitordate()==null?li.getMonitordate():format.format(li.getMonitordate()));
            entity.put("weater",li.getWeater());
            entity.put("remark",li.getRemark());
            entity.put("createDate",li.getCreatedate()==null?li.getCreatedate():format.format(li.getCreatedate()));
            entity.put("createUser",li.getCreateuser());
            entity.put("updateDate",li.getUpdatedate()==null?li.getUpdatedate():format.format(li.getUpdatedate()));
            entity.put("updateUser",li.getUpdateuser());
            entity.put("deleteDate",li.getDeletedate()==null?li.getDeletedate():format.format(li.getDeletedate()));
            entity.put("deleteUser",li.getDeleteuser());
            entity.put("del",li.getDel());
            data.add(entity);
        });
        hashMap.put("total",pageUtils.getTotalCount());
        hashMap.put("rows",data);
        return hashMap;
    }
    //获取单个
    @Override
    public ProjectMonitordailyEntity getPr(String sysId) {
        return this.getById(sysId);
    }
    //根据reportSysId更改状态
    @Override
    public void deleteByReportSysId(String userId, String reportSysId) {
        ProjectMonitordailyEntity entity = new ProjectMonitordailyEntity();
        entity.setDel(1);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        this.update(entity,new UpdateWrapper<ProjectMonitordailyEntity>().eq("reportSysId",reportSysId));
    }
    //获取所有id
    @Override
    public List<String> getAllIdByReportSysId(String reportSysId) {
        List<ProjectMonitordailyEntity> list = this.list(new QueryWrapper<ProjectMonitordailyEntity>().eq("reportSysId", reportSysId));
        List<String> id = new ArrayList<>();
        list.forEach(li ->{
            id.add(li.getSysid());
        });
        return id;
    }
    //添加
    @Override
    public void addMonitorDaily(ProjectMonitordailyEntity entity) {
        this.save(entity);
    }
    //分页获取点位信息
    @Override
    public R getPagePointList(String reportSysId, String pointStartDate, String pointEndDate, Integer pageSize, Integer pageIndex, String sort, Boolean isAsc) {
        QueryParams params = new QueryParams();
        params.setId(reportSysId);
        params.setStartdate(pointStartDate);
        params.setEnddate(pointEndDate);
        params.setSort(sort);
        if(isAsc){
            params.setIsasc("asc");
        }else{
            params.setIsasc("desc");
        }
        params.setPageIndex((pageIndex-1)*pageSize);
        params.setPageSize(pageIndex*pageSize);
        List<dsParams> list = baseMapper.getPagePointList(params);
        Sort.dsSort(list);//排序
        List<Map<String, Object>> data = new ArrayList<>();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                Map<String, Object> map =new HashMap<>();
                map.put("num",i+1);
                map.put("pointD",list.get(i).getPointD());
                map.put("pointTotalD",list.get(i).getPointTotalD());
                map.put("currentValueD",list.get(i).getCurrentValueD());
                map.put("pointZ",list.get(i).getPointZ());
                map.put("pointTotalZ",list.get(i).getPointTotalZ());
                map.put("currentValueZ",list.get(i).getCurrentValueZ());
                map.put("pointName",list.get(i).getPointName());
                map.put("remark",list.get(i).getRemark());
                map.put("monitorDate",list.get(i).getMonitorDate());
                data.add(map);
            }
        }
        Integer total = baseMapper.getTotal(params);
        Map<String, Object> map = new HashMap<>();
        map.put("ds",data);
        List<Map<String, Object>> totals = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("total",total);
        totals.add(map1);
        map.put("ds1",totals);
        return R.ok(1,"获取成功!",map,true,null);
    }
    //根据点位名称和时间查询点位数据列表
    @Override
    public R getDataList(String reportSysId, String startDate, String endDate, String pointName) {
        QueryParams params = new QueryParams();
        params.setId(reportSysId);
        params.setStartdate(startDate);
        params.setEnddate(endDate);
        params.setSort(pointName);
        List<Map<String, Object>> list = baseMapper.getDataList(params);
        return R.ok(1,"获取成功!",list,true,null);
    }
    //删除报表数据信息
    @Override
    public void deleteMonitorDaily(String sysId) {
        ProjectMonitordailyEntity entity = this.getById(sysId);
        if(entity!=null){
            entity.setDel(1);
            this.updateById(entity);
            projectMonitordataService.deleteMonitorDaily(sysId);
        }
    }

}