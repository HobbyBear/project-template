package com.blog.template.service.monitor;

import com.blog.template.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: xch
 * @create: 2019-06-19 06:33
 **/
@Service
@Slf4j
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private ZSetOperations<String, String> zSetOperations;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 同一个账号最大的会话数
     */
    private static final Long MAX_SESSION = 1L;

    /**
     * @Description: 踢出同一账号超过同时登陆人数的用户
     * @Author: xch
     * @Date: 2019/6/19 6:47
     */
    @Override
    public void kickoutCheck(Long userId, HttpSession session) {
        String key = String.join(":", Constant.RedisKey.USER_SESSION_SET, String.valueOf(userId));
        Boolean getLock = false;
        log.info("------------------获得分布式锁----------------------");
        while (!getLock) {
            getLock = valueOperations.setIfAbsent(String.join(":", Constant.RedisKey.LOGIN_LIMIT_LOCK, String.valueOf(userId)),
                    "any value is ok", 3000, TimeUnit.SECONDS);
        }
        session.setAttribute(Constant.SessionKey.SESSION_USERID, String.valueOf(userId));
        zSetOperations.add(key, session.getId(), System.currentTimeMillis());
        if (MAX_SESSION.compareTo(Objects.requireNonNull(zSetOperations.size(key))) < 0) {
            //取出时间戳最靠前的sessionId
            String sessionId = zSetOperations.range(key, 0, 0).stream().findFirst().get();
            zSetOperations.removeRange(key, 0, 0);
            //删除对应的在redis中的session
            sessionRepository.deleteById(sessionId);
        }
        log.info("-----------------释放分布式锁---------------------");
        redisTemplate.delete(String.join(":", Constant.RedisKey.LOGIN_LIMIT_LOCK, String.valueOf(userId)));

    }
}
