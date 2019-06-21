package com.blog.template.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.PageBeanConvertUtil;
import com.blog.template.dao.RoleDao;
import com.blog.template.dao.UserDao;
import com.blog.template.dao.UserRoleDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.role.Role;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.models.userinfo.UserInfoVo;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.models.userrole.UserRole;
import com.blog.template.service.mail.MailService;
import com.blog.template.vo.PageBean;
import com.blog.template.vo.RegEmailRequest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 19624
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ValueOperations<String, String> valueOperations;


    //集群环境下不适用,已修改为redis存储code值，
//    private Cache<String,Long> cache = CacheBuilder.newBuilder()
//            .maximumSize(100)
//            .expireAfterWrite(5,TimeUnit.MINUTES)
//            .initialCapacity(10)
//            .removalListener(new RemovalListener<String, Long>() {
//                @Override
//                public void onRemoval(RemovalNotification<String, Long> removalNotification) {
//                    String key = removalNotification.getKey();
//                    Long value = removalNotification.getValue();
//                    if (StrUtil.startWith(key,Constant.RedisKey.EMAIL_INVOKE)){
//                       userDao.findById(value).ifPresent(u->{
//                           if (u.getStatus().equals(Constant.USER.NOT_INVOKE)){
//                               userDao.deleteById(value);
//                           }
//                       });
//                    }
//                }
//            })
//            .build();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regByEmail(RegEmailRequest regEmailRequest, String invokeUrl) throws MessagingException {
        if (userDao.findByEmail(regEmailRequest.getEmail()).isPresent()) {
            throw new CustomerException("邮件已注册");
        }
        //创建未激活用户
        UserInfo userInfo = UserInfo
                .builder()
                .email(regEmailRequest.getEmail())
                .nickname(StringUtils.substring(regEmailRequest.getEmail(), 0, 4))
                .password(new BCryptPasswordEncoder().encode(regEmailRequest.getPassword()))
                .username(StringUtils.substring(regEmailRequest.getEmail(), 0, 4))
                .status(Constant.USER.NOT_INVOKE)
                .createTime(LocalDateTime.now())
                //todo  传入默认头像地址
                .avatar("")
                .build();
        userDao.save(userInfo);
        //构建邮件内容
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("email", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("invoke_user.html");
        String code = RandomStringUtils.randomAlphabetic(5);
        //缓存code
        //5分钟未激活用户得删除用户表中得用户数据，(改用redis)
        valueOperations.set(String.join(":", Constant.RedisKey.EMAIL_INVOKE, code)
                , String.valueOf(userInfo.getId()), 5, TimeUnit.MINUTES);
        invokeUrl = invokeUrl + "/invoke/user/" + code;
        String content = template.render(Dict
                .create()
                .set("username", regEmailRequest.getEmail())
                .set("url", invokeUrl)
        );
        //发送激活邮件
        mailService.sendHtmlMail(regEmailRequest.getEmail(), "微博客账号激活", content);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invokeUser(String code) {
        String userId = valueOperations.get(String.join(":", Constant.RedisKey.EMAIL_INVOKE, code));
        if (userId == null) {
            log.info("非法激活操作....");
            return;
        }
        Optional<UserInfo> userInfoOptional = userDao.findById(Long.valueOf(userId));
        userInfoOptional.ifPresent(u -> {
            u.setStatus(Constant.USER.INVOKE);
            userDao.save(u);
            userRoleDao.save(UserRole.builder().roleId(Constant.Role.ROLE_USER).userId(u.getId()).build());
        });
    }

    /**
     * @Description: 支持动态更新
     * @Author: xch
     * @Date: 2019/6/21 8:31
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateUser(UserInfo userInfo) {
        if (userInfo.getId() != null) {
            UserInfo metaUser = userDao.getOne(userInfo.getId());
            BeanUtil.copyProperties(userInfo, metaUser, CopyOptions.create().setIgnoreNullValue(true));
            userDao.save(metaUser);
        } else {
            userDao.save(userInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delUserById(Long id) {
        userDao.deleteById(id);
        userRoleDao.deleteAllByUserId(id);
    }

    @Override
    public Optional<UserInfo> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public PageBean<UserInfoVo> findUserVoByUserSearchVo(UserSearchVo userSearchVo, Integer page, Integer pageSize) {
        Specification<UserInfo> specification = (Specification<UserInfo>) (root, criteriaQuery, cb) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            if (userSearchVo.getUsername() != null){
                predicateList.add(cb.like(root.get("username"),"%"+userSearchVo.getUsername()+"%"));
            }
            if (userSearchVo.getEndTime() != null){
                predicateList.add(cb.lessThanOrEqualTo(root.get("createTime"),userSearchVo.getEndTime()));
            }
            if (userSearchVo.getStartTime() != null){
                predicateList.add(cb.greaterThanOrEqualTo(root.get("createTime"),userSearchVo.getStartTime()));
            }
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        Page<UserInfo> userInfoPage = userDao.findAll(specification,PageRequest.of(page-1,pageSize));
        PageBean pageBean = PageBeanConvertUtil.convertCustomerPageBean(userInfoPage);
        List<UserInfoVo> userInfoVos = Lists.newArrayList();
        pageBean.getContent().forEach(userInfo->{
            UserInfo u = (UserInfo) userInfo;
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtil.copyProperties(u,userInfoVo);
            if (userInfoVo.getSource() != null){
                userInfoVo.setSupportChangeRole(false);
            }else {
                userInfoVo.setSupportChangeRole(true);
            }
            userInfoVo.setRoles(roleDao.findRoleListByUserId(u.getId()).stream().map(Role::getRoleName).collect(Collectors.toList()));
            userInfoVos.add(userInfoVo);
        });
        pageBean.setContent(userInfoVos);
        return pageBean;
    }


}
