package com.blog.template.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 19624
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * 手机号或用户名或邮件
     */
    @NotBlank(message = "登陆账号不能为空")
    private String usernameOrEmail;


    @NotBlank(message = "密码不能为空")
    private String password;
}
