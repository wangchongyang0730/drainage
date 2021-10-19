package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/28
 * Time: 9:27
 */
@ApiModel(value = "修改对象22")
@Data
public class UpdateDeviceClash implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String note;
    private String enclosure;
    private String pointInfo;
}
