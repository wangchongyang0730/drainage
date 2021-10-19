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
 * Date: 2020/11/19
 * Time: 16:05
 */
@Data
@TableName("project_criticalProject")
public class ProjectCriticalProjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField("dangerousType")
    private Integer dangerousType;
    @TableField("startTime")
    private Date startTime;
    @TableField("endTime")
    private Date endTime;
    private Boolean overscale;
    private Boolean review;
    @TableField("checkPoint")
    private String checkPoint;
    @TableField("briefContent")
    private String briefContent;
    @TableField("acceptanceAndPublicity")
    private Integer acceptanceAndPublicity;
    @TableField("fileId")
    private String fileId;
    @TableField("evaluationAgency")
    private String evaluationAgency;
    @TableField("reviewProgress")
    private Integer reviewProgress;
    @TableField("approvalStatus")
    private Integer approvalStatus;
    private String quantity;
    @TableField("createUserId")
    private String createUserId;
    @TableField("createDate")
    private Date createDate;
    @TableField("createUserName")
    private String createUserName;


}
