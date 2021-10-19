package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/13
 * Time: 10:35
 */
@Data
@ApiModel(value = "修改进度请求对象")
public class JinDuParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String wbsId;
    @JsonProperty("PlanBeginDate")
    private String PlanBeginDate;
    @JsonProperty("PlanEndDate")
    private String PlanEndDate;
    @JsonProperty("FactBeginDate")
    private String FactBeginDate;
    @JsonProperty("FactEndDate")
    private String FactEndDate;
    private String isKey;
    @JsonProperty("ImagePath")
    private String ImagePath;
    @JsonProperty("Remark")
    private String Remark;
    private String progressInfo;
}
