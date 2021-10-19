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
 * @date 2020-08-28 11:48:33
 * project_ProjectBasicInfo表实体类
 */
@Data
@TableName("project_ProjectBasicInfo")
public class ProjectProjectbasicinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	/**
	 * 合同总价
	 */
	private String contractamount;
	/**
	 * 安措费总额
	 */
	private String safetyamount;
	/**
	 * 安措费比例
	 */
	private String safetyproportion;
	/**
	 * 工程概况
	 */
	private String projectprofile;
	/**
	 * 概况图片
	 */
	private String profilepic;
	
	private String projectid;
	
	private Date createdate;
	
	private String createuser;
	
	private Date updatedate;
	
	private String updateuser;
	
	private Date deletedate;
	
	private String deleteuser;
	
	private Integer del;

}
