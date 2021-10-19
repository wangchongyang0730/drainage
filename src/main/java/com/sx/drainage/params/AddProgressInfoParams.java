package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 10:48
 */
@ApiModel(value = "请求对象4")
@Data
public class AddProgressInfoParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String proName;
    private String projectId;
    private String [] wbsId;
}
