package com.sx.drainage.controller;

import com.sx.drainage.common.JwtUtil;
import com.sx.drainage.common.R;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.service.OmAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/1/4
 * Time: 15:56
 */
@Api(value = "/api/thirdParty", description = "企业微信 *")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thirdParty")
public class ThirdPartyController {
    private final RestTemplate restTemplate;
    private final Environment env;
    private final JwtUtil jwtUtil;
    private final OmAccountService omAccountService;

    @GetMapping("/bindToWeChat")
    @ApiOperation(value = "绑定微信 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "code", value = "通过成员授权获取到的code", required = true, dataType = "String")
    })
    public R bindToWeChat(@RequestParam("code") String code, HttpServletRequest request){
        Map<String, Object> map = getToken();
        if(map.get("errmsg").equals("ok")){
            Object access_token = map.get("access_token");
            String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+access_token+"&code="+code;
            Map data = restTemplate.getForObject(url, Map.class);
            if(data.get("errmsg").equals("ok")){
                String openId = data.get("UserId").toString();
                OmAccountEntity byOpenId = omAccountService.getByOpenId(openId);
                if(byOpenId!=null){
                    return R.error(500,"此微信已绑定用户!");
                }
                String userId = (String) request.getAttribute("userId");
                OmAccountEntity entity = new OmAccountEntity();
                entity.setSysid(userId);
                entity.setOpenid(openId);
                entity.setRemoved(1);
                omAccountService.updateById(entity);
                return R.ok(1,"绑定成功!",null,true,null);
            }
            return R.error(500,"获取用户信息失败!");
        }
        return R.error(500,"获取授权码错误!");
    }
    @GetMapping("/Login")
    @ApiOperation(value = "使用微信账号登录 *")
    @ApiImplicitParam(paramType = "query", name = "code", value = "通过成员授权获取到的code", required = true, dataType = "String")
    public R login(@RequestParam("code") String code){
        Map<String, Object> map = getToken();
        if(map.get("errmsg").equals("ok")){
            Object access_token = map.get("access_token");
            String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+access_token+"&code="+code;
            Map data = restTemplate.getForObject(url, Map.class);
            if(data.get("errmsg").equals("ok")){
                String openId = data.get("UserId").toString();
                OmAccountEntity users = omAccountService.getByOpenId(openId);
                if(users!=null){
                    String token = jwtUtil.createJWT(users.getSysid(), users.getAccountid(), users.getPassword());
                    HashMap<String, Object> res = new HashMap<>();
                    res.put("token", token);
                    res.put("backUrl", null);
                    return R.ok(1,"登录成功!",res,true,null);
                }
                return R.error(500,"用户不存在!");
            }
            return R.error(500,"获取用户信息失败!");
        }
        return R.error(500,"获取授权码错误!");
    }

    /*
    * 获取微信授权码
    * */
    private Map<String,Object> getToken(){
        String corpId = env.getProperty("weixin.corpId");
        String secret = env.getProperty("weixin.secret");
        String url="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpId+"&corpsecret="+secret;
        return restTemplate.getForObject(url, Map.class);
    }
}
