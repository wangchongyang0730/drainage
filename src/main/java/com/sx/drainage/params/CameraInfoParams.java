package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/21
 * Time: 13:33
 */
@Data
@ApiModel(value = "监控请求对象")
public class CameraInfoParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String projectId;
    private List<Camera> camera;
}

