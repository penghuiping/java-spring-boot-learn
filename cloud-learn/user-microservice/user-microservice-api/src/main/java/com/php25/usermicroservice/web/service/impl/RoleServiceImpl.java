package com.php25.usermicroservice.web.service.impl;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.core.specification.SearchParamBuilder;
import com.php25.usermicroservice.web.dto.RoleCreateDto;
import com.php25.usermicroservice.web.dto.RoleDetailDto;
import com.php25.usermicroservice.web.dto.RolePageDto;
import com.php25.usermicroservice.web.model.Role;
import com.php25.usermicroservice.web.repository.RoleRepository;
import com.php25.usermicroservice.web.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/3 10:23
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/adminRole")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RolePageDto> queryPage(Integer pageNum, Integer pageSize, List<SearchParam> searchParams, String property, Sort.Direction direction) {
        SearchParamBuilder searchParamBuilder = SearchParamBuilder.builder().append(searchParams);
        Pageable pageable = PageRequest.of(pageNum, pageSize, direction, property);
        Page<Role> rolePage = roleRepository.findAll(searchParamBuilder, pageable);
        if (null != rolePage && rolePage.getTotalElements() > 0) {
            List<Role> roles = rolePage.getContent();
            if (roles.size() > 0) {
                List<RolePageDto> result = roles.stream().map(role -> {
                    RolePageDto rolePageDto = new RolePageDto();
                    BeanUtils.copyProperties(role, rolePageDto);
                    return rolePageDto;
                }).collect(Collectors.toList());
                return result;
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public Boolean unableRole(String appId, Long roleId) {
        boolean result = roleRepository.softDelete(Lists.newArrayList(roleId), appId);
        return result;
    }

    @Override
    public Boolean create(String appId, RoleCreateDto roleCreateDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleCreateDto, role);
        role.setAppId(appId);
        roleRepository.save(role);
        return true;
    }

    @Override
    public Boolean changeInfo(String appId, Long roleId, String roleDescription) {
        boolean result = roleRepository.changeInfo(roleDescription, roleId, appId);
        return result;
    }

    @Override
    public RoleDetailDto detailInfo(Long roleId) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (!roleOptional.isPresent()) {
            throw Exceptions.throwIllegalStateException(String.format("无法通过id:%d找到对应的角色记录", roleId));
        } else {
            Role role = roleOptional.get();
            RoleDetailDto roleDetailDto = new RoleDetailDto();
            BeanUtils.copyProperties(role, roleDetailDto);
            return roleDetailDto;
        }
    }

}
