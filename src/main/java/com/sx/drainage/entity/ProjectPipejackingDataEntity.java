package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 9:45
 * 顶管监测数据
 */
@Data
@TableName("project_pipejacking_data")
public class ProjectPipejackingDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("pipejackId")
    private String pipejackId;
    @TableField("name")
    private String name;
    @TableField("pointName")
    private String pointName;
    @TableField("pointValue")
    private String pointValue;
    @TableField("remark")
    private String remark;
    @TableField("totalPointName")
    private String totalPointName;
    @TableField("totalPointValue")
    private String totalPointValue;
    @TableField("totalRemark")
    private String totalRemark;
    @TableField("horizontalX")
    private String horizontalX;
    @TableField("distanceFromHead")
    private String distanceFromHead;
    @TableField("distanceFromStart")
    private String distanceFromStart;
    @TableField("totalHorizontalX")
    private String totalHorizontalX;
    @TableField("totalDistanceFromHead")
    private String totalDistanceFromHead;
    @TableField("totalDistanceFromStart")
    private String totalDistanceFromStart;
    @TableField("caveEntrance")
    private Double caveEntrance;
    private Integer del;
}
