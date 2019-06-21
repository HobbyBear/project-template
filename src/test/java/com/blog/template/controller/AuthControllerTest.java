package com.blog.template.controller;

import cn.hutool.json.JSONUtil;
import com.blog.template.ControllerBaseTest;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.LoginRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AuthControllerTest extends ControllerBaseTest {


    @Test
    public void register() throws Exception {
        UserInfo userInfo = UserInfo.builder()
                .email("test")
                .nickname("test")
                .password("123")
                .username("test")
                .build();
       String res =  mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(JSONUtil.toJsonStr(userInfo))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(res);
    }


    @Test
    public void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test","123");
        String res =
                mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(JSONUtil.toJsonStr(loginRequest))
                ).andDo(print())
                        .andExpect(authenticated()).andReturn().getResponse().getContentAsString();
        System.out.println(res);
    }

}
