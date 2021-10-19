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
 * Date: 2020/11/18
 * Time: 14:36
 */
@Data
@TableName("project_monthlyReview")
public class ProjectMonthlyReviewEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    private String year;
    private String month;
    @TableField("userId")
    private String userId;
    @TableField("userName")
    private String userName;
    @TableField("evaluationScore")
    private Float evaluationScore;
    @TableField("fileId")
    private String fileId;
    private Integer states;
    @TableField("appraisalTime")
    private Date appraisalTime;
    private Integer orders;
    @TableField(exist = false)
    private Integer star;
}
