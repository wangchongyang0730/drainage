package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsbimtypeEntity;
import com.sx.drainage.params.BindBimParams;
import com.sx.drainage.params.WbsBimTypeParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 10:12:43
 */
public interface ProjectWbsbimtypeService extends IService<ProjectWbsbimtypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有bim类型绑定的bim模型
    List<Map<String, Object>> getAllWbsBimInfo(String projectId);
    //绑定bim模型
    void bindBim(BindBimParams bindBimParams, String userId);
    //获取BIM模型的项目Id
    Map<String, Object> getBIMProjectId(String projectId, String bimTypeName);
    //获取所有模型类别
    List<Map<String, Object>> GetAllWbsbimType();
    //新增模型类别信息
    void postWbsbimType(WbsBimTypeParams wbsBimTypeParams, String userId);
    //更新模型类别信息
    void putWbsbimType(WbsBimTypeParams wbsBimTypeParams, String userId);
    //删除模型类别信息
    void deleteWbsbimType(String sysId, String userId);
    //获取所有bim模型
    List<Map<String, Object>> getAllBim(String userId);
    //获取绑定的bim模型
    List<Map<String, Object>> getBindBim(String projectId);
    //删除模型绑定
    void deleteWbsBind(String sysId, String userId);
}

