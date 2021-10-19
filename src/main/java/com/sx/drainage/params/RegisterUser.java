package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 17:43
 */
@ApiModel(value = "注册对象",description = "注册对象")
@Data
public class RegisterUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String sex;
    private String accountId;
    private String mobile;
    private String wechartCode;
    private String applyRemark;
    private String code;
    private String sysId;
}
