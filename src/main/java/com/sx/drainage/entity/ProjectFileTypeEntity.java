package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/19
 * Time: 14:10
 */
@Data
@TableName("project_fileType")
public class ProjectFileTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("parentId")
    private String parentId;
    @TableField("typeName")
    private String typeName;
    private Integer sort;
    @TableField(exist = false)
    private List<ProjectFileTypeEntity> child;
}
