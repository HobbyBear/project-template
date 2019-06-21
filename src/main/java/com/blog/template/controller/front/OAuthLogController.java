package com.blog.template.controller.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.service.monitor.MonitorService;
import com.blog.template.service.oauth.OAuthService;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.vo.UserPrincipal;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: xch
 * @create: 2019-06-20 16:41
 **/
@RestController
@RequestMapping("/oauth")
public class OAuthLogController {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private MonitorService monitorService;

   /**
    * @Description: 第三方登陆
    * @Author: xch
    * @Date: 2019/6/20 16:50
   */
    @GetMapping("/login/{oauthType}")
    public void renderAuth(@PathVariable("oauthType") String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = oAuthService.getAuthRequest(oauthType);
        response.sendRedirect(authRequest.authorize());
    }

    /***
     * @Description: 第三方登陆回调地址
     * @Author: xch
     * @Date: 2019/6/20 17:07
    */
    @RequestMapping("/{oauthType}/callback")
    public ResponseMsg login(@PathVariable String oauthType, String code, HttpSession session){
        AuthRequest authRequest = oAuthService.getAuthRequest(oauthType);
        AuthResponse authResponse =  authRequest.login(code);
        if (authResponse == null){
            return ResponseMsg.fail400("非法访问");
        }
        UserInfo userInfo = oAuthService.saveOrUpdateOAuthUserInfo(authResponse);
        UserPrincipal userPrincipal = new UserPrincipal();
        BeanUtil.copyProperties(userInfo,userPrincipal,CopyOptions.create().setIgnoreNullValue(true));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userPrincipal,null,AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(token);
        monitorService.kickoutCheck(userInfo.getId(),session);
        return ResponseMsg.success200("登陆成功");
    }


}
