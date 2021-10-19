package com.sx.drainage.interceptor;

import com.sx.drainage.common.ContrastException;
import com.sx.drainage.common.JwtUtil;
import com.sx.drainage.common.NullTokenException;
import com.sx.drainage.entity.OmAccountEntity;
import com.sx.drainage.service.OmAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
* 请求前统一拦截，验证token
* */
@Slf4j
@Component //放到容器中
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OmAccountService omAccountService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.info("用户token----------"+token);
        if(token==null||token==""){
            throw new NullTokenException();
        }else{
            String s = request.getRequestURI();
            log.debug("这是请求路径--------"+s);
            String userId = (String) jwtUtil.parseJWT(token).get("jti");
            request.setAttribute("userId",userId);
            OmAccountEntity user = omAccountService.getUser(userId);
            String newPass = null;
            if(user!=null){
                newPass=user.getPassword();
            }
            if(newPass!=null){
                String password = (String) jwtUtil.parseJWT(token).get("password");
                if(!newPass.equals(password)){
                    throw new ContrastException();
                }else{
                    request.setAttribute("userId",userId);
                }
            }else{
                request.setAttribute("userId",userId);
            }
        }
        return true;
    }
}
