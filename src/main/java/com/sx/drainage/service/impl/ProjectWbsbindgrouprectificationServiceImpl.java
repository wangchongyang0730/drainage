package com.sx.drainage.service.impl;

import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.QueryParams;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ProjectWbsbindgrouprectificationDao;
import com.sx.drainage.entity.ProjectWbsbindgrouprectificationEntity;
import com.sx.drainage.service.ProjectWbsbindgrouprectificationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("projectWbsbindgrouprectificationService")
@Transactional
public class ProjectWbsbindgrouprectificationServiceImpl extends ServiceImpl<ProjectWbsbindgrouprectificationDao, ProjectWbsbindgrouprectificationEntity> implements ProjectWbsbindgrouprectificationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsbindgrouprectificationEntity> page = this.page(
                new Query<ProjectWbsbindgrouprectificationEntity>().getPage(params),
                new QueryWrapper<ProjectWbsbindgrouprectificationEntity>()
        );

        return new PageUtils(page);
    }
    //获取无流程提交质量和安全信息
    @Override
    public Map<String, Object> getNoProcessRectification(String projectId, Integer type) {
        Map<String, Object> map=new HashMap<>();
        QueryParams queryParams = new QueryParams();
        queryParams.setType(type);
        queryParams.setProjectId(projectId);
        List<Map<String, Object>> list = baseMapper.getNoProcessRectification(queryParams);
        int complete=0;
        for(int i=0;i<list.size();i++){
            if(!StringUtils.isEmpty(list.get(i).get("After"))&&!StringUtils.isEmpty(list.get(i).get("Before"))&&!StringUtils.isEmpty(list.get(i).get("RectificationDate"))){
                complete++;
            }
        }
        map.put("allCount",list.size());
        map.put("completeCount",complete);
        return map;
    }
    //删除wbs
    @Override
    public void deleteWbs(String sysid) {
        this.remove(new QueryWrapper<ProjectWbsbindgrouprectificationEntity>().eq("WbsId",sysid));
    }
    //获取质量或安全信息
    @Override
    public Map<String, Object> getAnQuanOrZhiLiang(ProjectWbsEntity wbsEntity, int i) {
        List<ProjectWbsbindgrouprectificationEntity> list = this.list(new QueryWrapper<ProjectWbsbindgrouprectificationEntity>().eq("WbsId", wbsEntity.getSysid()).eq("ZhiLiangOrAnQuan", i));
        if(list!=null&&list.size()>0){
            Map<String, Object> map = new HashMap<>();
            map.put("wbsId",wbsEntity.getSysid());
            map.put("partName",wbsEntity.getPartname());
            if(i==1){
                map.put("zhiliangRemarks",list.get(0).getRemarks());
                map.put("zhiliangBefore",list.get(0).getBefore());
                map.put("zhilinagAfter",list.get(0).getAfter());
                map.put("zhiliangRectificationDate",list.get(0).getRectificationdate());
                map.put("zhiliangHiddenType",getHiddenType(list.get(0).getHiddentype()));
                map.put("zhiliangType",getZhiLiangType(list.get(0).getType()));
            }
            if(i==2){
                map.put("anquanRemarks",list.get(0).getRemarks());
                map.put("anquanBefore",list.get(0).getBefore());
                map.put("anquanAfter",list.get(0).getAfter());
                map.put("anquanRectificationDate",list.get(0).getRectificationdate());
                map.put("anquanHiddenType",getHiddenType(list.get(0).getHiddentype()));
                map.put("anquanType",getAnQuanType(list.get(0).getType()));
            }
            return map;
        }
        return null;
    }
    //判断隐患等级
    private String getHiddenType(String type){
        switch (type){
            case "1":
                return "轻微";
            case "2":
                return "一般";
            case "3":
                return "严重";
            default:
                return null;
        }
    }
    //判断质量整改类型
    private String getZhiLiangType(String type){
        switch (type){
            case "1_1":
                return "原材料";
            case "1_2":
                return "成品与半成品";
            case "1_3":
                return "模板砼/施工缝";
            case "1_4":
                return "接缝与连接/钢结构与紧固件/灰缝与砌筑/功能性检验/涂料与装饰/资料台账";
            case "1_5":
                return "其他";
            default:
                return null;
        }
    }
    //判断安全整改类型
    private String getAnQuanType(String type){
        switch (type){
            case "1_1":
                return "临边围护";
            case "1_2":
                return "危化品";
            case "1_3":
                return "文明施工与人员违章";
            case "1_4":
                return "其他";
            case "1_5":
                return "施工临时用电";
            case "1_6":
                return "爬梯与坑槽支护";
            case "1_7":
                return "脚手架与排架";
            case "1_8":
                return "消防";
            case "1_9":
                return "施工机械设备";
            default:
                return null;
        }
    }
}