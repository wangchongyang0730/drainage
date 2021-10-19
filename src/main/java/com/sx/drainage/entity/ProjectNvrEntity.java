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
 * @date 2020-09-24 11:23:29
 * project_NVR表实体类
 */
@Data
@TableName("project_NVR")
public class ProjectNvrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String name;
	private String coordinatepoint;
	private String monitorypoint;
	private String projectid;

}
