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
 * @date 2020-09-10 13:49:47
 * Monitor_NVR表实体类
 */
@Data
@TableName("Monitor_NVR")
public class MonitorNvrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String name;
	private String projectid;
	private String ipaddress;
	private Integer port;
	private Integer port2;
	private Integer port3;
	private String user;
	private String pwd;
	private String remark;
	private Integer del;

}
