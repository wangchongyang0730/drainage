package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectProgrammetypeEntity;
import com.sx.drainage.params.ProgrammeTypeParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-24 09:49:50
 */
public interface ProjectProgrammetypeService extends IService<ProjectProgrammetypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有方案类型
    List<Map<String, Object>> GetAllProgrammeType();
    //修改方案类型信息
    void putProgrammeType(ProgrammeTypeParams programmeTypeParams, String userId);
    //新增方案类型信息
    void postProgrammeType(ProgrammeTypeParams programmeTypeParams, String userId);
    //假删除方案类型信息
    void deleteProgrammeType(String sysId, String userId);
    //获取方案类型信息
    Map<String, Object> getProgrammeType(String sysId);
}

