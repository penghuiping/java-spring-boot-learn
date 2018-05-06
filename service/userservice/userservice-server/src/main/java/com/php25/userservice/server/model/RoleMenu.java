package com.php25.userservice.server.model;

import javax.persistence.*;

/**
 * 角色-菜单中间关系表
 * Created by Zhangbing on 2017/4/24.
 */
@Entity
@Table(name = "userservice_role_menu")
public class RoleMenu {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private AdminRole adminRole;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private AdminMenuButton adminMenuButton;

    public AdminRole getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRole adminRole) {
        this.adminRole = adminRole;
    }

    public AdminMenuButton getAdminMenuButton() {
        return adminMenuButton;
    }

    public void setAdminMenuButton(AdminMenuButton adminMenuButton) {
        this.adminMenuButton = adminMenuButton;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
