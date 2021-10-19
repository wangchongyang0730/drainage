package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/27
 * Time: 15:29
 */
@ApiModel(value = "请求对象23")
@Data
public class UpdateEarlierreport implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String code;
    private String certificateName;
    private String ratifyDate;
    private String ratifyDept;
    private String uploadfile;
}
