package com.blog.template.dao;

import com.blog.template.models.userrole.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

/**
 * @author 19624
 */
public interface UserRoleDao extends JpaRepository<UserRole, Long> {

    @Modifying
    void deleteAllByUserId(Long UserId);

}
