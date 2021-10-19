package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 15:59
 */
@ApiModel(value = "请求对象16")
@Data
public class ProjectBasicParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contractAmount;
    private String safetyAmount;
    private String safetyProportion;
    private String projectProfile;
    private String profilePic;
    private String projectId;
}
