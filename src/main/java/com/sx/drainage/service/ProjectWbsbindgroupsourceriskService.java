package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsbindgroupsourceriskEntity;
import com.sx.drainage.params.BindSourceRiskParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 15:48:38
 */
public interface ProjectWbsbindgroupsourceriskService extends IService<ProjectWbsbindgroupsourceriskEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取无流程提交风险源信息
    List<ProjectWbsbindgroupsourceriskEntity> getNoProcessSourceRisk(List<String> wbsId);
    //删除wbs
    void deleteWbs(String sysid);
    //获取wbs绑定模型重大风险源信息
    Map<String, Object> getSourceRisk(String wbsId);
    //添加wbs绑定模型重大风险源信息
    void addSourceRisk(BindSourceRiskParams bindSourceRiskParams);
    //修改wbs绑定模型重大风险源信息
    void putSourceRisk(BindSourceRiskParams bindSourceRiskParams);
    //删除wbs绑定模型重大风险源信息
    void deleteSourceRisk(String sysId);
}

