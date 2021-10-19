package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/9
 * Time: 16:46
 */
@ApiModel(value = "请求对象19")
@Data
public class ProjectParticipantsParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sysId;
    private String tagid;
    private String department;
    private String projectId;
    private String account_id;
}
