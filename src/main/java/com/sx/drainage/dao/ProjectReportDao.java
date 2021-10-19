package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectReportEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.params.QueryParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-16 14:11:27
 */
@Mapper
public interface ProjectReportDao extends BaseMapper<ProjectReportEntity> {
	//获取报告
    List<Map<String,Object>> getAllReport(QueryParams queryParams);
    //获取条数
    List<Map<String,Object>> getCount(QueryParams queryParams);
}
