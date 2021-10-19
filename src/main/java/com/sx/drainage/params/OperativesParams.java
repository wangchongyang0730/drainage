package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/22
 * Time: 10:26
 */
@ApiModel(value = "请求对象13")
@Data
public class OperativesParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceSysId;
    private String licenseCode;
    private String licenseDate;
    private String licenseType;
    private String lincenseProject;
    private String operativesName;
    private String remark;
    private String sysId;
}
