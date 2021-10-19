package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/1
 * Time: 9:41
 */
@TableName("project_notification")
@Data
public class ProjectNotificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("userId")
    private String userId;
    @TableField("processId")
    private String processId;
    @TableField("projectId")
    private String projectId;
    @TableField("processType")
    private String processType;
    private Integer states;
    private Integer del;
    @TableField("createUserId")
    private String createUserId;
    @TableField("createUser")
    private String createUser;
    private String msg;
    @TableField("evaluationId")
    private String evaluationId;
}
