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
 * Date: 2020/12/14
 * Time: 10:11
 * 季度考评实体类
 */
@Data
@TableName("project_quarterlyAssessment")
public class ProjectQuarterlyAssessmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    private Integer states;
    @TableField("fileId")
    private String fileId;
    @TableField("userId")
    private String userId;
    @TableField("userName")
    private String userName;
    @TableField("submitDate")
    private Date submitDate;
    @TableField("quarterlyScore")
    private Float quarterlyScore;
    private Integer orders;
    private String quarterly;
    private String year;
    @TableField("totalScore")
    private Float totalScore;
}
