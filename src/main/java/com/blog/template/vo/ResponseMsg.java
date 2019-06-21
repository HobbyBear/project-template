package com.blog.template.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 19624
 */
@Data
@NoArgsConstructor
public class ResponseMsg<T> {
    /**
     * 400 失败
     * 200成功
     * 500内部系统错误
     */
    private Integer code;

    private String msg;


    private T data;


    private ResponseMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseMsg(Integer code, String msg, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }


    public static ResponseMsg success200(String msg) {
        return new ResponseMsg(200, msg);
    }

    public static <T> ResponseMsg<T> success200(T data) {
        return new ResponseMsg<>(200, "请求成功", data);
    }

    public static <T> ResponseMsg<T> success200(String msg, T data) {
        return new ResponseMsg<>(200, msg, data);
    }

    public static ResponseMsg fail500(){
        return new ResponseMsg(500,"内部系统错误");
    }

    public static ResponseMsg fail403(){
        return new ResponseMsg(403,"权限不足");
    }

    public static ResponseMsg fail400(String msg) {
        return new ResponseMsg(400, msg);
    }

    public static <T> ResponseMsg<T> fail400(T data) {
        return new ResponseMsg<>(400, "请求失败", data);
    }

    public static <T> ResponseMsg<T> fail400(String msg, T data) {
        return new ResponseMsg<T>(400, msg, data);
    }

    public static <T>ResponseMsg<T> customer(Integer code,String msg,T data){
        return new ResponseMsg<>(code,msg,data);
    }

}
