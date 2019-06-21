package com.blog.template.controller.front;

import com.blog.template.service.user.UserService;
import com.blog.template.vo.RegEmailRequest;
import com.blog.template.vo.ResponseMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: xch
 * @create: 2019-06-15 22:49
 **/
@RestController
public class RegController {

    @Autowired
    private UserService userService;

    /**
    * @Description: 邮件注册
    * @Author: xch
    * @Date: 2019/6/15
    */
    @PostMapping("/regByEmail")
    public ResponseMsg regByEmail(@RequestBody RegEmailRequest regEmailRequest, HttpServletRequest request) throws MessagingException {
       String invokeUrl = StringUtils.substringBefore(request.getRequestURL().toString(),
               request.getRequestURI());
        userService.regByEmail(regEmailRequest,invokeUrl);
        return ResponseMsg.success200("发送邮件成功，请激活");
    }

    /**
    * @Description: 激活用户账号(邮件方式注册的)
    * @Author: xch
    * @Date: 2019/6/15
    */
    @GetMapping("invoke/user/{code}")
    public ResponseMsg invokeUser(@PathVariable("code")String code){
        userService.invokeUser(code);
        //todo 生产上应该重定向到前端登陆页面
        return ResponseMsg.success200("激活成功");
    }
}
