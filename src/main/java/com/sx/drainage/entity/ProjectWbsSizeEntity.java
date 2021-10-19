package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/29
 * Time: 9:38
 */
@Data
@TableName("project_WbsSize")
public class ProjectWbsSizeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String sysid;
    private String wbsid;
    private String wbssize;
    private String fileid;
}
