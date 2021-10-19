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
 * @date 2020-09-22 13:41:33
 * SourceRisk表实体类
 */
@Data
@TableName("SourceRisk")
public class SourceriskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String name;
	private String wbsid;
	private String wbsname;
	private Date createtime;
	private String createuser;
	private Date updatetime;
	private String updateuser;
	private String isfolw;
	private String projectid;
	private String planid;
	private Boolean del;

}
