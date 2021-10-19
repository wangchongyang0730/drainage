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
 * @date 2020-08-28 14:37:13
 * project_Device表实体类
 */
@Data
@TableName("project_Device")
public class ProjectDeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String projectid;
	private String wbsid;
	private String name;
	private String code;
	private String model;
	private String status;
	private String entrytime;
	private String outtime;
	private String checktime;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;
	private String servicetime;
	private String manufacturer;
	private String fileupload;
	private String qualifiedno;
	private String qualifieddate;
	private String inoutmark;
	private String checkstatus;
	private String checkperson;
	private String approvetime;
	private String approveperson;
	private String deviceclassid;
	private String deviceclass;
	private String useunit;
	private Integer isspecial;

}
