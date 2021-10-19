package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.params.AddClashReport;
import com.sx.drainage.params.UpdateDeviceClash;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.ProjectDeviceclashDao;
import com.sx.drainage.entity.ProjectDeviceclashEntity;
import com.sx.drainage.service.ProjectDeviceclashService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectDeviceclashService")
@Transactional
public class ProjectDeviceclashServiceImpl extends ServiceImpl<ProjectDeviceclashDao, ProjectDeviceclashEntity> implements ProjectDeviceclashService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String search = (String) params.get("search");
        Boolean isAsc = (Boolean) params.get("isAsc");
        String sort = (String) params.get("sort");
        String projectId = (String) params.get("projectId");
        if(StringUtils.isEmpty(sort)||sort.equals("''")||sort.equals("")){
            QueryWrapper<ProjectDeviceclashEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("projectId",projectId).eq("del",0);;
            if(!StringUtils.isEmpty(search)){
                wrapper.and((qw) -> {
                    qw.like("sysId", search).or().like("note", search)
                            .or().like("enclosure", search);
                });
            }
            IPage<ProjectDeviceclashEntity> page = this.page(
                    new Query<ProjectDeviceclashEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }else{
            QueryWrapper<ProjectDeviceclashEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("projectId",projectId).eq("del",0);
            if(!StringUtils.isEmpty(search)){
                if(isAsc){
                    wrapper.and((qw) -> {
                        qw.like("sysId", search).or().like("note", search)
                                .or().like("enclosure", search).orderByAsc(sort);
                    });
                }else{
                    wrapper.and((qw) -> {
                        qw.like("sysId", search).or().like("note", search)
                                .or().like("enclosure", search).orderByDesc(sort);
                    });
                }
            }else{
                if(isAsc){
                    wrapper.orderByAsc(sort);
                }else{
                    wrapper.orderByDesc(sort);
                }
            }
            IPage<ProjectDeviceclashEntity> page = this.page(
                    new Query<ProjectDeviceclashEntity>().getPage(params),
                    wrapper
            );

            return new PageUtils(page);
        }
    }
    //获取冲突报告列表
    @Override
    public List<ProjectDeviceclashEntity> getProjectPageList(String projectId) {

        return this.list(new QueryWrapper<ProjectDeviceclashEntity>().eq("projectId",projectId));
    }
    //修改
    @Override
    public void updateProjectDeviceclash(UpdateDeviceClash updateDeviceClash) {
        ProjectDeviceclashEntity entity = new ProjectDeviceclashEntity();
        entity.setSysid(updateDeviceClash.getSysId());
        entity.setEnclosure(updateDeviceClash.getEnclosure());
        entity.setNote(updateDeviceClash.getNote());
        entity.setPointinfo(updateDeviceClash.getPointInfo());
        this.updateById(entity);
    }
    //删除
    @Override
    public void deleteDeviceclash(String sysId) {
        this.removeById(sysId);
    }
    //获取冲突报告信息
    @Override
    public ProjectDeviceclashEntity getDeviceclashById(String id) {
        return this.getById(id);
    }
    //新增并创建报告流程
    @Override
    public void addDeviceclash(AddClashReport addClashReport,String userId) {
        ProjectDeviceclashEntity entity = new ProjectDeviceclashEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setNote(addClashReport.getNote());
        entity.setEnclosure(addClashReport.getEnclosure());
        entity.setProjectid(addClashReport.getProjectId());
        entity.setDel(0);
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        this.save(entity);
    }

}