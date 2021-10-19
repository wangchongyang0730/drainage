package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.InspectionpriceEntity;
import com.sx.drainage.params.PriceParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:11:32
 */
public interface InspectionpriceService extends IService<InspectionpriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取项目验工计价和安措费
    List<InspectionpriceEntity> getProjectPrice(String projectId);
    //获取分页列表
    R getPageList(String type, String projectId, Integer currentPage, Integer pageSize, String sort, Boolean isAsc, String search, String queryJson);
    //获取所有数据
    R getAllList(String projectId, String type, String search);
    //获取详细信息
    R getDetails(String id);
    //新增
    void addPrice(PriceParams priceParams,String userId);
    //修改
    void updatePrice(PriceParams priceParams);
    //删除
    void delete(String type, String id);
}

