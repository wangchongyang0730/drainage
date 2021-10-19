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
 * @date 2020-09-02 11:35:38
 * OM_Rel_Role_Module表实体类
 */
@Data
@TableName("OM_Rel_Role_Module")
public class OmRelRoleModuleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String moduleId;
	private String roleId;

}
