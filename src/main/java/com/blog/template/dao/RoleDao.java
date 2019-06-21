package com.blog.template.dao;

import com.blog.template.models.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 19624
 */
public interface RoleDao extends JpaRepository<Role,Integer> {

    @Query(nativeQuery = true,value = "select r.* from role r,user_role ur where r.id = ur.role_id and ur.user_id = ?1")
    List<Role> findRoleListByUserId(Long userId);

}
