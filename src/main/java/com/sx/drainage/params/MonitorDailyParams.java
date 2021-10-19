package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/8
 * Time: 17:55
 */
@ApiModel(value = "请求对象11")
@Data
public class MonitorDailyParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String code;
    private String monitorDate;
    private String reportSysId;
    private String weater;
    private String remark;
    private List<MonitorDataList> monitordataList;
}
