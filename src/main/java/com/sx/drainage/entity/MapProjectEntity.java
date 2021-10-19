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
 * @date 2020-08-28 17:25:02
 * Map_project表实体类
 */
@Data
@TableName("Map_project")
public class MapProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String projectid;
	private String centerpoint;
	private String remarks;
	private String images;
	private String address;
	private String name;
	private String value;
	private String status;
	private Date createtime;
	private String deviceid;
	private String devicename;
	private Integer del;

}
