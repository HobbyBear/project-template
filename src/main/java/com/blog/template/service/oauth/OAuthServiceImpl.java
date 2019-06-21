package com.blog.template.service.oauth;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blog.template.common.constants.Constant;
import com.blog.template.dao.UserDao;
import com.blog.template.dao.UserRoleDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author: xch
 * @create: 2019-06-20 17:17
 **/
@Service
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * gitee
     */
    @Value("${oauth.gitee.client-id}")
    private String giteeClientId;

    @Value("${oauth.gitee.client-secret}")
    private String giteeClientSecret;

    @Value("${oauth.gitee.redirect-uri}")
    private String giteeRedirectUrl;

    /**
     * github
     */
    @Value("${oauth.github.client-id}")
    private String githubClientId;

    @Value("${oauth.github.client-secret}")
    private String githubClientSecret;

    @Value("${oauth.github.redirect-uri}")
    private String githubRedirectUrl;


    @Override
    public AuthRequest getAuthRequest(String oAuthType) {

        switch (oAuthType) {
            case Constant.OAuth.GITEE:
                return new AuthGiteeRequest
                        (AuthConfig.builder().clientId(giteeClientId).clientSecret(giteeClientSecret).redirectUri(giteeRedirectUrl).build());
            case Constant.OAuth.GITHUB:
                return new AuthGithubRequest(
                        AuthConfig.builder().clientId(githubClientId).clientSecret(githubClientSecret).redirectUri(githubRedirectUrl).build()
                );
            default:
                throw new CustomerException("暂不支持第三方登陆");
        }
    }

    /**
     * @Description: 如果本地没有该用户信息则添加该用户信息否则更新用户信息返回该用户
     * @Author: xch
     * @Date: 2019/6/20 18:19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo saveOrUpdateOAuthUserInfo(AuthResponse authResponse) {
        JSONObject jsonObject = JSONUtil.parseObj(authResponse.getData());
        String username = jsonObject.getStr("username");
        String source = jsonObject.getStr("source");
        String openId = String.valueOf(jsonObject.get("uuid"));
        String avatar = jsonObject.getStr("avatar");
        log.debug("----------------第三方登陆认证---------------");
        log.debug("登陆途径：{}", source);
        log.debug("第三方id：{}", openId);
        log.debug("用户名：{}", username);
        log.debug("头像：{}", avatar);
        log.debug("----------------第三方登陆认证---------------");
        Optional<UserInfo> userInfoOptional = userDao.findBySourceAndOpenId(source, openId);
        UserInfo userInfo = userInfoOptional.orElse(
                UserInfo
                        .builder()
                        .nickname(username)
                        .username(jsonObject.getStr("nickname") == null ? username : jsonObject.getStr("nickname"))
                        .avatar(avatar)
                        .createTime(LocalDateTime.now())
                        .status(Constant.USER.INVOKE)
                        .source(source)
                        .openId(openId)
                        .build()
        );
        userDao.save(userInfo);
        return userInfo;
    }

}
