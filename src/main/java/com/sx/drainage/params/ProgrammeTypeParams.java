package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/24
 * Time: 10:00
 */
@ApiModel(value = "请求对象15")
@Data
public class ProgrammeTypeParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String typeName;
}
