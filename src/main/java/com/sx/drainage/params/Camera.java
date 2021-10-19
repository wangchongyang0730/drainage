package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/21
 * Time: 13:45
 */
@Data
@ApiModel(value = "监控信息请求对象")
public class Camera implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cameraIndexCode;
    private String cameraName;
}
