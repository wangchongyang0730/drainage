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
 * @date 2020-08-28 14:36:40
 * project_Wbs表实体类
 */
@Data
@TableName("project_Wbs")
public class ProjectWbsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String projectid;
	private String parentid;
	private String fullsysid;
	private String partcode;
	private String partcodebim;
	private String partname;
	private Integer parttype;
	private Integer iskey;
	private Date planbegindate;
	private Date planenddate;
	private Float finishpercent;
	private Integer finishstate;
	private Date factbegindate;
	private Date factenddate;
	private Date auditedenddate;
	private String orderval;
	private Boolean isshow;
	private String createuser;
	private Date createdate;
	private String updateuser;
	private Date updatedate;
	private String deleteuser;
	private Date deletedate;
	private Integer del;
	private String foreignkey;
	private String mpppath;
	private String mppid;
	private Integer mpplevel;
	private String mppwbs;
	private String mppfrontid;
	private Date mppplanbegindate;
	private Date mppplanenddate;
	private String remark;
	private String bimtype;
	private String ipcid;
	private String setbacksupload;
	private String completepercent;
	private String completequantity;
	private String unit;
	private String reportperson;
	private String reportdate;
	private String mainunit;
	private Boolean binding;
	private Boolean progressinfo;

}
