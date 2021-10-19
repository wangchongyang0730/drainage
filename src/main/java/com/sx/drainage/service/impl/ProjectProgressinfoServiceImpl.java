package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.AddProgressInfoParams;
import com.sx.drainage.service.ProjectWbsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectProgressinfoDao;
import com.sx.drainage.entity.ProjectProgressinfoEntity;
import com.sx.drainage.service.ProjectProgressinfoService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("projectProgressinfoService")
@Transactional
public class ProjectProgressinfoServiceImpl extends ServiceImpl<ProjectProgressinfoDao, ProjectProgressinfoEntity> implements ProjectProgressinfoService {

    @Autowired
    private ProjectWbsService projectWbsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectProgressinfoEntity> page = this.page(
                new Query<ProjectProgressinfoEntity>().getPage(params),
                new QueryWrapper<ProjectProgressinfoEntity>()
        );

        return new PageUtils(page);
    }

    //获取项目总进度计划信息
    @Override
    public List<Map<String, Object>> getOverAllProgress(String projectId) {
        DecimalFormat df = new DecimalFormat("0.00");
        List<Map<String, Object>> maps = new ArrayList<>();
        //获取总名称
        List<String> name = baseMapper.getAllName(projectId);
        if (name.size() > 0) {
            //获取所有项目
            for (int i = 0; i < name.size(); i++) {
                List<ProjectProgressinfoEntity> info = this.list(new QueryWrapper<ProjectProgressinfoEntity>().eq("projectId", projectId).eq("proName", name.get(i)));
                long begin = 0;
                long end = 0;
                for (int j = 0; j < info.size(); j++) {
                    List<ProjectWbsEntity> details = new ArrayList<>();
                    ProjectWbsEntity wbs = projectWbsService.getWbs(info.get(j).getWbsid());
                    details.add(wbs);
                    long[] longs = getPercentage(details, begin, end);
                    begin = longs[0];
                    end = longs[1];

                }
                Map<String, Object> map = new HashMap<>();
                map.put("Name", name.get(i));
                map.put("ProgressInfo", df.format((end * 1.0 / begin) * 100));
                maps.add(map);
            }
        }
        return maps;
    }

    //添加总体进度节点信息,先删除已有proName信息再添加信息
    @Override
    public void addProgressInfo(AddProgressInfoParams addProgressInfoParams) {
        this.remove(new QueryWrapper<ProjectProgressinfoEntity>().eq("proName", addProgressInfoParams.getProName()));
        List<ProjectProgressinfoEntity> list = new ArrayList<>();
        String[] wbsId = addProgressInfoParams.getWbsId();
        for (String id : wbsId) {
            ProjectProgressinfoEntity entity = new ProjectProgressinfoEntity();
            entity.setSysid(CreateUuid.uuid());
            entity.setProname(addProgressInfoParams.getProName());
            entity.setProjectid(addProgressInfoParams.getProjectId());
            entity.setWbsid(id);
            list.add(entity);
        }
        this.saveBatch(list);
    }

    //删除总体进度节点信息
    @Override
    public void deleteProgressInfo(String proName, String projectId) {
        this.remove(new QueryWrapper<ProjectProgressinfoEntity>().eq("projectId", projectId).eq("proName", proName));
    }


    //计算分子分母
    private long[] getPercentage(List<ProjectWbsEntity> childList, long begin, long end) {
        for (int j = 0; j < childList.size(); j++) {
            String childParentId = childList.get(j).getSysid();
            List<ProjectWbsEntity> childParentList = projectWbsService.getAllProjectDetailsToEntity(childParentId);
            if (childParentList != null && childParentList.size() > 0) {
                long[] longs = getPercentage(childParentList, begin, end);
                begin = longs[0];
                end = longs[1];
            } else {

                if (childList.get(j).getPlanbegindate() != null && childList.get(j).getPlanenddate() != null) {

                    long beginTime = childList.get(j).getPlanbegindate().getTime();
                    long endTime = childList.get(j).getPlanenddate().getTime();
                    long planTime = endTime - beginTime;
                    begin += planTime;
                }
                if (childList.get(j).getFactbegindate() != null && childList.get(j).getFactenddate() != null && childList.get(j).getPlanbegindate() != null && childList.get(j).getPlanenddate() != null) {
                    long beginTime = childList.get(j).getPlanbegindate().getTime();
                    long endTime = childList.get(j).getPlanenddate().getTime();
                    long planTime = endTime - beginTime;
                    end += planTime;
                }

            }
        }
        return new long[]{begin, end};
    }
}