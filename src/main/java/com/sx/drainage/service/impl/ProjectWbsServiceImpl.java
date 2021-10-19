package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.*;
import com.sx.drainage.entity.ProjectWbsbindgroupEntity;
import com.sx.drainage.params.*;
import com.sx.drainage.service.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.mpxj.*;
import net.sf.mpxj.mpp.MPPReader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sx.drainage.dao.ProjectWbsDao;
import com.sx.drainage.entity.ProjectWbsEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service("projectWbsService")
@Transactional
public class ProjectWbsServiceImpl extends ServiceImpl<ProjectWbsDao, ProjectWbsEntity> implements ProjectWbsService {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @Autowired
    private ProjectWbsbindgrouprectificationService projectWbsbindgrouprectificationService;
    @Autowired
    private ProjectWbsbindgroupsourceriskService projectWbsbindgroupsourceriskService;
    @Autowired
    private ProjectWbsbindgroupequipmentService projectWbsbindgroupequipmentService;
    @Autowired
    private ProjectWbsbindgroupService projectWbsbindgroupService;
    @Autowired
    private ProjectWbsSizeService projectWbsSizeService;
    @Autowired
    private ProjectReportService projectReportService;
    @Autowired
    private Environment env;//资源操作

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProjectWbsEntity> page = this.page(
                new Query<ProjectWbsEntity>().getPage(params),
                new QueryWrapper<ProjectWbsEntity>()
        );
        return new PageUtils(page);
    }

    //获取项目当前作业面信息
    @Override
    public List<Map<String, Object>> getProjectNowSide(String projectId) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).isNotNull("factBeginDate").isNull("factEndDate"));
        List<ProjectWbsEntity> all = this.list();
        log.info("获取到的数量："+list.size());
        Iterator<ProjectWbsEntity> iterator = list.iterator();
        while (iterator.hasNext()){
            ProjectWbsEntity next = iterator.next();
            if(checkLast(all,next)){
                iterator.remove();
            }
        }
        log.info("符合条件数"+list.size());
        list.forEach(next -> {
            Map<String, Object> data = new HashMap<>();
            String name="";
            name=getNow(all,next,name);
            String substring = name.substring(0, name.lastIndexOf("-"));
            data.put("parentId",next.getParentid());
            data.put("partName",substring);
            data.put("sysId",next.getSysid());
            data.put("_factEndDate",next.getFactenddate());
            data.put("_planBeginDate",next.getPlanbegindate());
            data.put("_planEndDate",next.getPlanenddate());
            res.add(data);
            log.info("名称："+substring);
        });
//        return baseMapper.getProjectNowSide(projectId);
        return res;
    }

    //获取项目里程碑节点信息
    @Override
    public Map<String, Object> getProjectMajorNode(String projectId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-hh");
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", projectId).eq("isKey", 1).eq("del",0));
//        List<Map<String, Object>> maps = new ArrayList<>();
        List<Map<String,Object>> wanCheng = new ArrayList<>();
        List<Map<String,Object>> weiWanCheng = new ArrayList<>();
        List<Map<String,Object>> yanQi = new ArrayList<>();
        List<Map<String,Object>> yanQiWeiWanCheng = new ArrayList<>();
        list.forEach(li -> {
            if(li.getPlanbegindate()!=null&&li.getPlanenddate()!=null&&li.getFactbegindate()!=null&&li.getFactenddate()==null){
                Date date = new Date();
                long now = date.getTime();
                long plan = li.getPlanenddate().getTime();
                if(plan>=now){
                    Map<String,Object> map = new HashMap<>();
                    map.put("parentId", li.getParentid());
                    map.put("sysId", li.getSysid());
                    map.put("partName", li.getPartname());
                    map.put("_planBeginDate", li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
                    map.put("_planEndDate", li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
                    map.put("_factBeginDate", li.getFactbegindate() == null ? "" : format.format(li.getFactbegindate()));
                    map.put("_factEndDate", li.getFactenddate() == null ? "" : format.format(li.getFactenddate()));
                    weiWanCheng.add(map);
                }else{
                    Map<String,Object> map = new HashMap<>();
                    map.put("parentId", li.getParentid());
                    map.put("sysId", li.getSysid());
                    map.put("partName", li.getPartname());
                    map.put("_planBeginDate", li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
                    map.put("_planEndDate", li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
                    map.put("_factBeginDate", li.getFactbegindate() == null ? "" : format.format(li.getFactbegindate()));
                    map.put("_factEndDate", li.getFactenddate() == null ? "" : format.format(li.getFactenddate()));
                    yanQiWeiWanCheng.add(map);
                }

            }
            if(li.getPlanbegindate()!=null&&li.getPlanenddate()!=null&&li.getFactbegindate()!=null&&li.getFactenddate()!=null){
                long planEnd = li.getPlanenddate().getTime();
                long factEnd = li.getFactenddate().getTime();
                if(planEnd>=factEnd) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("parentId", li.getParentid());
                    map.put("sysId", li.getSysid());
                    map.put("partName", li.getPartname());
                    map.put("_planBeginDate", li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
                    map.put("_planEndDate", li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
                    map.put("_factBeginDate", li.getFactbegindate() == null ? "" : format.format(li.getFactbegindate()));
                    map.put("_factEndDate", li.getFactenddate() == null ? "" : format.format(li.getFactenddate()));
                    wanCheng.add(map);

                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("parentId", li.getParentid());
                    map.put("sysId", li.getSysid());
                    map.put("partName", li.getPartname());
                    map.put("_planBeginDate", li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
                    map.put("_planEndDate", li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
                    map.put("_factBeginDate", li.getFactbegindate() == null ? "" : format.format(li.getFactbegindate()));
                    map.put("_factEndDate", li.getFactenddate() == null ? "" : format.format(li.getFactenddate()));
                    yanQi.add(map);
                }
            }
        });
        Map<String, Object> map = new HashMap<>();
        map.put("wanCheng",wanCheng);
        map.put("weiWanCheng",weiWanCheng);
        map.put("yanQi",yanQi);
        map.put("yanQiWeiWanCheng",yanQiWeiWanCheng);
//        list.forEach(li -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("parentId", li.getParentid());
//            map.put("sysId", li.getSysid());
//            map.put("partName", li.getPartname());
//            map.put("_planBeginDate", li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
//            map.put("_planEndDate", li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
//            map.put("_factBeginDate", li.getFactbegindate() == null ? "" : format.format(li.getFactbegindate()));
//            map.put("_factEndDate", li.getFactenddate() == null ? "" : format.format(li.getFactenddate()));
//            maps.add(map);
//        });
        return map;
    }

    //获取所有项目信息
    @Override
    public List<Map<String, Object>> getAllProjectDetails(String projectId) {
        return baseMapper.getAllProjectDetails(projectId);
    }

    @Override
    public List<ProjectWbsEntity> getAllProjectDetailsToEntity(String parentId) {
        return this.list(new QueryWrapper<ProjectWbsEntity>().eq("parentId",parentId).eq("del",0));
    }

    //查询WBS子节点树
    @Override
    public List<Map<String, Object>> getWbsTree(String parentId, String projectId) {
        List<ProjectWbsEntity> all = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).orderByAsc("partCode"));
        if (parentId == null) {
            List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).and(wr -> {
                wr.isNull("parentId").or().eq("parentId", "");
            }).orderByAsc("partCode"));
            return getTree(projectId, list ,all);
        } else {
            List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).eq("parentId", parentId).orderByAsc("partCode"));
            return getTree(projectId, list ,all);
        }
    }

    //获取详细信息
    @Override
    public Map<String, Object> getDetail(String id) {
        return baseMapper.getDetail(id);
    }

    //更新wbs excel
    @Override
    public void putWbs(WbsParams wbsParams) {
        ProjectWbsEntity entity = new ProjectWbsEntity();
        entity.setSysid(wbsParams.getSysId());
        if (wbsParams.getParentId() != null) {
            if (wbsParams.getSysId().equals(wbsParams.getParentId())) {
                entity.setParentid(null);
            } else {
                ProjectWbsEntity parent = this.getById(wbsParams.getParentId());
                if (parent.getPlanbegindate().getTime() > wbsParams.getPlanBeginDate().getTime()) {
                    parent.setPlanbegindate(wbsParams.getPlanBeginDate());
                }
                if (parent.getPlanenddate().getTime() < wbsParams.getPlanEndDate().getTime()) {
                    parent.setPlanenddate(wbsParams.getPlanEndDate());
                }
                this.updateById(parent);
                entity.setParentid(wbsParams.getParentId());
            }
        }
        entity.setIpcid(wbsParams.getIpcId());
        entity.setIskey(wbsParams.getIsKey());
        entity.setPartname(wbsParams.getPartName());
        entity.setParttype(Integer.parseInt(wbsParams.getPartType()));
        entity.setPlanbegindate(wbsParams.getPlanBeginDate());
        entity.setPlanenddate(wbsParams.getPlanEndDate());
        entity.setProgressinfo(wbsParams.getProgressInfo());
        this.updateById(entity);
    }

    //新增wbs
    @Override
    public void postWbs(WbsParams wbsParams) {
        ProjectWbsEntity entity = new ProjectWbsEntity();
        entity.setSysid(CreateUuid.uuid());
        ProjectWbsEntity parent = this.getById(wbsParams.getParentId());
        if (parent != null) {
            UpdateParentPlanBeginEnd(wbsParams.getPlanBeginDate(),wbsParams.getPlanEndDate(),parent);
            List<ProjectWbsEntity> list = new ArrayList<>();
            EachUpdateWbs(parent.getParentid(),list);
            this.updateBatchById(list);
        }
        if (parent.getFullsysid() != null) {
            entity.setFullsysid(parent.getFullsysid() + "/" + entity.getSysid());
        } else {
            entity.setFullsysid(parent.getSysid() + "/" + entity.getSysid());
        }
        String code = getCode(parent);
        entity.setPartcode(code);
        String mppWbs = getMppWbs(code);
        entity.setMppwbs(mppWbs);
        String partCodeBim = getPartCodeBim(mppWbs);
        entity.setPartcodebim(partCodeBim);
        entity.setOrderval(code);
        entity.setMpplevel(mppWbs.split(",").length);
        entity.setForeignkey(CreateUuid.uuid());
        entity.setCreatedate(new Date());
        entity.setFinishpercent(0f);
        entity.setFinishstate(0);
        entity.setIsshow(true);
        entity.setBimtype("");
        entity.setParentid(wbsParams.getParentId());
        entity.setIpcid(wbsParams.getIpcId());
        entity.setIskey(wbsParams.getIsKey());
        entity.setPartname(wbsParams.getPartName());
        entity.setParttype(Integer.parseInt(wbsParams.getPartType()));
        entity.setPlanbegindate(wbsParams.getPlanBeginDate());
        entity.setPlanenddate(wbsParams.getPlanEndDate());
        entity.setProgressinfo(wbsParams.getProgressInfo());
        entity.setProjectid(wbsParams.getProjectId());
        entity.setDel(0);
        this.save(entity);
    }

    //删除wbs
    @Override
    public void deleteWbs(String sysid) {
        ProjectWbsEntity entity = new ProjectWbsEntity();
        entity.setDel(1);
        this.update(entity, new UpdateWrapper<ProjectWbsEntity>().eq("sysId", sysid).or().eq("parentId", sysid));
        projectWbsbindgroupService.deleteWbs(sysid);
        projectWbsbindgroupequipmentService.deleteWbs(sysid);
        projectWbsbindgrouprectificationService.deleteWbs(sysid);
        projectWbsbindgroupsourceriskService.deleteWbs(sysid);
    }

    //导出wbs
    @Override
    public void getDeriveWBSData(String projectId, HttpServletResponse response) {
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).and(wr -> {
            wr.isNull("parentId").or().eq("parentId", "");
        }));
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("wbs数据");
        //设置每列宽度
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("分部分项名称");
        row.createCell(2).setCellValue("分部分项编码");
        row.createCell(3).setCellValue("BIM编码");
        row.createCell(4).setCellValue("计划开始时间");
        row.createCell(5).setCellValue("计划结束日期");
        row.createCell(6).setCellValue("实际开始日期");
        row.createCell(7).setCellValue("实际结束日期");
        //synchronized
        int num = 1;
        getExecl(projectId, list, sheet, num);
        try {
            response.setContentType("application/octet-stream");
            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
            response.setHeader("Content-Disposition", "attachment;filename=" + formats.format(new Date()) + ".xlsx");
            //刷新缓冲
            response.flushBuffer();
            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(response.getOutputStream());
            log.info("最后num" + num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入WBS的工程文件数据到项目（.mpp文件）
    @Override
    public R importWBSData(String projectId, String bimType, Integer importType, String parentId, MultipartFile importFile) {
        try {
            String filename = importFile.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));
            if (!suffixName.equals(".mpp")) {
                return R.ok(1, "请上传mpp文件!", null, false, null);
            }
            String filePath = env.getProperty("file.upload") + "ProjectPart/TempMpp/";
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = CreateUuid.uuid() + suffixName;
            MPPReader mppReader = new MPPReader();
            ProjectFile pf = mppReader.read(importFile.getInputStream());
            List<Task> list = pf.getChildTasks();
            log.error("parentId是否为空:"+StringUtils.isEmpty(parentId));
            if (!StringUtils.isEmpty(parentId)) {
                ProjectWbsEntity parent = this.getById(parentId);
                for (int i = 0; i < list.size(); i++) {
                    Task task = list.get(i);
                    //获取partCode
                    String code = getCode(parent);
                    //获取mppwbs
                    String mppWbs = getMppWbs(code);
                    //获取partCodeBim
                    String partCodeBim = getPartCodeBim(mppWbs);
                    //添加数据
                    ProjectWbsEntity entity = new ProjectWbsEntity();
                    entity.setSysid(CreateUuid.uuid());
                    entity.setPartname(task.getName());
                    entity.setProjectid(projectId);
                    entity.setParentid(parentId);
                    entity.setPartcode(code);
                    entity.setPartcodebim(partCodeBim);
                    entity.setMppwbs(mppWbs);
                    entity.setOrderval(code);
                    entity.setPlanbegindate(task.getStart());
                    entity.setPlanenddate(task.getFinish());
                    entity.setMpplevel(parent.getMpplevel() + 1);
                    entity.setMpppath(filePath + fileName);//等待文件地址
                    entity.setDel(0);
                    entity.setCreatedate(new Date());
                    entity.setIskey(0);
                    entity.setForeignkey(CreateUuid.uuid());
                    entity.setFinishstate(0);
                    entity.setFinishpercent(0f);
                    entity.setFullsysid(parent.getSysid() + "/" + entity.getSysid());
                    this.save(entity);
                    getChildrenTask(task, entity, filePath + fileName, projectId);
                    File upload = new File(filePath + fileName);
                    importFile.transferTo(upload);
                }
            }else {
                //老方法
                //TaskContainer tasks = pf.getAllTasks();
                //根节点导入，移除此工程下原有老信息
                ProjectWbsEntity remove = new ProjectWbsEntity();
                remove.setDel(1);
                this.update(remove, new UpdateWrapper<ProjectWbsEntity>().eq("projectId", projectId));
                for (int i = 0; i < list.size(); i++) {
                    Task task = list.get(i);
                    //添加数据
                    ProjectWbsEntity entity = new ProjectWbsEntity();
                    entity.setSysid(CreateUuid.uuid());
                    entity.setPartname(task.getName());
                    entity.setProjectid(projectId);
                    entity.setPartcode("00" + (i + 1) + "");
                    entity.setPartcodebim((i + 1) + "");
                    entity.setMppwbs((i + 1) + "");
                    entity.setOrderval("00" + (i + 1) + "");
                    entity.setPlanbegindate(task.getStart());
                    entity.setPlanenddate(task.getFinish());
                    entity.setMpplevel(1);
                    entity.setMpppath(filePath + fileName);//等待文件地址
                    entity.setDel(0);
                    entity.setCreatedate(new Date());
                    entity.setIskey(0);
                    entity.setForeignkey(CreateUuid.uuid());
                    entity.setFinishstate(0);
                    entity.setFinishpercent(0f);
                    entity.setFullsysid(entity.getSysid());
                    this.save(entity);
                    getChildrenTask(task, entity, filePath + fileName, projectId);
                    File upload = new File(filePath + fileName);
                    importFile.transferTo(upload);
                }
            }
        } catch (MPXJException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok(1,"上传成功!",null,true,null);
    }

    //WBS进度上报并发起上报流程
    @Override
    public void postWBSProgress(WbsParams wbsParams) {
        ProjectWbsEntity entity = new ProjectWbsEntity();
        entity.setSysid(wbsParams.getSysId());
        entity.setProjectid(wbsParams.getProjectId());
        entity.setFactbegindate(wbsParams.getFactBeginDate());
        entity.setFactenddate(wbsParams.getFactEndDate());
        entity.setMainunit(wbsParams.getMainUnit());
        entity.setReportperson(wbsParams.getReportPerson());
        entity.setRemark(wbsParams.getRemark());
        entity.setSetbacksupload(wbsParams.getSetbacksupload());
        this.updateById(entity);
        checkParent(wbsParams.getSysId());
    }
    /*
    * 判断是否拥有父级
    * */
    public void checkParent(String sysId){
        ProjectWbsEntity entity = this.getById(sysId);
        if(entity.getParentid()!=null) {
            ProjectWbsEntity byId = this.getById(entity.getParentid());
            UpdateParentFactBeginEnd(byId);
            checkParent(entity.getParentid());
        }
    }
    //获取wbs
    @Override
    public ProjectWbsEntity getWbs(String wbsid) {
        return this.getById(wbsid);
    }

    //根据日期排序
    @Override
    public List<ProjectWbsEntity> orderByTime(String[] id, String time) {
        return this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).orderByDesc(time));
    }

    //获取wbs与树节点关系
    @Override
    public R getBindWbsGroup(String wbsId) {
        List<ProjectWbsbindgroupEntity> list = new ArrayList<>();
        String directoryTreeId = "";
        String constructionId = "";
        checkWbs(wbsId, list);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                if (list.get(i).getDirectorytreeid() != null && list.get(i).getDirectorytreeid() != "") {
                    directoryTreeId += list.get(i).getDirectorytreeid() + ",";
                }
                if (list.get(i).getConstructionid() != null && list.get(i).getConstructionid() != "") {
                    constructionId += list.get(i).getConstructionid() + ",";
                }
            }
        }
        if (directoryTreeId != null && directoryTreeId.length() > 0) {
            directoryTreeId = directoryTreeId.substring(0, directoryTreeId.length() - 1);
        }
        if (constructionId != null && constructionId.length() > 0) {
            constructionId = constructionId.substring(0, constructionId.length() - 1);
        }
        if (directoryTreeId != null && directoryTreeId != "") {
            String[] split = directoryTreeId.split(",");
            List<String> strings = Arrays.asList(split);
            List<String> collect = strings.stream().distinct().collect(Collectors.toList());
            directoryTreeId = String.join(",", collect);
        }
        if (constructionId != null & constructionId != "") {
            String[] split = constructionId.split(",");
            List<String> strings = Arrays.asList(split);
            List<String> collect = strings.stream().distinct().collect(Collectors.toList());
            constructionId = String.join(",", collect);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("SysId", 0);
        map.put("WbsId", wbsId);
        map.put("DirectoryTreeId", directoryTreeId);
        map.put("ConstructionId", constructionId);
        return R.ok(1, "获取成功!", map, true, null);
    }

    //获取wbs绑定模型重大风险源信息
    @Override
    public R getSourceRisk(String wbsId) {
        Map<String, Object> data = projectWbsbindgroupsourceriskService.getSourceRisk(wbsId);
        return R.ok(1, "获取成功!", data, true, null);
    }

    //获取wbs绑定模型施工重大设备信息
    @Override
    public R getEquipment(String wbsId) {
        Map<String, Object> map = projectWbsbindgroupequipmentService.getEquipment(wbsId);
        return R.ok(1, "获取成功!", map, true, null);
    }

    //绑定wbs与树节点关系
    @Override
    public R bindWbsGroupId(WbsBindParams wbsBindParams) {
        String[] tree = wbsBindParams.getConstructionId().split(",");
        String[] projectTree = projectWbsbindgroupService.getNotWbsId(wbsBindParams.getWbsId());
        for (int i = 0; i < tree.length; i++) {
            if (tree[i].equals("0")) {
                continue;
            }
            if (ArrayUtils.contains(projectTree, tree[i])) {
                return R.ok(0, "构建id已绑定!", "构建id已绑定!", true, null);
            }
        }
        ProjectWbsEntity entity = this.getById(wbsBindParams.getWbsId());
        entity.setBinding(true);
        ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbsBindParams.getWbsId());
        if (wbsBind == null) {
            ProjectWbsbindgroupEntity binds = new ProjectWbsbindgroupEntity();
            binds.setConstructionid(wbsBindParams.getConstructionId());
            binds.setDirectorytreeid(wbsBindParams.getDirectoryTreeId());
            binds.setWbsid(wbsBindParams.getWbsId());
            ProjectWbsEntity wbsEntity = new ProjectWbsEntity();
            wbsEntity.setBinding(true);
            wbsEntity.setSysid(wbsBindParams.getWbsId());
            projectWbsbindgroupService.addWbsBind(binds);
            this.updateById(wbsEntity);
        } else {
            wbsBind.setConstructionid(wbsBindParams.getConstructionId());
            wbsBind.setDirectorytreeid(wbsBindParams.getDirectoryTreeId());
            projectWbsbindgroupService.updateWbsBind(wbsBind);
        }
        return R.ok(1, "绑定成功!", null, true, null);
    }

    //解除wbs与树节点关系
    @Override
    public void deleteBindWbsGroup(String wbsId) {
        projectWbsbindgroupService.deleteBindWbsGroup(wbsId);
        ProjectWbsEntity entity = new ProjectWbsEntity();
        entity.setSysid(wbsId);
        entity.setBinding(false);
        this.updateById(entity);
    }

    //获取所有管理报告信息
    @Override
    public R getManagementReportInformation(String projectId, String wbsId) {
        if (!StringUtils.isEmpty(wbsId)) {
            ProjectWbsEntity wbsEntity = this.getById(wbsId);
            List<String> list = new ArrayList<>();
            checkWbsParent(wbsEntity.getParentid(), list);
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> equipment = projectWbsbindgroupequipmentService.getEquipment(wbsId);
            if (equipment != null) {
                equipment.remove("WbsId");
                equipment.put("wbsId", wbsId);
                equipment.put("partName", wbsEntity.getPartname());
            }
            map.put("Equipment", equipment);
            map.put("FactBeginDate", wbsEntity.getFactbegindate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
            map.put("FactEndDate", wbsEntity.getFactenddate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
            map.put("IsKey", wbsEntity.getIskey() == 1 ? "是" : "否");
            map.put("PartName", wbsEntity.getPartname());
            map.put("PhotoId", wbsEntity.getSetbacksupload() == null ? "" : wbsEntity.getSetbacksupload());
            map.put("PlanBeginDate", wbsEntity.getPlanbegindate() == null ? "" : format.format(wbsEntity.getPlanbegindate()));
            map.put("PlanEndDate", wbsEntity.getPlanenddate() == null ? "" : format.format(wbsEntity.getPlanenddate()));
            map.put("Remark", wbsEntity.getRemark());
            map.put("WbsFullPath", getFullName(wbsEntity));
            map.put("WbsId", wbsEntity.getSysid());
//            Map<String, Object> zhiliang = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 1);
//            Map<String, Object> anquan = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 2);
            List<Map<String, Object>> zhiliang = projectReportService.getAllAnQuanOrZhiLiang(projectId, wbsEntity.getSysid(), "1", wbsEntity.getPartname());
            List<Map<String, Object>> anquan = projectReportService.getAllAnQuanOrZhiLiang(projectId, wbsEntity.getSysid(), "3", wbsEntity.getPartname());
            Map<String, Object> sourceRisk = projectWbsbindgroupsourceriskService.getSourceRisk(wbsId);
            if (sourceRisk != null) {
                sourceRisk.remove("WbsId");
                sourceRisk.put("wbsId", wbsId);
                sourceRisk.put("partName", wbsEntity.getPartname());
            }
            Map<String, Object> wbsSize = projectWbsSizeService.queryWbsSize(wbsId, "1");
            map.put("ZhiLiangManage", wbsSize);
            map.put("ZhiLiang", zhiliang);
            map.put("Sourcerisk", sourceRisk);
            map.put("AnQuan", anquan);
            data.add(map);
            list.forEach(li -> {
                ProjectWbsEntity entity = this.getById(li);
                Map<String, Object> maps = new HashMap<>();
                Map<String, Object> equipments = projectWbsbindgroupequipmentService.getEquipment(entity.getSysid());
                if (equipments != null) {
                    equipments.remove("WbsId");
                    equipments.put("wbsId", entity.getSysid());
                    equipments.put("partName", entity.getPartname());
                }
                maps.put("Equipment", equipments);
                maps.put("FactBeginDate", entity.getFactbegindate() == null ? "" : format.format(entity.getFactbegindate()));
                maps.put("FactEndDate", entity.getFactenddate() == null ? "" : format.format(entity.getFactenddate()));
                maps.put("IsKey", entity.getIskey() == 1 ? "是" : "否");
                maps.put("PartName", entity.getPartname());
                maps.put("PhotoId", entity.getSetbacksupload() == null ? "" : entity.getSetbacksupload());
                maps.put("PlanBeginDate", entity.getPlanbegindate() == null ? "" : format.format(entity.getPlanbegindate()));
                maps.put("PlanEndDate", entity.getPlanenddate() == null ? "" : format.format(entity.getPlanenddate()));
                maps.put("Remark", entity.getRemark());
                maps.put("WbsFullPath", getFullName(entity));
                maps.put("WbsId", entity.getSysid());
//                Map<String, Object> zhiliangs = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 1);
//                Map<String, Object> anquans = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 2);
                List<Map<String, Object>> zhiliangs = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "1", entity.getPartname());
                List<Map<String, Object>> anquans = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "3", entity.getPartname());
                Map<String, Object> sourceRisks = projectWbsbindgroupsourceriskService.getSourceRisk(entity.getSysid());
                if (sourceRisks != null) {
                    sourceRisks.remove("WbsId");
                    sourceRisks.put("wbsId", entity.getSysid());
                    sourceRisks.put("partName", entity.getPartname());
                }
                Map<String, Object> wbsSize1 = projectWbsSizeService.queryWbsSize(li, "1");
                maps.put("ZhiLiangManage", wbsSize1);
                maps.put("ZhiLiang", zhiliangs);
                maps.put("Sourcerisk", sourceRisks);
                maps.put("AnQuan", anquans);
                data.add(maps);
            });
            return R.ok(1, "获取成功!", data, true, null);
        } else {
            List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", projectId).eq("del", 0));
            List<Map<String, Object>> data = new ArrayList<>();
            list.forEach(entity -> {
                Map<String, Object> maps = new HashMap<>();
                Map<String, Object> equipments = projectWbsbindgroupequipmentService.getEquipment(entity.getSysid());
                if (equipments != null) {
                    equipments.remove("WbsId");
                    equipments.put("wbsId", entity.getSysid());
                    equipments.put("partName", entity.getPartname());
                }
                maps.put("Equipment", equipments);
                maps.put("FactBeginDate", entity.getFactbegindate() == null ? "" : format.format(entity.getFactbegindate()));
                maps.put("FactEndDate", entity.getFactenddate() == null ? "" : format.format(entity.getFactenddate()));
                maps.put("IsKey", entity.getIskey() == 1 ? "是" : "否");
                maps.put("PartName", entity.getPartname());
                maps.put("PhotoId", entity.getSetbacksupload() == null ? "" : entity.getSetbacksupload());
                maps.put("PlanBeginDate", entity.getPlanbegindate() == null ? "" : format.format(entity.getPlanbegindate()));
                maps.put("PlanEndDate", entity.getPlanenddate() == null ? "" : format.format(entity.getPlanenddate()));
                maps.put("Remark", entity.getRemark());
                maps.put("WbsFullPath", getFullName(entity));
                maps.put("WbsId", entity.getSysid());
//                Map<String, Object> zhiliangs = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 1);
//                Map<String, Object> anquans = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 2);
                List<Map<String, Object>> zhiliangs = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "1", entity.getPartname());
                List<Map<String, Object>> anquans = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "3", entity.getPartname());
                Map<String, Object> sourceRisks = projectWbsbindgroupsourceriskService.getSourceRisk(entity.getSysid());
                if (sourceRisks != null) {
                    sourceRisks.remove("WbsId");
                    sourceRisks.put("wbsId", entity.getSysid());
                    sourceRisks.put("partName", entity.getPartname());
                }
                Map<String, Object> wbsSize = projectWbsSizeService.queryWbsSize(entity.getSysid(), "1");
                maps.put("ZhiLiangManage", wbsSize);
                maps.put("ZhiLiang", zhiliangs);
                maps.put("Sourcerisk", sourceRisks);
                maps.put("AnQuan", anquans);
                data.add(maps);
            });
            return R.ok(1, "获取成功!", data, true, null);
        }
    }

    //获取构建id管理报告信息
    @Override
    public R getWbsIdManagementReportInfo(String projectId, String constructionId) {
        String wbsId = projectWbsbindgroupService.getWbsId(constructionId);
        if (wbsId != null) {
            ProjectWbsEntity wbsEntity = this.getById(wbsId);
            List<String> list = new ArrayList<>();
            checkWbsParent(wbsEntity.getParentid(), list);
            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> equipment = projectWbsbindgroupequipmentService.getEquipment(wbsId);
            if (equipment != null) {
                equipment.remove("WbsId");
                equipment.put("wbsId", wbsId);
                equipment.put("partName", wbsEntity.getPartname());
            }
            map.put("Equipment", equipment);
            map.put("FactBeginDate", wbsEntity.getFactbegindate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
            map.put("FactEndDate", wbsEntity.getFactenddate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
            map.put("IsKey", wbsEntity.getIskey() == 1 ? "是" : "否");
            map.put("PartName", wbsEntity.getPartname());
            map.put("PhotoId", wbsEntity.getSetbacksupload() == null ? "" : wbsEntity.getSetbacksupload());
            map.put("PlanBeginDate", wbsEntity.getPlanbegindate() == null ? "" : format.format(wbsEntity.getPlanbegindate()));
            map.put("PlanEndDate", wbsEntity.getPlanenddate() == null ? "" : format.format(wbsEntity.getPlanenddate()));
            map.put("Remark", wbsEntity.getRemark());
            map.put("WbsFullPath", getFullName(wbsEntity));
            map.put("WbsId", wbsEntity.getSysid());
//            Map<String, Object> zhiliang = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 1);
//            Map<String, Object> anquan = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 2);
            List<Map<String, Object>> zhiliang = projectReportService.getAllAnQuanOrZhiLiang(projectId, wbsEntity.getSysid(), "1", wbsEntity.getPartname());
            List<Map<String, Object>> anquan = projectReportService.getAllAnQuanOrZhiLiang(projectId, wbsEntity.getSysid(), "3", wbsEntity.getPartname());
            Map<String, Object> sourceRisk = projectWbsbindgroupsourceriskService.getSourceRisk(wbsId);
            if (sourceRisk != null) {
                sourceRisk.remove("WbsId");
                sourceRisk.put("wbsId", wbsId);
                sourceRisk.put("partName", wbsEntity.getPartname());
            }
            Map<String, Object> wbsSize = projectWbsSizeService.queryWbsSize(wbsId, "1");
            map.put("ZhiLiangManage", wbsSize);
            map.put("ZhiLiang", zhiliang);
            map.put("Sourcerisk", sourceRisk);
            map.put("AnQuan", anquan);
            data.add(map);
            list.forEach(li -> {
                ProjectWbsEntity entity = this.getById(li);
                Map<String, Object> maps = new HashMap<>();
                Map<String, Object> equipments = projectWbsbindgroupequipmentService.getEquipment(entity.getSysid());
                if (equipments != null) {
                    equipments.remove("WbsId");
                    equipments.put("wbsId", entity.getSysid());
                    equipments.put("partName", entity.getPartname());
                }
                maps.put("Equipment", equipments);
                maps.put("FactBeginDate", entity.getFactbegindate() == null ? "" : format.format(entity.getFactbegindate()));
                maps.put("FactEndDate", entity.getFactenddate() == null ? "" : format.format(entity.getFactenddate()));
                maps.put("IsKey", entity.getIskey() == 1 ? "是" : "否");
                maps.put("PartName", entity.getPartname());
                maps.put("PhotoId", entity.getSetbacksupload() == null ? "" : entity.getSetbacksupload());
                maps.put("PlanBeginDate", entity.getPlanbegindate() == null ? "" : format.format(entity.getPlanbegindate()));
                maps.put("PlanEndDate", entity.getPlanenddate() == null ? "" : format.format(entity.getPlanenddate()));
                maps.put("Remark", entity.getRemark());
                maps.put("WbsFullPath", getFullName(entity));
                maps.put("WbsId", entity.getSysid());
//                Map<String, Object> zhiliangs = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 1);
//                Map<String, Object> anquans = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(entity, 2);
                List<Map<String, Object>> zhiliangs = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "1", entity.getPartname());
                List<Map<String, Object>> anquans = projectReportService.getAllAnQuanOrZhiLiang(projectId, entity.getSysid(), "3", entity.getPartname());
                Map<String, Object> sourceRisks = projectWbsbindgroupsourceriskService.getSourceRisk(entity.getSysid());
                if (sourceRisks != null) {
                    sourceRisks.remove("WbsId");
                    sourceRisks.put("wbsId", entity.getSysid());
                    sourceRisks.put("partName", entity.getPartname());
                }
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(entity.getSysid(), "1");
                maps.put("ZhiLiangManage", wbsSizes);
                maps.put("ZhiLiang", zhiliangs);
                maps.put("Sourcerisk", sourceRisks);
                maps.put("AnQuan", anquans);
                data.add(maps);
            });
            return R.ok(1, "获取成功!", data, true, null);
        }
        return R.ok(1, "获取失败!", null, true, null);
    }

    //形象进度信息
    @Override
    public R getImageProgress(String projectId, String planBeginDate, String planEnDate) {
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", projectId).eq("del", 0).isNull("parentId"));
        List<String> id = new ArrayList<>();
        getLastWbsId(list, id);
        List<Map<String, Object>> wangong = new ArrayList<>();
        List<Map<String, Object>> weikaigong = new ArrayList<>();
        List<Map<String, Object>> yanqi = new ArrayList<>();
        List<Map<String, Object>> yikaigong = new ArrayList<>();
        List<ProjectWbsEntity> wangongs = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("planBeginDate").isNotNull("planEndDate").isNotNull("factBeginDate").isNotNull("factEndDate"));
        List<ProjectWbsEntity> weikaigongs = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("planBeginDate").isNotNull("planEndDate").isNull("factBeginDate").isNull("factEndDate"));
        List<ProjectWbsEntity> yanqis = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("planBeginDate").isNotNull("planEndDate").isNotNull("factBeginDate").isNull("factEndDate").lt("planEndDate", new Date()));
        List<ProjectWbsEntity> yikaigongs = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("planBeginDate").isNotNull("planEndDate").isNotNull("factBeginDate").isNull("factEndDate").gt("planEndDate", new Date()));
        wangongs.forEach(wg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("WbsId", wg.getSysid());
            map.put("partName", wg.getPartname());
            map.put("planBeginDate", format.format(wg.getPlanbegindate()));
            map.put("planEndDate", format.format(wg.getPlanbegindate()));
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wg.getSysid());
            if (wbsBind != null) {
                map.put("ConstructionId", wbsBind.getConstructionid());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
            } else {
                map.put("ConstructionId", null);
                map.put("DirectoryTreeId", null);
            }
            wangong.add(map);
        });
        weikaigongs.forEach(wg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("WbsId", wg.getSysid());
            map.put("partName", wg.getPartname());
            map.put("planBeginDate", format.format(wg.getPlanbegindate()));
            map.put("planEndDate", format.format(wg.getPlanbegindate()));
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wg.getSysid());
            if (wbsBind != null) {
                map.put("ConstructionId", wbsBind.getConstructionid());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
            } else {
                map.put("ConstructionId", null);
                map.put("DirectoryTreeId", null);
            }
            weikaigong.add(map);
        });
        yanqis.forEach(wg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("WbsId", wg.getSysid());
            map.put("partName", wg.getPartname());
            map.put("planBeginDate", format.format(wg.getPlanbegindate()));
            map.put("planEndDate", format.format(wg.getPlanbegindate()));
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wg.getSysid());
            if (wbsBind != null) {
                map.put("ConstructionId", wbsBind.getConstructionid());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
            } else {
                map.put("ConstructionId", null);
                map.put("DirectoryTreeId", null);
            }
            yanqi.add(map);
        });
        yikaigongs.forEach(wg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("WbsId", wg.getSysid());
            map.put("partName", wg.getPartname());
            map.put("planBeginDate", format.format(wg.getPlanbegindate()));
            map.put("planEndDate", format.format(wg.getPlanbegindate()));
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wg.getSysid());
            if (wbsBind != null) {
                map.put("ConstructionId", wbsBind.getConstructionid());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
            } else {
                map.put("ConstructionId", null);
                map.put("DirectoryTreeId", null);
            }
            yikaigong.add(map);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("wanGong", wangong);
        res.put("weiKaiGong", weikaigong);
        res.put("yanQi", yanqi);
        res.put("yiKaiGong", yikaigong);
        return R.ok(1, "获取成功!", res, true, null);
    }

    //添加wbs绑定模型重大风险源信息
    @Override
    public void addSourceRisk(BindSourceRiskParams bindSourceRiskParams) {
        projectWbsbindgroupsourceriskService.addSourceRisk(bindSourceRiskParams);
    }

    //添加wbs绑定模型施工重大设备信息
    @Override
    public void addEquipment(BindEquipmentParams bindEquipmentParams) {
        projectWbsbindgroupequipmentService.addEquipment(bindEquipmentParams);
    }

    //修改wbs绑定模型重大风险源信息
    @Override
    public void putSourceRisk(BindSourceRiskParams bindSourceRiskParams) {
        projectWbsbindgroupsourceriskService.putSourceRisk(bindSourceRiskParams);
    }

    //修改wbs绑定模型施工重大设备信息
    @Override
    public void putEquipment(BindEquipmentParams bindEquipmentParams) {
        projectWbsbindgroupequipmentService.putEquipment(bindEquipmentParams);
    }

    //删除wbs绑定模型重大风险源信息
    @Override
    public void deleteSourceRisk(String sysId) {
        projectWbsbindgroupsourceriskService.deleteSourceRisk(sysId);
    }

    //删除wbs绑定模型施工重大设备信息
    @Override
    public void deleteEquipment(String sysId) {
        projectWbsbindgroupequipmentService.deleteEquipment(sysId);
    }

    //获取周报信息和周报的报告管理信息
    @Override
    public R getWeeklyInfo(String projectId, Date startWeek, Date endWeek) {
        Calendar next = Calendar.getInstance();
        Calendar pre = Calendar.getInstance();
        Calendar nextTwo = Calendar.getInstance();
        next.setTime(endWeek);
        next.add(Calendar.DATE, 7);
        pre.setTime(startWeek);
        pre.add(Calendar.DATE, -1);
        nextTwo.setTime(endWeek);
        nextTwo.add(Calendar.DATE, 14);
        Date nextWeek = next.getTime();//下周星期天
        Date prevWeek = pre.getTime();//上周星期天
        Date twoWeekAfter = nextTwo.getTime();//两周后周日
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", projectId).eq("del", 0).isNull("parentId"));
        List<String> id = new ArrayList<>();
        getLastWbsId(list, id);
        List<Map<String, Object>> thisWeek = new ArrayList<>();//本周在做
        List<Map<String, Object>> thisWeekComplete = new ArrayList<>();//本周完成
        List<Map<String, Object>> nextWeekDo = new ArrayList<>();//下周做什么
        List<Map<String, Object>> completeOnWeek = new ArrayList<>();//实际完成时间在上周之前(包括上周)
        List<Map<String, Object>> noFactBeginAfterTwoWeek = new ArrayList<>();//没有实际开始时间且计划开始时间在两周后
        List<Map<String, Object>> weekManagementReport = new ArrayList<>();
        List<Map<String, Object>> weekCompleteManagementReport = new ArrayList<>();
        log.info("startWeek" + startWeek);
        log.info("endWeek" + endWeek);
        //本周在做
        List<ProjectWbsEntity> thisWeekList = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("factBeginDate").isNull("factEndDate"));
        //本周完成
        List<ProjectWbsEntity> thisWeekCompleteList = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("factBeginDate").isNotNull("factEndDate").ge("factEndDate", format.format(startWeek)).le("factEndDate", format.format(endWeek)));
        //下周做什么
        List<ProjectWbsEntity> nextWeekDoList = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNull("factBeginDate").lt("planBeginDate", format.format(nextWeek)));
        //实际完成时间在上周之前(包括上周)
        List<ProjectWbsEntity> completeOnWeekList = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNotNull("factEndDate").le("factEndDate", format.format(prevWeek)));
        //没有实际开始时间且计划开始时间在两周后
        List<ProjectWbsEntity> noFactBeginAfterTwoWeekList = this.list(new QueryWrapper<ProjectWbsEntity>().in("sysId", id).eq("del", 0).isNull("factBeginDate").gt("planBeginDate", format.format(twoWeekAfter)));
        thisWeekList.forEach(wbs -> {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbs.getSysid());
            if (wbsBind != null&&wbsBind.getConstructionid()!=null&&wbsBind.getConstructionid()!="") {
                Map<String, Object> map = new HashMap<>();
                map.put("WbsId", wbs.getSysid());
                map.put("partName", wbs.getPartname());
                map.put("planBeginDate", wbs.getPlanbegindate());
                map.put("planEndDate", wbs.getPlanenddate());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
                map.put("ConstructionId", wbsBind.getConstructionid());
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(wbs.getSysid(), "1");
                map.put("ZhiLiangManage", wbsSizes);
                thisWeek.add(map);
                Map<String, Object> report = getWeekManagementReport(wbs.getSysid(), projectId);
                if (report != null) {
                    weekManagementReport.add(report);
                }
            }
        });
        thisWeekCompleteList.forEach(wbs -> {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbs.getSysid());
            if (wbsBind != null&&wbsBind.getConstructionid()!=null&&wbsBind.getConstructionid()!="") {
                Map<String, Object> map = new HashMap<>();
                map.put("WbsId", wbs.getSysid());
                map.put("partName", wbs.getPartname());
                map.put("planBeginDate", wbs.getPlanbegindate());
                map.put("planEndDate", wbs.getPlanenddate());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
                map.put("ConstructionId", wbsBind.getConstructionid());
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(wbs.getSysid(), "1");
                map.put("ZhiLiangManage", wbsSizes);
                thisWeekComplete.add(map);
                Map<String, Object> report = getWeekManagementReport(wbs.getSysid(), projectId);
                if (report != null) {
                    weekCompleteManagementReport.add(report);
                }
            }
        });
        nextWeekDoList.forEach(wbs -> {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbs.getSysid());
            if (wbsBind != null&&wbsBind.getConstructionid()!=null&&wbsBind.getConstructionid()!="") {
                Map<String, Object> map = new HashMap<>();
                map.put("WbsId", wbs.getSysid());
                map.put("partName", wbs.getPartname());
                map.put("planBeginDate", wbs.getPlanbegindate());
                map.put("planEndDate", wbs.getPlanenddate());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
                map.put("ConstructionId", wbsBind.getConstructionid());
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(wbs.getSysid(), "1");
                map.put("ZhiLiangManage", wbsSizes);
                nextWeekDo.add(map);
            }
        });
        completeOnWeekList.forEach(wbs -> {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbs.getSysid());
            if (wbsBind != null&&wbsBind.getConstructionid()!=null&&wbsBind.getConstructionid()!="") {
                Map<String, Object> map = new HashMap<>();
                map.put("WbsId", wbs.getSysid());
                map.put("partName", wbs.getPartname());
                map.put("planBeginDate", wbs.getPlanbegindate());
                map.put("planEndDate", wbs.getPlanenddate());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
                map.put("ConstructionId", wbsBind.getConstructionid());
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(wbs.getSysid(), "1");
                map.put("ZhiLiangManage", wbsSizes);
                completeOnWeek.add(map);
            }
        });
        noFactBeginAfterTwoWeekList.forEach(wbs -> {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbs.getSysid());
            if (wbsBind != null&&wbsBind.getConstructionid()!=null&&wbsBind.getConstructionid()!="") {
                Map<String, Object> map = new HashMap<>();
                map.put("WbsId", wbs.getSysid());
                map.put("partName", wbs.getPartname());
                map.put("planBeginDate", wbs.getPlanbegindate());
                map.put("planEndDate", wbs.getPlanenddate());
                map.put("DirectoryTreeId", wbsBind.getDirectorytreeid());
                map.put("ConstructionId", wbsBind.getConstructionid());
                Map<String, Object> wbsSizes = projectWbsSizeService.queryWbsSize(wbs.getSysid(), "1");
                map.put("ZhiLiangManage", wbsSizes);
                noFactBeginAfterTwoWeek.add(map);
            }
        });
        Map<String, Object> res = new HashMap<>();
        res.put("ThisWeek", thisWeek);
        res.put("ThisWeekComplete", thisWeekComplete);
        res.put("NextWeek", nextWeekDo);
        res.put("CompleteOnWeek", completeOnWeek);
        res.put("NoFactBeginAfterTwoWeek", noFactBeginAfterTwoWeek);
        res.put("ThisWeekManagementReport", weekManagementReport);
        res.put("ThisWeekCompleteManagementReport", weekCompleteManagementReport);
        return R.ok(1, "获取成功!", res, true, null);
    }

    //修改进度管理信息
    @Override
    public void putJinDu(JinDuParams jinDuParams) {
        ProjectWbsEntity jindu = this.getById(jinDuParams.getWbsId());
        try {
            if (jindu != null) {
                if (jinDuParams.getFactBeginDate() != "" && jinDuParams.getPlanBeginDate() != null) {
                    jindu.setPlanbegindate(format.parse(jinDuParams.getPlanBeginDate()));
                } else {
                    jindu.setPlanbegindate(null);
                }
                if (jinDuParams.getPlanEndDate() != "" && jinDuParams.getPlanEndDate() != null) {
                    jindu.setPlanenddate(format.parse(jinDuParams.getPlanEndDate()));
                } else {
                    jindu.setPlanenddate(null);
                }
                //获取父类
                ProjectWbsEntity parent = this.getById(jindu.getParentid());
                if (parent != null) {
                    //修改父类计划开始结束信息
                    UpdateParentPlanBeginEnd(jindu.getPlanbegindate(), jindu.getPlanenddate(), parent);
                    List<ProjectWbsEntity> list = new ArrayList<>();
                    EachUpdateWbs(parent.getParentid(),list);//修改父类的父类计划开始结束信息
                    this.updateBatchById(list);
                }

                if (jinDuParams.getFactBeginDate() != "" && jinDuParams.getFactBeginDate() != null) {
                    jindu.setFactbegindate(format.parse(jinDuParams.getFactBeginDate()));
                } else {
                    jindu.setFactbegindate(null);
                }
                if (jinDuParams.getFactEndDate() != "" && jinDuParams.getFactEndDate() != null) {
                    jindu.setFactenddate(format.parse(jinDuParams.getFactEndDate()));
                } else {
                    jindu.setFactenddate(null);
                }
                jindu.setIskey(0);
                if (jinDuParams.getIsKey().equals("是")) {
                    jindu.setIskey(1);
                }
                jindu.setSetbacksupload(jinDuParams.getImagePath());
                jindu.setRemark(jinDuParams.getRemark());
                this.updateById(jindu);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //获取全部里程碑节点
    @Override
    public List<ProjectWbsEntity> getMilestoneNode() {
        return this.list(new QueryWrapper<ProjectWbsEntity>().eq("del",0).eq("isKey",1));
    }

    //获取无流程提交信息Wbs的id
    @Override
    public List<String> getWbsId(String projectId) {
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", projectId).eq("del",0));
        List<String> wbsId = new ArrayList<>();
        list.forEach(li -> {
            wbsId.add(li.getSysid());
        });
        return wbsId;
    }

    //获取分部分项信息
    @Override
    public List<ProjectWbsEntity> getWbsDetails(String sysid) {
        return this.list(new QueryWrapper<ProjectWbsEntity>().eq("projectId", sysid).eq("del",0));
    }

    //获取树形数据
    private List<Map<String, Object>> getTree(String projectId, List<ProjectWbsEntity> list, List<ProjectWbsEntity> all) {
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li -> {
            Map<String, Object> map = new HashMap<>();
//            List<ProjectWbsEntity> child = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).eq("parentId", li.getSysid()).orderByAsc("partCode"));
            List<ProjectWbsEntity> child = new ArrayList<>();
            Iterator<ProjectWbsEntity> iterator = all.iterator();
            while (iterator.hasNext()){
                ProjectWbsEntity next = iterator.next();
                if(li.getSysid().equals(next.getParentid())){
                    child.add(next);
                    iterator.remove();
                }
            }
            map.put("parentId", li.getParentid());
            map.put("id", li.getSysid());
            map.put("text", li.getPartname());
            map.put("value", li.getPartname());
            map.put("checkstate", null);
            map.put("showcheck", false);
            map.put("complete", false);
            map.put("isexpand", false);
            map.put("level", li.getMpplevel());
            map.put("hasChildren", child.size() > 0 ? true : false);
            map.put("img", null);
            map.put("title", null);
            Map<String, Object> entity = new HashMap<>();
            entity.put("sysId", li.getSysid());
            entity.put("projectId", li.getProjectid());
            entity.put("parentId", li.getParentid());
            entity.put("fullSysId", li.getFullsysid());
            entity.put("partCode", li.getPartcode());
            entity.put("partCodeBim", li.getPartcodebim());
            entity.put("partName", li.getPartname());
            entity.put("partType", li.getParttype());
            entity.put("iskey", li.getIskey().toString());
            entity.put("planBeginDate", li.getPlanbegindate());
            entity.put("planEndDate", li.getPlanenddate());
            entity.put("finishPercent", li.getFinishpercent());
            entity.put("finishState", li.getFinishstate());
            entity.put("factBeginDate", li.getFactbegindate());
            entity.put("factEndDate", li.getFactenddate());
            entity.put("orderVal", li.getOrderval());
            entity.put("setbacksupload", li.getSetbacksupload());
            entity.put("unit", li.getUnit());
            entity.put("reportPerson", li.getReportperson());
            entity.put("reportDate", li.getReportdate());
            entity.put("mainUnit", li.getMainunit());
            entity.put("completePercent", li.getCompletepercent());
            entity.put("completeQuantity", li.getCompletequantity());
            entity.put("auditedEndDate", li.getAuditedenddate());
            entity.put("remark", li.getRemark());
            entity.put("imagePath", "");
            entity.put("binding", child.size() > 0 ? chekBinding(child) : li.getBinding() == null ? "False" : (li.getBinding() == true ? "True" : "False"));
            map.put("entity", entity);
            map.put("childNodes", child.size() > 0 ? getTree(projectId, child, all) : new ArrayList<>());
            data.add(map);
        });
        return data;
    }
    //判断父级是否需要高亮
    public String chekBinding(List<ProjectWbsEntity> child){
        for(int i=0;i<child.size();i++){
            if(child.get(i).getBinding()==null||child.get(i).getBinding()==false){
                return "False";
            }
        }
        return "True";
    }

    //计算code
    private String getCode(ProjectWbsEntity parent) {
//        if(parent==null){
//            return "001";
//        }
        List<ProjectWbsEntity> list = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("parentId", parent.getSysid()).eq("projectId", parent.getProjectid()).orderByDesc("partCode"));
        if (list!=null&&list.size() > 0) {
            String code = list.get(0).getPartcode();
            String s = code.substring(code.length() - 3);
            int i = Integer.parseInt(s) + 1;
            if (i > 999) {
                return "";
            } else {
                if (i < 10) {
                    String rp = "00" + String.valueOf(i);
                    StringBuilder builder = new StringBuilder(code);
                    builder.replace(code.length() - 3, code.length(), rp);
                    return builder.toString();
                } else if (i >= 10 && i < 100) {
                    String rp = "0" + String.valueOf(i);
                    StringBuilder builder = new StringBuilder(code);
                    builder.replace(code.length() - 3, code.length(), rp);
                    return builder.toString();
                } else if (i >= 100) {
                    StringBuilder builder = new StringBuilder(code);
                    builder.replace(code.length() - 3, code.length(), String.valueOf(i));
                    return builder.toString();
                }
            }
        } else {
            String code = parent.getPartcode();
            return code + "001";
        }
        return "";
    }

    //计算mppwbs
    private String getMppWbs(String partCode) {
        if (!StringUtils.isEmpty(partCode)) {
            String mppwbs = "";
            for (int i = 0; i < partCode.length() / 3; i++) {
//                mppwbs += partCode.substring(i * 3 + 2, i * 3 + 3) + ".";
                mppwbs += Integer.parseInt(partCode.substring(i * 3 , i * 3 + 3)) + ".";
            }
            mppwbs = mppwbs.substring(0, mppwbs.length() - 1);
            return mppwbs;
        } else {
            return "";
        }
    }

    //计算partCodeBim
    private String getPartCodeBim(String mppWbs) {
        if (!StringUtils.isEmpty(mppWbs)) {
            String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ", "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III", "JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR", "SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ"};
            String[] str = mppWbs.split("\\.");
            String bimcod = "";
            log.error("mppWbs"+mppWbs);
            log.error("letter长度"+letter.length);
            log.error("str长度"+str.length);
            for (int i = 0; i < str.length; i++) {
                if (i % 2 == 0) {
                    bimcod += str[i];
                } else {
                    bimcod += letter[Integer.parseInt(str[i]) - 1];
                }
            }
            return bimcod;
        } else {
            return "";
        }
    }

    //生成execl数据
    private int getExecl(String projectId, List<ProjectWbsEntity> list, XSSFSheet sheet, int num) {
        for (ProjectWbsEntity li : list) {
            log.info("插入数据" + li.getPartname());
            log.info("插入数据num" + num);
            XSSFRow row = sheet.createRow(num);
            row.createCell(0).setCellValue(num);
            row.createCell(1).setCellValue(li.getPartname());
            row.createCell(2).setCellValue(li.getPartcode());
            row.createCell(3).setCellValue(li.getPartcodebim());
            row.createCell(4).setCellValue(li.getPlanbegindate() == null ? "" : format.format(li.getPlanbegindate()));
            row.createCell(5).setCellValue(li.getPlanenddate() == null ? "" : format.format(li.getPlanenddate()));
            row.createCell(6).setCellValue(li.getMppplanbegindate() == null ? "" : format.format(li.getMppplanbegindate()));
            row.createCell(7).setCellValue(li.getMppplanenddate() == null ? "" : format.format(li.getMppplanenddate()));
            num++;
            List<ProjectWbsEntity> child = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("projectId", projectId).eq("parentId", li.getSysid()));
            num = getExecl(projectId, child, sheet, num);
        }
        return num;
    }

    //project文件导入
    private void getChildrenTask(Task task, ProjectWbsEntity project, String filePath, String projectId) {
        if (task.getResourceAssignments().size() == 0) {
            List<Task> list = task.getChildTasks();
            for (int i = 0; i < list.size(); i++) {
                //获取partCode
                String code = getCode(project);
                //获取mppwbs
                String mppWbs = getMppWbs(code);
                //获取partCodeBim
                String partCodeBim = getPartCodeBim(mppWbs);
                //还有子元素
                ProjectWbsEntity entity = new ProjectWbsEntity();
                entity.setSysid(CreateUuid.uuid());
                entity.setPartname(list.get(i).getName());
                entity.setParentid(project.getSysid());
                entity.setProjectid(projectId);
                entity.setPartcode(code);
                entity.setPartcodebim(partCodeBim);
                entity.setMppwbs(mppWbs);
                entity.setOrderval(code);
                entity.setPlanbegindate(list.get(i).getStart());
                entity.setPlanenddate(list.get(i).getFinish());
                entity.setMpplevel(project.getMpplevel() + 1);
                entity.setMpppath(filePath);//等待文件地址
                entity.setDel(0);
                entity.setCreatedate(new Date());
                entity.setIskey(0);
                entity.setForeignkey(CreateUuid.uuid());
                entity.setFinishstate(0);
                entity.setFinishpercent(0f);
                entity.setFullsysid(project.getSysid() + "/" + entity.getSysid());
                this.save(entity);
                getChildrenTask(list.get(i), entity, filePath, projectId);
            }
        }
    }

    //判断wbs是否含有子节点
    private void checkWbs(String wbsId, List<ProjectWbsbindgroupEntity> list) {
        List<ProjectWbsEntity> list1 = this.list(new QueryWrapper<ProjectWbsEntity>().eq("del", 0).eq("parentId", wbsId));
        if (list1.size() > 0 && list1 != null) {
            list1.forEach(li -> {
                checkWbs(li.getSysid(), list);
            });
        } else {
            ProjectWbsbindgroupEntity wbsBind = projectWbsbindgroupService.getWbsBind(wbsId);
            list.add(wbsBind);
        }
    }

    //获取wbs所有父节点
    private void checkWbsParent(String wbsId, List<String> list) {
        ProjectWbsEntity entity = this.getById(wbsId);
        if (entity != null) {
            list.add(entity.getSysid());
            if (!StringUtils.isEmpty(entity.getParentid())) {
                checkWbsParent(entity.getParentid(), list);
            }
        }
    }

    //获取全路径
    private String getFullName(ProjectWbsEntity wbs) {
        String[] split = wbs.getFullsysid().split("/");
        String name = null;
        for (int i = 0; i < split.length; i++) {
            ProjectWbsEntity entity = this.getById(split[i]);
            if (i == 0) {
                name = entity.getPartname();
            } else {
                name += "/" + entity.getPartname();
            }
        }
        return name;
    }

    //获取最底部wbsId
    private void getLastWbsId(List<ProjectWbsEntity> list, List<String> id) {
        for (int i = 0; i < list.size(); i++) {
            List<ProjectWbsEntity> entityList = this.list(new QueryWrapper<ProjectWbsEntity>().eq("parentId", list.get(i).getSysid()).eq("del", 0));
            if (entityList != null && entityList.size() > 0) {
                getLastWbsId(entityList, id);
            } else {
                id.add(list.get(i).getSysid());
            }
        }
    }

    //周报中本周在做和本周完成的报告信息
    private Map<String, Object> getWeekManagementReport(String wbsId, String projectId) {
        ProjectWbsEntity wbsEntity = this.getById(wbsId);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> equipment = projectWbsbindgroupequipmentService.getEquipment(wbsId);
        if (equipment != null) {
            equipment.remove("WbsId");
            equipment.put("wbsId", wbsId);
            equipment.put("partName", wbsEntity.getPartname());
        }
        map.put("Equipment", equipment);
        map.put("FactBeginDate", wbsEntity.getFactbegindate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
        map.put("FactEndDate", wbsEntity.getFactenddate() == null ? "" : format.format(wbsEntity.getFactbegindate()));
        map.put("IsKey", wbsEntity.getIskey() == 1 ? "是" : "否");
        map.put("PartName", wbsEntity.getPartname());
        map.put("PhotoId", wbsEntity.getSetbacksupload() == null ? "" : wbsEntity.getSetbacksupload());
        map.put("PlanBeginDate", wbsEntity.getPlanbegindate() == null ? "" : format.format(wbsEntity.getPlanbegindate()));
        map.put("PlanEndDate", wbsEntity.getPlanenddate() == null ? "" : format.format(wbsEntity.getPlanenddate()));
        map.put("Remark", wbsEntity.getRemark());
        map.put("WbsFullPath", getFullName(wbsEntity));
        map.put("WbsId", wbsEntity.getSysid());
        Map<String, Object> zhiliang = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 1);
        Map<String, Object> anquan = projectWbsbindgrouprectificationService.getAnQuanOrZhiLiang(wbsEntity, 2);
        Map<String, Object> sourceRisk = projectWbsbindgroupsourceriskService.getSourceRisk(wbsId);
        if (sourceRisk != null) {
            sourceRisk.remove("WbsId");
            sourceRisk.put("wbsId", wbsId);
            sourceRisk.put("partName", wbsEntity.getPartname());
        }
        map.put("ZhiLiang", zhiliang);
        map.put("Sourcerisk", sourceRisk);
        map.put("AnQuan", anquan);
        return map;
    }
    //修改父类计划时间
    public void UpdateParentPlanBeginEnd(Date planBeginDate,Date planEndDate, ProjectWbsEntity parent) {
        if (planBeginDate != null)
        {
            if (planBeginDate.getTime() < parent.getPlanbegindate().getTime())
            {
                parent.setPlanbegindate(planBeginDate);
            }
        }
        if (planEndDate != null)
        {
            if (planEndDate.getTime() > parent.getPlanenddate().getTime())
            {
                parent.setPlanenddate(planEndDate);
            }
        }
        //子类计划开始结束改变时父类的计划开始结束时间取子类的最小值和最大值，父类开始结束随子类开始结束变化而变化
        List<ProjectWbsEntity> childs = this.list(new QueryWrapper<ProjectWbsEntity>().eq("parentId",parent.getSysid()).eq("del",0));
        for (ProjectWbsEntity item : childs)
        {
            if (item.getPlanbegindate() != null)
            {
                if (item.getPlanbegindate().getTime() < parent.getPlanbegindate().getTime())
                {
                    parent.setPlanbegindate(item.getPlanbegindate());
                }
            }
            if (item.getPlanenddate() != null)
            {
                if (item.getPlanenddate().getTime() > parent.getPlanenddate().getTime())
                {
                    parent.setPlanenddate(item.getPlanenddate());
                }
            }
        }
        this.updateById(parent);
    }

    //父类的计划开始结束时间随子类变化而变化
    public void EachUpdateWbs(String parentId,List<ProjectWbsEntity> list)
    {
        List<ProjectWbsEntity> wbsParentChilds = this.list(new QueryWrapper<ProjectWbsEntity>().eq("parentId",parentId).eq("del",0));
        ProjectWbsEntity wbsParent = this.getById(parentId);
        if (wbsParent != null)
        {
            for (ProjectWbsEntity item : wbsParentChilds)
            {
                if (item.getPlanbegindate().getTime() < wbsParent.getPlanbegindate().getTime())
                {
                    wbsParent.setPlanbegindate(item.getPlanbegindate());
                }
                if (item.getPlanenddate().getTime() > wbsParent.getPlanenddate().getTime())
                {
                    wbsParent.setPlanenddate(item.getPlanenddate());
                }
            }
            list.add(wbsParent);
            EachUpdateWbs(wbsParent.getParentid(),list);
        }
    }
    //检查是否为最大或最小级
    public boolean checkLast(List<ProjectWbsEntity> all,ProjectWbsEntity wbs){
        for(int i=0;i<all.size();i++){
            if(all.get(i).getParentid()!=null){
                if(wbs.getParentid()==null){
                    return true;
                }else{
                    if(all.get(i).getParentid().equals(wbs.getSysid())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //获取当前作业名称
    public String getNow(List<ProjectWbsEntity> all,ProjectWbsEntity wbs,String name){
        for(int i=0;i<all.size();i++){
            if(all.get(i).getSysid().equals(wbs.getParentid())){
                if(all.get(i).getParentid()!=null){
                    name=all.get(i).getPartname()+"-"+name;
                    name=getNow(all,all.get(i),name);
                }
            }
        }
        return name;
    }
    //修改父类实际时间
    public void UpdateParentFactBeginEnd(ProjectWbsEntity parent) {
        //Date factBeginDate,Date factEndDate,
//        if (factBeginDate != null)
//        {
//            if (factBeginDate.getTime() < parent.getPlanbegindate().getTime())
//            {
//                parent.setPlanbegindate(factBeginDate);
//            }
//        }
//        if (factEndDate != null)
//        {
//            if (factEndDate.getTime() > parent.getPlanenddate().getTime())
//            {
//                parent.setPlanenddate(factEndDate);
//            }
//        }
        //子类计划开始结束改变时父类的计划开始结束时间取子类的最小值和最大值，父类开始结束随子类开始结束变化而变化
        List<ProjectWbsEntity> childs = this.list(new QueryWrapper<ProjectWbsEntity>().eq("parentId",parent.getSysid()).eq("del",0));
        List<ProjectWbsEntity> collect = childs.stream().filter(l -> l.getFactenddate() != null).collect(Collectors.toList());
        for (ProjectWbsEntity item : childs)
        {
            if (item.getFactbegindate() != null)
            {
                if(parent.getFactbegindate()==null){
                    parent.setFactbegindate(item.getFactbegindate());
                } else if (item.getFactbegindate().getTime() < parent.getFactbegindate().getTime())
                {
                    parent.setFactbegindate(item.getFactbegindate());
                }
            }
            if(childs.size()==collect.size()){
                if (item.getFactenddate() != null)
                {
                    if(parent.getFactenddate()==null){
                        parent.setFactenddate(item.getFactenddate());
                    } else if (item.getFactenddate().getTime() > parent.getFactenddate().getTime())
                    {
                        parent.setFactenddate(item.getFactenddate());
                    }
                }
            }
        }
        this.updateById(parent);
    }
}