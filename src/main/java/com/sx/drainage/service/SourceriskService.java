package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.SourceriskEntity;
import com.sx.drainage.params.SourceriskParams;

import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-22 13:41:33
 */
public interface SourceriskService extends IService<SourceriskEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取分页列表
    R getPageList(String projectId, Integer pageSize, Integer currentPage, String sort, Boolean isAsc, String search);
    //获取详细信息
    R getDetails(String id);
    //修改
    void put(SourceriskParams sourceriskParams, String userId);
    //添加
    void post(SourceriskParams sourceriskParams, String userId);
    //删除
    void delete(String sysId, String userId);
}

