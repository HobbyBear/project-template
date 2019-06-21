package com.blog.template.models.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: xch
 * @create: 2019-06-21 10:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchVo {

    private String username;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
