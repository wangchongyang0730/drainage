package com.sx.drainage.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectParticipantsDao;
import com.sx.drainage.entity.ProjectParticipantsEntity;
import com.sx.drainage.service.ProjectParticipantsService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectParticipantsService")
@Transactional
public class ProjectParticipantsServiceImpl extends ServiceImpl<ProjectParticipantsDao, ProjectParticipantsEntity> implements ProjectParticipantsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectParticipantsEntity> page = this.page(
                new Query<ProjectParticipantsEntity>().getPage(params),
                new QueryWrapper<ProjectParticipantsEntity>()
        );

        return new PageUtils(page);
    }

}