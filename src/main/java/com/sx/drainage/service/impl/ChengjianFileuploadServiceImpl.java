package com.sx.drainage.service.impl;

import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.service.OmAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;

import com.sx.drainage.dao.ChengjianFileuploadDao;
import com.sx.drainage.entity.ChengjianFileuploadEntity;
import com.sx.drainage.service.ChengjianFileuploadService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("chengjianFileuploadService")
@Transactional
@RequiredArgsConstructor
public class ChengjianFileuploadServiceImpl extends ServiceImpl<ChengjianFileuploadDao, ChengjianFileuploadEntity> implements ChengjianFileuploadService {

    private final OmAccountService omAccountService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ChengjianFileuploadEntity> page = this.page(
                new Query<ChengjianFileuploadEntity>().getPage(params),
                new QueryWrapper<ChengjianFileuploadEntity>()
        );

        return new PageUtils(page);
    }
    //获取图片信息
    @Override
    public List<Map<String, Object>> getImage(String projectId) {
        List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().eq("entityId", projectId));
        List<Map<String, Object>> maps=new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("entityId",li.getEntityid());
            map.put("creatorId",li.getCreatorid());
            map.put("uploadType",li.getUploadtype());
            map.put("contentType",li.getContenttype());
            map.put("fileSize",li.getFilesize());
            map.put("name",li.getName());
            map.put("path",li.getPath());
            map.put("fullPath",li.getFullpath());
            map.put("webPath",li.getWebpath());
            map.put("uploadName",li.getUploadname());
            map.put("createdDate",li.getCreateddate());
            map.put("thumbWebPath",li.getThumbwebpath());
            maps.add(map);
        });
        return maps;
    }
    //上传文件
    @Override
    public void addFile(String sysId, String userId, String entityId,String filename, String filePath, String newfilename, String suffixName, BigDecimal bigDecimal,String folderType,String contentType,Date date) {
        ChengjianFileuploadEntity entity = new ChengjianFileuploadEntity();
        entity.setSysid(sysId);
        entity.setCreatorid(userId);
        entity.setEntityid(entityId);
        entity.setUploadtype(folderType);
        entity.setFilesize(bigDecimal);
        entity.setName(filename);
        entity.setPath(folderType+"/"+newfilename);
        entity.setFullpath(filePath+"/"+newfilename+suffixName);
        entity.setWebpath("/upload/"+folderType+"/"+newfilename+suffixName);
        entity.setThumbwebpath(null);
        entity.setUploadname(newfilename+suffixName);
        entity.setContenttype(contentType);
        entity.setCreateddate(date);
        this.save(entity);
    }
    //根据外键id查询文件集合
    @Override
    public List<Map<String, Object>> getFileList(String entityId) {
        List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().eq("entityId", entityId));
        List<Map<String, Object>> maps=new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("entityId",li.getEntityid());
            map.put("creatorId",li.getCreatorid());
            map.put("uploadType",li.getUploadtype());
            map.put("contentType",li.getContenttype());
            map.put("fileSize",li.getFilesize());
            map.put("name",li.getName());
            map.put("path",li.getPath());
            map.put("fullPath",li.getFullpath());
            map.put("webPath",li.getWebpath());
            map.put("uploadName",li.getUploadname());
            map.put("createDate",li.getCreateddate());
            map.put("thumbWebPath",li.getThumbwebpath());
            maps.add(map);
        });
        return maps;
    }
    //根据外键id集合查询文件集合
    @Override
    public List<Map<String, Object>> getFileListByIds(String entityId) {
        String[] split = entityId.split(",");
        List<Map<String, Object>> maps=new ArrayList<>();
        for (String s : split) {
            List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().eq("entityId", s));
            List<Map<String, Object>> entity=new ArrayList<>();
            list.forEach(li ->{
                Map<String, Object> map = new HashMap<>();
                map.put("sysId",li.getSysid());
                map.put("entityId",li.getEntityid());
                map.put("creatorId",li.getCreatorid());
                map.put("uploadType",li.getUploadtype());
                map.put("contentType",li.getContenttype());
                map.put("fileSize",li.getFilesize());
                map.put("name",li.getName());
                map.put("path",li.getPath());
                map.put("fullPath",li.getFullpath());
                map.put("webPath",li.getWebpath());
                map.put("uploadName",li.getUploadname());
                map.put("createDate",li.getCreateddate());
                map.put("thumbWebPath",li.getThumbwebpath());
                entity.add(map);
            });
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("entityId",s);
            hashMap.put("list",entity);
            maps.add(hashMap);
        }
        return maps;
    }
    //根据文件主键获取文件信息
    @Override
    public Map<String, Object> getFile(String id) {
        ChengjianFileuploadEntity li = this.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("entityId",li.getEntityid());
        map.put("creatorId",li.getCreatorid());
        map.put("uploadType",li.getUploadtype());
        map.put("contentType",li.getContenttype());
        map.put("fileSize",li.getFilesize());
        map.put("name",li.getName());
        map.put("path",li.getPath());
        map.put("fullPath",li.getFullpath());
        map.put("webPath",li.getWebpath());
        map.put("uploadName",li.getUploadname());
        map.put("createDate",li.getCreateddate());
        map.put("thumbWebPath",li.getThumbwebpath());
        return map;
    }
    //根据全路径获取文件信息
    @Override
    public Map<String, Object> getFullPathFile(String idOrUrl) {
        ChengjianFileuploadEntity li = this.getOne(new QueryWrapper<ChengjianFileuploadEntity>().eq("fullPath", idOrUrl));
        Map<String, Object> map = new HashMap<>();
        map.put("sysId",li.getSysid());
        map.put("entityId",li.getEntityid());
        map.put("creatorId",li.getCreatorid());
        map.put("uploadType",li.getUploadtype());
        map.put("contentType",li.getContenttype());
        map.put("fileSize",li.getFilesize());
        map.put("name",li.getName());
        map.put("path",li.getPath());
        map.put("fullPath",li.getFullpath());
        map.put("webPath",li.getWebpath());
        map.put("uploadName",li.getUploadname());
        map.put("createDate",li.getCreateddate());
        map.put("thumbWebPath",li.getThumbwebpath());
        return map;
    }
    //删除文件
    @Override
    public void deleteFile(String id) {
        //删除本地文件
        ChengjianFileuploadEntity entity = this.getById(id);
        try{
            File file = new File(entity.getFullpath());
            if(file.exists()){
                file.delete();
            }
        }catch (Exception e){
            log.error("无本地文件，删除记录");
        }finally {
            this.removeById(id);
        }
    }

    //根据id或url找到文件
    @Override
    public ChengjianFileuploadEntity getFileByIdOrUrl(String idOrUrl) {
        List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().eq("sysId", idOrUrl).or().eq("webPath", idOrUrl));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
    //根据外键id集合和文件名查询文件集合
    @Override
    public List<Map<String, Object>> getFileListByIdsAndName(List<String> entityId, String name, String projectName) {
        List<OmAccountEntity> userList = omAccountService.getAllUserList();
        if(StringUtils.isEmpty(name)){
            List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().in("entityId", entityId));
            List<Map<String, Object>> entity=new ArrayList<>();
            list.forEach(li ->{
                Map<String, Object> map = new HashMap<>();
                map.put("sysId",li.getSysid());
                map.put("entityId",li.getEntityid());
                List<OmAccountEntity> collect = userList.stream().filter(u -> u.getSysid().equals(li.getCreatorid())).collect(Collectors.toList());
                map.put("createUser",collect.size()!=0?collect.get(0).getName():null);
                map.put("uploadType",li.getUploadtype());
                map.put("contentType",li.getContenttype());
                map.put("fileSize",li.getFilesize());
                map.put("name",li.getName());
                map.put("path",li.getPath());
                map.put("fullPath",li.getFullpath());
                map.put("webPath",li.getWebpath());
                map.put("uploadName",li.getUploadname());
                map.put("createDate",li.getCreateddate());
                map.put("thumbWebPath",li.getThumbwebpath());
                map.put("projectName",projectName);
                entity.add(map);
            });
            return entity;
        }else{
            List<ChengjianFileuploadEntity> list = this.list(new QueryWrapper<ChengjianFileuploadEntity>().in("entityId", entityId).like("name",name));
            List<Map<String, Object>> entity=new ArrayList<>();
            list.forEach(li ->{
                Map<String, Object> map = new HashMap<>();
                map.put("sysId",li.getSysid());
                map.put("entityId",li.getEntityid());
                List<OmAccountEntity> collect = userList.stream().filter(u -> u.getSysid().equals(li.getCreatorid())).collect(Collectors.toList());
                map.put("createUser",collect.size()!=0?collect.get(0).getName():null);
                map.put("uploadType",li.getUploadtype());
                map.put("contentType",li.getContenttype());
                map.put("fileSize",li.getFilesize());
                map.put("name",li.getName());
                map.put("path",li.getPath());
                map.put("fullPath",li.getFullpath());
                map.put("webPath",li.getWebpath());
                map.put("uploadName",li.getUploadname());
                map.put("createDate",li.getCreateddate());
                map.put("thumbWebPath",li.getThumbwebpath());
                map.put("projectName",projectName);
                entity.add(map);
            });
            return entity;
        }
    }
    //删除文件
    @Override
    @Async
    public void deleteFiles(List<String> fileIdsList) {
        this.remove(new QueryWrapper<ChengjianFileuploadEntity>().in("entityId",fileIdsList));
    }
}