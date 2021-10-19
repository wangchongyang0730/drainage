package com.sx.drainage.entity;

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
 * @date 2020-08-26 17:34:36
 * OM_Role表实体类
 */
@Data
@TableName("OM_Role")
public class OmRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	
	private Date endtime;
	
	private Date starttime;
	
	private Integer valid;
	
	private String parentId;
	
	private String fullname;
	
	private String enterpriseid;
	
	private Date createddate;
	
	private String description;
	
	private String name;

}
