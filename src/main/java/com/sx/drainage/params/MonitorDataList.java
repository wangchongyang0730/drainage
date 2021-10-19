package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/8
 * Time: 17:57
 */
@ApiModel(value = "请求对象12")
@Data
public class MonitorDataList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String dailySysId;
    private String pointName;
    private String pointD;
    private String pointTotalD;
    private String currentValueD;
    private String pointZ;
    private String pointTotalZ;
    private String currentValueZ;
    private String remark;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;
    private String deleteDate;
    private String deleteUser;
    private Integer del;
}
