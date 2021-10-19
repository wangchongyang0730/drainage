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
 * Date: 2020/12/15
 * Time: 10:43
 */
@Data
@TableName("project_monitoringUnitEvaluation")
public class ProjectMonitoringUnitEvaluationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    private String year;
    private String month;
    @TableField("quarterlyScore")
    private Float quarterlyScore;
    private Integer states;
    @TableField("userId")
    private String userId;
    @TableField("userName")
    private String userName;
    @TableField("appraisalTime")
    private Date appraisalTime;
    private Integer orders;
    @TableField("projectId")
    private String projectId;
    private String evaluation;
    @TableField("companyId")
    private String companyId;
    @TableField("userGroup")
    private String userGroup;
    @TableField("fileId")
    private String fileId;
}
