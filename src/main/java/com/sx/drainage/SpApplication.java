package com.sx.drainage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 11:17
 */
/*
* */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SpApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpApplication.class,args);
    }
}
