package com.sx.drainage.execption;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.sx.drainage.common.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/26
 * Time: 15:21
 * 全局异常信息处理
 */
@Slf4j
@RestControllerAdvice
public class JwtExecption {
    //拦截由过期tocken导致的异常
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<R> jwtTimeOut(){
        return new ResponseEntity<R>(R.ok(401,"tocken已过期!",null,true,null), HttpStatus.UNAUTHORIZED);
    }
    //拦截非法tocken
    @ExceptionHandler(value = MalformedJwtException.class)
    public ResponseEntity<R> jwtIllegal(){
        return new ResponseEntity<R>(R.error(102,"非法tocken!"), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = NullTokenException.class)
    public ResponseEntity<R> jwtNull(){
        return new ResponseEntity<R>( R.error(103,"请携带token验证!"), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = ContrastException.class)
    public R password(){
        return R.error(104,"密码已修改，请重新登录!");
    }
    @ExceptionHandler(value = SQLServerException.class)
    public R sqlExecption(Exception e){
        String message = e.getMessage();
        if(message.contains("FK")){
            return R.error(105,"删除失败，请解除关联后重试!");
        }
        return R.error(105,message);
    }
    @ExceptionHandler(value = MonthlyException.class)
    public R monthlyException(){
        return R.error(400,"请先完成月度考评!");
    }
    @ExceptionHandler(value = QuarterlyException.class)
    public R quarterlyException(){
        return R.error(400,"请先完成季度考评!");
    }
    //NestedServletException.class,IllegalArgumentException.class
//    @ExceptionHandler(value = {Exception.class})
//    public R jwtCannot(Exception e){
//        log.error("异常信息--------------------------------");
//        log.error(e.getMessage());
//        log.error(e.getLocalizedMessage());
//        log.error(e.toString());
//        log.error("异常结束--------------------------------");
//        return R.error(106,"系统异常，请稍联系管理员!");
//    }
}
