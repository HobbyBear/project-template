package com.blog.template.service;

import com.blog.template.BaseTest;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.PageBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * @author: xch
 * @create: 2019-06-21 09:02
 **/
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;


    @Test
    public void findUserVoByUsernameLike(){
        PageBean pageBean = userService.findUserVoByUserSearchVo(new UserSearchVo("1",
                LocalDateTime.of(2019,Month.MAY,10,10,0,0),
                LocalDateTime.of(2019,Month.AUGUST,10,10,0,0)),1,2);

        System.out.println(pageBean.getContent());
    }
}
