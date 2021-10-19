package com.sx.drainage.config;

import com.sx.drainage.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class JwtInterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //拦截器要声明拦截器对象和要拦截的请求
        //addPathPatterns拦截的路径
        //excludePathPatterns放行的路径
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/User/Login")
                .excludePathPatterns("/api/thirdParty/Login")
                .excludePathPatterns("/api/User/AnonymRegister")
                .excludePathPatterns("/api/WBSManage/GetDeriveWBSData")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
                .excludePathPatterns("/upload/**");//文件读取路径
    }
    //放行swagger文档接口
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/upload/**")//文件读取路径
//                .addResourceLocations("file:G://shixi//idea//upload//");//文件读取路径 本地
//                .addResourceLocations("file:D://web//BIM//interface//upload//");//文件读取路径 测试机
                .addResourceLocations("file:E://BIM//upload//");//文件读取路径 bimm
        super.addResourceHandlers(registry);
    }
}
