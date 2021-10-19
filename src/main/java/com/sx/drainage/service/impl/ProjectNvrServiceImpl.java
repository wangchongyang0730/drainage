package com.sx.drainage.service.impl;

import com.sx.drainage.common.R;
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

import com.sx.drainage.dao.ProjectNvrDao;
import com.sx.drainage.entity.ProjectNvrEntity;
import com.sx.drainage.service.ProjectNvrService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectNvrService")
@Transactional
public class ProjectNvrServiceImpl extends ServiceImpl<ProjectNvrDao, ProjectNvrEntity> implements ProjectNvrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectNvrEntity> page = this.page(
                new Query<ProjectNvrEntity>().getPage(params),
                new QueryWrapper<ProjectNvrEntity>()
        );

        return new PageUtils(page);
    }
    //获取模型视频监控信息
    @Override
    public R getModelMonitoring(String projectId) {
        List<ProjectNvrEntity> list = this.list(new QueryWrapper<ProjectNvrEntity>().eq("projectId", projectId));
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("name",li.getName());
            map.put("coordinatePoint",li.getCoordinatepoint());
            map.put("monitoryPoint",li.getMonitorypoint());
            map.put("projectId",li.getProjectid());
            data.add(map);
        });
        return R.ok(1,"获取成功!",data,true,null);
    }

}