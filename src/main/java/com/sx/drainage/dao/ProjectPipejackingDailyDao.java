package com.sx.drainage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.entity.ProjectPipejackingDailyEntity;
import com.sx.drainage.params.Pipejacking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/1
 * Time: 14:03
 */
@Mapper
public interface ProjectPipejackingDailyDao extends BaseMapper<ProjectPipejackingDailyEntity> {

    List<Pipejacking> getDate(@Param("id") String id);
}
