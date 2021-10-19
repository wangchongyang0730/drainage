package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.ProjectFileList;
import com.sx.drainage.dao.ProjectFileTypeDao;
import com.sx.drainage.entity.*;
import com.sx.drainage.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/19
 * Time: 14:13
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectFileTypeServiceImpl extends ServiceImpl<ProjectFileTypeDao, ProjectFileTypeEntity> implements ProjectFileTypeService {
    private final ProjectProjectService projectProjectService;
    private final ProjectInformationService projectInformationService;//父级项目资料
    private final ProjectProcessDocumentationService projectProcessDocumentationService;//流程文件
    private final ProjectPhaseService projectPhaseService;//项目阶段资料
    private final ProjectPhasedAcceptanceService projectPhasedAcceptanceService;//阶段性验收文件
    private final ChengjianFileuploadService chengjianFileuploadService;
    private final ProjectParentProjectService projectParentProjectService;
    private final ProjectStopProcessService  projectStopProcessService;
    /*
     * 添加文件类型
     * */
    @Override
    public void addFileType(ProjectFileTypeEntity entity) {
        entity.setSysId(CreateUuid.uuid());
        this.save(entity);
    }
    /*
     * 获取文件类型树
     * */
    @Override
    public List<ProjectFileTypeEntity> getFileTypeTree() {
        List<ProjectFileTypeEntity> list = this.list(new QueryWrapper<ProjectFileTypeEntity>().isNull("parentId").orderByAsc("sort"));
        List<ProjectFileTypeEntity> child = this.list(new QueryWrapper<ProjectFileTypeEntity>().isNotNull("parentId").orderByAsc("sort"));
        list.forEach(l -> {
            List<ProjectFileTypeEntity> data = new ArrayList<>();
            child.forEach(c -> {
                if(l.getSysId().equals(c.getParentId())){
                    data.add(c);
                }
            });
            l.setChild(data);
        });
        return list;
    }
    /*
     * 获取文件类型
     * */
    @Override
    public List<ProjectFileTypeEntity> getFileType() {
        return this.list(new QueryWrapper<ProjectFileTypeEntity>().isNotNull("parentId"));
    }
    /*
     * 获取父级文件类型
     * */
    @Override
    public List<ProjectFileTypeEntity> getParentFileType() {
        return this.list(new QueryWrapper<ProjectFileTypeEntity>().isNull("parentId"));
    }
    /*
    * 资料获取
    * */
    @Override
    public List<Map<String, Object>> getFiles(String sysId, String projectId, String fileName) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<ProjectProjectEntity> allProject = projectProjectService.getAllProject();
        List<ProjectFileList> list = new ArrayList<>();
        if(StringUtils.isEmpty(sysId)){
            getAll(projectId,list);
            List<String> projectIdList = new ArrayList<>();
            list.forEach(l -> {
                projectIdList.add(l.getProjectId());
            });
            List<String> projectCollect = projectIdList.stream().distinct().collect(Collectors.toList());
            projectCollect.forEach(p -> {
                if(!StringUtils.isEmpty(p)){
                    log.error("projectId:"+p);
                    List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                    List<String> fileId = new ArrayList<>();
                    list.forEach(l -> {
                        if(l.getProjectId().equals(p)){
                            fileId.add(l.getFileId());
                        }
                    });
                        //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                        if(fileId.size()>0){
                            //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                            if(fileId.size()>2000){
                                int a=fileId.size()/2000+1;
                                for(int i=0;i<a;i++){
                                    if(collect.size()!=0){
                                        data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                    }else{
                                        ProjectParentProjectEntity entity = projectParentProjectService.getById(p);
                                        if(entity!=null){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entity.getName()));
                                        }else{
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                        }
                                    }
                                }
                            }else{
                                if(collect.size()!=0){
                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                }else{
                                    ProjectParentProjectEntity entity = projectParentProjectService.getById(p);
                                    if(entity!=null){
                                        data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entity.getName()));
                                    }else{
                                        data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                    }
                                }
                            }
                        }
                }
            });
            return data;
        }else{
            ProjectFileTypeEntity entity = this.getById(sysId);
            List<String> projectIdList = new ArrayList<>();
            if(entity!=null){
                switch (entity.getTypeName()){
                    case "工程阶段文件":
                        projectImplementation(projectId,list);
                        constructionPreparation(projectId,list);
                        defectLiabilityPeriod(projectId,list);
                        completionAcceptance(projectId,list);
                        earlyStageOfTheProject(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "工程实施":
                        projectImplementation(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect1 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect1.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "施工准备":
                        constructionPreparation(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect2 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect2.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "缺陷责任期":
                        defectLiabilityPeriod(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect3 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect3.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "竣工验收":
                        completionAcceptance(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect4 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect4.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "工程前期":
                        earlyStageOfTheProject(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect5 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect5.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "管理制度文件":
                        supervisionManagement(projectId,list);
                        safetyManagement(projectId,list);
                        constructionPreparation(projectId,list);
                        earlyStageOfTheProject(projectId,list);
                        civilizationConstruction(projectId,list);
                        acceptance(projectId,list);
                        qualityControl(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect6 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect6.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "监理管理文件":
                        supervisionManagement(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect7 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect7.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "安全管理文件":
                        safetyManagement(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect8 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect8.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "开工文件":
                        constructionPreparation(projectId,list);
                        earlyStageOfTheProject(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect9 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect9.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "文明施工文件":
                        civilizationConstruction(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect10 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect10.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "验收文件":
                        acceptance(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect11 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect11.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "质量管理文件":
                        qualityControl(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect12 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect12.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "部门文件":
                        otherDepartment(projectId,list);
                        supervision(projectId,list);
                        pipeJackingDepartment(projectId,list);
                        construction(projectId,list);
                        engineeringDepartment(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect13 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect13.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "其他部门文件":
                        otherDepartment(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect14 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect14.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "监理文件":
                        supervision(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect15 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect15.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "项管部文件":
                        pipeJackingDepartment(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect16 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect16.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "施工文件":
                        construction(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect17 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect17.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                    case "工程部文件":
                        engineeringDepartment(projectId,list);
                        list.forEach(l -> {
                            projectIdList.add(l.getProjectId());
                        });
                        List<String> projectCollect18 = projectIdList.stream().distinct().collect(Collectors.toList());
                        projectCollect18.forEach(p -> {
                            if(!StringUtils.isEmpty(p)){
                                List<ProjectProjectEntity> collect = allProject.stream().filter(a -> a.getSysid().equals(p)).collect(Collectors.toList());
                                List<String> fileId = new ArrayList<>();
                                list.forEach(l -> {
                                    if(l.getProjectId().equals(p)){
                                        fileId.add(l.getFileId());
                                    }
                                });
                                if(fileId.size()>0){
                                    //.filter(l ->  (!l.equals("")&&!l.equals("''")&&l!=null))
                                    if(fileId.size()>2000){
                                        int a=fileId.size()/2000+1;
                                        for(int i=0;i<a;i++){
                                            if(collect.size()!=0){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,collect.get(0).getName()));
                                            }else{
                                                ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                                if(entitys!=null){
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,entitys.getName()));
                                                }else{
                                                    data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId.subList(i*2000,(i+1)*2000),fileName,null));
                                                }
                                            }
                                        }
                                    }else{
                                        if(collect.size()!=0){
                                            data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,collect.get(0).getName()));
                                        }else{
                                            ProjectParentProjectEntity entitys = projectParentProjectService.getById(p);
                                            if(entitys!=null){
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,entitys.getName()));
                                            }else{
                                                data.addAll(chengjianFileuploadService.getFileListByIdsAndName(fileId,fileName,null));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        return data;
                }
            }
            return data;
        }
    }
    /*
    *工程阶段文件
    * */
    //工程实施
    private void projectImplementation(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectPhaseEntity> data = projectPhaseService.getAll();
            data.forEach(d -> {
                if(!StringUtils.isEmpty(d.getSpecialSubmission())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getSpecialSubmission()));
                }
                if(!StringUtils.isEmpty(d.getAcceptanceReport())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getAcceptanceReport()));
                }
                if(!StringUtils.isEmpty(d.getMaintenanceReport())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getMaintenanceReport()));
                }
                if(!StringUtils.isEmpty(d.getCreateAplan())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getCreateAplan()));
                }
                if(!StringUtils.isEmpty(d.getFloodControlDocument())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFloodControlDocument()));
                }
                if(!StringUtils.isEmpty(d.getTrafficOrganizationPlan())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getTrafficOrganizationPlan()));
                }
            });
            //TODO:业务需求变更，暂停部分流程
            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("监理细则");
            List<ProjectProcessDocumentationEntity> files = projectProcessDocumentationService.getFileByProcessName("施工专项方案");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(files!=null&&files.size()>0){
                files.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            //停用流程
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessFile("施工专项方案");
            List<ProjectStopProcessEntity> files = projectStopProcessService.getStopProcessFile("监理细则");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(files!=null&&files.size()>0){
                files.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId2()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId3()));
                });
            }
        }else{
            ProjectPhaseEntity entity = projectPhaseService.getByProjectId(projectId);
            if(!StringUtils.isEmpty(entity.getSpecialSubmission())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getSpecialSubmission()));
            }
            if(!StringUtils.isEmpty(entity.getAcceptanceReport())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getAcceptanceReport()));
            }
            if(!StringUtils.isEmpty(entity.getMaintenanceReport())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getMaintenanceReport()));
            }
            if(!StringUtils.isEmpty(entity.getCreateAplan())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getCreateAplan()));
            }
            if(!StringUtils.isEmpty(entity.getFloodControlDocument())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getFloodControlDocument()));
            }
            if(!StringUtils.isEmpty(entity.getTrafficOrganizationPlan())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getTrafficOrganizationPlan()));
            }

            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理细则",projectId);
            List<ProjectProcessDocumentationEntity> files = projectProcessDocumentationService.getFileByProcessNameAndProjectId("施工专项方案",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(files!=null&&files.size()>0){
                files.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            //停用流程
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessArchive("施工专项方案",projectId);
            List<ProjectStopProcessEntity> files = projectStopProcessService.getStopProcessArchive("监理细则",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(files!=null&&files.size()>0){
                files.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId2()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId3()));
                });
            }
        }
    }
    //施工准备
    private void constructionPreparation(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectPhaseEntity> data = projectPhaseService.getAll();
            data.forEach(d -> {
                if(!StringUtils.isEmpty(d.getScenarioConstruction())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getScenarioConstruction()));
                }
                if(!StringUtils.isEmpty(d.getStartWork())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getStartWork()));
                }
            });

            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("总体施工方案");
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessName("监理规划");
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessName("大临设施验收");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessFile("施工组织总体设计");
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessFile("监理规划");
            List<ProjectStopProcessEntity> file2 = projectStopProcessService.getStopProcessFile("大临验收");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getProblemFileId()));
                });
            }
        }else{
            ProjectPhaseEntity entity = projectPhaseService.getByProjectId(projectId);
            if(!StringUtils.isEmpty(entity.getScenarioConstruction())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getScenarioConstruction()));
            }
            if(!StringUtils.isEmpty(entity.getStartWork())){
                fileList.add(new ProjectFileList(entity.getProjectId(),entity.getStartWork()));
            }
            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("总体施工方案",projectId);
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理规划",projectId);
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("大临设施验收",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessArchive("施工组织总体设计",projectId);
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessArchive("监理规划",projectId);
            List<ProjectStopProcessEntity> file2 = projectStopProcessService.getStopProcessArchive("大临验收",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getProblemFileId()));
                });
            }
        }
    }
    //缺陷责任期
    private void defectLiabilityPeriod(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("缺陷责任期");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("缺陷责任期", projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }
    }
    //竣工验收
    private void completionAcceptance(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("竣工验收");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("竣工验收", projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }
    }
    //工程前期
    private void earlyStageOfTheProject(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectInformationEntity> data = projectInformationService.geAll();
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    if(!StringUtils.isEmpty(d.getProposals())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getProposals()));
                    }
                    if(!StringUtils.isEmpty(d.getFeasibilityReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getFeasibilityReport()));
                    }
                    if(!StringUtils.isEmpty(d.getDesignReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getDesignReport()));
                    }
                    if(!StringUtils.isEmpty(d.getSurveyReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getSurveyReport()));
                    }
                    if(!StringUtils.isEmpty(d.getBiddingInformation())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getBiddingInformation()));
                    }
                    if(!StringUtils.isEmpty(d.getProjectReviewAndApproval())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getProjectReviewAndApproval()));
                    }
                    if(!StringUtils.isEmpty(d.getFeasibilityStudyReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getFeasibilityStudyReport()));
                    }
                    if(!StringUtils.isEmpty(d.getInvestmentPlan())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getInvestmentPlan()));
                    }
                    if(!StringUtils.isEmpty(d.getManagementManual())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getManagementManual()));
                    }
                    if(!StringUtils.isEmpty(d.getProjectManagementManual())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getProjectManagementManual()));
                    }
                    if(!StringUtils.isEmpty(d.getProjectContract())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getProjectContract()));
                    }
                    if(!StringUtils.isEmpty(d.getDesignText())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getDesignText()));
                    }
                    if(!StringUtils.isEmpty(d.getPlanningLicense())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getPlanningLicense()));
                    }
                    if(!StringUtils.isEmpty(d.getOtherFiles())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getOtherFiles()));
                    }
                });
            }
            List<ProjectPhaseEntity> all = projectPhaseService.getAll();
            if(all!=null&&all.size()>0){
                all.forEach(a -> {
                    if(!StringUtils.isEmpty(a.getConstructionBidding())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getConstructionBidding()));
                    }
                    if(!StringUtils.isEmpty(a.getConstructionPermit())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getConstructionPermit()));
                    }
                    if(!StringUtils.isEmpty(a.getStartWork())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getStartWork()));
                    }
                    if(!StringUtils.isEmpty(a.getSupervisionBidding())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getSupervisionBidding()));
                    }
                    if(!StringUtils.isEmpty(a.getMonitorBidding())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getMonitorBidding()));
                    }
                    if(!StringUtils.isEmpty(a.getSupervision())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getSupervision()));
                    }
                    if(!StringUtils.isEmpty(a.getPlanningPermit())){
                        fileList.add(new ProjectFileList(a.getProjectId(),a.getPlanningPermit()));
                    }
                });
            }
        }else {
            ProjectPhaseEntity entity = projectPhaseService.getByProjectId(projectId);
            if(entity!=null){
                if(!StringUtils.isEmpty(entity.getConstructionBidding())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getConstructionBidding()));
                }
                if(!StringUtils.isEmpty(entity.getConstructionPermit())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getConstructionPermit()));
                }
                if(!StringUtils.isEmpty(entity.getStartWork())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getStartWork()));
                }
                if(!StringUtils.isEmpty(entity.getSupervisionBidding())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getSupervisionBidding()));
                }
                if(!StringUtils.isEmpty(entity.getMonitorBidding())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getMonitorBidding()));
                }
                if(!StringUtils.isEmpty(entity.getSupervision())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getSupervision()));
                }
                if(!StringUtils.isEmpty(entity.getPlanningPermit())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getPlanningPermit()));
                }
            }
            ProjectProjectEntity project = projectProjectService.getProject(projectId);
            if(project!=null) {
                ProjectInformationEntity entity1 = projectInformationService.getByProjectId(project.getParentid());
                if(entity1!=null){
                    if(!StringUtils.isEmpty(entity1.getProposals())){
                        fileList.add(new ProjectFileList(entity1.getProjectId(),entity1.getProposals()));
                    }
                    if(!StringUtils.isEmpty(entity1.getFeasibilityReport())){
                        fileList.add(new ProjectFileList(entity1.getProjectId(),entity1.getFeasibilityReport()));
                    }
                    if(!StringUtils.isEmpty(entity1.getDesignReport())){
                        fileList.add(new ProjectFileList(entity1.getProjectId(),entity1.getDesignReport()));
                    }
                    if(!StringUtils.isEmpty(entity1.getSurveyReport())){
                        fileList.add(new ProjectFileList(entity1.getProjectId(),entity1.getSurveyReport()));
                    }
                    if(!StringUtils.isEmpty(entity1.getBiddingInformation())){
                        fileList.add(new ProjectFileList(entity1.getProjectId(),entity1.getBiddingInformation()));
                    }
                }
            }
        }
    }
    /*
    * 管理制度文件
    * supervisionManagement(projectId,list);
    * safetyManagement(projectId,list);
    * constructionPreparation(projectId,list);
    * earlyStageOfTheProject(projectId,list);
    * civilizationConstruction(projectId,list);
    * acceptance(projectId,list);
    * qualityControl(projectId,list);
    * */
    //监理管理文件
    private void supervisionManagement(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("监理规划");
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessName("监理细则");*/
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessName("监理总监总代管理");
            List<ProjectProcessDocumentationEntity> file3 = projectProcessDocumentationService.getFileByProcessName("监理整改");
            List<ProjectProcessDocumentationEntity> file4 = projectProcessDocumentationService.getFileByProcessName("监理(安全)月报");
           /* if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file3!=null&&file3.size()>0){
                file3.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file4!=null&&file4.size()>0){
                file4.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            //停用流程
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessFile("监理规划");
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessFile("监理细则");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId2()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId3()));
                });
            }
        }else{
            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理规划",projectId);
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理细则",projectId);*/
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理总监总代管理",projectId);
            List<ProjectProcessDocumentationEntity> file3 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理整改",projectId);
            List<ProjectProcessDocumentationEntity> file4 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("监理(安全)月报",projectId);
            /*if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file3!=null&&file3.size()>0){
                file3.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file4!=null&&file4.size()>0){
                file4.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            //停用流程
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessArchive("监理规划",projectId);
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessArchive("监理细则",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId2()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId3()));
                });
            }
        }
    }
    //安全管理文件
    private void safetyManagement(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("安全整改");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("安全整改",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }
    }
    //开工文件=施工准备+工程前期
    //文明施工文件
    private void civilizationConstruction(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectPhaseEntity> data = projectPhaseService.getAll();
            data.forEach(d -> {
                if(!StringUtils.isEmpty(d.getSpecialSubmission())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getSpecialSubmission()));
                }
                if(!StringUtils.isEmpty(d.getAcceptanceReport())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getAcceptanceReport()));
                }
                if(!StringUtils.isEmpty(d.getMaintenanceReport())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getMaintenanceReport()));
                }
                if(!StringUtils.isEmpty(d.getCreateAplan())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getCreateAplan()));
                }
                if(!StringUtils.isEmpty(d.getFloodControlDocument())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFloodControlDocument()));
                }
                if(!StringUtils.isEmpty(d.getTrafficOrganizationPlan())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getTrafficOrganizationPlan()));
                }
                if(!StringUtils.isEmpty(d.getSpecialPrograms())){
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getSpecialPrograms()));
                }
            });
        }else{
            ProjectPhaseEntity entity = projectPhaseService.getByProjectId(projectId);
            if(entity!=null){
                if(!StringUtils.isEmpty(entity.getSpecialSubmission())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getSpecialPrograms()));
                }
                if(!StringUtils.isEmpty(entity.getAcceptanceReport())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getAcceptanceReport()));
                }
                if(!StringUtils.isEmpty(entity.getMaintenanceReport())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getMaintenanceReport()));
                }
                if(!StringUtils.isEmpty(entity.getCreateAplan())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getCreateAplan()));
                }
                if(!StringUtils.isEmpty(entity.getFloodControlDocument())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getFloodControlDocument()));
                }
                if(!StringUtils.isEmpty(entity.getTrafficOrganizationPlan())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getTrafficOrganizationPlan()));
                }
                if(!StringUtils.isEmpty(entity.getSpecialPrograms())){
                    fileList.add(new ProjectFileList(entity.getProjectId(),entity.getSpecialPrograms()));
                }
            }
        }
    }
    //验收文件
    private void acceptance(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectPhasedAcceptanceEntity> data = projectPhasedAcceptanceService.getAllFile();
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    if(!StringUtils.isEmpty(d.getMeetingMinutes())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getMeetingMinutes()));
                    }
                    if(!StringUtils.isEmpty(d.getPhasedAcceptanceReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getPhasedAcceptanceReport()));
                    }
                    if(!StringUtils.isEmpty(d.getReportMaterial())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getReportMaterial()));
                    }
                });
            }
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("竣工验收");
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessName("完工验收");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }else{
            List<ProjectPhasedAcceptanceEntity> all = projectPhasedAcceptanceService.getAll(projectId);
            if(all!=null&&all.size()>0){
                all.forEach(d -> {
                    if(!StringUtils.isEmpty(d.getMeetingMinutes())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getMeetingMinutes()));
                    }
                    if(!StringUtils.isEmpty(d.getPhasedAcceptanceReport())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getPhasedAcceptanceReport()));
                    }
                    if(!StringUtils.isEmpty(d.getReportMaterial())){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getReportMaterial()));
                    }
                });
            }
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("竣工验收",projectId);
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("完工验收",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
        }
    }
    //质量管理文件
    private void qualityControl(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            /*List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessName("总体施工方案");
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessName("施工专项方案");*/
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessName("质量事故处理");
            List<ProjectProcessDocumentationEntity> file3 = projectProcessDocumentationService.getFileByProcessName("质量事故报告");
            List<ProjectProcessDocumentationEntity> file4 = projectProcessDocumentationService.getFileByProcessName("质量整改");
            List<ProjectProcessDocumentationEntity> file5 = projectProcessDocumentationService.getFileByProcessName("缺陷责任期");
            /*if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file3!=null&&file3.size()>0){
                file3.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file4!=null&&file4.size()>0){
                file4.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file5!=null&&file5.size()>0){
                file5.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            //停用流程
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessFile("施工组织总体设计");
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessFile("施工专项方案");
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
        }else{
           /* List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getFileByProcessNameAndProjectId("总体施工方案",projectId);
            List<ProjectProcessDocumentationEntity> file1 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("施工专项方案",projectId);*/
            List<ProjectProcessDocumentationEntity> file2 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("质量事故处理",projectId);
            List<ProjectProcessDocumentationEntity> file3 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("质量事故报告",projectId);
            List<ProjectProcessDocumentationEntity> file4 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("质量整改",projectId);
            List<ProjectProcessDocumentationEntity> file5 = projectProcessDocumentationService.getFileByProcessNameAndProjectId("缺陷责任期",projectId);
            /*if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }*/
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file3!=null&&file3.size()>0){
                file3.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file4!=null&&file4.size()>0){
                file4.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file5!=null&&file5.size()>0){
                file5.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            List<ProjectStopProcessEntity> file = projectStopProcessService.getStopProcessArchive("施工组织总体设计",projectId);
            List<ProjectStopProcessEntity> file1 = projectStopProcessService.getStopProcessArchive("施工专项方案",projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
            if(file1!=null&&file1.size()>0){
                file1.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getOpinionFileId()));
                });
            }
        }
    }
    /*
    * 部门文件
    * otherDepartment(projectId,list);
    * supervision(projectId,list);
    * pipeJackingDepartment(projectId,list);
    * construction(projectId,list);
    * engineeringDepartment(projectId,list);
    * */
    //其他部门文件
    private void otherDepartment(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getOtherDepartmentFile();
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getOtherDepartmentFileByProjectId(projectId);
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }
    }
    //监理文件
    private void supervision(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByProcessNameLike("监理");
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    if(d.getTaskId()==null){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                    }else{
                        if(d.getTaskName().contains("监理")){
                            fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                        }
                    }
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByProcessNameLikeAndProjectId("监理",projectId);
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    if(d.getTaskId()==null){
                        fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                    }else{
                        if(d.getTaskName().contains("监理")){
                            fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                        }
                    }
                });
            }
        }
    }
    //项管部文件
    private void pipeJackingDepartment(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLike("项管部");
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLikeAndProjectId("项管部",projectId);
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }
    }
    //施工文件
    private void construction(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLike("施工");
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLikeAndProjectId("施工",projectId);
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }
    }
    //工程部文件
    private void engineeringDepartment(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLike("工程部");
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> data = projectProcessDocumentationService.getFileByTaskNameLikeAndProjectId("工程部",projectId);
            if(data!=null&&data.size()>0){
                data.forEach(d -> {
                    fileList.add(new ProjectFileList(d.getProjectId(),d.getFileId()));
                });
            }
        }
    }
    /*
    * 全部文件
    * */
    private void getAll(String projectId,List<ProjectFileList> fileList){
        if(StringUtils.isEmpty(projectId)){
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getAllFile();
            List<ProjectPhasedAcceptanceEntity> file2 = projectPhasedAcceptanceService.getAllFile();
            List<ProjectPhaseEntity> file3 = projectPhaseService.getAll();
            List<ProjectInformationEntity> file4 = projectInformationService.geAll();
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getReportMaterial())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getReportMaterial()));
                    }
                });
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getPhasedAcceptanceReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getPhasedAcceptanceReport()));
                    }
                });
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getMeetingMinutes())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getMeetingMinutes()));
                    }
                });
            }
            if(file3!=null&&file3.size()>0){
                file3.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getConstructionBidding())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getConstructionBidding()));
                    }
                    if(!StringUtils.isEmpty(f.getConstructionPermit())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getConstructionPermit()));
                    }
                    if(!StringUtils.isEmpty(f.getConstructionPermit())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getConstructionPermit()));
                    }
                    if(!StringUtils.isEmpty(f.getSupervision())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getSupervision()));
                    }
                    if(!StringUtils.isEmpty(f.getScenarioConstruction())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getScenarioConstruction()));
                    }
                    if(!StringUtils.isEmpty(f.getStartWork())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getStartWork()));
                    }
                    if(!StringUtils.isEmpty(f.getSupervisionBidding())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getSupervisionBidding()));
                    }
                    if(!StringUtils.isEmpty(f.getMonitorBidding())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getMonitorBidding()));
                    }
                    if(!StringUtils.isEmpty(f.getSpecialSubmission())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getSpecialSubmission()));
                    }
                    if(!StringUtils.isEmpty(f.getAcceptanceReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getAcceptanceReport()));
                    }
                    if(!StringUtils.isEmpty(f.getMaintenanceReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getMaintenanceReport()));
                    }
                    if(!StringUtils.isEmpty(f.getCreateAplan())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getCreateAplan()));
                    }
                    if(!StringUtils.isEmpty(f.getFloodControlDocument())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getFloodControlDocument()));
                    }
                    if(!StringUtils.isEmpty(f.getTrafficOrganizationPlan())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getTrafficOrganizationPlan()));
                    }
                    if(!StringUtils.isEmpty(f.getWorkTicket())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getWorkTicket()));
                    }
                    if(!StringUtils.isEmpty(f.getPlanningPermit())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getPlanningPermit()));
                    }
                });
            }
            if(file4!=null&&file4.size()>0){
                file4.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getBiddingInformation())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getBiddingInformation()));
                    }
                    if(!StringUtils.isEmpty(f.getProposals())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getProposals()));
                    }
                    if(!StringUtils.isEmpty(f.getFeasibilityReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getFeasibilityReport()));
                    }
                    if(!StringUtils.isEmpty(f.getDesignReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getDesignReport()));
                    }
                    if(!StringUtils.isEmpty(f.getSurveyReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getSurveyReport()));
                    }
                });
            }
        }else{
            List<ProjectProcessDocumentationEntity> file = projectProcessDocumentationService.getAllFileByProjectId(projectId);
            List<ProjectPhasedAcceptanceEntity> file2 = projectPhasedAcceptanceService.getAll(projectId);
            ProjectProjectEntity project = projectProjectService.getProject(projectId);
            if(project!=null){
                ProjectInformationEntity file3 = projectInformationService.getByProjectId(project.getParentid());
                if(file3!=null){
                        if(!StringUtils.isEmpty(file3.getBiddingInformation())){
                            fileList.add(new ProjectFileList(file3.getProjectId(),file3.getBiddingInformation()));
                        }
                        if(!StringUtils.isEmpty(file3.getProposals())){
                            fileList.add(new ProjectFileList(file3.getProjectId(),file3.getProposals()));
                        }
                        if(!StringUtils.isEmpty(file3.getFeasibilityReport())){
                            fileList.add(new ProjectFileList(file3.getProjectId(),file3.getFeasibilityReport()));
                        }
                        if(!StringUtils.isEmpty(file3.getDesignReport())){
                            fileList.add(new ProjectFileList(file3.getProjectId(),file3.getDesignReport()));
                        }
                        if(!StringUtils.isEmpty(file3.getSurveyReport())){
                            fileList.add(new ProjectFileList(file3.getProjectId(),file3.getSurveyReport()));
                        }
                }
            }
            ProjectPhaseEntity file4 = projectPhaseService.getByProjectId(projectId);
            if(file!=null&&file.size()>0){
                file.forEach(f -> {
                    fileList.add(new ProjectFileList(f.getProjectId(),f.getFileId()));
                });
            }
            if(file2!=null&&file2.size()>0){
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getReportMaterial())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getReportMaterial()));
                    }
                });
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getPhasedAcceptanceReport())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getPhasedAcceptanceReport()));
                    }
                });
                file2.forEach(f -> {
                    if(!StringUtils.isEmpty(f.getMeetingMinutes())){
                        fileList.add(new ProjectFileList(f.getProjectId(),f.getMeetingMinutes()));
                    }
                });
            }
            if(file4!=null){
                if(!StringUtils.isEmpty(file4.getConstructionBidding())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getConstructionBidding()));
                }
                if(!StringUtils.isEmpty(file4.getConstructionPermit())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getConstructionPermit()));
                }
                if(!StringUtils.isEmpty(file4.getConstructionPermit())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getConstructionPermit()));
                }
                if(!StringUtils.isEmpty(file4.getSupervision())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getSupervision()));
                }
                if(!StringUtils.isEmpty(file4.getScenarioConstruction())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getScenarioConstruction()));
                }
                if(!StringUtils.isEmpty(file4.getStartWork())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getStartWork()));
                }
                if(!StringUtils.isEmpty(file4.getSupervisionBidding())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getSupervisionBidding()));
                }
                if(!StringUtils.isEmpty(file4.getMonitorBidding())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getMonitorBidding()));
                }
                if(!StringUtils.isEmpty(file4.getSpecialSubmission())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getSpecialSubmission()));
                }
                if(!StringUtils.isEmpty(file4.getAcceptanceReport())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getAcceptanceReport()));
                }
                if(!StringUtils.isEmpty(file4.getMaintenanceReport())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getMaintenanceReport()));
                }
                if(!StringUtils.isEmpty(file4.getCreateAplan())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getCreateAplan()));
                }
                if(!StringUtils.isEmpty(file4.getFloodControlDocument())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getFloodControlDocument()));
                }
                if(!StringUtils.isEmpty(file4.getTrafficOrganizationPlan())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getTrafficOrganizationPlan()));
                }
                if(!StringUtils.isEmpty(file4.getWorkTicket())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getWorkTicket()));
                }
                if(!StringUtils.isEmpty(file4.getPlanningPermit())){
                    fileList.add(new ProjectFileList(file4.getProjectId(),file4.getPlanningPermit()));
                }
            }
        }
    }
}

