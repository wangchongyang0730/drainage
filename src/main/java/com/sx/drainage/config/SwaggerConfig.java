package com.sx.drainage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 11:20
 * swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())        //这个方法的作用（生成接口的时候页面显示的信息）
                .select()         //表示的是选择那些路径和API生成文档
                .apis(RequestHandlerSelectors.basePackage("com.sx.drainage.controller"))           //告诉他要扫描的接口存在的这个包
                .paths(PathSelectors.any())          //对所有的API进行监控
                .build();         //构建
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("平台API")    //文档的标题
                .description("平台API")      //文档的描述
                .version("v1.0")                          //版本
                .build();
        // .contact("QianMo")                        //作者
    }
}
