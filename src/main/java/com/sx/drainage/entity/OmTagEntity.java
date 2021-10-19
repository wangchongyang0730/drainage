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
 * @date 2020-08-28 11:45:32
 * OM_Tag表实体类
 */
@Data
@TableName("OM_Tag")
public class OmTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@TableId
	private String sysid;
	
	private String name;
	
	private String remark;
	
	private Date createdate;
	
	private String createuser;
	
	private Date updatedate;
	
	private String updateuser;
	
	private Date deletedate;
	
	private String deleteuser;
	
	private Integer del;
	
	private String parentid;
	
	private String tagtype;
	
	private String useFlag;
	
	private Integer sort;

}
