package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ProjectWbsbindgroupequipmentEntity;
import com.sx.drainage.params.BindEquipmentParams;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:03:22
 */
public interface ProjectWbsbindgroupequipmentService extends IService<ProjectWbsbindgroupequipmentEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取无流程提交施工设备信息
    List<ProjectWbsbindgroupequipmentEntity> getNoProcessDeviceNow(List<String> wbsId);
    //删除wbs
    void deleteWbs(String sysid);
    //获取wbs绑定模型施工重大设备信息
    Map<String, Object> getEquipment(String wbsId);
    //添加wbs绑定模型施工重大设备信息
    void addEquipment(BindEquipmentParams bindEquipmentParams);
    //修改wbs绑定模型施工重大设备信息
    void putEquipment(BindEquipmentParams bindEquipmentParams);
    //删除wbs绑定模型施工重大设备信息
    void deleteEquipment(String sysId);
}

