package com.blog.template.controller.common;

import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.service.monitor.MonitorService;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.LoginRequest;
import com.blog.template.vo.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author 19624
 */

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseMsg login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserInfo userInfo = userService.findByUsername(authentication.getName()).get();
        monitorService.kickoutCheck(userInfo.getId(),session);
        return ResponseMsg.success200("登陆成功");
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseMsg logout() {
        SecurityContextHolder.clearContext();
        return ResponseMsg.success200("退出成功");
    }


}
