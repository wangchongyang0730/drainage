package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/10
 * Time: 14:41
 */
@ApiModel(value = "请求对象27")
@Data
public class WbsParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String partName;
    private String partType;
    private Date planBeginDate;
    private Date planEndDate;
    private Integer isKey;
    private String IpcId;
    private String parentId;
    private Boolean progressInfo;
    private String ipcText;
    private Integer isShowDashbord;
    private String wbsText;
    private String projectId;
    private Date factBeginDate;
    private Date factEndDate;
    private String mainUnit;
    private String reportPerson;
    private String remark;
    private String setbacksupload;
}
