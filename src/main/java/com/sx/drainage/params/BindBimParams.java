package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/10
 * Time: 10:28
 */
@ApiModel(value = "请求对象7")
@Data
public class BindBimParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bimType;
    private String bimId;
    private String engineType;
    private String projectId;
    private String filePath;
}
