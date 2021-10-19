package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/3
 * Time: 11:01
 * 停用流程实体类
 */
@Data
@TableName("project_stop_process")
public class ProjectStopProcessEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField("fileId")
    private String fileId;
    @TableField("fileId2")
    private String fileId2;
    @TableField("fileId3")
    private String fileId3;
    @TableField("processType")
    private String processType;
    @TableField("remark")
    private String remark;
    @TableField("opinionFileId")
    private String opinionFileId;
    @TableField("name")
    private String name;
    @TableField("type")
    private String type;
    @TableField("isHighRiskProject")
    private Boolean isHighRiskProject;
    @TableField("highRiskProject")
    private String highRiskProject;
    @TableField("acceptanceTime")
    private Date acceptanceTime;
    @TableField("problemFileId")
    private String problemFileId;
    @TableField("rectificationFileId")
    private String rectificationFileId;
    @TableField("createTime")
    private Date createTime;
    @TableField("createUser")
    private String createUser;
    @TableField("createUserId")
    private String createUserId;
    private Integer del;
}
