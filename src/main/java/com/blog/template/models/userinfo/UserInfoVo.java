package com.blog.template.models.userinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: xch
 * @create: 2019-06-21 08:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoVo {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    /**
     * 1,激活  0 -未激活
     */
    private Integer status;

    /**
     * 头像地址
     */
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 第三方应用类型
     */
    private String source;

    private List<String> roles;
    /**
     * 是否支持改变角色,第三方登陆的用户只能是用户角色，不能成为管理员
     */
    private boolean supportChangeRole;

}
