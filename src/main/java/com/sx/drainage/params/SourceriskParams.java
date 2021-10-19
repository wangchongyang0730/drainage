package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/22
 * Time: 14:30
 */
@ApiModel(value = "请求对象21")
@Data
public class SourceriskParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String over;
    private String projectId;
    private String sysId;
    private String time;
    private String wbsId;
    private String wbsName;
}
