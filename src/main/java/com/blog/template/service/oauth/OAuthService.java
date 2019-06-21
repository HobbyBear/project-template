package com.blog.template.service.oauth;


import com.blog.template.models.userinfo.UserInfo;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;

public interface OAuthService {

    AuthRequest getAuthRequest(String oAuthType);

    /**
     * @Description: 如果本地没有该用户信息则添加该用户信息否则直接返回该用户
     * @Author: xch
     * @Date: 2019/6/20 18:19
     */
    UserInfo saveOrUpdateOAuthUserInfo(AuthResponse authResponse);
}
