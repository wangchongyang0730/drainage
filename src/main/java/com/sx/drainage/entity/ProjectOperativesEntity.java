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
 * @date 2020-09-21 17:31:14
 * project_Operatives表实体类
 */
@Data
@TableName("project_Operatives")
public class ProjectOperativesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String devicesysid;
	private String operativesname;
	private String licensecode;
	private String licensetype;
	private String licensedate;
	private String lincenseproject;
	private String remark;
	private String createdate;
	private String createuser;
	private String updatedate;
	private String updateuser;
	private String deletedate;
	private String deleteuser;
	private Integer del;

}
