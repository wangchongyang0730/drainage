package com.sx.drainage.service.impl;

import com.sx.drainage.entity.ProjectProjectEntity;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.service.EngineeringScreenService;
import com.sx.drainage.service.ProjectProjectService;
import com.sx.drainage.service.ProjectWbsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/12
 * Time: 17:19
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EngineeringScreenServiceImpl implements EngineeringScreenService {
    private final ProjectProjectService projectProjectService;
    private final ProjectWbsService projectWbsService;

    /*
     * 获取全部里程碑节点
     * */
    @Override
    public Map<String, Object> getMilestoneNode() {
        List<ProjectProjectEntity> project = projectProjectService.getProjects();
        List<ProjectWbsEntity> list = projectWbsService.getMilestoneNode();
        List<Map<String,Object>> wanCheng = new ArrayList<>();
        List<Map<String,Object>> weiWanCheng = new ArrayList<>();
        List<Map<String,Object>> yanQi = new ArrayList<>();
        List<Map<String,Object>> yanQiWeiWanCheng = new ArrayList<>();
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.execute(()->{
            list.forEach(li -> {
                if(li.getPlanbegindate()!=null&&li.getPlanenddate()!=null&&li.getFactbegindate()!=null&&li.getFactenddate()==null){
                    Date date = new Date();
                    long now = date.getTime();
                    long plan = li.getPlanenddate().getTime();
                    if(plan>=now){
                        Map<String,Object> map = new HashMap<>();
                        map.put("partName",li.getPartname());
                        project.forEach(pr -> {
                            if(li.getProjectid().equals(pr.getSysid())){
                                map.put("projectName",pr.getName());
                            }
                        });
                        if(map.size()==2){
                            weiWanCheng.add(map);
                        }
                    }else{
                        Map<String,Object> map = new HashMap<>();
                        map.put("partName",li.getPartname());
                        project.forEach(pr -> {
                            if(li.getProjectid().equals(pr.getSysid())){
                                map.put("projectName",pr.getName());
                            }
                        });
                        if(map.size()==2){
                            yanQiWeiWanCheng.add(map);
                        }
                    }

                }
                if(li.getPlanbegindate()!=null&&li.getPlanenddate()!=null&&li.getFactbegindate()!=null&&li.getFactenddate()!=null){
                    long planEnd = li.getPlanenddate().getTime();
                    long factEnd = li.getFactenddate().getTime();
                    if(planEnd>=factEnd) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("partName", li.getPartname());
                        project.forEach(pr -> {
                            if (li.getProjectid().equals(pr.getSysid())) {
                                map.put("projectName", pr.getName());
                            }
                        });
                        if (map.size() == 2) {
                            wanCheng.add(map);
                        }
                    }else{
                        Map<String, Object> map = new HashMap<>();
                        map.put("partName", li.getPartname());
                        project.forEach(pr -> {
                            if (li.getProjectid().equals(pr.getSysid())) {
                                map.put("projectName", pr.getName());
                            }
                        });
                        if (map.size() == 2) {
                            yanQi.add(map);
                        }
                    }
                }
            });
//        });
        Map<String, Object> map = new HashMap<>();
        map.put("wanCheng",wanCheng);
        map.put("weiWanCheng",weiWanCheng);
        map.put("yanQi",yanQi);
        map.put("yanQiWeiWanCheng",yanQiWeiWanCheng);
        return map;
    }

    @Override
    public List<Map<String, Object>> getAllProjectSchedule() {
        List<Map<String, Object>> data = new ArrayList<>();
        List<ProjectProjectEntity> list = projectProjectService.getAllProject();
        if(list!=null&&list.size()>0){
            list.forEach(l -> {
                Map<String, Object> map = new HashMap<>();
                Object d = projectProjectService.getProjectPhase(l.getSysid()).get("data");
                map.put("name",l.getName());
                map.put("data",d);
                data.add(map);

            });
        }
        return data;
    }

}
