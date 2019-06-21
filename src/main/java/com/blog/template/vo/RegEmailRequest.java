package com.blog.template.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author 19624
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegEmailRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入正确邮箱地址")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
