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
 * @date 2020-08-26 17:33:45
 * OM_Rel_Account_Role表实体类
 */
@Data
@TableName("OM_Rel_Account_Role")
public class OmRelAccountRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String accountId;
	
	private String roleId;

}
