package com.sx.drainage.common;

import java.util.UUID;

//UUID生成器
public class CreateUuid {
    public static String uuid(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
