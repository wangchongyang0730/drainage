package com.sx.drainage.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/20
 * Time: 10:41
 */
@Data
@ApiModel(value = "坐标请求对象")
public class MapParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sysId;
    private String projectId;
    @JsonProperty("CenterPoint")
    private String CenterPoint;
    private String remarks;
    private String images;
    private String address;
    private String name;
}
