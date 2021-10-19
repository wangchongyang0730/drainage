package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 13:30
 */
@ApiModel(value = "请求对象14")
@Data
public class PriceParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String projectId;
    private String type;
    private String remark;
    private String price;
    private Date reportTime;
    private String uploadfile;
}
