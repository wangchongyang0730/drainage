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
 * @date 2020-09-10 13:36:07
 * project_MonitoryPoint表实体类
 */
@Data
@TableName("project_MonitoryPoint")
public class ProjectMonitorypointEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String cameraindexcode;
	private String cameraname;
	private String projectid;

}
