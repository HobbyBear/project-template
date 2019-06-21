package com.blog.template.job;

import com.blog.template.common.constants.Constant;
import com.blog.template.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: xch
 * @create: 2019-06-19 17:11
 **/
@Component
public class TimeJob {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * @Description: 清除5分钟或大于5分钟内没有通过邮件激活的用户
     * @Author: xch
     * @Date: 2019/6/19 17:13
     */
    @Scheduled(cron = "0 1-30 * * * ? ")
    public void clearUnInvokeUserInfo() {

        boolean tryLock = valueOperations.setIfAbsent(Constant.RedisKey.CLEAR_NOT_INVOKE_USER_LOCK, " any value is ok", 500, TimeUnit.MILLISECONDS);
        if (tryLock) {
            userDao.deleteUnInvokeUserInHalf5Minutes();
            redisTemplate.delete(Constant.RedisKey.CLEAR_NOT_INVOKE_USER_LOCK);
        }
    }
}
