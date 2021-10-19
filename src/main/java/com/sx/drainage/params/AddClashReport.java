package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/31
 * Time: 13:52
 */
@ApiModel(value = "请求对象1")
@Data
public class AddClashReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private String projectId;
    private String note;
    private String enclosure;
    private String sysId;
    private String pointInfo;
}
