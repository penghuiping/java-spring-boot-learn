package com.php25.userservice.server.repository.impl;

import com.php25.common.jdbc.Db;
import com.php25.common.jdbc.repository.BaseRepositoryImpl;
import com.php25.userservice.server.model.UserRole;
import com.php25.userservice.server.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public UserRole findOneByRoleIdAndUserId(Long roleId, Long userId) {
        return db.cnd(UserRole.class).whereEq("adminRole", roleId).andEq("adminUser", userId).single();
    }
}
