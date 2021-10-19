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
 * @date 2020-08-28 11:55:35
 * project_company表实体类
 */
@Data
@TableName("project_company")
public class ProjectCompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	private String name;
	private String remark;

}
