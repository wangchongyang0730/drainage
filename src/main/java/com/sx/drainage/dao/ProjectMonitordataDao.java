package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectMonitordataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:18:23
 */
@Mapper
public interface ProjectMonitordataDao extends BaseMapper<ProjectMonitordataEntity> {

    List<ProjectMonitordataEntity> getAllSum(@Param("dailySysId") List<String> dailySysId);
    //获取一个
    List<ProjectMonitordataEntity> getOneSum(@Param("dailySysId") String dailySysId);
    //获取最近一次数据id
    String getLatelyDataId(@Param("reportSysId") String reportSysId,@Param("sysId") String sysId);
}
