package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.params.ProgrammeTypeParams;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectProgrammetypeDao;
import com.sx.drainage.entity.ProjectProgrammetypeEntity;
import com.sx.drainage.service.ProjectProgrammetypeService;
import org.springframework.transaction.annotation.Transactional;


@Service("projectProgrammetypeService")
@Transactional
public class ProjectProgrammetypeServiceImpl extends ServiceImpl<ProjectProgrammetypeDao, ProjectProgrammetypeEntity> implements ProjectProgrammetypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectProgrammetypeEntity> page = this.page(
                new Query<ProjectProgrammetypeEntity>().getPage(params),
                new QueryWrapper<ProjectProgrammetypeEntity>()
        );

        return new PageUtils(page);
    }
    //获取所有方案类型
    @Override
    public List<Map<String, Object>> GetAllProgrammeType() {
        List<ProjectProgrammetypeEntity> list = this.list(new QueryWrapper<ProjectProgrammetypeEntity>().eq("del", 0));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("typeName",li.getTypename());
            map.put("createDate",li.getCreatedate());
            map.put("createUser",li.getCreateuser());
            map.put("updateDate",li.getUpdatedate());
            map.put("updateUser",li.getUpdateuser());
            map.put("deleteDate",li.getDeletedate());
            map.put("deleteUser",li.getDeleteuser());
            data.add(map);
        });
        return data;
    }
    //修改方案类型信息
    @Override
    public void putProgrammeType(ProgrammeTypeParams programmeTypeParams, String userId) {
        ProjectProgrammetypeEntity entity = new ProjectProgrammetypeEntity();
        entity.setSysid(programmeTypeParams.getSysId());
        entity.setTypename(programmeTypeParams.getTypeName());
        entity.setUpdatedate(new Date());
        entity.setUpdateuser(userId);
        this.updateById(entity);
    }
    //新增方案类型信息
    @Override
    public void postProgrammeType(ProgrammeTypeParams programmeTypeParams, String userId) {
        ProjectProgrammetypeEntity entity = new ProjectProgrammetypeEntity();
        entity.setSysid(CreateUuid.uuid());
        entity.setTypename(programmeTypeParams.getTypeName());
        entity.setCreatedate(new Date());
        entity.setCreateuser(userId);
        entity.setDel(0);
        this.save(entity);
    }
    //假删除方案类型信息
    @Override
    public void deleteProgrammeType(String sysId, String userId) {
        ProjectProgrammetypeEntity entity = new ProjectProgrammetypeEntity();
        entity.setSysid(sysId);
        entity.setDeletedate(new Date());
        entity.setDeleteuser(userId);
        entity.setDel(1);
        this.updateById(entity);
    }
    //获取方案类型信息
    @Override
    public Map<String, Object> getProgrammeType(String sysId) {
        ProjectProgrammetypeEntity li = this.getById(sysId);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("typeName",li.getTypename());
        map.put("createDate",li.getCreatedate());
        map.put("createUser",li.getCreateuser());
        map.put("updateDate",li.getUpdatedate());
        map.put("updateUser",li.getUpdateuser());
        map.put("deleteDate",li.getDeletedate());
        map.put("deleteUser",li.getDeleteuser());
        return map;
    }

}