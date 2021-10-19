package com.sx.drainage.controller;

import com.itextpdf.text.DocumentException;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.ExeclToPdf;
import com.sx.drainage.common.R;
import com.sx.drainage.common.WordToPdf;
import com.sx.drainage.entity.ChengjianFileuploadEntity;
import com.sx.drainage.service.ChengjianFileuploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/31
 * Time: 13:59
 */
@Slf4j
@Api(value = "/api/FileManage",description = "上传文件")
@CrossOrigin
@RestController
@RequestMapping("/api/FileManage")
public class FileManageController {
    @Autowired
    private ChengjianFileuploadService chengjianFileuploadService;
    @Autowired
    private Environment env;
    @Value("${file.imageUrl}")
    private String imageUrl;
    @Value("${file.pdfUrl}")
    private String pdfUrl;

    @PostMapping("/UploadFile")
    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "entityId",value = "文件夹外键id",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "folderType",value = "文件夹（默认product）",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "form",name = "fileData1",value = "文件1",required = false,dataType = "file"),
            @ApiImplicitParam(paramType = "form",name = "fileData2",value = "文件2",required = false,dataType = "file"),
            @ApiImplicitParam(paramType = "form",name = "fileDatan",value = "文件n",required = false,dataType = "file"),
    })
    public R uploadFile(@RequestParam("entityId") String entityId, @RequestParam(value = "folderType",required = false,defaultValue = "product") String folderType,
                        @RequestParam(value = "fileData1",required = false) MultipartFile fileData1,
                        @RequestParam(value = "fileData2",required = false) MultipartFile fileData2,
                        @RequestParam(value = "fileDatan",required = false) MultipartFile fileDatan,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        List<MultipartFile> fileData = new ArrayList<>();
        if(fileData1!=null){
            fileData.add(fileData1);
        }
        if(fileData2!=null){
            fileData.add(fileData2);
        }
        if(fileDatan!=null){
            fileData.add(fileDatan);
        }
        if(fileData.size()==0){
            return R.error(200,"请选择文件!");
        }
        String filePath=env.getProperty("file.upload")+folderType;
        //判断文件夹是否存在，不存在就创建
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
            log.info("创建文件夹"+filePath);
        }
        log.info("开始上传！！！！");
        List<Map<String,Object>> list = new ArrayList<>();
        log.info("建立集合！！！！");
        for (MultipartFile li : fileData) {
            log.info("循环开始！！！！");
            String filename = li.getOriginalFilename();
            log.info("取到文件名！！！！"+filename);
            String suffixName = filename.substring(filename.lastIndexOf("."));
            log.info("取到文件后缀名！！！！"+suffixName);
            String newfilename=CreateUuid.uuid();
            log.info("创建新文件名！！！！"+newfilename);
            File upload = new File(filePath + "/"+ newfilename+suffixName);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
            try {
                log.info("上传中"+filename);
                li.transferTo(upload);
                Date date = new Date();
                chengjianFileuploadService.addFile(newfilename,userId,entityId,filename,filePath,newfilename,suffixName,new BigDecimal(li.getSize()),folderType,li.getContentType(),date);
                Map<String, Object> map = new HashMap<>();
                map.put("webPath","/upload/"+folderType+"/"+newfilename+suffixName);
                map.put("thumbWebPath",null);
                map.put("sysId",newfilename);
                map.put("entityId",entityId);
                map.put("creatorId",userId);
                map.put("uploaType",folderType);
                map.put("contentType",li.getContentType());
                map.put("fileSize",li.getSize());
                map.put("name",filename);
                map.put("path",folderType+"/"+newfilename);
                map.put("fullPath",filePath+"/"+newfilename+suffixName);
                map.put("uploadName",newfilename+suffixName);
                map.put("createDate",format.format(date));
                list.add(map);
            } catch (IOException e) {
                log.error("上传失败!");
                e.printStackTrace();
            }
        }
        return R.ok(1,"上传成功!",list,true,null);
    }
    @GetMapping("/GetFileList")
    @ApiOperation(value = "根据外键id查询文件集合")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "entityId",value = "文件夹外键id",required = true,dataType = "String")
    })
    public R getFileList(@RequestParam("entityId") String entityId){
        List<Map<String,Object>> list=chengjianFileuploadService.getFileList(entityId);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetFileListByIds")
    @ApiOperation(value = "根据外键id集合查询文件集合")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "entityIds",value = "文件夹外键id",required = true,dataType = "String")
    })
    public R getFileListByIds(@RequestParam("entityIds") String entityIds){
        List<Map<String,Object>> list=chengjianFileuploadService.getFileListByIds(entityIds);
        return R.ok(1,"获取成功!",list,true,null);
    }
    @GetMapping("/GetFile/{id}")
    @ApiOperation(value = "根据文件主键获取文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "id",value = "文件主键sysId",required = true,dataType = "String")
    })
    public R getFile(@PathVariable("id") String id){
        Map<String,Object> map=chengjianFileuploadService.getFile(id);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/GetFullPathFile")
    @ApiOperation(value = "根据全路径获取文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "idOrUrl",value = "",required = true,dataType = "String")
    })
    public R getFullPathFile(@PathVariable("idOrUrl") String idOrUrl){
        Map<String,Object> map=chengjianFileuploadService.getFullPathFile(idOrUrl);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @DeleteMapping("/DeleteFile/{id}")
    @ApiOperation(value = "根据文件主键删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "id",value = "",required = true,dataType = "String")
    })
    public R deleteFile(@PathVariable("id") String id){
        chengjianFileuploadService.deleteFile(id);
        return R.ok(1,"删除成功!",null,true,null);
    }
    @GetMapping("/OfficeToPdf")
    @ApiOperation(value = "Office转换pdf并返回pdf文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "idOrUrl",value = "",required = true,dataType = "String")
    })
    public R officeToPdf(@RequestParam("idOrUrl") String idOrUrl, HttpServletResponse response) throws IOException, DocumentException {
        ChengjianFileuploadEntity entity=chengjianFileuploadService.getFileByIdOrUrl(idOrUrl);
        if(entity.getFullpath()!=null&&entity!=null){
            String suffixName = entity.getFullpath().substring(entity.getFullpath().lastIndexOf("."));
            log.error("文件格式:"+suffixName);
            String name = CreateUuid.uuid();
            response.setContentType("application/octet-stream");
            //这后面可以设置导出Excel的名称，此例中名为statistic.xls
            response.setHeader("Content-Disposition", "attachment;filename=" +name+ ".pdf");
            //刷新缓冲
            response.flushBuffer();
            ServletOutputStream outputStream = response.getOutputStream();
            try {
                switch (suffixName) {
                    case ".doc":
                        byte[] doc = WordToPdf.docToPdfStream(entity.getFullpath(), imageUrl);
                        outputStream.write(doc);
                        outputStream.close();
                        return R.ok(1, "转换成功!", null, true, null);
                    case ".docx":
                        byte[] docx = WordToPdf.docxToPdfStream(entity.getFullpath(), imageUrl);
                        outputStream.write(docx);
                        outputStream.close();
                        return R.ok(1, "转换成功!", null, true, null);
                    case ".xls":
                        byte[] xls = ExeclToPdf.xlsToPdfStream(entity.getFullpath());
                        outputStream.write(xls);
                        outputStream.close();
                        return R.ok(1, "转换成功!", null, true, null);
                    case ".xlsx":
                        byte[] xlsx = ExeclToPdf.xlsxToPdfStream(entity.getFullpath());
                        outputStream.write(xlsx);
                        outputStream.close();
                        return R.ok(1, "转换成功!", null, true, null);
                    default:
                        return R.ok(1, "文件格式不支持!", null, true, null);
                }
            }catch (NullPointerException nullPointerException){
                return R.ok(1,"空文件!",null,true,null);
            }catch (IOException ioException){
                if(ioException.getMessage().contains("no pages")){
                    return R.ok(1,"空文件!",null,true,null);
                }else{
                    return R.ok(1,ioException.getMessage(),null,true,null);
                }
            }
        }else{
            return R.ok(1,"文件不存在!",null,true,null);
        }
    }
    @GetMapping("/GetOfficeToPdfInfo")
    @ApiOperation(value = "Office转换pdf并返回pdf文件路径")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "idOrUrl",value = "",required = true,dataType = "String")
    })
    public R getOfficeToPdfInfo(@RequestParam("idOrUrl") String idOrUrl) throws IOException, DocumentException {
        ChengjianFileuploadEntity entity=chengjianFileuploadService.getFileByIdOrUrl(idOrUrl);
        if(entity.getFullpath()!=null&&entity!=null){
            String suffixName = entity.getFullpath().substring(entity.getFullpath().lastIndexOf("."));
            log.error("文件格式:"+suffixName);
            String name = CreateUuid.uuid();
            Map<String, Object> map = new HashMap<>();
            try {
                switch (suffixName) {
                    case ".doc":
                        WordToPdf.docToPdf(entity.getFullpath(), imageUrl, pdfUrl + name + ".pdf");
                        map.put("sourcePath", entity.getWebpath());
                        map.put("pdfPath", "/upload/pdf/" + name + ".pdf");
                        map.put("htmlPath", "");
                        map.put("model", entity);
                        return R.ok(1, "转换成功!", map, true, null);
                    case ".docx":
                        WordToPdf.docxToPdf(entity.getFullpath(), imageUrl, pdfUrl + name + ".pdf");
                        map.put("sourcePath", entity.getWebpath());
                        map.put("pdfPath", "/upload/pdf/" + name + ".pdf");
                        map.put("htmlPath", "");
                        map.put("model", entity);
                        return R.ok(1, "转换成功!", map, true, null);
                    case ".xls":
                        ExeclToPdf.xlsToPdf(entity.getFullpath(), pdfUrl + name + ".pdf");
                        map.put("sourcePath", entity.getWebpath());
                        map.put("pdfPath", "/upload/pdf/" + name + ".pdf");
                        map.put("htmlPath", "");
                        map.put("model", entity);
                        return R.ok(1, "转换成功!", map, true, null);
                    case ".xlsx":
                        ExeclToPdf.xlsxToPdf(entity.getFullpath(), pdfUrl + name + ".pdf");
                        map.put("sourcePath", entity.getWebpath());
                        map.put("pdfPath", "/upload/pdf/" + name + ".pdf");
                        map.put("htmlPath", "");
                        map.put("model", entity);
                        return R.ok(1, "转换成功!", map, true, null);
                    default:
                        return R.ok(1, "文件格式不支持!", null, true, null);
                }
            }catch (NullPointerException nullPointerException){
                return R.ok(1,"空文件!",null,true,null);
            }catch (IOException ioException){
                if(ioException.getMessage().contains("no pages")){
                    return R.ok(1,"空文件!",null,true,null);
                }else{
                    return R.ok(1,ioException.getMessage(),null,true,null);
                }
            }
        }else{
            return R.ok(1,"文件不存在!",null,true,null);
        }
    }
}
