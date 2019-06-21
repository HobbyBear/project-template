package com.blog.template.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 * @author xch
 * @since 2019/6/12 9:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerException extends RuntimeException {

    private Integer code;

    private String msg;

    private Object data;

    public CustomerException(Integer code, String msg ){
        super(msg);
        this.code = code;
    }

    public CustomerException(String msg){
        super(msg);
        code = 400;
    }
    public CustomerException(Integer code,String msg,Object data){
        super(msg);
        this.code = code;
        this.data = data;
    }
}
