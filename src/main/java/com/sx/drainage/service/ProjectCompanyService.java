package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectCompanyEntity;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:55:35
 */
public interface ProjectCompanyService extends IService<ProjectCompanyEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取参建单位
    ProjectCompanyEntity getCompany(String companyId);
    //新建参建单位
    void addCompany(String name, String remark);
    //删除参建单位
    void deleteCompany(String sysId);
    //修改参建单位
    void updateCompany(String sysId, String name, String remark);

    List<ProjectCompanyEntity> getAll();
    //根据单位名称获取单位id
    String getCompanyByName(String department);
}

