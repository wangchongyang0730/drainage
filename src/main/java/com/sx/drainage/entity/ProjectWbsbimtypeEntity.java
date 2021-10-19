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
 * @date 2020-09-10 10:12:43
 * project_WbsBimType表实体类
 */
@Data
@TableName("project_WbsBimType")
public class ProjectWbsbimtypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	private String name;
	private Integer type;
	private String projectid;
	private String bimurn;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;
	private String remake;
	private String bimtype;
	private String filepath;

}
