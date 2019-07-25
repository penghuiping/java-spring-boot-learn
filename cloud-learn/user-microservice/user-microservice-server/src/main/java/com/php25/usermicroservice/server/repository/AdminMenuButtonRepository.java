package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.BaseRepository;
import com.php25.usermicroservice.server.model.AdminMenuButton;
import com.php25.usermicroservice.server.model.AdminRole;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author penghuiping
 * @date 2015-01-20
 */
public interface AdminMenuButtonRepository extends BaseRepository<AdminMenuButton, Long> {

    List<AdminMenuButton> findRootMenus();

    List<AdminMenuButton> findMenusByParent(@Param("parent") AdminMenuButton parent);

    List<AdminMenuButton> findMenusByRole(@Param("role") AdminRole role);

    List<AdminMenuButton> findRootMenusEnabled();

    List<AdminMenuButton> findMenusEnabledByParent(@Param("parent") AdminMenuButton parent);

    List<AdminMenuButton> findMenusEnabledByRole(@Param("role") AdminRole role);

    List<AdminMenuButton> findMenusEnabledByParentAndRole(@Param("parent") AdminMenuButton parent, @Param("role") AdminRole role);

    Integer findMenusMaxSort();
}
