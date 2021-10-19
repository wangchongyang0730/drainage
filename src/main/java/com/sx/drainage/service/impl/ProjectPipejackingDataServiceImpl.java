package com.sx.drainage.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.dao.ProjectPipejackingDataDao;
import com.sx.drainage.entity.ProjectPipejackingDataEntity;
import com.sx.drainage.service.ProjectPipejackingDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 9:48
 */

@Service
@Transactional
public class ProjectPipejackingDataServiceImpl extends ServiceImpl<ProjectPipejackingDataDao, ProjectPipejackingDataEntity> implements ProjectPipejackingDataService {

    /*
    * 获取数据
    * */
    @Override
    public List<ProjectPipejackingDataEntity> getData(String pipejackId) {
        return this.list(new QueryWrapper<ProjectPipejackingDataEntity>().eq("del",0).eq("pipejackId",pipejackId));
    }

}
