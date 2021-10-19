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
 * @date 2020-08-28 11:38:29
 * OM_Rel_Account_Role表实体类
 */
@Data
@TableName("OM_Tag_Rel_Post_Role")
public class OmTagRelPostRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;

	private String postId;

	private String roleId;
	
	private String remark;
	
	private Date createdate;
	
	private String createuser;
	
	private Date updatedate;
	
	private String updateuser;
	
	private Date deletedate;
	
	private String deleteuser;
	
	private Integer del;
	@TableField("projectId")
	private String projectId;

}
