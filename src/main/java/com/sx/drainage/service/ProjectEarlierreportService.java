package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectEarlierreportEntity;
import com.sx.drainage.params.AddEarlierreport;
import com.sx.drainage.params.UpdateEarlierreport;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-27 11:51:01
 */
public interface ProjectEarlierreportService extends IService<ProjectEarlierreportEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取所有
    List<ProjectEarlierreportEntity> getAllList(String projectId);
    //获取详细信息
    ProjectEarlierreportEntity getOne(String id);
    //添加
    void addEarlierreport(AddEarlierreport addEarlierreport);
    //修改
    void updateEarlierreport(UpdateEarlierreport updateEarlierreport);
    //删除
    void deleteEarlierreport(String sysId);
}

