package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 17:18
 */
@ApiModel(value = "绑定施工重大设备信息请求对象")
@Data
public class BindEquipmentParams implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("SysId")
    private String SysId;
    private String wbsId;
    @JsonProperty("EquipmentName")
    private String EquipmentName;
    @JsonProperty("EntryDate")
    private String EntryDate;
    @JsonProperty("DepartureDate")
    private String DepartureDate;
}
