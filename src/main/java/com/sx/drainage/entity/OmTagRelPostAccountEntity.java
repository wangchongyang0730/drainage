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
 * @date 2020-08-26 17:30:40
 * OM_Tag_Rel_Post_Account实体类
 */
@Data
@TableName("OM_Tag_Rel_Post_Account")
public class OmTagRelPostAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private String fkId;
	/**
	 * $column.comments
	 */
	private String projectid;
	/**
	 * $column.comments
	 */
	private String postId;
	/**
	 * $column.comments
	 */
	private String accountId;
	/**
	 * $column.comments
	 */
	private Date createdate;
	/**
	 * $column.comments
	 */
	private String createuser;
	/**
	 * $column.comments
	 */
	private Date updatedate;
	/**
	 * $column.comments
	 */
	private String updateuser;
	/**
	 * $column.comments
	 */
	private Date deletedate;
	/**
	 * $column.comments
	 */
	private String deleteuser;
	/**
	 * $column.comments
	 */
	private Integer del;

}
