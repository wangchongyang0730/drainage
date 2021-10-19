package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-15 15:20:06
 * device_operation表实体类
 */
@Data
@TableName("device_operation")
public class DeviceOperationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String code;
	private String name;
	private String type;
	private String parameter;
	private String properties;
	private String manufacturer;
	private String count;
	private String address;
	private String appearancedate;
	private String guaranteedate;
	private String maintenancemode;
	private String remark;
	private String fileid;
	private String imgurl;
	private String projectid;

}
