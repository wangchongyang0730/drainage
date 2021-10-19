package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 15:49
 */
@ApiModel(value = "请求对象18")
@Data
public class ProjectParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String code;
    private String name;
    private String shortName;
    private String managerId;
    private String planStartDate;
    private String planEndDate;
    private Integer weekDay;
    private Integer monthDay;
    private String address;
    private String parentId;
    private String fileId;
    private String type;
}
