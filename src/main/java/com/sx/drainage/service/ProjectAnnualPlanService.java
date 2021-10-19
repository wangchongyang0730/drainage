package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectAnnualPlanEntity;
import com.sx.drainage.params.AnnualPlanParams;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/16
 * Time: 17:28
 */
public interface ProjectAnnualPlanService extends IService<ProjectAnnualPlanEntity> {

    /*
    * 添加年度总计划投资额
    * */
    void add(String date,Float price);
    /*
    * 查询所有
    * */
    List<ProjectAnnualPlanEntity> getAll();
    /*
    * 修改年度总计划投资额
    * */
    void updateAll(List<AnnualPlanParams> list);
}
