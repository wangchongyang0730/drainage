package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/24
 * Time: 9:35
 */
@ApiModel(value = "请求对象25")
@Data
public class WbsBimTypeParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String projectId;
}
