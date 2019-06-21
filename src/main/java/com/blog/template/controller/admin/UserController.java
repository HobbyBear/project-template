package com.blog.template.controller.admin;

import com.blog.template.common.constants.Constant;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.models.userinfo.UserSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对用户进行增删查改
 *
 * @author xch
 * @since 2019/6/18 16:42
 **/
@RestController
@RequestMapping("/sys/user")
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * @return com.blog.template.vo.ResponseMsg
     * @Author xch
     * @Description 更新或者插入用户信息
     * @Date 16:51 2019/6/18
     * @Param [userInfo]
     **/
    @PostMapping
    public ResponseMsg updateOrSaveUser(@RequestBody UserInfo userInfo) {
        if (userInfo.getId() == null) {
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setStatus(Constant.USER.INVOKE);
            userInfo.setPassword(new BCryptPasswordEncoder().encode(userInfo.getPassword()));
        }
        userService.saveOrUpdateUser(userInfo);
        return ResponseMsg.success200("请求成功");
    }

    /**
     * @return com.blog.template.vo.ResponseMsg
     * @Author xch
     * @Description 删除用户根据id
     * @Date 17:26 2019/6/18
     * @Param [userId]
     **/

    @DeleteMapping("/{userId}")
    public ResponseMsg delUserById(@PathVariable("userId") Long userId) {
        userService.delUserById(userId);
        return ResponseMsg.success200("请求成功");
    }

    /**
     * @Description: 多条件搜索用户列表
     * @Author: xch
     * @Date: 2019/6/21 13:04
    */
    @GetMapping()
    public ResponseMsg findUserList(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    UserSearchVo userSearchVo){
        return ResponseMsg.success200(userService.findUserVoByUserSearchVo(userSearchVo,page,pageSize));
    }

}
