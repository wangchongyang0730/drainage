package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/22
 * Time: 9:24
 */
@ApiModel(value = "请求对象10")
@Data
public class DeviceParams implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("Manufacturer")
    private String Manufacturer;
    private String code;
    private String deviceClass;
    private String deviceClassId;
    private String fileupload;
    private String isSpecial;
    private String model;
    private String name;
    private String projectId;
    private String qualifiedDate;
    private String qualifiedNo;
    private String serviceTime;
    private String status;
    private String sysId;
    private String useUnit;
    private String wbsId;
}
