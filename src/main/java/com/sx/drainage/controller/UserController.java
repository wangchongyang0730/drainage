package com.sx.drainage.controller;

import com.sx.drainage.common.JwtUtil;
import com.sx.drainage.common.PageUtils;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.params.RegisterUser;
import com.sx.drainage.params.User;
import com.sx.drainage.service.OmAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 11:25
 */
@Slf4j
@Api(value = "/api/User", description = "用户管理")
@RequestMapping("/api/User")
@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private OmAccountService omAccountService; //用户
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/Login")
    @ApiOperation(value = "用户登录", notes = "调用方法传递对象过来")
    public R login(@RequestBody User user) {
        OmAccountEntity users = omAccountService.login(user.getUserName(), user.getPassword());
        if (users == null) {
            return R.error(405, "用户名或密码错误!");
        }
        if(users.getValid()==0){
            return R.error(405, "用户已停用，请联系管理员!");
        }
        String roleName = omAccountService.getRoleName(users.getSysid());
        if(roleName==null){
            return R.error(405, "暂无权限，拒绝访问!");
        }
        String token = jwtUtil.createJWT(users.getSysid(), users.getAccountid(), users.getPassword());
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("backUrl", null);
        return R.ok(1, "登录成功!", map, true, null);
    }

    @GetMapping("/SignOut")
    @ApiOperation(value = "登出")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R signOut() {
        return R.ok(1, "登出成功", null, true, null);
    }

    @PostMapping("/ModifyCurrentPassword")
    @ApiOperation(value = "修改当前用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "oldPass", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPass", value = "新密码", required = true, dataType = "String")
    })
    public R modifyCurrentPassword(@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        OmAccountEntity entity = omAccountService.currentUserInformation(userId);
        if (!entity.getPassword().equals(oldPass)) {
            return R.error(405, "原密码错误!");
        }
        omAccountService.modifyCurrentPassword(userId, newPass);
        return R.ok(1, "修改成功!", null, true, null);
    }

    @DeleteMapping("/deleteInfoBySysId")
    @ApiOperation(value = "删除指定用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "用户Id", required = true, dataType = "String"),
    })
    public R deleteInfoBySysId(@RequestParam("sysId") String sysId) {
        omAccountService.deleteInfoBySysId(sysId);
        return R.ok(1, "删除成功!", null, true, null);
    }

    @PostMapping("/resetPasswords")
    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "用户Id", required = true, dataType = "String"),
    })
    public R resetPasswords(@RequestParam("sysId") String SysId) {
        omAccountService.resetPasswords(SysId);
        return R.ok(1, "重置成功!", null, true, null);
    }

    @GetMapping("/GetAllUserList")
    @ApiOperation(value = "获取所有用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getAllUserList() {
        List<OmAccountEntity> list = omAccountService.getAllUserList();
        return R.ok(1, "获取成功!", list, true, null);
    }

    @PostMapping("/AnonymRegister")
    @ApiOperation(value = "匿名注册", notes = "调用方法传递对象过来")
    public R anonymRegister(@RequestBody RegisterUser user) {
        omAccountService.anonymRegister(user);
        return R.ok(1, "注册成功!", null, true, null);
    }

    @PostMapping("/InsertOrUpdateUser")
    @ApiOperation(value = "新增、更新用户信息保存")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sysId", value = "系统编码为空时新增，有编码时为修改", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "accountId", value = "用户账号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "序号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mobile", value = "手机号1", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mobile2", value = "手机号2", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "wechartCode", value = "微信号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "valid", value = "是否有效", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "通讯地址1", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address2", value = "通讯地址1", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "roleid", value = "角色编码id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "email", value = "Email", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "描述", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "particid", value = "单位id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "removed", value = "微信是否绑定", required = true, dataType = "String")
    })
    public R insertOrUpdateUser(@RequestParam(value = "sysId", defaultValue = "",required = false) String sysId, @RequestParam("accountId") String accountId, @RequestParam("name") String name, @RequestParam("sort") String sort, @RequestParam("sex") String sex,
                                @RequestParam("mobile") String mobile, @RequestParam("mobile2") String mobile2, @RequestParam("wechartCode") String wechartCode, @RequestParam("valid") String valid, @RequestParam("address") String address,
                                @RequestParam("address2") String address2, @RequestParam("roleid") String roleid, @RequestParam("email") String email,
                                @RequestParam("description") String description, @RequestParam("particid") String particid, @RequestParam("removed") String removed) {
        if(!sysId.equals("''")&&!StringUtils.isEmpty(sysId)){
            omAccountService.insertOrUpdateUser(sysId, accountId, name, sort, sex, mobile, mobile2, wechartCode, valid, address, address2, roleid, email, description, particid, removed);
            return R.ok(1, "修改成功!", null, true, null);
        }
        boolean check = omAccountService.checkAccountId(accountId);
        if(check) {
            omAccountService.insertOrUpdateUser(sysId, accountId, name, sort, sex, mobile, mobile2, wechartCode, valid, address, address2, roleid, email, description, particid, removed);
            return R.ok(1, "添加成功!", null, true, null);
        }
        return R.error(405,"账户已存在!");
    }

    @GetMapping("/GetCurrentUser")
    @ApiOperation(value = "获取当前用户信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String")
    public R getCurrentUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Map<String, Object> user = omAccountService.getCurrentUser(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(user);
        return R.ok(1, "获取成功!", list, true, null);
    }

    @GetMapping("/Get/{id}")
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户SysId", required = true, dataType = "String")
    })
    public R getOneUser(@PathVariable("id") String id) {
        Map<String, Object> user = omAccountService.getCurrentUser(id);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(user);
        return R.ok(1, "获取成功!", list, true, null);
    }

    @GetMapping("/GetAllUserPageList")
    @ApiOperation(value = "获取用户账户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageRecord", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "", required = false, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "companyId", value = "", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "searchVal", value = "查询条件", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "operateUserId", value = "为空", required = false, dataType = "String")
    })
    public R getAllUserPageList(@RequestParam(value = "pageRecord", required = false, defaultValue = "10") Integer pageRecord, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "searchVal", required = false, defaultValue = "") String searchVal,
                                @RequestParam(value = "operateUserId", required = false, defaultValue = "") String operateUserId,@RequestParam(value = "companyId",required = false) String companyId) {
        log.error("查询角色进来了-------------");
        Map<String, Object> map = new HashMap<>();
        if (searchVal.equals("%22%22")||searchVal.equals("''") || StringUtils.isEmpty(searchVal)) {
            map.put("search", null);
        } else {
            map.put("search", searchVal);
        }
        map.put("operateUserId", null);
        map.put("page", page.toString());
        map.put("limit", pageRecord.toString());
        Map<String, Object> maps = new HashMap<>();
        log.error("封装条件---------------");
        PageUtils pageUtils;
        if(StringUtils.isEmpty(companyId)) {
            log.error("封装条件，单位id为空------------");
            pageUtils = omAccountService.queryPage(map);
        }else{
            map.put("id",companyId);
            log.error("封装条件，单位id不为空------------");
            pageUtils = omAccountService.getPageByCompanyId(map);
        }
        List<OmAccountEntity> list = (List<OmAccountEntity>) pageUtils.getList();
        List<Map<String, Object>> data=new ArrayList<>();
        log.error("进行循环---------------------");
        list.forEach(li->{
            String roleName=omAccountService.getRoleName(li.getSysid());
            String companyName=omAccountService.getCompanyName(li.getCompanyId());
            String postName=omAccountService.getPostName(li.getSysid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("postName",postName);
            hashMap.put("roleName",roleName);
            hashMap.put("companyName",companyName);
            hashMap.put("sysId",li.getSysid());
            hashMap.put("accountId",li.getAccountid());
            hashMap.put("empId",li.getEmpid());
            hashMap.put("expire",li.getExpire());
            hashMap.put("human_id",li.getHumanId());
            hashMap.put("inact",li.getInact());
            hashMap.put("lastchg",li.getLastchg());
            hashMap.put("max",li.getMax());
            hashMap.put("min",li.getMin());
            hashMap.put("password",li.getPassword());
            hashMap.put("removed",li.getRemoved());
            hashMap.put("valid",li.getValid());
            hashMap.put("warn",li.getWarn());
            hashMap.put("accountType",li.getAccounttype());
            hashMap.put("email",li.getEmail());
            hashMap.put("msn",li.getMsn());
            hashMap.put("wechartCode",li.getWechartcode());
            hashMap.put("address",li.getAddress());
            hashMap.put("address2",li.getAddress2());
            hashMap.put("postalcode",li.getPostalcode());
            hashMap.put("postalcode2",li.getPostalcode2());
            hashMap.put("workTelphone",li.getWorktelphone());
            hashMap.put("workTelphone2",li.getWorktelphone2());
            hashMap.put("mobile",li.getMobile());
            hashMap.put("mobile2",li.getMobile2());
            hashMap.put("faxphone",li.getFaxphone());
            hashMap.put("faxphone2",li.getFaxphone2());
            hashMap.put("pinYin",li.getPinyin());
            hashMap.put("del",li.getDel());
            hashMap.put("logonDate",li.getLogondate());
            hashMap.put("logonTimes",li.getLogontimes());
            hashMap.put("enterPriseId",li.getEnterpriseid());
            hashMap.put("mobileValid",li.getMobilevalid());
            hashMap.put("mobileConfirmCode",li.getMobileconfirmcode());
            hashMap.put("synchroId",li.getSynchroid());
            hashMap.put("createdDate",li.getCreateddate());
            hashMap.put("description",li.getDescription());
            hashMap.put("name",li.getName());
            hashMap.put("keyId",li.getKeyid());
            hashMap.put("sex",li.getSex());
            hashMap.put("position",li.getPosition());
            hashMap.put("duty",li.getDuty());
            hashMap.put("identityCard",li.getIdentitycard());
            hashMap.put("edBackground",li.getEdbackground());
            hashMap.put("officeHolder",li.getOfficeholder());
            hashMap.put("qualificationCert",li.getQualificationcert());
            hashMap.put("postCert",li.getPostcert());
            hashMap.put("photo",li.getPhoto());
            hashMap.put("file1",li.getFile1());
            hashMap.put("file2",li.getFile2());
            hashMap.put("file3",li.getFile3());
            hashMap.put("file4",li.getFile4());
            hashMap.put("file5",li.getFile5());
            hashMap.put("fileName1",li.getFilename1());
            hashMap.put("fileName2",li.getFilename2());
            hashMap.put("fileName3",li.getFilename3());
            hashMap.put("fileName4",li.getFilename4());
            hashMap.put("fileName5",li.getFilename5());
            hashMap.put("updateContentDate",li.getUpdatecontentdate());
            hashMap.put("dynamicPassword",li.getDynamicpassword());
            hashMap.put("dynamicPwdDate",li.getDynamicpwddate());
            hashMap.put("sort",li.getSort());
            hashMap.put("emailaccount",li.getEmailaccount());
            hashMap.put("taskCount",li.getTaskcount());
            hashMap.put("wechartUserId",li.getWechartuserid());
            hashMap.put("fixedTelephone",li.getFixedtelephone());
            hashMap.put("officeAddress",li.getOfficeaddress());
            hashMap.put("officeRoomNo",li.getOfficeroomno());
            hashMap.put("conEnterprise",li.getConenterprise());
            hashMap.put("wechartMobile",li.getWechartmobile());
            hashMap.put("wechartEmail",li.getWechartemail());
            hashMap.put("recMsgType",li.getRecmsgtype());
            hashMap.put("accountState",li.getAccountstate());
            hashMap.put("openid",li.getOpenid());
            hashMap.put("company_id",li.getCompanyId());
            hashMap.put("unionid",li.getUnionid());
            data.add(hashMap);
        });
        maps.put("rows",data);
        maps.put("total",pageUtils.getTotalCount());
        return R.ok(1, "获取成功!", maps, true, null);
    }
}
