package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 16:58:17
 * project_ProgressInfo表实体类
 */
@Data
@TableName("project_ProgressInfo")
public class ProjectProgressinfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String sysid;
    private String proname;
    private String projectid;
    private String wbsid;

}
