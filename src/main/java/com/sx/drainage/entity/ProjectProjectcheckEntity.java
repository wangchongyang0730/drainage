package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 项目验收
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-22 14:49:56
 * project_ProjectCheck表实体类
 */
@Data
@TableName("project_ProjectCheck")
public class ProjectProjectcheckEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private Integer type;
	private String wbsid;
	private String wbsname;
	private String constructwarranty;
	private String constructremark;
	private String constructfile;
	private String constructionremark;
	private String constructionfile;
	private String supervisorremark;
	private String supervisorfile;
	private String projectid;
	private String checkstatus;
	private Integer del;
	private String createuser;
	private Date createdate;
	private String updateuser;
	private Date updatedate;
	private String deleteuser;
	private Date deletedate;
	private String shbuploadfile;

}
