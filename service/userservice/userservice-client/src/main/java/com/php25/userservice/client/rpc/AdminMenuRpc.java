package com.php25.userservice.client.rpc;

import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;

import java.util.List;

/**
 * Created by penghuiping on 16/8/12.
 */
public interface AdminMenuRpc {

    /**
     * 根据角色查询所有的有效菜单按钮
     *
     * @param adminRole
     * @return
     */
    List<AdminMenuButtonDto> findMenusEnabledByRole(AdminRoleDto adminRole);

    /**
     * 根据父菜单与角色所有的有效菜单按钮
     *
     * @param parent
     * @param adminRole
     * @return
     */
    List<AdminMenuButtonDto> findMenusEnabledByParentAndRole(AdminMenuButtonDto parent, AdminRoleDto adminRole);

    /**
     * 获取菜单按钮树状结构
     *
     * @return
     */
    List<AdminMenuButtonDto> findRootMenus();

    /**
     * 根据父菜单按钮查询字菜单按钮
     *
     * @param parent
     * @return
     */
    List<AdminMenuButtonDto> findMenusByParent(AdminMenuButtonDto parent);

    /**
     * 根据角色查询菜单按钮
     *
     * @param role
     * @return
     */
    List<AdminMenuButtonDto> findMenusByRole(AdminRoleDto role);

    /**
     * 获取有效的菜单按钮树状结构
     *
     * @return
     */
    List<AdminMenuButtonDto> findRootMenusEnabled();

    /**
     * 根据父菜单按钮获取所有有效的菜单按钮
     *
     * @param parent
     * @return
     */
    List<AdminMenuButtonDto> findMenusEnabledByParent(AdminMenuButtonDto parent);
}
