package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.params.Camera;
import com.sx.drainage.params.CameraInfoParams;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectMonitorypointDao;
import com.sx.drainage.entity.ProjectMonitorypointEntity;
import com.sx.drainage.service.ProjectMonitorypointService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectMonitorypointService")
@Transactional
public class ProjectMonitorypointServiceImpl extends ServiceImpl<ProjectMonitorypointDao, ProjectMonitorypointEntity> implements ProjectMonitorypointService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectMonitorypointEntity> page = this.page(
                new Query<ProjectMonitorypointEntity>().getPage(params),
                new QueryWrapper<ProjectMonitorypointEntity>()
        );

        return new PageUtils(page);
    }
    //获取监控点信息
    @Override
    public List<Map<String, Object>> getMonitoryPoint(String projectId) {
        List<ProjectMonitorypointEntity> list = this.list(new QueryWrapper<ProjectMonitorypointEntity>().eq("ProjectId", projectId));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("SysId",li.getSysid());
            map.put("cameraIndexCode",li.getCameraindexcode());
            map.put("cameraName",li.getCameraname());
            map.put("ProjectId",li.getProjectid());
            data.add(map);
        });
        return data;
    }
    //添加更新监控点信息
    @Override
    public void postMonitoryPoint(CameraInfoParams cameraInfoParams) {
        if(cameraInfoParams!=null){
            this.remove(new QueryWrapper<ProjectMonitorypointEntity>().eq("projectId",cameraInfoParams.getProjectId()));
            List<Camera> camera = cameraInfoParams.getCamera();
            List<ProjectMonitorypointEntity> list = new ArrayList<>();
            camera.forEach(li -> {
                ProjectMonitorypointEntity entity = new ProjectMonitorypointEntity();
                entity.setSysid(CreateUuid.uuid());
                entity.setProjectid(cameraInfoParams.getProjectId());
                entity.setCameraindexcode(li.getCameraIndexCode());
                entity.setCameraname(li.getCameraName());
                list.add(entity);
            });
            this.saveBatch(list);
        }
    }
    //删除监控点
    @Override
    public void deleteMonitoryPoint(String sysId) {
        this.removeById(sysId);
    }

}