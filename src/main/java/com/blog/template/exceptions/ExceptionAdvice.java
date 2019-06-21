package com.blog.template.exceptions;

import com.blog.template.vo.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author xch
 * @since 2019/6/12 8:59
 **/
@RestControllerAdvice
public class ExceptionAdvice {


    /**
     * 捕捉所有方法上校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseMsg handle(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer();
        e.getConstraintViolations().forEach(error ->{
            sb.append(error.getMessage()).append(";");
        });
        return ResponseMsg.fail400(sb.toString());
    }


    /**
     * 捕捉所有方法上校验异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMsg handle(MethodArgumentNotValidException e) {
        StringBuffer sb = new StringBuffer();
        e.getBindingResult().getAllErrors().forEach(error ->{
            sb.append(error.getDefaultMessage()).append(";");
        });
        return ResponseMsg.fail400(sb.toString());
    }

    /**
     * 权限不足异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseMsg handle403(AccessDeniedException e){
        return ResponseMsg.fail403();
    }

    /**
     * 登陆账号不存在异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseMsg handle400(UsernameNotFoundException e){
        return ResponseMsg.fail400(e.getMessage());
    }

    /**
     * @Description: 登陆密码错误异常
     * @Author: xch
     * @Date: 2019/6/19 10:26
    */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseMsg handle400(BadCredentialsException e){return ResponseMsg.fail400("登陆密码错误");}

    /**
     * 处理自定义异常
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerException.class)
    public ResponseMsg handel400(CustomerException e){
        return ResponseMsg.customer(e.getCode(),e.getMsg(),e.getData());
    }

    /**
     * 处理系统异常
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseMsg handel500(Exception e){
        e.printStackTrace();
        return ResponseMsg.fail500();
    }
}
