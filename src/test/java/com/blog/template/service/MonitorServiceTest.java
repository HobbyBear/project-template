package com.blog.template.service;

import com.blog.template.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.SessionRepository;

/**
 * @author: xch
 * @create: 2019-06-19 09:26
 **/
public class MonitorServiceTest extends BaseTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SessionRepository repository;

    @Test
    public void deleteSession(){
        String sessionId = "9508f5c8-4e5d-4424-906b-dba8ac61f89b";
        //删除对应的在redis中的session
        Object o = repository.findById(sessionId);
        Assert.assertNotNull(o);
        repository.deleteById(sessionId);
    }
}
