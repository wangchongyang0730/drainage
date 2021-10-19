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
 * Date: 2020/11/4
 * Time: 13:55
 * 项目阶段文件实体类
 */
@Data
@TableName("project_phase")
public class ProjectPhaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField(value = "constructionBidding",fill = FieldFill.UPDATE)
    private String constructionBidding;
    @TableField(value = "constructionPermit",fill = FieldFill.UPDATE)
    private String constructionPermit;
    @TableField(fill = FieldFill.UPDATE)
    private String supervision;
    @TableField(value = "organizationalDesign",fill = FieldFill.UPDATE)
    private String organizationalDesign;
    @TableField(value = "supervisedPlanning",fill = FieldFill.UPDATE)
    private String supervisedPlanning;
    @TableField(value = "scenarioConstruction",fill = FieldFill.UPDATE)
    private String scenarioConstruction;
    @TableField(fill = FieldFill.UPDATE)
    private String acceptance;
    @TableField(value = "designModel",fill = FieldFill.UPDATE)
    private String designModel;
    @TableField(value = "startWork",fill = FieldFill.UPDATE)
    private String startWork;
    @TableField(value = "specialPrograms",fill = FieldFill.UPDATE)
    private String specialPrograms;
    @TableField(value = "supervisionDetails",fill = FieldFill.UPDATE)
    private String supervisionDetails;
    @TableField(value = "projectSchedule",fill = FieldFill.UPDATE)
    private String projectSchedule;
    @TableField(value = "constructionModel",fill = FieldFill.UPDATE)
    private String constructionModel;
    @TableField(value = "videoSurveillance",fill = FieldFill.UPDATE)
    private String videoSurveillance;
    @TableField(value = "completedModel",fill = FieldFill.UPDATE)
    private String completedModel;  //****
    @TableField(value = "completionAcceptance",fill = FieldFill.UPDATE)
    private String completionAcceptance;//****
    @TableField(fill = FieldFill.UPDATE)
    private String record;//****
    @TableField(value = "filingCertificate",fill = FieldFill.UPDATE)
    private String filingCertificate;//****
    @TableField(fill = FieldFill.UPDATE)
    private String responsibility;
    @TableField(fill = FieldFill.UPDATE)
    private String code;
    @TableField(value = "supervisionBidding",fill = FieldFill.UPDATE)
    private String supervisionBidding;
    @TableField(value = "monitorBidding",fill = FieldFill.UPDATE)
    private String monitorBidding;
    @TableField(value = "specialSubmission",fill = FieldFill.UPDATE)
    private String specialSubmission;
    @TableField(value = "acceptanceReport",fill = FieldFill.UPDATE)
    private String acceptanceReport;
    @TableField(value = "maintenanceReport",fill = FieldFill.UPDATE)
    private String maintenanceReport;
    @TableField(value = "createAplan",fill = FieldFill.UPDATE)
    private String createAplan;
    @TableField(value = "floodControlDocument",fill = FieldFill.UPDATE)
    private String floodControlDocument;
    @TableField(value = "trafficOrganizationPlan",fill = FieldFill.UPDATE)
    private String trafficOrganizationPlan;
    @TableField(value = "workTicket",fill = FieldFill.UPDATE)
    private String workTicket;
    @TableField(value = "planningPermit",fill = FieldFill.UPDATE)
    private String planningPermit;
}
