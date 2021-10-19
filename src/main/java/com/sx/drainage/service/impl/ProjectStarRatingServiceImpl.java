package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectStarRatingDao;
import com.sx.drainage.entity.ProjectStarRatingEntity;
import com.sx.drainage.service.ProjectStarRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/15
 * Time: 9:39
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectStarRatingServiceImpl extends ServiceImpl<ProjectStarRatingDao, ProjectStarRatingEntity> implements ProjectStarRatingService {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy");
    /*
     * 初始化月度考评
     * */
    @Override
    public void initAppraisal(String projectId) {
        Date date = new Date();
        String year = format.format(date);
        List<ProjectStarRatingEntity> list = new ArrayList<>();
        String [] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        for(int i=0;i<12;i++){
            ProjectStarRatingEntity entity = new ProjectStarRatingEntity();
            entity.setSysId(CreateUuid.uuid());
            entity.setOrders(i);
            entity.setProjectId(projectId);
            entity.setStates(0);
            entity.setStar(0);
            entity.setMonth(months[i]);
            entity.setStates(0);
            entity.setYear(year);
            list.add(entity);
        }
        this.saveBatch(list);
    }
    /*
     * 获取星级考评
     * */
    @Override
    public List<ProjectStarRatingEntity> getAppraisal(String projectId, String year) {
        return this.list(new QueryWrapper<ProjectStarRatingEntity>().eq("projectId",projectId).eq("year",year).orderByAsc("orders"));
    }
    /*
    * 修改星级考评
    * */
    @Override
    public void updateAppraisal(ProjectStarRatingEntity projectStarRatingEntity) {
        this.updateById(projectStarRatingEntity);
    }
    /*
    * 各星级数量
    * */
    @Override
    public Map<String, Object> getNumberOfStar(String projectId) {
        Map<String, Object> data = new HashMap<>();
        log.error("进来了------------------------");
        log.error("projectId:"+projectId);
        log.error("判断结果"+StringUtils.isEmpty(projectId));
        Date date = new Date();
        String year = format.format(date);
        if(StringUtils.isEmpty(projectId)||projectId.equals("{projectId}")){
            List<ProjectStarRatingEntity> list = this.list(new QueryWrapper<ProjectStarRatingEntity>().eq("year",year));
            log.error("星级考评总条数:"+list.size());
            if(list!=null&&list.size()>0){
                List<ProjectStarRatingEntity> one = list.stream().filter(l -> l.getStar() == 1).collect(Collectors.toList());
                List<ProjectStarRatingEntity> two = list.stream().filter(l -> l.getStar() == 2).collect(Collectors.toList());
                List<ProjectStarRatingEntity> three = list.stream().filter(l -> l.getStar() == 3).collect(Collectors.toList());
                List<ProjectStarRatingEntity> four = list.stream().filter(l -> l.getStar() == 4).collect(Collectors.toList());
                List<ProjectStarRatingEntity> five = list.stream().filter(l -> l.getStar() == 5).collect(Collectors.toList());
                data.put("yiXing",one.size());
                data.put("erXing",two.size());
                data.put("sanXing",three.size());
                data.put("siXing",four.size());
                data.put("wuXing",five.size());
            }else{
                data.put("yiXing",0);
                data.put("erXing",0);
                data.put("sanXing",0);
                data.put("siXing",0);
                data.put("wuXing",0);
            }
        }else{
            List<ProjectStarRatingEntity> list = this.list(new QueryWrapper<ProjectStarRatingEntity>().eq("projectId", projectId).eq("year",year));
            if(list!=null&&list.size()>0){
                List<ProjectStarRatingEntity> one = list.stream().filter(l -> l.getStar() == 1).collect(Collectors.toList());
                List<ProjectStarRatingEntity> two = list.stream().filter(l -> l.getStar() == 2).collect(Collectors.toList());
                List<ProjectStarRatingEntity> three = list.stream().filter(l -> l.getStar() == 3).collect(Collectors.toList());
                List<ProjectStarRatingEntity> four = list.stream().filter(l -> l.getStar() == 4).collect(Collectors.toList());
                List<ProjectStarRatingEntity> five = list.stream().filter(l -> l.getStar() == 5).collect(Collectors.toList());
                data.put("yiXing",one.size());
                data.put("erXing",two.size());
                data.put("sanXing",three.size());
                data.put("siXing",four.size());
                data.put("wuXing",five.size());
            }else{
                data.put("yiXing",0);
                data.put("erXing",0);
                data.put("sanXing",0);
                data.put("siXing",0);
                data.put("wuXing",0);
            }
        }
        return data;
    }
}
