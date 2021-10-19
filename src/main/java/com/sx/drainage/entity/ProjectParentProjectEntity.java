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
 * Date: 2020/11/2
 * Time: 13:28
 */
@Data
@TableName("project_parentProject")
public class ProjectParentProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    private String name;
    @TableField("projectDescribe")
    private String projectDescribe;
    private Integer del;
    @TableField("createUser")
    private String createUser;
    @TableField("createDate")
    private Date createDate;
    @TableField("updateUser")
    private String updateUser;
    @TableField("updateDate")
    private Date updateDate;
    @TableField("deleteUser")
    private String deleteUser;
    @TableField("deleteDate")
    private Date deleteDate;
    private String type;
    @TableField("fileId")
    private String fileId;
}

