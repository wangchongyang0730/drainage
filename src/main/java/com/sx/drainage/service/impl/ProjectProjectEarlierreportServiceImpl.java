package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.params.AddEarlierreport;
import com.sx.drainage.params.UpdateEarlierreport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.ProjectEarlierreportDao;
import com.sx.drainage.entity.ProjectEarlierreportEntity;
import com.sx.drainage.service.ProjectEarlierreportService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectEarlierreportService")
@Transactional
public class ProjectProjectEarlierreportServiceImpl extends ServiceImpl<ProjectEarlierreportDao, ProjectEarlierreportEntity> implements ProjectEarlierreportService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String search = (String) params.get("search");
        QueryWrapper<ProjectEarlierreportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId", params.get("projectId")).eq("del",0);
        if (!StringUtils.isEmpty(search)) {
            wrapper.and((qw) -> {
                qw.like("code", params.get("search")).or().like("certificateName", params.get("search"))
                        .or().like("ratifyDept", params.get("search"));
            });
        }
        IPage<ProjectEarlierreportEntity> page = this.page(
                new Query<ProjectEarlierreportEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }
    //获取所有
    @Override
    public List<ProjectEarlierreportEntity> getAllList(String projectId) {
        List<ProjectEarlierreportEntity> list = baseMapper.selectList(new QueryWrapper<ProjectEarlierreportEntity>().eq("projectId", projectId));
        return list;
    }
    //获取详细信息
    @Override
    public ProjectEarlierreportEntity getOne(String id) {
        return this.getById(id);
    }
    //添加
    @Override
    public void addEarlierreport(AddEarlierreport addEarlierreport) {
        ProjectEarlierreportEntity entity = new ProjectEarlierreportEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setProjectid(addEarlierreport.getProjectId());
        entity.setCertificatename(addEarlierreport.getCertificateName());
        entity.setCode(addEarlierreport.getCode());
        entity.setRatifydate(addEarlierreport.getRatifyDate());
        entity.setRatifydept(addEarlierreport.getRatifyDept());
        entity.setUploadfile(addEarlierreport.getUploadfile());
        entity.setDel(0);
        this.save(entity);
    }
    //修改
    @Override
    public void updateEarlierreport(UpdateEarlierreport updateEarlierreport) {
        ProjectEarlierreportEntity entity = new ProjectEarlierreportEntity();
        entity.setSysid(updateEarlierreport.getSysId());
        entity.setCertificatename(updateEarlierreport.getCertificateName());
        entity.setCode(updateEarlierreport.getCode());
        entity.setRatifydate(updateEarlierreport.getRatifyDate());
        entity.setRatifydept(updateEarlierreport.getRatifyDept());
        entity.setUploadfile(updateEarlierreport.getUploadfile());
        this.updateById(entity);
    }
    //删除
    @Override
    public void deleteEarlierreport(String sysId) {
        this.removeById(sysId);
    }


}