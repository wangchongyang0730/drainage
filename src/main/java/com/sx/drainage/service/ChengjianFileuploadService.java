package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.ChengjianFileuploadEntity;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 14:12:14
 */
public interface ChengjianFileuploadService extends IService<ChengjianFileuploadEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //获取图片信息
    List<Map<String, Object>> getImage(String projectId);
    //上传文件
    void addFile(String sysId, String userId, String entityId, String filename, String filePath, String newfilename, String suffixName, BigDecimal bigDecimal, String folderType, String contentType, Date date);
    //根据外键id查询文件集合
    List<Map<String, Object>> getFileList(String entityId);
    //根据外键id集合查询文件集合
    List<Map<String, Object>> getFileListByIds(String entityId);
    //根据文件主键获取文件信息
    Map<String, Object> getFile(String id);
    //根据全路径获取文件信息
    Map<String, Object> getFullPathFile(String idOrUrl);
    //删除文件
    void deleteFile(String id);
    //根据id或url找到文件
    ChengjianFileuploadEntity getFileByIdOrUrl(String idOrUrl);
    //根据外键id集合和文件名查询文件集合
    List<Map<String, Object>> getFileListByIdsAndName(List<String> entityId, String name, String projectName);
    //删除项目同时删除文件
    void deleteFiles(List<String> fileIdsList);
}

