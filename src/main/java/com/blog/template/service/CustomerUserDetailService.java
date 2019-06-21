package com.blog.template.service;

import cn.hutool.core.bean.BeanUtil;
import com.blog.template.common.constants.Constant;
import com.blog.template.dao.RoleDao;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 19624
 */
@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmai) throws UsernameNotFoundException {
        UserInfo userInfo = userDao.findByUsernameOrEmail(usernameOrEmai,usernameOrEmai)
                .orElseThrow(()-> new UsernameNotFoundException("用户不存在："+usernameOrEmai));
        if (userInfo.getStatus().equals(Constant.USER.NOT_INVOKE)){
            throw new CustomerException("用户未激活，请先根据邮件激活");
        }
        UserPrincipal userPrincipal = new UserPrincipal();
        BeanUtil.copyProperties(userInfo,userPrincipal);
        userPrincipal.setRoles(roleDao.findRoleListByUserId(userInfo.getId()));
        return userPrincipal;
    }
}
