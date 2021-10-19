package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/24
 * Time: 13:53
 */
@ApiModel(value = "请求对象26")
@Data
public class WbsBindParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String wbsId;
    private String directoryTreeId;
    private String constructionId;
}
