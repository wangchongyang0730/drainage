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
 * @date 2020-09-09 12:03:20
 * project_DeviceCate表实体类
 */
@Data
@TableName("project_DeviceCate")
public class ProjectDevicecateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String projectid;
	private String parentid;
	private String name;
	private String remark;
	private String createuser;
	private Date createdate;
	private String updateuser;
	private Date updatedate;
	private String deleteuser;
	private Date deletedate;
	private Integer del;

}
