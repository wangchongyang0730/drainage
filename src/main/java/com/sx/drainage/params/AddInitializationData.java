package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/7
 * Time: 14:58
 */
@ApiModel(value = "请求对象3")
@Data
public class AddInitializationData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String reportSysId;
    private String pointName;
    private String standardD;
    private String standardZ;
    private String actualPosition;
    private String relativePosition;
    private String relativePoint;
}
