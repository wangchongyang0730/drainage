package com.sx.drainage.entity;

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
 * @date 2020-08-28 14:12:14
 * chengjian_FileUpload表实体类
 */
@Data
@TableName("chengjian_FileUpload")
public class ChengjianFileuploadEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String entityid;
	private String creatorid;
	private String uploadtype;
	private String contenttype;
	private BigDecimal filesize;
	private String name;
	private String path;
	private String fullpath;
	private String webpath;
	private String uploadname;
	private Date createddate;
	private String thumbwebpath;

}
