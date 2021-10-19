package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-27 11:00:53
 * OM_Module表实体类
 */
@Data
@TableName("OM_Module")
public class OmModuleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	
	private String parentId;
	
	private String fullname;
	
	private String moduletype;
	
	private Date createddate;
	
	private String description;
	
	private String name;
	
	private String sequencenum;
	
	private String url;
	
	private Boolean hide;
	
	private String iconcss;

	@TableField(exist = false)
	private String isHaveChildNode;
}
