package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectMonitorinitEntity;
import com.sx.drainage.params.AddInitializationData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:12:37
 */
public interface ProjectMonitorinitService extends IService<ProjectMonitorinitEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取初始化数据
    Map<String, Object> getInitializationData(String reportSysId, Integer page, Integer pageRecord);
    //新增初始化数据
    void addInitializationData(AddInitializationData addInitializationData);
    //修改初始化数据
    void updateInitializationData(AddInitializationData addInitializationData);
    //删除初始化数据
    void deleteInitializationData(String sysId);
    //导入初始化数据
    boolean importInitializationData(String userId, String reportSysId, MultipartFile importFile);
    //获取初始化数据(返回实体类)
    List<ProjectMonitorinitEntity> getAllData(String reportSysId);
    //获取所有id
    List<String> getAllIdByReportSysId(String reportSysId);
}

