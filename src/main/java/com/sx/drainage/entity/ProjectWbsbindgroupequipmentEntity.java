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
 * @date 2020-08-28 16:03:22
 * project_WbsBindGroupEquipment表实体类
 */
@Data
@TableName("project_WbsBindGroupEquipment")
public class ProjectWbsbindgroupequipmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String sysid;
	private String entrydate;
	private String departuredate;
	private String wbsid;
	private String equipmentname;

}
