package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/10/27
 * Time: 9:43
 */
@Data
@TableName("Activiti_WorkFlow")
public class ActivitiWorkFlowEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value ="sysId")
    private String sysId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "processName")
    private String processName;
    @TableField(value = "createUser")
    private String createUser;
    @TableField(value = "createUserId")
    private String createUserId;
    @TableField(value = "createDate")
    private Date createDate;
    @TableField(value = "submitDate")
    private Date submitDate;
    @TableField(value = "states")
    private Integer states;
    @TableField(value = "submitUser")
    private String submitUser;
    @TableField(value = "num")
    private Integer num;
    @TableField(value = "workType")
    private String workType;
    @TableField(value = "processId")
    private String processId;
    @TableField(value = "projectId")
    private String projectId;
    @TableField(value = "del")
    private Integer del;
    @TableField(value = "url")
    private String url;
    @TableField("toDoUser")
    private String toDoUser;
    @TableField("taskId")
    private String taskId;
    @TableField(exist = false)
    private Map<String,Object> data;
}
