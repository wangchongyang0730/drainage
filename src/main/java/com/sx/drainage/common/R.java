package com.sx.drainage.common;


import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 14:14
 * 返回结果类
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 1);
        put("msg", "success");
    }

    public static R error() {
        return error(301, "未知异常，请联系管理员");
    }

    public static R error(Integer code,String msg) {
        return error(code, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(Integer code,String msg,Object data,Boolean success,String extra) {
        R r = new R();
        r.put("code",code);
        r.put("success",success);
        r.put("data",data);
        r.put("msg", msg);
        r.put("extra",extra);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    public  Integer getCode() {

        return (Integer) this.get("code");
    }

}
