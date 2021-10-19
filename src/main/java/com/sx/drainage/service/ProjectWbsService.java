package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectWbsEntity;
import com.sx.drainage.params.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 14:36:40
 */
public interface ProjectWbsService extends IService<ProjectWbsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取Wbs的id
    List<String> getWbsId(String projectId);
    //获取分部分项信息
    List<ProjectWbsEntity> getWbsDetails(String sysid);
    //获取项目当前作业面信息
    List<Map<String,Object>> getProjectNowSide(String projectId);
    //获取项目里程碑节点信息
    Map<String, Object> getProjectMajorNode(String projectId);
    //获取所有项目信息
    List<Map<String,Object>> getAllProjectDetails(String projectId);
    //获取所有项目信息(实体类)
    List<ProjectWbsEntity> getAllProjectDetailsToEntity(String parentId);
    //查询WBS子节点树
    List<Map<String, Object>> getWbsTree(String parentId, String projectId);
    //获取详细信息
    Map<String, Object> getDetail(String id);
    //更新wbs excel
    void putWbs(WbsParams wbsParams);
    //新增wbs
    void postWbs(WbsParams wbsParams);
    //删除wbs
    void deleteWbs(String sysid);
    //导出wbs
    void getDeriveWBSData(String projectId, HttpServletResponse response);
    //导入WBS的工程文件数据到项目（.mpp文件）
    R importWBSData(String projectId, String bimType, Integer importType, String parentId, MultipartFile importFile);
    //WBS进度上报并发起上报流程
    void postWBSProgress(WbsParams wbsParams);
    //获取wbs
    ProjectWbsEntity getWbs(String wbsid);
    //日期排序
    List<ProjectWbsEntity> orderByTime(String [] id,String time);
    //获取wbs与树节点关系
    R getBindWbsGroup(String wbsId);
    //获取wbs绑定模型重大风险源信息
    R getSourceRisk(String wbsId);
    //获取wbs绑定模型施工重大设备信息
    R getEquipment(String wbsId);
    //绑定wbs与树节点关系
    R bindWbsGroupId(WbsBindParams wbsBindParams);
    //解除wbs与树节点关系
    void deleteBindWbsGroup(String wbsId);
    //获取所有管理报告信息
    R getManagementReportInformation(String projectId, String wbsId);
    //获取构建id管理报告信息
    R getWbsIdManagementReportInfo(String projectId, String constructionId);
    //形象进度信息
    R getImageProgress(String projectId, String planBeginDate, String planEnDate);
    //添加wbs绑定模型重大风险源信息
    void addSourceRisk(BindSourceRiskParams bindSourceRiskParams);
    //添加wbs绑定模型施工重大设备信息
    void addEquipment(BindEquipmentParams bindEquipmentParams);
    //修改wbs绑定模型重大风险源信息
    void putSourceRisk(BindSourceRiskParams bindSourceRiskParams);
    //修改wbs绑定模型施工重大设备信息
    void putEquipment(BindEquipmentParams bindEquipmentParams);
    //删除wbs绑定模型重大风险源信息
    void deleteSourceRisk(String sysId);
    //删除wbs绑定模型施工重大设备信息
    void deleteEquipment(String sysId);
    //获取周报信息和周报的报告管理信息
    R getWeeklyInfo(String projectId ,Date startWeek, Date endWeek);
    //修改进度管理信息
    void putJinDu(JinDuParams jinDuParams);
    //获取全部里程碑节点
    List<ProjectWbsEntity> getMilestoneNode();
}

