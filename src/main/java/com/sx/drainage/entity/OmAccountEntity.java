package com.sx.drainage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @date 2020-08-26 11:57:58
 * OM_Account表实体类
 */
@Data
@TableName("OM_Account")
public class OmAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String sysid;
	
	private String accountid;
	
	private String empid;
	
	private BigDecimal expire;
	
	private String humanId;
	
	private Integer inact;
	
	private BigDecimal lastchg;
	
	private Integer max;
	
	private Integer min;
	
	private String password;
	
	private Integer removed;
	
	private Integer valid;
	
	private Integer warn;
	
	private String accounttype;
	
	private String email;
	
	private String msn;
	
	private String wechartcode;
	
	private String address;
	
	private String address2;
	
	private String postalcode;
	
	private String postalcode2;
	
	private String worktelphone;
	
	private String worktelphone2;
	
	private String mobile;
	
	private String mobile2;
	
	private String faxphone;
	
	private String faxphone2;
	
	private String pinyin;
	
	private Integer del;
	
	private Date logondate;
	
	private BigDecimal logontimes;
	
	private String enterpriseid;
	
	private Integer mobilevalid;
	
	private String mobileconfirmcode;
	
	private String synchroid;
	
	private Date createddate;
	
	private String description;
	
	private String name;
	
	private String keyid;
	
	private String sex;
	
	private String position;
	
	private String duty;
	
	private String identitycard;
	
	private String edbackground;
	
	private String officeholder;
	
	private String qualificationcert;
	
	private String postcert;
	
	private String photo;
	
	private String file1;
	
	private String file2;
	
	private String file3;
	
	private String file4;
	
	private String filename1;
	
	private String filename2;
	
	private String filename3;
	
	private String filename4;
	
	private String file5;
	
	private String filename5;
	
	private Date updatecontentdate;
	
	private String dynamicpassword;
	
	private Date dynamicpwddate;
	
	private String sort;
	
	private String emailaccount;
	
	private Integer taskcount;
	
	private String wechartuserid;
	
	private String fixedtelephone;
	
	private String officeaddress;
	
	private String officeroomno;
	
	private Boolean conenterprise;
	
	private String wechartmobile;
	
	private String wechartemail;
	
	private String recmsgtype;

	private Integer mandatory;
	
	private Integer accountstate;
	@TableField(fill = FieldFill.UPDATE)
	private String openid;
	
	private String companyId;
	
	private String unionid;

	@TableField(exist = false) //此字段数据库中不存在
	private Object obj;

	@TableField(exist = false) //此字段数据库中不存在
	private Object obj2;
}
