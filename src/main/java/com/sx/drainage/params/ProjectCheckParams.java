package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/22
 * Time: 15:35
 */
@ApiModel(value = "请求对象17")
@Data
public class ProjectCheckParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String type;
    private String constructionFile;
    private String constructionRemark;
    private String projectId;
}
