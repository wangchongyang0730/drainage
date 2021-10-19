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
 * @date 2020-08-28 16:28:08
 * project_WbsBindGroupRectification表实体类
 */
@Data
@TableName("project_WbsBindGroupRectification")
public class ProjectWbsbindgrouprectificationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String remarks;
	private String wbsid;
	private String type;
	private String before;
	private String after;
	private String hiddentype;
	private String rectificationdate;
	private Integer zhiliangoranquan;

}
