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
 * @date 2020-09-07 10:12:37
 * project_MonitorInit表实体类
 */
@Data
@TableName("project_MonitorInit")
public class ProjectMonitorinitEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String reportsysid;
	private String pointname;
	private String standardd;
	private String standardz;
	private String actualposition;
	private String relativeposition;
	private String relativepoint;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;

	@TableField(exist = false)
	private int sort;//用来排序
}
