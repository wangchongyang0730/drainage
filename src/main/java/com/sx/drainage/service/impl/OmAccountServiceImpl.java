package com.sx.drainage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.drainage.common.CreateUuid;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.Query;
import com.sx.drainage.dao.OmAccountDao;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.entity.OmRelAccountRoleEntity;
import com.sx.drainage.entity.OmRoleEntity;
import com.sx.drainage.entity.ProjectCompanyEntity;
import com.sx.drainage.params.RegisterUser;
import com.sx.drainage.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("omAccountService")
@Transactional
public class OmAccountServiceImpl extends ServiceImpl<OmAccountDao, OmAccountEntity> implements OmAccountService {
    @Autowired
    private OmRoleService omRoleService;
    @Autowired
    private OmRelAccountRoleService omRelAccountRoleService;
    @Autowired
    private OmTagRelPostAccountService omTagRelPostAccountService;
    @Autowired
    private ProjectCompanyService projectCompanyService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String search = (String) params.get("search");
        if(StringUtils.isEmpty(search)){
            List<String> not = (List<String>) params.get("not");
            if(not!=null){
                IPage<OmAccountEntity> page = this.page(
                        new Query<OmAccountEntity>().getPage(params),
                        new QueryWrapper<OmAccountEntity>().notIn("sysId",not).eq("del",0)
                );
                return new PageUtils(page);
            }else{
                IPage<OmAccountEntity> page = this.page(
                        new Query<OmAccountEntity>().getPage(params),
                        new QueryWrapper<OmAccountEntity>().eq("del",0)
                );
                return new PageUtils(page);
            }
        }else{
            QueryWrapper<OmAccountEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("del",0);
            wrapper.and((wr)->{
                wr.like("name",search).or().like("accountId",search);
            });
            IPage<OmAccountEntity> page = this.page(
                    new Query<OmAccountEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }
    }
    //用户登录
    @Override
    public OmAccountEntity login(String username, String password) {
        QueryWrapper<OmAccountEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accountid",username).eq("password",password).eq("del",0);
        OmAccountEntity one = this.getOne(queryWrapper);
        return one;
    }
    //修改当前用户密码
    @Override
    public void modifyCurrentPassword(String userId, String newPass) {
        UpdateWrapper<OmAccountEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sysid",userId);
        OmAccountEntity entity = new OmAccountEntity();
        entity.setSysid(userId);
        entity.setMandatory(1);
        entity.setPassword(newPass);
        this.update(entity,updateWrapper);
    }

    //查询当前用户
    @Override
    public OmAccountEntity currentUserInformation(String userId) {
        OmAccountEntity entity = this.getById(userId);
        return entity;
    }
    //删除指定用户
    @Override
    public void deleteInfoBySysId(String SysId) {
        omRelAccountRoleService.deleteByUserId(SysId);
        OmAccountEntity entity = new OmAccountEntity();
        entity.setSysid(SysId);
        entity.setDel(1);
        entity.setValid(0);
        this.updateById(entity);
    }
    //重置密码
    @Override
    public void resetPasswords(String SysId) {
        OmAccountEntity entity = new OmAccountEntity();
        OmAccountEntity byId = this.getById(SysId);
        entity.setSysid(SysId);
        entity.setMandatory(0);
        entity.setPassword(byId.getAccountid()+byId.getMobile());
        this.updateById(entity);
    }
    //获取所有用户
    @Override
    public List<OmAccountEntity> getAllUserList() {
        List<OmAccountEntity> list = this.list(new QueryWrapper<OmAccountEntity>().eq("del",0));
        return list;
    }
    //匿名注册
    @Override
    public void anonymRegister(RegisterUser user) {
        OmAccountEntity entity = new OmAccountEntity();
        entity.setPassword("123456");
        entity.setSysid(user.getSysId());
        entity.setAccountid(user.getAccountId());
        entity.setMandatory(0);
        //user.getApplyRemark();
        //user.getCode();
        entity.setMobile(user.getMobile());
        entity.setName(user.getName());
        entity.setSex(user.getSex());
        entity.setWechartcode(user.getWechartCode());
        this.save(entity);
    }
    //修改或添加
    @Override
    public void insertOrUpdateUser(String sysId, String accountId, String name, String sort, String sex, String mobile,
                                   String mobile2, String wechartCode, String valid, String address, String address2,
                                   String roleid, String email, String description, String particid, String removed) {
        if(StringUtils.isEmpty(sysId)||sysId.equals("''")){
            OmAccountEntity entity = new OmAccountEntity();
            entity.setSysid(CreateUuid.uuid());
            entity.setAccountid(accountId.equals("''")?null:accountId);
            entity.setName(name.equals("''")?null:name);
            entity.setSort(sort.equals("''")?null:sort);
            entity.setSex(sex.equals("''")?null:sex);
            entity.setMobile(mobile.equals("''")?null:mobile);
            entity.setMobile2(mobile2.equals("''")?null:mobile2);
            entity.setWechartcode(wechartCode.equals("''")?null:wechartCode);
            entity.setValid(Integer.parseInt(valid));
            entity.setAddress(address.equals("''")?null:address);
            entity.setAddress2(address2.equals("''")?null:address2);
            entity.setEmail(email.equals("''")?null:email);
            entity.setDescription(description.equals("''")?null:description);
            entity.setPassword(accountId+mobile);
            entity.setCreateddate(new Date());
            entity.setRemoved(Integer.parseInt(removed));
            entity.setCompanyId(particid.equals("''")?null:particid);
            entity.setDel(0);
            entity.setMandatory(0);
            this.save(entity);
            if (!StringUtils.isEmpty(roleid)&&!roleid.equals("''")){
                String[] split = roleid.split(",");
                List<OmRelAccountRoleEntity> list = new ArrayList<>();
                for (String str:split) {
                    OmRelAccountRoleEntity entity1 = new OmRelAccountRoleEntity();
                    entity1.setRoleId(str);
                    entity1.setAccountId(entity.getSysid());
                    list.add(entity1);
                }
                omRelAccountRoleService.saveBatch(list);
            }
        }else{
            OmAccountEntity byId = this.getById(sysId);
            OmAccountEntity entity = new OmAccountEntity();
            entity.setSysid(sysId);
            entity.setAccountid(accountId.equals("''")?null:accountId);
            entity.setName(name.equals("''")?null:name);
            entity.setSort(sort.equals("''")?null:sort);
            entity.setSex(sex.equals("''")?null:sex);
            entity.setMobile(mobile.equals("''")?null:mobile);
            entity.setMobile2(mobile2.equals("''")?null:mobile2);
            entity.setWechartcode(wechartCode.equals("''")?null:wechartCode);
            entity.setValid(Integer.parseInt(valid));
            entity.setAddress(address.equals("''")?null:address);
            entity.setAddress2(address2.equals("''")?null:address2);
            entity.setEmail(email.equals("''")?null:email);
            entity.setDescription(description.equals("''")?null:description);
            //entity.setPassword("123456");
            entity.setRemoved(Integer.parseInt(removed));
            if(Integer.parseInt(removed)==0){
                entity.setOpenid(null);
            }else{
                entity.setOpenid(byId.getOpenid());
            }
            entity.setCompanyId(particid.equals("''")?null:particid);
            this.updateById(entity);
            if (!StringUtils.isEmpty(roleid)&&!roleid.equals("''")){
                omRelAccountRoleService.remove(new QueryWrapper<OmRelAccountRoleEntity>().eq("account_id",sysId));
                String[] split = roleid.split(",");
                List<OmRelAccountRoleEntity> list = new ArrayList<>();
                for (String str:split) {
                    OmRelAccountRoleEntity entity1 = new OmRelAccountRoleEntity();
                    entity1.setRoleId(str);
                    entity1.setAccountId(entity.getSysid());
                    list.add(entity1);
                }
                omRelAccountRoleService.saveBatch(list);
            }else{
                log.error("删除角色！！！！！！！！！");
                omRelAccountRoleService.remove(new QueryWrapper<OmRelAccountRoleEntity>().eq("account_id",sysId));
            }
        }
    }
    //获取用户角色
    @Override
    public Map<String,Object> getCurrentUser(String accountId) {
        OmAccountEntity user = this.getById(accountId);
        List<String> allRoleId = omRelAccountRoleService.getAllRoleId(user.getSysid());
        List<OmRoleEntity> allMyRole=null;
        if(allRoleId.size()>1){
            allMyRole = omRoleService.getAllMyRole(allRoleId);
        }else if(allRoleId.size()==1){
            allMyRole = omRoleService.getOneRole(allRoleId.get(0));
        }
        List<Map<String,Object>> list = new ArrayList<>();
        if(allMyRole!=null){
            allMyRole.forEach(li->{
                HashMap<String, Object> map = new HashMap<>();
                map.put("role_id",li.getSysid());
                map.put("name",li.getName());
                list.add(map);
            });
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("sysId",user.getSysid());
        map.put("accountId",user.getAccountid());
        map.put("name",user.getName());
        map.put("sort",user.getSort());
        map.put("mobile",user.getMobile());
        map.put("mobile2",user.getMobile2());
        map.put("wechartCode",user.getWechartcode());
        map.put("valid",user.getValid());
        map.put("address",user.getAddress());
        map.put("address2",user.getAddress2());
        map.put("email",user.getEmail());
        map.put("description",user.getDescription());
        map.put("workTelphone2",user.getWorktelphone2());
        map.put("removed",user.getRemoved());
        map.put("openid",user.getOpenid());
        map.put("mandatory",user.getMandatory());
        map.put("particid",user.getCompanyId());
        map.put("companyname","");
        Map<String, Object> nameAndId = omTagRelPostAccountService.getPostNameAndId(user.getSysid());
        List<Map<String, Object>> list1 = new ArrayList<>();
        if(nameAndId!=null){
            list1.add(nameAndId);
        }
        map.put("obj1",list1);
        map.put("obj2",list);
        return map;
    }
    //获取参建用户
    @Override
    public OmAccountEntity getUser(String userId) {
        return this.getById(userId);
    }
    //根据Id获取所有用户
    @Override
    public List<Map<String, Object>> getAllUserByIds(List<String> userId) {
        List<OmAccountEntity> list=null;
        if(userId.size()>1){
            list = this.list(new QueryWrapper<OmAccountEntity>().in("sysId", userId).eq("del",0));
        }else {
            list = this.list(new QueryWrapper<OmAccountEntity>().eq("sysId", userId.get(0)).eq("del",0));
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(li ->{
            Map<String, Object> map = new HashMap<>();
            map.put("name",li.getName());
            map.put("fullname",li.getAccountid());
            map.put("description",li.getDescription());
            maps.add(map);
        });
        return maps;
    }
    //根据条件和Id获取所有用户
    @Override
    public List<OmAccountEntity> getAllUserBySearch(List<String> userId, String search) {
        if(userId.size()>1){
            return this.list(new QueryWrapper<OmAccountEntity>().in("sysId", userId).eq("del",0).like("name",search).or().like("accountId",search));
        }else {
            return this.list(new QueryWrapper<OmAccountEntity>().eq("sysId", userId.get(0)).eq("del",0).like("name",search).or().like("accountId",search));
        }
    }
    //不根据条件，仅根据Id获取所有用户
    @Override
    public List<OmAccountEntity> getAllUserNotBySearch(List<String> userId) {
        if(userId.size()>1){
            return this.list(new QueryWrapper<OmAccountEntity>().in("sysId", userId).eq("del",0));
        }else {
            return this.list(new QueryWrapper<OmAccountEntity>().eq("sysId", userId.get(0)).eq("del",0));
        }
    }
    //获取用户所拥有的的角色名称
    @Override
    public String getRoleName(String sysid) {
        List<String> allRoleId = omRelAccountRoleService.getAllRoleId(sysid);
        List<OmRoleEntity> allMyRole=null;
        if(allRoleId.size()>1){
            allMyRole = omRoleService.getAllMyRole(allRoleId);
        }else if(allRoleId.size()==1){
            allMyRole = omRoleService.getOneRole(allRoleId.get(0));
        }
        List<String> list = new ArrayList<>();
        if(allMyRole!=null){
            allMyRole.forEach(li->{
                list.add(li.getName());
            });
            String roleName=null;
            for(int i=0;i<list.size();i++){
                if(i==0){
                    roleName=list.get(i);
                }else{
                    roleName+=","+list.get(i);
                }
            }
            return roleName;
        }else{
            return null;
        }
    }
    //获取用户所在单位
    @Override
    public String getCompanyName(String commonid) {
        ProjectCompanyEntity entity=projectCompanyService.getCompany(commonid);
        if(entity!=null){
            return entity.getName();
        }
        return null;
    }
    //获取用户所在岗位
    @Override
    public String getPostName(String sysid) {
        String postName=omTagRelPostAccountService.getPostName(sysid);
        return postName;
    }
    //获取单位下所有人员
    @Override
    public List<OmAccountEntity> getAllUserByCompanyId(String companyId) {
        return this.list(new QueryWrapper<OmAccountEntity>().eq("del",0).eq("company_id",companyId));
    }
    //分页获取单位下所有人员
    @Override
    public PageUtils getPageByCompanyId(Map<String, Object> map) {
        String search = (String) map.get("search");
        String id = (String) map.get("id");
        QueryWrapper<OmAccountEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id",id).eq("del",0);
        if(search!=null){
            wrapper.and((wr) ->{
                wr.like("accountId",search).or().like("name",search);
            });
        }
        IPage<OmAccountEntity> page = this.page(new Query<OmAccountEntity>().getPage(map), wrapper);
        return new PageUtils(page);
    }
    //获取在此id集合范围内符合条件的用户
    @Override
    public PageUtils getBySearch(Map<String, Object> map, List<String> userId, String companyId) {
        String search = (String) map.get("search");
        QueryWrapper<OmAccountEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id",companyId);
        if(search!=null){
                wrapper.in("sysId",userId).like("accountId",search).or().like("name",search);
        }else {
            wrapper.in("sysId", userId);
        }
        IPage<OmAccountEntity> page = this.page(new Query<OmAccountEntity>().getPage(map), wrapper);
        return new PageUtils(page);
    }
    //去除某些用户
    @Override
    public PageUtils getPageByCompanyIdDis(Map<String, Object> map, List<String> notIn) {
        String search = (String) map.get("search");
        String id = (String) map.get("id");
        QueryWrapper<OmAccountEntity> wrapper = new QueryWrapper<>();
        wrapper.notIn("sysId",notIn).eq("company_id",id).eq("del",0);
        if(search!=null){
            wrapper.and((wr) ->{
                wr.like("accountId",search).or().like("name",search);
            });
        }
        IPage<OmAccountEntity> page = this.page(new Query<OmAccountEntity>().getPage(map), wrapper);
        return new PageUtils(page);
    }
    //获取所有二级管理员用户
    @Override
    public List<Map<String, Object>> getTwoUser(List<String> userId) {
        List<OmAccountEntity> list = this.list(new QueryWrapper<OmAccountEntity>().in("sysId", userId));
        List<Map<String, Object>> data = new ArrayList<>();
        list.forEach(li ->{
            HashMap<String, Object> map = new HashMap<>();
            map.put("sysId",li.getSysid());
            map.put("accountId",li.getAccountid());
            map.put("empId",li.getEmpid());
            map.put("expire",li.getExpire());
            map.put("human_id",li.getHumanId());
            map.put("inact",li.getInact());
            map.put("lastchg",li.getLastchg());
            map.put("max",li.getMax());
            map.put("min",li.getMin());
            map.put("password",li.getPassword());
            map.put("removed",li.getRemoved());
            map.put("valid",li.getValid());
            map.put("warn",li.getWarn());
            map.put("accountType",li.getAccounttype());
            map.put("email",li.getEmail());
            map.put("msn",li.getMsn());
            map.put("wechartCode",li.getWechartcode());
            map.put("address",li.getAddress());
            map.put("address2",li.getAddress2());
            map.put("postalcode",li.getPostalcode());
            map.put("postalcode2",li.getPostalcode2());
            map.put("workTelphone",li.getWorktelphone());
            map.put("workTelphone2",li.getWorktelphone2());
            map.put("mobile",li.getMobile());
            map.put("mobile2",li.getMobile2());
            map.put("faxphone",li.getFaxphone());
            map.put("faxphone2",li.getFaxphone2());
            map.put("pinYin",li.getPinyin());
            map.put("del",li.getDel());
            map.put("logonDate",li.getLogondate());
            map.put("logonTimes",li.getLogontimes());
            map.put("enterPriseId",li.getEnterpriseid());
            map.put("mobileValid",li.getMobilevalid());
            map.put("mobileConfirmCode",li.getMobileconfirmcode());
            map.put("synchroId",li.getSynchroid());
            map.put("createdDate",li.getCreateddate());
            map.put("description",li.getDescription());
            map.put("name",li.getName());
            map.put("keyId",li.getKeyid());
            map.put("sex",li.getSex());
            map.put("position",li.getPosition());
            map.put("duty",li.getDuty());
            map.put("identityCard",li.getIdentitycard());
            map.put("edBackground",li.getEdbackground());
            map.put("officeHolder",li.getOfficeholder());
            map.put("qualificationCert",li.getQualificationcert());
            map.put("postCert",li.getPostcert());
            map.put("photo",li.getPhoto());
            map.put("file1",li.getFile1());
            map.put("file2",li.getFile2());
            map.put("file3",li.getFile3());
            map.put("file4",li.getFile4());
            map.put("fileName1",li.getFilename1());
            map.put("fileName2",li.getFilename2());
            map.put("fileName3",li.getFilename3());
            map.put("fileName4",li.getFilename4());
            map.put("file5",li.getFile5());
            map.put("fileName5",li.getFilename5());
            map.put("updateContentDate",li.getUpdatecontentdate());
            map.put("dynamicPassword",li.getDynamicpassword());
            map.put("dynamicPwdDate",li.getDynamicpwddate());
            map.put("sort",li.getSort());
            map.put("emailaccount",li.getEmailaccount());
            map.put("taskCount",li.getTaskcount());
            map.put("wechartUserId",li.getWechartuserid());
            map.put("fixedTelephone",li.getFixedtelephone());
            map.put("officeAddress",li.getOfficeaddress());
            map.put("officeRoomNo",li.getOfficeroomno());
            map.put("conEnterprise",li.getConenterprise());
            map.put("wechartMobile",li.getWechartmobile());
            map.put("wechartEmail",li.getWechartemail());
            map.put("recMsgType",li.getRecmsgtype());
            map.put("accountState",li.getAccountstate());
            map.put("openid",li.getOpenid());
            map.put("company_id",li.getCompanyId());
            map.put("unionid",li.getUnionid());
            data.add(map);
        });
        return data;
    }
    //accountId
    @Override
    public boolean checkAccountId(String accountId) {
        List<OmAccountEntity> list = this.list(new QueryWrapper<OmAccountEntity>().eq("accountId", accountId).eq("del", 0));
        if(list.size()>0) {
            return false;
        }
        return true;
    }
    //根据openId获取用户
    @Override
    public OmAccountEntity getByOpenId(String openId) {
        List<OmAccountEntity> list = this.list(new QueryWrapper<OmAccountEntity>().eq("del", 0).eq("openid", openId));
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

}