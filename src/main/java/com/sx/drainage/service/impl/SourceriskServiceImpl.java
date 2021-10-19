package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.SourceriskParams;
import com.sx.drainage.service.ProjectWbsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.SourceriskDao;
import com.sx.drainage.entity.SourceriskEntity;
import com.sx.drainage.service.SourceriskService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("sourceriskService")
@Transactional
public class SourceriskServiceImpl extends ServiceImpl<SourceriskDao, SourceriskEntity> implements SourceriskService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private ProjectWbsService projectWbsService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SourceriskEntity> page = this.page(
                new Query<SourceriskEntity>().getPage(params),
                new QueryWrapper<SourceriskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R getPageList(String projectId, Integer pageSize, Integer currentPage, String sort, Boolean isAsc, String search) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        QueryWrapper<SourceriskEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId",projectId).eq("del",0);
        if(!StringUtils.isEmpty(search)&&!search.equals("''")){
            wrapper.like("name",search).or().like("wbsName",search);
        }
        if(!StringUtils.isEmpty(sort)&&!sort.equals("''")){
            if(isAsc){
                wrapper.orderByAsc(sort);
            }else{
                wrapper.orderByDesc(sort);
            }
        }
        IPage<SourceriskEntity> page = this.page(
                new Query<SourceriskEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<SourceriskEntity> list = (List<SourceriskEntity>) pageUtils.getList();
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("name",li.getName());
            map.put("wbsId",li.getWbsid());
            map.put("wbsName",li.getWbsname());
            map.put("createtime",li.getCreatetime()==null?"":format.format(li.getCreatetime()));
            map.put("createuser",li.getCreateuser());
            map.put("updatetime",li.getUpdatetime()==null?"":format.format(li.getUpdatetime()));
            map.put("updateuser",li.getUpdateuser());
            map.put("isFolw",li.getIsfolw());
            map.put("projectId",li.getProjectid());
            map.put("planId",li.getPlanid());
            map.put("del",li.getDel());
            if(li.getWbsid().contains(",")){
                String[] split = li.getWbsid().split(",");
                Date planBeginDate = projectWbsService.orderByTime(split, "planBeginDate").get(0).getPlanbegindate();
                Date planEndDate = projectWbsService.orderByTime(split, "planEndDate").get(0).getPlanenddate();
                Date factBeginDate = projectWbsService.orderByTime(split, "factBeginDate").get(0).getFactbegindate();
                Date factEndDate = projectWbsService.orderByTime(split, "factEndDate").get(0).getFactenddate();
                map.put("planBeginDate",planBeginDate==null?"":format.format(planBeginDate));
                map.put("planEndDate",planEndDate==null?"":format.format(planEndDate));
                map.put("factBeginDate",factBeginDate==null?"":format.format(factBeginDate));
                map.put("factEndDate",factEndDate==null?"":format.format(factEndDate));
            }else{
            ProjectWbsEntity wbs = projectWbsService.getWbs(li.getWbsid());
            map.put("planBeginDate",wbs.getPlanbegindate()==null?"":format.format(wbs.getPlanbegindate()));
            map.put("planEndDate",wbs.getPlanenddate()==null?"":format.format(wbs.getPlanenddate()));
            map.put("factBeginDate",wbs.getFactbegindate()==null?"":format.format(wbs.getFactbegindate()));
            map.put("factEndDate",wbs.getFactenddate()==null?"":format.format(wbs.getFactenddate()));
            }
            data.add(map);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return R.ok(1,"获取成功!",res,true,null);
    }
    //获取详细信息
    @Override
    public R getDetails(String id) {
        SourceriskEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("name",li.getName());
        map.put("wbsId",li.getWbsid());
        map.put("wbsName",li.getWbsname());
        map.put("createtime",li.getCreatetime()==null?"":format.format(li.getCreatetime()));
        map.put("createuser",li.getCreateuser());
        map.put("updatetime",li.getUpdatetime()==null?"":format.format(li.getUpdatetime()));
        map.put("updateuser",li.getUpdateuser());
        map.put("isFolw",li.getIsfolw());
        map.put("projectId",li.getProjectid());
        map.put("planId",li.getPlanid());
        map.put("del",li.getDel());
        return R.ok(1,"获取成功!",map,true,null);
    }
    //修改
    @Override
    public void put(SourceriskParams sourceriskParams, String userId) {
        SourceriskEntity entity = new SourceriskEntity();
        entity.setName(sourceriskParams.getName());
        entity.setIsfolw(sourceriskParams.getOver());
        entity.setProjectid(sourceriskParams.getProjectId());
        entity.setSysid(sourceriskParams.getSysId());
        entity.setWbsid(sourceriskParams.getWbsId());
        entity.setWbsname(sourceriskParams.getWbsName());
        entity.setUpdatetime(new Date());
        entity.setUpdateuser(userId);
        this.updateById(entity);
    }
    //添加
    @Override
    public void post(SourceriskParams sourceriskParams, String userId) {
        SourceriskEntity entity = new SourceriskEntity();
        entity.setName(sourceriskParams.getName());
        entity.setIsfolw(sourceriskParams.getOver());
        entity.setProjectid(sourceriskParams.getProjectId());
        entity.setSysid(CreateUuid.uuid());
        entity.setWbsid(sourceriskParams.getWbsId());
        entity.setWbsname(sourceriskParams.getWbsName());
        entity.setCreatetime(new Date());
        entity.setCreateuser(userId);
        entity.setDel(false);
        this.save(entity);
    }
    //删除
    @Override
    public void delete(String sysId, String userId) {
        SourceriskEntity entity = new SourceriskEntity();
        entity.setDel(true);
        entity.setSysid(sysId);
        this.updateById(entity);
    }

}