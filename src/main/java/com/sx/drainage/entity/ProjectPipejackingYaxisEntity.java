package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/30
 * Time: 10:02
 * 顶管日报统计y轴值
 */
@Data
@TableName("project_pipejacking_yaxis")
public class ProjectPipejackingYaxisEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField("initId")
    private String initId;
    @TableField("maxElevationDeviation")
    private Integer maxElevationDeviation;
    @TableField("maxPlaneDeviation")
    private Integer maxPlaneDeviation;
    @TableField("maxTotalJackingForce")
    private Integer maxTotalJackingForce;
    @TableField("maxUnitFriction")
    private Integer maxUnitFriction;
    @TableField("minElevationDeviation")
    private Integer minElevationDeviation;
    @TableField("minPlaneDeviation")
    private Integer minPlaneDeviation;
    @TableField("minTotalJackingForce")
    private Integer minTotalJackingForce;
    @TableField("minUnitFriction")
    private Integer minUnitFriction;
}
