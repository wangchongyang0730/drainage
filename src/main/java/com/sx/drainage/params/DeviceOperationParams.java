package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/15
 * Time: 16:46
 */
@ApiModel(value = "请求对象9")
@Data
public class DeviceOperationParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String projectId;
    private String code;
    private String name;
    private String type;
    private String parameter;
    private String properties;
    @JsonProperty("Manufacturer")
    private String Manufacturer;
    private String count;
    @JsonProperty("AppearanceDate")
    private Date AppearanceDate;
    private String address;
    @JsonProperty("GuaranteeDate")
    private Date GuaranteeDate;
    @JsonProperty("MaintenanceMode")
    private String MaintenanceMode;
    private String remark;
    private String imgUrl;
    private String fileId;
}
