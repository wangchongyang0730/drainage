package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 14:01:42
 * Monitor_IPC表实体类
 */
@Data
@TableName("Monitor_IPC")
public class MonitorIpcEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String nvrid;
	private String name;
	private String ipaddress;
	private Integer port;
	private Integer port2;
	@TableField("[user]")
	private String user;
	private String pwd;
	private String remark;
	private Integer pass;
	private Integer isenable;
	private Integer del;

}
