package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/2
 * Time: 15:41
 */
@Data
@TableName("project_information")
public class ProjectInformationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField(fill = FieldFill.UPDATE)
    private String proposals;
    @TableField(value = "feasibilityReport",fill = FieldFill.UPDATE)
    private String feasibilityReport;
    @TableField(value = "designReport",fill = FieldFill.UPDATE)
    private String designReport;
    @TableField(value = "surveyReport",fill = FieldFill.UPDATE)
    private String surveyReport;
    private String code;
    @TableField("projectId")
    private String projectId;
    @TableField(value = "biddingInformation",fill = FieldFill.UPDATE)
    private String biddingInformation;
    @TableField(value = "projectReviewAndApproval",fill = FieldFill.UPDATE)
    private String projectReviewAndApproval;
    @TableField(value = "feasibilityStudyReport",fill = FieldFill.UPDATE)
    private String feasibilityStudyReport;
    @TableField(value = "investmentPlan",fill = FieldFill.UPDATE)
    private String investmentPlan;
    @TableField(value = "managementManual",fill = FieldFill.UPDATE)
    private String managementManual;
    @TableField(value = "projectManagementManual",fill = FieldFill.UPDATE)
    private String projectManagementManual;
    @TableField(value = "projectContract",fill = FieldFill.UPDATE)
    private String projectContract;
    @TableField(value = "designText",fill = FieldFill.UPDATE)
    private String designText;
    @TableField(value = "planningLicense",fill = FieldFill.UPDATE)
    private String planningLicense;
    @TableField("otherFiles")
    private String otherFiles;
}
