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
 * @date 2020-08-28 16:11:32
 * InspectionPrice表实体类
 */
@Data
@TableName("InspectionPrice")
public class InspectionpriceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private Float price;
	private String remark;
	private Date createtime;
	private String createuser;
	private String type;
	private String projectid;
	private String uploadfile;
	private Date reporttime;

}
