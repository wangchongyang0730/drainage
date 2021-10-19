package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.dao.ActivitiWorkFlowDao;
import com.sx.drainage.entity.ActivitiWorkFlowEntity;
import com.sx.drainage.service.ActivitiWorkFlowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/27
 * Time: 9:51
 */
@Service
@Transactional
public class ActivitiWorkFlowServiceImpl extends ServiceImpl<ActivitiWorkFlowDao, ActivitiWorkFlowEntity> implements ActivitiWorkFlowService {

    /*
    * 添加流程存档
    * */
    @Override
    public void add(ActivitiWorkFlowEntity entity) {
        this.save(entity);
    }
    /*
     * 根据类型和项目id查询流程存档
     * */
    @Override
    public List<ActivitiWorkFlowEntity> selectByWorkTypeAndProjectId(String workType, String projectId) {
        return this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("del",0).eq("projectId",projectId).eq("workType",workType));
    }
    /*
     * 根据流程实例id查询流程存档
     * */
    @Override
    public ActivitiWorkFlowEntity selectByProcessId(String processId) {
        return this.getOne(new QueryWrapper<ActivitiWorkFlowEntity>().eq("del",0).eq("processId",processId));
    }
    /*
     * 根据主键id删除流程存档
     * */
    @Override
    public boolean deleteBySysId(String sysId) {
        ActivitiWorkFlowEntity byId = this.getById(sysId);
        if(byId!=null){
            if(byId.getSubmitUser()!=null){
                return false;
            }else{
                byId.setDel(1);
                this.updateById(byId);
                return true;
            }
        }
        return false;
    }
    /*
     * 修改流程存档
     * */
    @Override
    public void update(ActivitiWorkFlowEntity entity) {
        this.updateById(entity);
    }
    /*
    * 判断文件是否存在
    * */
    @Override
    public Boolean checkFile(String sysId, String workType) {
        List<ActivitiWorkFlowEntity> list = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", sysId).eq("del", 0).eq("states", 2).eq("workType",workType));
        List<ActivitiWorkFlowEntity> list1 = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", sysId).eq("del", 0).eq("workType",workType).eq("states", 0));
        List<ActivitiWorkFlowEntity> list2 = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", sysId).eq("del", 0).eq("workType",workType).eq("states", 1));
        if(list!=null&&list.size()>0){
            return true;
        }
        if(list1!=null&& list1.size()>0){
           return null;
        }
        if(list2!=null&& list2.size()>0){
            return null;
        }
        return false;
    }
    /*
    * 获取整改完成情况
    * */
    @Override
    public Map<String, Object> getRectificationInformation(String projectId) {
        List<ActivitiWorkFlowEntity> safety = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).eq("states", 2).eq("workType","安全整改"));
        List<ActivitiWorkFlowEntity> quality = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).eq("states", 2).eq("workType","质量整改"));
        List<ActivitiWorkFlowEntity> supervision = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).eq("states", 2).eq("workType","监理整改"));

        List<ActivitiWorkFlowEntity> noSafety = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).ne("states", 2).eq("workType","安全整改"));
        List<ActivitiWorkFlowEntity> noQuality = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).ne("states", 2).eq("workType","质量整改"));
        List<ActivitiWorkFlowEntity> noSupervision = this.list(new QueryWrapper<ActivitiWorkFlowEntity>().eq("projectId", projectId).eq("del", 0).ne("states", 2).eq("workType","监理整改"));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> safetys = new HashMap<>();
        Map<String, Object> qualitys = new HashMap<>();
        Map<String, Object> supervisions = new HashMap<>();
        if(safety!=null&&safety.size()>0){
            safetys.put("completed",safety.size());
        }else{
            safetys.put("completed",0);
        }

        if(quality!=null&&quality.size()>0){
            qualitys.put("completed",quality.size());
        }else{
            qualitys.put("completed",0);
        }

        if(supervision!=null&&supervision.size()>0){
            supervisions.put("completed",supervision.size());
        }else{
            supervisions.put("completed",0);
        }

        if(noSafety!=null&&noSafety.size()>0){
            safetys.put("undone",noSafety.size());
        }else{
            safetys.put("undone",0);
        }
        if(noQuality!=null&&noQuality.size()>0){
            qualitys.put("undone",noQuality.size());
        }else{
            qualitys.put("undone",0);
        }
        if(noSupervision!=null&&noSupervision.size()>0){
            supervisions.put("undone",noSupervision.size());
        }else{
            supervisions.put("undone",0);
        }
        map.put("safety",safetys);
        map.put("quality",qualitys);
        map.put("supervision",supervisions);
        return map;
    }
}
