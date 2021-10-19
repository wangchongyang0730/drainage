package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/14
 * Time: 14:28
 * 年度考评实体类
 */
@Data
@TableName("project_annualEvaluation")
public class ProjectAnnualEvaluationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("userId")
    private String userId;
    @TableField("userName")
    private String userName;
    private String year;
    private Integer states;
    @TableField("projectId")
    private String projectId;
    @TableField("firstQuarter")
    private Float firstQuarter;
    @TableField("secondQuarter")
    private Float secondQuarter;
    @TableField("thirdQuarter")
    private Float thirdQuarter;
    @TableField("fourthQuarter")
    private Float fourthQuarter;
    @TableField("qualityAccident")
    private Float qualityAccident;
    @TableField("safetyIncident")
    private Float safetyIncident;
    @TableField("totalScore")
    private Float totalScore;
    private String evaluation;

}
