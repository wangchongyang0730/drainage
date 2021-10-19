package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/16
 * Time: 13:46
 * 阶段性验收文件实体类
 */
@Data
@TableName("project_phasedAcceptance")
public class ProjectPhasedAcceptanceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("sysId")
    private String sysId;
    @TableField("projectId")
    private String projectId;
    @TableField("meetingMinutes")
    private String meetingMinutes;
    @TableField("phasedAcceptanceReport")
    private String phasedAcceptanceReport;
    @TableField("reportMaterial")
    private String reportMaterial;
}
