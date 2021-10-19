package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/21
 * Time: 10:41
 */
@ApiModel(value = "请求对象24")
@Data
public class VideoParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String projectId;
    private String type;
    private String title;
    private String path;
    private String imgUrl;
}
