package com.php25.userservice.server.service.impl;

import com.php25.common.dto.DataGridPageDto;
import com.php25.common.service.IdGeneratorService;
import com.php25.common.service.impl.BaseServiceImpl;
import com.php25.common.specification.BaseSpecsFactory;
import com.php25.userservice.client.dto.AdminMenuButtonDto;
import com.php25.userservice.client.dto.AdminRoleDto;
import com.php25.userservice.server.model.AdminMenuButton;
import com.php25.userservice.server.model.AdminRole;
import com.php25.userservice.server.model.RoleMenu;
import com.php25.userservice.server.repository.AdminRoleRepository;
import com.php25.userservice.server.repository.RoleMenuRepository;
import com.php25.userservice.server.service.AdminRoleService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by penghuiping on 16/8/12.
 */
@Transactional
@Service
@Primary
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRoleDto, AdminRole, Long> implements AdminRoleService {

    private AdminRoleRepository adminRoleRepository;

    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    public void setAdminRoleRepository(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository;
        this.baseRepository = adminRoleRepository;
    }

    @Autowired
    public void setRoleMenuRepository(RoleMenuRepository roleMenuRepository) {
        this.roleMenuRepository = roleMenuRepository;
    }


    @Override
    public Optional<AdminRoleDto> save(AdminRoleDto obj) {
        Assert.notNull(obj, "adminRoleDto不能为null");
        Assert.notEmpty(obj.getMenus(), "adminRoleDto.menus至少需要包含一个元素");
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(obj, adminRole);

        if (null == obj.getId() || "".equals(obj.getId())) {
            adminRole.setCreateTime(new Date());
        }
        List<AdminMenuButton> adminMenuButtons = obj.getMenus().stream().map(temp -> {
            AdminMenuButton adminMenuButton = new AdminMenuButton();
            BeanUtils.copyProperties(temp, adminMenuButton);
            return adminMenuButton;
        }).collect(Collectors.toList());
        adminRole.setUpdateTime(new Date());
        adminRole = adminRoleRepository.save(adminRole);
        for (AdminMenuButton menuButton : adminMenuButtons) {
            RoleMenu roleMenu = roleMenuRepository.findOneByRoleIdAndMenuId(adminRole.getId(), menuButton.getId());
            if (null == roleMenu) {
                roleMenu = new RoleMenu();
                roleMenu.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
                roleMenu.setAdminRole(adminRole);
                roleMenu.setAdminMenuButton(menuButton);
                roleMenuRepository.save(roleMenu);
            }
        }
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.ofNullable(adminRoleDto);
    }


    @Override
    public Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids) {
        Assert.notEmpty((List<Long>) ids, "ids集合至少需要包含一个元素");
        return findAll(ids, true);
    }


    public Optional<List<AdminRoleDto>> findAll(Iterable<Long> ids, Boolean lazy) {
        Assert.notEmpty((List<Long>) ids, "ids集合至少需要包含一个元素");
        Assert.notNull(lazy, "lazy不能为null");
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAllById(ids);
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            if (lazy)
                BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            else {
                BeanUtils.copyProperties(adminRole, adminRoleDto);
                List<AdminMenuButtonDto> adminMenuButtonDtos = adminRole.getAdminMenuButtons().stream().map(adminMenuButton -> {
                    AdminMenuButtonDto adminMenuButtonDto = new AdminMenuButtonDto();
                    BeanUtils.copyProperties(adminMenuButton, adminMenuButtonDto);
                    return adminMenuButtonDto;
                }).collect(Collectors.toList());
                adminRoleDto.setMenus(adminMenuButtonDtos);
            }
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminRoleDto>> findAllEnabled() {
        List<AdminRole> adminRoles = adminRoleRepository.findAllEnabled();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<AdminRoleDto>> findAll() {
        List<AdminRole> adminRoles = (List<AdminRole>) adminRoleRepository.findAll();
        return Optional.ofNullable(adminRoles.parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList()));
    }

    @Override
    public Optional<DataGridPageDto<AdminRoleDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        Assert.notNull(pageNum, "pageNum不能为null");
        Assert.notNull(pageSize, "pageSize不能为null");
        Assert.hasText(searchParams, "searchParams不能为空，如没有搜索条件请使用[]");
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Page<AdminRole> userPage = adminRoleRepository.findAll(BaseSpecsFactory.getJpaInstance().getSpecs(searchParams), pageRequest);
        List<AdminRoleDto> adminRoleBos = userPage.getContent().parallelStream().map(adminRole -> {
            AdminRoleDto adminRoleDto = new AdminRoleDto();
            BeanUtils.copyProperties(adminRole, adminRoleDto, "adminMenuButtons");
            return adminRoleDto;
        }).collect(Collectors.toList());

        PageImpl<AdminRoleDto> adminRolePage = new PageImpl<AdminRoleDto>(adminRoleBos, null, userPage.getTotalElements());
        return Optional.ofNullable(toDataGridPageDto(adminRolePage));
    }

    @Override
    public Optional<AdminRoleDto> findOne(Long id) {
        Assert.notNull(id, "id不能为null");
        AdminRole adminRole = adminRoleRepository.findById(id).orElse(null);
        if (null == adminRole) return Optional.empty();
        AdminRoleDto adminRoleDto = new AdminRoleDto();
        BeanUtils.copyProperties(adminRole, adminRoleDto);
        return Optional.ofNullable(adminRoleDto);
    }

}
