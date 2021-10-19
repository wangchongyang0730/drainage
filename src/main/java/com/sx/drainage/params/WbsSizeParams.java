package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 13:26
 */
@ApiModel(value = "请求对象28")
@Data
public class WbsSizeParams implements Serializable{
    private static final long serialVersionUID = 1L;
    private String wbsId;
    private String fileId;
    private String [][] wbsSize;
}
