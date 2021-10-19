package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 14:09
 * 接受登录参数实体类
 */
@Data
@ApiModel(value = "用户登录对象",description ="用户登录对象" )
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "密码")
    private String password;
}
