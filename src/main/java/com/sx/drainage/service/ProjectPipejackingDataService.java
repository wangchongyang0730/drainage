package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.entity.ProjectPipejackingDataEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 9:48
 */
public interface ProjectPipejackingDataService extends IService<ProjectPipejackingDataEntity> {
    /*
    * 获取数据
    * */
    List<ProjectPipejackingDataEntity> getData(String pipejackId);
}
