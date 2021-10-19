package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-07 10:18:23
 * project_MonitorData表实体类
 */
@Data
@TableName("project_MonitorData")
public class ProjectMonitordataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String dailysysid;
	private String pointname;
	private BigDecimal pointd;
	private BigDecimal pointtotald;
	private BigDecimal currentvalued;
	private BigDecimal pointz;
	private BigDecimal pointtotalz;
	private BigDecimal currentvaluez;
	private String remark;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;

	@TableField(exist = false)
	private int sort;
}
