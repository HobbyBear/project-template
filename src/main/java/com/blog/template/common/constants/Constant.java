package com.blog.template.common.constants;

public class Constant {


    public interface Role {
        Integer ROLE_ADMIN = 1;
        Integer ROLE_USER = 2;
    }

    public interface USER {
        /**
         * 激活状态
         */
        Integer INVOKE = 1;
        /**
         * 未激活状态
         */
        Integer NOT_INVOKE = 2;

    }

    public interface SessionKey {
        /**
         * session中的用户id标志
         */
        String SESSION_USERID = "sessionUserId";
    }

    public interface RedisKey {
        String EMAIL_INVOKE = "email:invoke";
        /**
         * 同一用户账号会话set
         */
        String USER_SESSION_SET = "user:session:set";
        /**
         * 同一个账号登陆次数限定分布式锁
         */
        String LOGIN_LIMIT_LOCK = "login:limit:lock";

        /**
         * 清除没有激活的用户分布式锁
         */
        String CLEAR_NOT_INVOKE_USER_LOCK = "clear:not_invoke_user:lock";
    }

    /**
     * oauth 登陆类型
     */
    public interface OAuth {
        String GITEE = "gitee";

        String GITHUB = "github";
    }
}
