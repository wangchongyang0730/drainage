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
 * @date 2020-09-21 10:06:20
 * project_TechnologVideo表实体类
 */
@Data
@TableName("project_TechnologVideo")
public class ProjectTechnologvideoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String title;
	private String path;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Integer del;
	private String type;
	private String imgurl;
	private String projectid;

}
