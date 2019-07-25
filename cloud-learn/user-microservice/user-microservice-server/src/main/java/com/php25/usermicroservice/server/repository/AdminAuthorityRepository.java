package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.BaseRepository;
import com.php25.usermicroservice.server.model.AdminAuthority;

import java.util.List;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2018/10/17 13:59
 * @description:
 */
public interface AdminAuthorityRepository extends BaseRepository<AdminAuthority, Long> {

    public Optional<List<AdminAuthority>> findAllByAdminMenuButtonIds(List<Long> ids);
}
