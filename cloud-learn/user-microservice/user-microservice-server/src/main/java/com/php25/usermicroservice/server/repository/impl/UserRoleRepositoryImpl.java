package com.php25.usermicroservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.UserRole;
import com.php25.userservice.server.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/9/4 21:26
 * @description:
 */
@Repository
public class UserRoleRepositoryImpl extends BaseRepositoryImpl<UserRole, Long> implements UserRoleRepository {

    @Autowired
    private Db db;

    @Override
    public Optional<UserRole> findOneByRoleIdAndUserId(Long roleId, Long userId) {
        UserRole userRole = db.cnd(UserRole.class).whereEq("adminRole", roleId).andEq("adminUser", userId).single();
        return Optional.ofNullable(userRole);
    }

    @Override
    public Optional<List<UserRole>> findAllByUserId(Long userId) {
        List<UserRole> userRoles = db.cnd(UserRole.class).whereEq("adminUser", userId).select();
        return Optional.ofNullable(userRoles);
    }
}
