package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/1
 * Time: 13:56
 * 顶管日报
 */
@Data
@TableName("project_pipejacking_daily")
public class ProjectPipejackingDailyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("initId")
    private String initId;
    @TableField("projectId")
    private String projectId;
    @TableField("topToday")
    private Double topToday;
    @TableField("accumulatedJacking")
    private Double accumulatedJacking;
    @TableField("planeDeviation")
    private Double planeDeviation;
    @TableField("elevationDeviation")
    private Double elevationDeviation;
    @TableField("totalJackingForce")
    private Double totalJackingForce;
    @TableField("unitFriction")
    private Double unitFriction;
    @TableField("monitorProjectName")
    private String monitorProjectName;
    @TableField("monitorDate")
    private String monitorDate;
    @TableField("checkUser")
    private String checkUser;
    @TableField("weather")
    private String weather;
    @TableField("monitorOverview")
    private String monitorOverview;
    @TableField("constructionOverview")
    private String constructionOverview;
    @TableField("suggestion")
    private String suggestion;
    @TableField("reportTime")
    private Date reportTime;
    @TableField("reportUserId")
    private String reportUserId;
    @TableField("maker")
    private String maker;
    @TableField("company")
    private String company;
    @TableField("horizontalX")
    private String horizontalX;
    @TableField("distanceFromHead")
    private String distanceFromHead;
    @TableField("distanceFromStart")
    private String distanceFormStart;
    private Integer del;
    @TableField(exist = false)
    private List<ProjectPipejackingDataEntity> data;
}
