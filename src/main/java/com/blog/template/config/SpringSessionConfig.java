package com.blog.template.config;

import com.blog.template.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * @author: xch
 * @create: 2019-06-19 15:28
 **/
@Slf4j
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)  //s为单位
public class SpringSessionConfig {

    @Autowired
    private ZSetOperations<String, String> zSetOperations;


    /**
     * 过期session事件监听
     */
    @EventListener
    public void onSessionExpired(SessionExpiredEvent expiredEvent) {
        Session session = expiredEvent.getSession();
        //session 过期删除USER_SESSION_SET中同一用户对应的sessionId值
        String userId = session.getAttribute(Constant.SessionKey.SESSION_USERID);
        log.info("userId :{} ,sesssionId:{} 过期:", userId, session.getId());
        zSetOperations.remove(String.join(":",Constant.RedisKey.USER_SESSION_SET,userId),session.getId());
    }

    /**
     * 删除session事件监听
     */
//    @EventListener
//    public void onSessionDeleted(SessionDeletedEvent deletedEvent) {
//        String sessionId = deletedEvent.getSessionId();
//        System.out.println("删除" + sessionId);
//    }
}