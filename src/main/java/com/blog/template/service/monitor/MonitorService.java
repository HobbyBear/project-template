package com.blog.template.service.monitor;

import javax.servlet.http.HttpSession;

/**
 * @author: xch
 * @create: 2019-06-19 06:33
 **/
public interface MonitorService {

    /**
     * @Description: 踢出同一账号超过同时登陆人数的用户
     * @Author: xch
     * @Date: 2019/6/19 6:44
     *
    */
     void kickoutCheck(Long userId, HttpSession session);

}
