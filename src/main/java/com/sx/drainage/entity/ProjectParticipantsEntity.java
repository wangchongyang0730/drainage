package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-28 11:53:39
 * project_Participants表实体类
 */
@Data
@TableName("project_Participants")
public class ProjectParticipantsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String name;
	private String department;
	private String username;
	private String phone;
	private String projectid;
	private String accountId;

}
