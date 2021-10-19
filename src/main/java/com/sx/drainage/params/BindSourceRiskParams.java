package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 17:11
 */
@ApiModel(value = "绑定重大风险源请求对象")
@Data
public class BindSourceRiskParams implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("SysId")
    private String SysId;
    @JsonProperty("WbsId")
    private String WbsId;
    @JsonProperty("SourceRiskName")
    private String SourceRiskName;
    @JsonProperty("PlanBeginDate")
    private String PlanBeginDate;
    @JsonProperty("PlanEndDate")
    private String PlanEndDate;
    @JsonProperty("FactBeginDate")
    private String FactBeginDate;
    @JsonProperty("FactEndDate")
    private String FactEndDate;
}
