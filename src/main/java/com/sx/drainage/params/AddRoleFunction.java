package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/3
 * Time: 10:32
 */
@ApiModel(value = "请求对象6")
@Data
public class AddRoleFunction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleid;
    private String [] moduleids;
}
