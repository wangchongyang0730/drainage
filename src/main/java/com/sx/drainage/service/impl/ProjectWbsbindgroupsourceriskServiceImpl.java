package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.params.BindSourceRiskParams;
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

import com.sx.drainage.dao.ProjectWbsbindgroupsourceriskDao;
import com.sx.drainage.entity.ProjectWbsbindgroupsourceriskEntity;
import com.sx.drainage.service.ProjectWbsbindgroupsourceriskService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectWbsbindgroupsourceriskService")
@Transactional
public class ProjectWbsbindgroupsourceriskServiceImpl extends ServiceImpl<ProjectWbsbindgroupsourceriskDao, ProjectWbsbindgroupsourceriskEntity> implements ProjectWbsbindgroupsourceriskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsbindgroupsourceriskEntity> page = this.page(
                new Query<ProjectWbsbindgroupsourceriskEntity>().getPage(params),
                new QueryWrapper<ProjectWbsbindgroupsourceriskEntity>()
        );

        return new PageUtils(page);
    }
    //获取无流程提交风险源信息
    @Override
    public List<ProjectWbsbindgroupsourceriskEntity> getNoProcessSourceRisk(List<String> wbsId) {
        List<ProjectWbsbindgroupsourceriskEntity> list=new ArrayList<>();
        wbsId.forEach(id->{
            List<ProjectWbsbindgroupsourceriskEntity> danger = this.list(new QueryWrapper<ProjectWbsbindgroupsourceriskEntity>().eq("WbsId", id));
            list.addAll(danger);
        });
        return list;
    }
    //删除wbs
    @Override
    public void deleteWbs(String sysid) {
        this.remove(new QueryWrapper<ProjectWbsbindgroupsourceriskEntity>().eq("WbsId",sysid));
    }
    //获取wbs绑定模型重大风险源信息
    @Override
    public Map<String, Object> getSourceRisk(String wbsId) {
        List<ProjectWbsbindgroupsourceriskEntity> list = this.list(new QueryWrapper<ProjectWbsbindgroupsourceriskEntity>().eq("WbsId", wbsId));
        if(list.size()>0&&list!=null){
            Map<String,Object> map = new HashMap<>();
            map.put("SysId",list.get(0).getSysid());
            map.put("FactBeginDate",list.get(0).getFactbegindate());
            map.put("FactEndDate",list.get(0).getFactenddate());
            map.put("PlanBeginDate",list.get(0).getPlanbegindate());
            map.put("PlanEndDate",list.get(0).getPlanenddate());
            map.put("SourceRiskName",list.get(0).getSourceriskname());
            map.put("WbsId",list.get(0).getWbsid());
            return map;
        }
        return null;
    }
    //添加wbs绑定模型重大风险源信息
    @Override
    public void addSourceRisk(BindSourceRiskParams bindSourceRiskParams) {
        ProjectWbsbindgroupsourceriskEntity entity = new ProjectWbsbindgroupsourceriskEntity();
        entity.setFactbegindate(bindSourceRiskParams.getFactBeginDate());
        entity.setFactenddate(bindSourceRiskParams.getFactEndDate());
        entity.setPlanbegindate(bindSourceRiskParams.getPlanBeginDate());
        entity.setPlanenddate(bindSourceRiskParams.getPlanEndDate());
        entity.setSourceriskname(bindSourceRiskParams.getSourceRiskName());
        entity.setWbsid(bindSourceRiskParams.getWbsId());
        entity.setSysid(CreateUuid.uuid());
        this.save(entity);
    }
    //修改wbs绑定模型重大风险源信息
    @Override
    public void putSourceRisk(BindSourceRiskParams bindSourceRiskParams) {
        ProjectWbsbindgroupsourceriskEntity entity = new ProjectWbsbindgroupsourceriskEntity();
        entity.setFactbegindate(bindSourceRiskParams.getFactBeginDate());
        entity.setFactenddate(bindSourceRiskParams.getFactEndDate());
        entity.setPlanbegindate(bindSourceRiskParams.getPlanBeginDate());
        entity.setPlanenddate(bindSourceRiskParams.getPlanEndDate());
        entity.setSourceriskname(bindSourceRiskParams.getSourceRiskName());
        entity.setWbsid(bindSourceRiskParams.getWbsId());
        entity.setSysid(bindSourceRiskParams.getSysId());
        this.updateById(entity);
    }
    //删除wbs绑定模型重大风险源信息
    @Override
    public void deleteSourceRisk(String sysId) {
        this.removeById(sysId);
    }

}