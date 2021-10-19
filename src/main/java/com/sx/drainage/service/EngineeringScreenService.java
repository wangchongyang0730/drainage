package com.sx.drainage.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/12
 * Time: 17:18
 */
public interface EngineeringScreenService {
    /**
     * 获取全部里程碑节点
     * @return 结果集
     */
    Map<String,Object> getMilestoneNode();
    /**
     * 获取所有项目进度
     * @return 结果集
     */
    List<Map<String, Object>> getAllProjectSchedule();
}
