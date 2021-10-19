package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/7
 * Time: 13:20
 */
@ApiModel(value = "请求对象5")
@Data
public class AddReportInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value = "Sysid")
    private String sysId;
    private String name;
    private String projectId;
    private String templateType;
    private String thresholdStartD;
    private String thresholdEndD;
    private String totalThresholdStartD;
    private String totalThresholdEndD;
    private String thresholdStartZ;
    private String thresholdEndZ;
    private String totalThresholdStartZ;
    private String totalThresholdEndZ;
    private Integer sortValue;
    private String picFileId;
}
