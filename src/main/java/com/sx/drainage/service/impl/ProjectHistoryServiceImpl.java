package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.dao.ProjectHistoryDao;
import com.sx.drainage.entity.ProjectHistoryEntity;
import com.sx.drainage.service.ProjectHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/18
 * Time: 9:46
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectHistoryServiceImpl extends ServiceImpl<ProjectHistoryDao, ProjectHistoryEntity> implements ProjectHistoryService {

    /*
    * 添加历史足迹
    * */
    @Override
    public void addHistory(ProjectHistoryEntity historyEntity, String userId) {
        this.remove(new QueryWrapper<ProjectHistoryEntity>().eq("projectId", historyEntity.getProjectId()).eq("userId", userId));
        historyEntity.setSysId(CreateUuid.uuid());
        historyEntity.setUserId(userId);
        historyEntity.setAccessTime(new Date());
        this.save(historyEntity);
        List<ProjectHistoryEntity> list = this.list(new QueryWrapper<ProjectHistoryEntity>().eq("userId", userId).orderByDesc("accessTime"));
        if(list.size()>4){
            list.remove(0);
            list.remove(0);
            list.remove(0);
            list.remove(0);
            list.forEach(li -> {
                this.removeById(li.getSysId());
            });
        }
    }
    /*
    * 获取历史足迹
    * */
    @Override
    public List<ProjectHistoryEntity> getHistory(String userId) {
        return this.list(new QueryWrapper<ProjectHistoryEntity>().eq("userId", userId).orderByDesc("accessTime"));
    }
    /*
    * 删除历史足迹
    * */
    @Override
    public void deleteByProjectId(String sysId) {
        this.remove(new QueryWrapper<ProjectHistoryEntity>().eq("projectId", sysId));
    }
}
