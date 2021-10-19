package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmRelRoleModuleEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-02 11:35:38
 */
public interface OmRelRoleModuleService extends IService<OmRelRoleModuleEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有角色关联的模块Id
    List<String> getAllModuleId(String sysId);
    //保存角色与功能关系
    void saveRoleFunction(List<OmRelRoleModuleEntity> list);
    //获取所有模块id
    List<String> getModuleByRoleIds(List<String> roleId);
}

