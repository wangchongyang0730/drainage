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
 * Time: 9:43
 */
@Data
@TableName("project_history")
public class ProjectHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectName")
    private String projectName;
    @TableField("projectId")
    private String projectId;
    @TableField("accessTime")
    private Date accessTime;
    @TableField("userId")
    private String userId;
}
