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
 * Time: 9:40
 * 初始化顶管区间
 */
@Data
@TableName("project_initPipejacking")
public class ProjectInitPipejackingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField("name")
    private String name;
    @TableField("totalLength")
    private Double totalLength;
    private Integer del;
}
