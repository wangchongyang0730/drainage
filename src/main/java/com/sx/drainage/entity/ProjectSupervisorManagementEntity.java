package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/18
 * Time: 9:43
 */
@Data
@TableName("project_supervisorManagement")
public class ProjectSupervisorManagementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("positionName")
    private String positionName;
    @TableField("projectId")
    private String projectId;
    @TableField("userId")
    private String userId;
    @TableField("positionId")
    private String positionId;
}
