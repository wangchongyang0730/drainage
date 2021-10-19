//package com.sx.drainage.config;
//
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * Created with IntelliJ IDEA.
// * User: QianMo
// * Date: 2020/10/28
// * Time: 10:08
// */
///*
//* 屏蔽Security
//* */
//@EnableWebSecurity
//public class ActivitiConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //禁用csrf 和关闭权限验证
//        http.csrf().disable().authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }
//}
