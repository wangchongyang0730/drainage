package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectAnnualPlanDao;
import com.sx.drainage.entity.ProjectAnnualPlanEntity;
import com.sx.drainage.params.AnnualPlanParams;
import com.sx.drainage.service.ProjectAnnualPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/16
 * Time: 17:28
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectAnnualPlanServiceImpl extends ServiceImpl<ProjectAnnualPlanDao, ProjectAnnualPlanEntity> implements ProjectAnnualPlanService {

    /*
     * 添加年度总计划投资额
     * */
    @Override
    public void add(String date, Float price) {
        ProjectAnnualPlanEntity entity = new ProjectAnnualPlanEntity();
        entity.setSysId(CreateUuid.uuid());
        entity.setPrice(price);
        entity.setDate(date);
        this.save(entity);
    }
    /*
    * 查询所有
    * */
    @Override
    public List<ProjectAnnualPlanEntity> getAll() {
        return this.list();
    }
    /*
     * 修改年度总计划投资额
     * */
    @Override
    public void updateAll(List<AnnualPlanParams> list) {
        List<ProjectAnnualPlanEntity> update = new ArrayList<>();
        List<ProjectAnnualPlanEntity> add = new ArrayList<>();
        List<ProjectAnnualPlanEntity> query = this.list();
        Iterator<AnnualPlanParams> iterator = list.iterator();
        while (iterator.hasNext()){
            AnnualPlanParams next = iterator.next();
            query.forEach(q -> {
                if(q.getDate().equals(next.getDate())){
                    q.setPrice(next.getPlan());
                    update.add(q);
                    iterator.remove();
                }
            });
        }
        list.forEach(li -> {
            ProjectAnnualPlanEntity entity = new ProjectAnnualPlanEntity();
            entity.setSysId(CreateUuid.uuid());
            entity.setDate(li.getDate());
            entity.setPrice(li.getPlan());
            add.add(entity);
        });
        this.updateBatchById(update);
        this.saveBatch(add);
    }
}
