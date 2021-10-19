package com.sx.drainage.service.impl;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/1
 * Time: 14:04
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectPipejackingDailyDao;
import com.sx.drainage.entity.ProjectInitPipejackingEntity;
import com.sx.drainage.entity.ProjectPipejackingDailyEntity;
import com.sx.drainage.entity.ProjectPipejackingDataEntity;
import com.sx.drainage.params.Pipejacking;
import com.sx.drainage.service.ProjectInitPipejackingService;
import com.sx.drainage.service.ProjectPipejackingDailyService;
import com.sx.drainage.service.ProjectPipejackingDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectPipejackingDailyServiceImpl extends ServiceImpl<ProjectPipejackingDailyDao, ProjectPipejackingDailyEntity> implements ProjectPipejackingDailyService {
    private final ProjectPipejackingDataService projectPipejackingDataService;
    private final ProjectInitPipejackingService projectInitPipejackingService;

    /*
     * 新增日报
     * */
    @Override
    public void addPipejackingDaily(List<ProjectPipejackingDailyEntity> list, String userId) {
        List<ProjectPipejackingDataEntity> data = new ArrayList<>();
        list.forEach(l -> {
            String uuid = CreateUuid.uuid();
            l.setSysId(uuid);
            l.setDel(0);
            l.setReportUserId(userId);
            if (l.getData() != null && l.getData().size() > 0) {
                l.getData().forEach(ll -> {
                    ll.setSysId(CreateUuid.uuid());
                    ll.setDel(0);
                    ll.setPipejackId(uuid);
                    data.add(ll);
                });
            }
        });
        this.saveBatch(list);
        if (data.size() > 0) {
            data.forEach(d -> {
                d.setSysId(CreateUuid.uuid());
                d.setDel(0);
            });
            projectPipejackingDataService.saveBatch(data);
        }
    }

    /*
     * 获取上次监测数据
     * */
    @Override
    public ProjectPipejackingDailyEntity getLastData(String sysId) {
        List<ProjectPipejackingDailyEntity> list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().eq("del", 0).eq("initId", sysId).orderByDesc("reportTime"));
        if (list != null && list.size() > 0) {
            ProjectPipejackingDailyEntity entity = list.get(0);
            List<ProjectPipejackingDataEntity> data = projectPipejackingDataService.getData(entity.getSysId());
            entity.setData(data);
            return entity;
        }
        return null;
    }

    /*
     * 获取所有日报已填报日期
     * */
    @Override
    public List<Date> getDate(String projectId) {
        List<Pipejacking> pipejackings = baseMapper.getDate(projectId);
        if (pipejackings != null && pipejackings.size() > 0) {
            log.error("日期长度："+pipejackings.size());
            List<Date> data = new ArrayList<>();
            pipejackings.forEach(p -> {
                data.add(p.getReportTime());
            });
            log.error("日期长度2："+data.size());
            return data;
        }
        return null;
    }

    /*
     * 获取日报详情
     * */
    @Override
    public List<ProjectPipejackingDailyEntity> getPipejackingDaily(String projectId, String reportTime) {
        List<ProjectPipejackingDailyEntity> list = null;
        List<ProjectInitPipejackingEntity> pipejacking = projectInitPipejackingService.getPipejacking(projectId);
        if(pipejacking!=null&&pipejacking.size()>0){
            List<String> ids = new ArrayList<>();
            pipejacking.forEach(p -> {
                ids.add(p.getSysId());
            });
            list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().eq("del", 0).in("initId",ids).likeRight("reportTime",reportTime));
            if (list != null && list.size() > 0) {
                list.forEach(l -> {
                    List<ProjectPipejackingDataEntity> data = projectPipejackingDataService.getData(l.getSysId());
                    l.setData(data);
                });
            }
            return list;
        }
        return list;
    }

    /*
     * 删除日报
     * */
    @Override
    public void deletePipejackingDaily(String projectId, String reportTime) {
        List<ProjectInitPipejackingEntity> pipejacking = projectInitPipejackingService.getPipejacking(projectId);
        if(pipejacking!=null&&pipejacking.size()>0) {
            List<String> ids = new ArrayList<>();
            pipejacking.forEach(p -> {
                ids.add(p.getSysId());
            });
            List<ProjectPipejackingDailyEntity> list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().in("initId",ids).likeRight("reportTime",reportTime));
            ProjectPipejackingDailyEntity entity = new ProjectPipejackingDailyEntity();
            entity.setDel(1);
            this.update(entity,new QueryWrapper<ProjectPipejackingDailyEntity>().in("initId",ids).likeRight("reportTime",reportTime));
            if(list!=null&&list.size()>0) {
                List<String> dailyIds = new ArrayList<>();
                list.forEach(l->{
                    dailyIds.add(l.getSysId());
                });
                ProjectPipejackingDataEntity dataEntity = new ProjectPipejackingDataEntity();
                dataEntity.setDel(1);
                projectPipejackingDataService.update(dataEntity, new QueryWrapper<ProjectPipejackingDataEntity>().in("pipejackId", dailyIds));
            }
        }
    }

    /*
     * 统计
     * */
    @Override
    public Map<String, Object> getStatistics(String sysId, Date startDate, Date endDate) {
        Map<String, Object> data = new HashMap<>();
        if (startDate != null && endDate != null) {
            List<ProjectPipejackingDailyEntity> list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().eq("del", 0).eq("initId", sysId).between("monitorDate", startDate, endDate).orderByAsc("monitorDate"));
            List<Map<String, Object>> planeDeviation = new ArrayList<>();
            List<Map<String, Object>> elevationDeviation = new ArrayList<>();
            List<Map<String, Object>> totalJackingForce = new ArrayList<>();
            List<Map<String, Object>> unitFriction = new ArrayList<>();
            if (list != null && list.size() > 0) {
                list.forEach(l -> {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("date", l.getReportTime());
                    map1.put("value", l.getPlaneDeviation()==null?0:l.getPlaneDeviation());
                    map1.put("length", l.getAccumulatedJacking());
                    planeDeviation.add(map1);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("date", l.getReportTime());
                    map2.put("value", l.getElevationDeviation()==null?0:l.getElevationDeviation());
                    map2.put("length", l.getAccumulatedJacking());
                    elevationDeviation.add(map2);
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("date", l.getReportTime());
                    map3.put("value", l.getTotalJackingForce()==null?0:l.getTotalJackingForce());
                    map3.put("length", l.getAccumulatedJacking());
                    totalJackingForce.add(map3);
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("date", l.getReportTime());
                    map4.put("value", l.getUnitFriction()==null?0:l.getUnitFriction());
                    map4.put("length", l.getAccumulatedJacking());
                    unitFriction.add(map4);
                });
            }
            data.put("planeDeviation", planeDeviation);
            data.put("elevationDeviation", elevationDeviation);
            data.put("totalJackingForce", totalJackingForce);
            data.put("unitFriction", unitFriction);
        } else {
            List<ProjectPipejackingDailyEntity> list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().eq("del", 0).eq("initId", sysId).orderByAsc("monitorDate"));
            List<Map<String, Object>> planeDeviation = new ArrayList<>();
            List<Map<String, Object>> elevationDeviation = new ArrayList<>();
            List<Map<String, Object>> totalJackingForce = new ArrayList<>();
            List<Map<String, Object>> unitFriction = new ArrayList<>();
            if (list != null && list.size() > 0) {
                list.forEach(l -> {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("date", l.getReportTime());
                    map1.put("value", l.getPlaneDeviation()==null?0:l.getPlaneDeviation());
                    map1.put("length", l.getAccumulatedJacking());
                    planeDeviation.add(map1);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("date", l.getReportTime());
                    map2.put("value", l.getElevationDeviation()==null?0:l.getElevationDeviation());
                    map2.put("length", l.getAccumulatedJacking());
                    elevationDeviation.add(map2);
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("date", l.getReportTime());
                    map3.put("value", l.getTotalJackingForce()==null?0:l.getTotalJackingForce());
                    map3.put("length", l.getAccumulatedJacking());
                    totalJackingForce.add(map3);
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("date", l.getReportTime());
                    map4.put("value", l.getUnitFriction()==null?0:l.getUnitFriction());
                    map4.put("length", l.getAccumulatedJacking());
                    unitFriction.add(map4);
                });
            }
            data.put("planeDeviation", planeDeviation);
            data.put("elevationDeviation", elevationDeviation);
            data.put("totalJackingForce", totalJackingForce);
            data.put("unitFriction", unitFriction);
        }
        return data;
    }
    /*
    * 删除顶管区间
    * */
    @Override
    public void deletePipejacking(String sysId) {
        projectInitPipejackingService.deletePipejacking(sysId);
        ProjectPipejackingDailyEntity entity = new ProjectPipejackingDailyEntity();
        entity.setDel(1);
        List<ProjectPipejackingDailyEntity> list = this.list(new QueryWrapper<ProjectPipejackingDailyEntity>().eq("initId", sysId));
        this.update(entity,new QueryWrapper<ProjectPipejackingDailyEntity>().eq("initId",sysId));
        if(list!=null&&list.size()>0){
            List<String> dailyIds = new ArrayList<>();
            list.forEach(l->{
                dailyIds.add(l.getSysId());
            });
            ProjectPipejackingDataEntity dataEntity = new ProjectPipejackingDataEntity();
            dataEntity.setDel(1);
            projectPipejackingDataService.update(dataEntity, new QueryWrapper<ProjectPipejackingDataEntity>().in("pipejackId", dailyIds));
        }
    }
}
