package com.sx.drainage.service.impl;

import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.params.PriceParams;
import com.sx.drainage.service.OmAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.InspectionpriceDao;
import com.sx.drainage.entity.InspectionpriceEntity;
import com.sx.drainage.service.InspectionpriceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("inspectionpriceService")
@Transactional
public class InspectionpriceServiceImpl extends ServiceImpl<InspectionpriceDao, InspectionpriceEntity> implements InspectionpriceService {

    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private OmAccountService omAccountService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectionpriceEntity> page = this.page(
                new Query<InspectionpriceEntity>().getPage(params),
                new QueryWrapper<InspectionpriceEntity>()
        );

        return new PageUtils(page);
    }
    //获取项目验工计价和安措费
    @Override
    public List<InspectionpriceEntity> getProjectPrice(String projectId) {
        return this.list(new QueryWrapper<InspectionpriceEntity>().eq("projectId",projectId));
    }
    //获取分页列表
    @Override
    public R getPageList(String type, String projectId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search, String queryJson) {
        Map<String, Object> params = new HashMap<>();
        params.put("page",currentPage.toString());
        params.put("limit",pageSize.toString());
        QueryWrapper<InspectionpriceEntity> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(projectId)){
            wrapper.eq("projectId",projectId);
        }
        if(!StringUtils.isEmpty(type)){
            wrapper.eq("type",type);
        }
        if(!StringUtils.isEmpty(search)){
            wrapper.like("title",search);
        }
        if(sort!=null&&isAsc!=null){
            sort = sort.substring(sort.lastIndexOf(",") + 1, sort.length() - sort.lastIndexOf(",") - 1);
            if (isAsc) {
                wrapper.orderByAsc(sort);
            } else {
                wrapper.orderByDesc(sort);
            }
        }else{
            wrapper.orderByDesc("reportTime");
        }
        IPage<InspectionpriceEntity> page = this.page(
                new Query<InspectionpriceEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<InspectionpriceEntity> list = (List<InspectionpriceEntity>) pageUtils.getList();
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("price",li.getPrice());
            map.put("remark",li.getRemark());
            map.put("createTime",li.getCreatetime()==null?null:format.format(li.getCreatetime()));
            map.put("createUser",li.getCreateuser());
            map.put("type",li.getType());
            map.put("projectId",li.getProjectid());
            map.put("uploadfile",li.getUploadfile());
            map.put("reportTime",li.getReporttime()==null?null:format.format(li.getReporttime()));
            OmAccountEntity user = omAccountService.getUser(li.getCreateuser());
            map.put("userName",user==null?"":user.getName());
            data.add(map);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total",pageUtils.getTotalCount());
        res.put("rows",data);
        return R.ok(1,null,res,true,null);
    }
    //获取所有数据
    @Override
    public R getAllList(String projectId, String type, String search) {
        QueryWrapper<InspectionpriceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("projectId",projectId).eq("type",type);
        if(!StringUtils.isEmpty(search)){
            wrapper.like("remark",search);
        }
        List<InspectionpriceEntity> list = this.list(wrapper);
        List<Map<String,Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("price",li.getPrice());
            map.put("remark",li.getRemark());
            map.put("createTime",li.getCreatetime()==null?null:format.format(li.getCreatetime()));
            map.put("createUser",li.getCreateuser());
            map.put("type",li.getType());
            map.put("projectId",li.getProjectid());
            map.put("uploadfile",li.getUploadfile());
            map.put("reportTime",li.getReporttime()==null?null:format.format(li.getReporttime()));
            OmAccountEntity user = omAccountService.getUser(li.getCreateuser());
            map.put("userName",user==null?"":user.getName());
            data.add(map);
        });
        return R.ok(1,null,data,true,null);
    }
    //获取详细信息
    @Override
    public R getDetails(String id) {
        InspectionpriceEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("price",li.getPrice());
        map.put("remark",li.getRemark());
        map.put("createTime",li.getCreatetime()==null?null:format.format(li.getCreatetime()));
        map.put("createUser",li.getCreateuser());
        map.put("type",li.getType());
        map.put("projectId",li.getProjectid());
        map.put("uploadfile",li.getUploadfile());
        map.put("reportTime",li.getReporttime()==null?null:format.format(li.getReporttime()));
        OmAccountEntity user = omAccountService.getUser(li.getCreateuser());
        map.put("userName",user==null?"":user.getName());
        return R.ok(1,"",map,true,null);
    }
    //新增
    @Override
    public void addPrice(PriceParams priceParams,String userId) {
            InspectionpriceEntity entity = new InspectionpriceEntity();
            entity.setSysid(CreateUuid.uuid());
            entity.setPrice(Float.parseFloat(priceParams.getPrice()));
            entity.setProjectid(priceParams.getProjectId());
            entity.setRemark(priceParams.getRemark());
            entity.setType(priceParams.getType());
            entity.setUploadfile(priceParams.getUploadfile());
            entity.setReporttime(priceParams.getReportTime());
            entity.setCreatetime(new Date());
            entity.setCreateuser(userId);
            this.save(entity);
    }
    //修改
    @Override
    public void updatePrice(PriceParams priceParams) {
            InspectionpriceEntity entity = new InspectionpriceEntity();
            entity.setSysid(priceParams.getSysId());
            entity.setPrice(Float.parseFloat(priceParams.getPrice()));
            entity.setProjectid(priceParams.getProjectId());
            entity.setRemark(priceParams.getRemark());
            entity.setType(priceParams.getType());
            entity.setUploadfile(priceParams.getUploadfile());
            entity.setReporttime(priceParams.getReportTime());
            this.updateById(entity);
    }
    //删除
    @Override
    public void delete(String type, String id) {
        this.removeById(id);
    }

}