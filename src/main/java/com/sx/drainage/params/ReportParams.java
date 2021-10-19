package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 14:21
 */
@ApiModel(value = "请求对象20")
@Data
public class ReportParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String projectId;
    private String remarks;
    private String sysId;
    private String uploadfile;
    private String uploadfile2;
    private String wbsId;
    private String wbsText;
    private Date planTime;
    private Date emitTime;
    private String filing;
    private String securityGrade;
}
