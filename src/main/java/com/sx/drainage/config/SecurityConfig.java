//package com.sx.drainage.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// * User: QianMo
// * Date: 2020/10/28
// * Time: 11:37
// */
//@Slf4j
//@Component
//public class SecurityConfig implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        List<GrantedAuthority> list = new ArrayList<>();
//        GrantedAuthority authority=
//                new SimpleGrantedAuthority("admin");
//        list.add(authority);
//        log.error("what?"+s);
//        User user = new User(s,s,list);
//        return user;
//    }
//}
