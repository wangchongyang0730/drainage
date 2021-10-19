package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsSizeEntity;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 9:42
 */
public interface ProjectWbsSizeService extends IService<ProjectWbsSizeEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //添加模型尺寸
    void addWbsSize(String wbsId, String[][] wbsSize, String fileId);
    //修改模型尺寸
    void updateWbsSize(String wbsId, String[][] wbsSize, String fileId);
    //获取模型尺寸
    Map<String, Object> queryWbsSize(String wbsId,String type);
    //删除模型尺寸
    void deleteWbsSize(String id);
}
