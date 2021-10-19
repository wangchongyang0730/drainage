package com.sx.drainage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.params.RegisterUser;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 *
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 11:57:58
 */
public interface OmAccountService extends IService<OmAccountEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //用户登录
    OmAccountEntity login(String username,String password);
    //修改当前用户密码
    void modifyCurrentPassword(String userId,String newPass);
    //查询当前用户
    OmAccountEntity currentUserInformation(String userId);
    //删除指定用户
    void deleteInfoBySysId(String SysId);
    //重置密码
    void resetPasswords(String SysId);
    //获取所有用户
    List<OmAccountEntity> getAllUserList();
    //匿名注册
    void anonymRegister(RegisterUser user);
    //修改或添加
    void insertOrUpdateUser(String sysId, String accountId, String name, String sort, String sex, String mobile, String mobile2, String wechartCode, String valid, String address, String address2, String roleid, String email, String description, String particid, String removed);
    //获取用户角色
    Map<String,Object> getCurrentUser(String accountId);
    //获取参建用户
    OmAccountEntity getUser(String userId);
    //根据Id获取所有用户
    List<Map<String, Object>> getAllUserByIds(List<String> userId);
    //根据条件和Id获取所有用户
    List<OmAccountEntity> getAllUserBySearch(List<String> userId,String search);
    //不根据条件，仅根据Id获取所有用户
    List<OmAccountEntity> getAllUserNotBySearch(List<String> userId);
    //获取用户所拥有的的角色名称
    String getRoleName(String sysid);
    //获取用户所在单位
    String getCompanyName(String commonid);
    //获取用户所在岗位
    String getPostName(String sysid);
    //获取单位下所有人员
    List<OmAccountEntity> getAllUserByCompanyId(String companyId);
    //分页获取单位下所有人员
    PageUtils getPageByCompanyId(Map<String, Object> map);
    //获取在此id集合范围内符合条件的用户
    PageUtils getBySearch(Map<String, Object> map, List<String> userId, String companyId);
    //去除某些用户
    PageUtils getPageByCompanyIdDis(Map<String, Object> maps, List<String> notIn);
    //获取所有二级管理员用户
    List<Map<String, Object>> getTwoUser(List<String> userId);
    //判断是否存在
    boolean checkAccountId(String accountId);
    //根据openId获取用户
    OmAccountEntity getByOpenId(String openId);
}

