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
 * @date 2020-09-07 10:16:45
 * project_MonitorReportSet表实体类
 */
@Data
@TableName("project_MonitorReportSet")
public class ProjectMonitorreportsetEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String name;
	private String projectid;
	private String templatetype;
	private String templateskintype;
	private String templateexcelpath;
	private String thresholdstartd;
	private String thresholdendd;
	private String totalthresholdstartd;
	private String totalthresholdendd;
	private String thresholdstartz;
	private String thresholdendz;
	private String totalthresholdstartz;
	private String totalthresholdendz;
	private String initvalued;
	private String initvaluez;
	private String picfileid;
	private Date createdate;
	private String createuser;
	private Date updatedate;
	private String updateuser;
	private Date deletedate;
	private String deleteuser;
	private Integer del;
	private Integer sortvalue;

}
