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
 * @date 2020-09-16 14:11:27
 * project_Report表实体类
 */
@Data
@TableName("project_Report")
public class ProjectReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String wbsid;
	private Date plantime;
	private Date emittime;
	private Date reporttime;
	private String result;
	private String remarks;
	private String uploadfile;
	private String uploadfile2;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;
	private String type;
	private String projectid;
	private String name;
	private String reportperson;
	private String securitygrade;
	private String filing;
	private String schemetype;
	private String shbuploadfile;

}
