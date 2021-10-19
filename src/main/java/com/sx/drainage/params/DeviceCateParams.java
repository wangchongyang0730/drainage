package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 13:49
 */
@ApiModel(value = "请求对象8")
@Data
public class DeviceCateParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String projectId;
    private String name;
    private String remark;
    private String parentId;
}
