package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/3
 * Time: 11:19
 */
@Data
@TableName("project_processDocumentation")
public class ProjectProcessDocumentationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("workType")
    private String workType;
    @TableField("processId")
    private String processId;
    @TableField("projectId")
    private String projectId;
    @TableField("taskId")
    private String taskId;
    @TableField("fileId")
    private String fileId;
    @TableField("userId")
    private String userId;
    @TableField("taskName")
    private String taskName;
}
