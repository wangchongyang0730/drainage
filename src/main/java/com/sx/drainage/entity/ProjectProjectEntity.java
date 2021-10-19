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
 * @date 2020-08-28 10:52:08
 * project_Project表实体类
 */
@Data
@TableName("project_Project")
public class ProjectProjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	
	private String parentid;
	
	private String code;
	
	private String name;
	
	private String shortname;
	
	private String managerid;
	
	private String telephone;
	
	private Date actualstartdate;
	
	private Date planstartdate;
	
	private Date planenddate;
	
	private String status;
	
	private String province;
	
	private String city;
	
	private String address;
	
	private Integer weekday;
	
	private Integer monthday;
	
	private Boolean del;
	
	private String creatorid;
	
	private Date createdate;
	
	private String bimpicid;
	
	private String constructionunit;
	
	private String supervisionunit;
	
	private String designunit;
	
	private String bimpicid2;
	
	private String bimpicid3;
	
	private String approvestate;
	
	private String pmscode;
	
	private String pmssysid;
	
	private String issyncdate;
	
	private String issyncdata;

	private String fileid;

	private String type;

	private Integer sort;
}
