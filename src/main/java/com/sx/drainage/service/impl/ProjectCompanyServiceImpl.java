package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectCompanyDao;
import com.sx.drainage.entity.ProjectCompanyEntity;
import com.sx.drainage.service.ProjectCompanyService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectCompanyService")
@Transactional
public class ProjectCompanyServiceImpl extends ServiceImpl<ProjectCompanyDao, ProjectCompanyEntity> implements ProjectCompanyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectCompanyEntity> page = this.page(
                new Query<ProjectCompanyEntity>().getPage(params),
                new QueryWrapper<ProjectCompanyEntity>()
        );

        return new PageUtils(page);
    }
    //获取参建单位
    @Override
    public ProjectCompanyEntity getCompany(String companyId) {
        return this.getById(companyId);
    }
    //新建参建单位
    @Override
    public void addCompany(String name, String remark) {
        ProjectCompanyEntity entity = new ProjectCompanyEntity();
        entity.setId(CreateUuid.uuid());
        entity.setName(name);
        if(remark!=null){
            entity.setRemark(remark);
        }
        this.save(entity);
    }
    //删除参建单位
    @Override
    public void deleteCompany(String sysId) {
        this.removeById(sysId);
    }
    //修改参建单位
    @Override
    public void updateCompany(String sysId, String name, String remark) {
        ProjectCompanyEntity entity = new ProjectCompanyEntity();
        entity.setId(sysId);
        entity.setName(name);
        if(remark!=null){
            entity.setRemark(remark);
        }
        this.updateById(entity);
    }

    @Override
    public List<ProjectCompanyEntity> getAll() {
        return this.list();
    }
    //根据单位名称获取单位id
    @Override
    public String getCompanyByName(String department) {
        ProjectCompanyEntity entity = this.getOne(new QueryWrapper<ProjectCompanyEntity>().eq("name", department));
        return entity.getId();
    }

}