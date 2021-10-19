package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/17
 * Time: 16:00
 * 项目管理人员实体类
 */
@Data
@TableName("project_managerPeople")
public class ProjectManagerPeopleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    private String department;
    @TableField("leadersInCharge")
    private String leadersInCharge;
    @TableField("projectManager")
    private String projectManager;
}
