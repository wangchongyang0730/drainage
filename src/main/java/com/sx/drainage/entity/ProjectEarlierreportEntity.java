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
 * @date 2020-08-27 11:51:01
 * project_EarlierReport表实体类
 */
@Data
@TableName("project_EarlierReport")
public class ProjectEarlierreportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	
	private String projectid;
	
	private String certificatename;
	
	private String code;
	
	private String ratifydate;
	
	private String ratifydept;
	
	private String uploadfile;
	
	private Date createdate;
	
	private String createuser;
	
	private Date updatedate;
	
	private String updateuser;
	
	private Date deletedate;
	
	private String deleteuser;
	
	private Integer del;
	
	private String type;

}
