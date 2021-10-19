package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-09-10 17:00:17
 * project_WbsBindGroup表实体类
 */
@Data
@TableName("project_WbsBindGroup")
public class ProjectWbsbindgroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer sysid;
	private String wbsid;
	private String directorytreeid;
	private String constructionid;

}
