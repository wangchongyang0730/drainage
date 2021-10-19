package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.ProjectWbsSizeDao;
import com.sx.drainage.entity.ProjectWbsSizeEntity;
import com.sx.drainage.service.ProjectWbsSizeService;
import com.sx.drainage.service.ProjectWbsbindgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 9:42
 */
@Service
@Transactional
public class ProjectWbsSizeServiceImpl extends ServiceImpl<ProjectWbsSizeDao, ProjectWbsSizeEntity> implements ProjectWbsSizeService {

    @Autowired
    private ProjectWbsbindgroupService projectWbsbindgroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsSizeEntity> page = this.page(
                new Query<ProjectWbsSizeEntity>().getPage(params),
                new QueryWrapper<ProjectWbsSizeEntity>()
        );
        return new PageUtils(page);
    }
    //添加模型尺寸
    @Override
    public void addWbsSize(String wbsId, String[][] wbsSize, String fileId) {
         List<ProjectWbsSizeEntity> list = new ArrayList<>();
         for(String[] str:wbsSize){
             ProjectWbsSizeEntity entity = new ProjectWbsSizeEntity();
             entity.setSysid(CreateUuid.uuid());
             entity.setWbsid(wbsId);
             entity.setFileid(fileId);
             entity.setWbssize(str[0]+","+str[1]+","+str[2]+","+str[3]);
             list.add(entity);
         }
         this.saveBatch(list);
    }
    //修改模型尺寸
    @Override
    public void updateWbsSize(String wbsId, String[][] wbsSize, String fileId) {
        this.remove(new QueryWrapper<ProjectWbsSizeEntity>().eq("wbsId",wbsId));
        List<ProjectWbsSizeEntity> list = new ArrayList<>();
        for(String[] str:wbsSize){
            ProjectWbsSizeEntity entity = new ProjectWbsSizeEntity();
            entity.setSysid(CreateUuid.uuid());
            entity.setWbsid(wbsId);
            entity.setFileid(fileId);
            entity.setWbssize(str[0]+","+str[1]+","+str[2]+","+str[3]);
            list.add(entity);
        }
        this.saveBatch(list);
    }
    //获取模型尺寸
    @Override
    public Map<String, Object> queryWbsSize(String wbsId,String type) {
        if(type.equals("1")) {
            List<ProjectWbsSizeEntity> list = this.list(new QueryWrapper<ProjectWbsSizeEntity>().eq("wbsId", wbsId));
            if(list!=null&& list.size()>0){
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> res = new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    Map<String, Object> entity = new HashMap<>();
                    String[] split = list.get(i).getWbssize().split(",");
                    entity.put("sysid",list.get(i).getSysid());
                    entity.put("data",split);
                    res.add(entity);
                }
                map.put("rows",res);
                map.put("file",list!=null&&list.size()>0?list.get(0).getFileid():null);
                return map;
            }else{
                return null;
            }
        }
        if(type.equals("2")){
            String id=projectWbsbindgroupService.getWbsId(wbsId);
            if(id==null){
                return null;
            }
            List<ProjectWbsSizeEntity> list = this.list(new QueryWrapper<ProjectWbsSizeEntity>().eq("wbsId", id));
            if(list!=null&& list.size()>0){
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> res = new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    Map<String, Object> entity = new HashMap<>();
                    String[] split = list.get(i).getWbssize().split(",");
                    entity.put("sysid",list.get(i).getSysid());
                    entity.put("data",split);
                    res.add(entity);
                }
                map.put("rows",res);
                map.put("file",list!=null&&list.size()>0?list.get(0).getFileid():null);
                return map;
            }else{
                return null;
            }
        }
       return null;
    }
    //删除模型尺寸
    @Override
    public void deleteWbsSize(String id) {
        this.remove(new QueryWrapper<ProjectWbsSizeEntity>().eq("wbsId",id));
    }
}
