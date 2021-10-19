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
 * Time: 9:34
 */
@Data
@TableName("project_starRating")
public class ProjectStarRatingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    private String year;
    private String month;
    private Integer star;
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
}
