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
 * @date 2020-08-28 15:48:38
 * project_WbsBindGroupSourceRisk表实体类
 */
@Data
@TableName("project_WbsBindGroupSourceRisk")
public class ProjectWbsbindgroupsourceriskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String factbegindate;
	private String factenddate;
	private String planbegindate;
	private String planenddate;
	private String sourceriskname;
	private String wbsid;

}
