package com.sx.drainage.dao;

import com.sx.drainage.entity.ProjectMonitordailyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.params.QueryParams;
import com.sx.drainage.params.dsParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:14:23
 */
@Mapper
public interface ProjectMonitordailyDao extends BaseMapper<ProjectMonitordailyEntity> {
	//分页获取点位信息
	List<dsParams> getPagePointList(QueryParams queryParams);
	//总条数
	Integer getTotal(QueryParams queryParams);
	//根据点位名称和时间查询点位数据列表
	List<Map<String,Object>> getDataList(QueryParams queryParams);
}
