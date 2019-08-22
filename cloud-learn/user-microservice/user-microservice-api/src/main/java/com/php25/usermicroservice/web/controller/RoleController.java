package com.php25.usermicroservice.web.controller;

import com.google.common.collect.Lists;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.specification.Operator;
import com.php25.common.core.specification.SearchParam;
import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.usermicroservice.web.dto.RoleCreateDto;
import com.php25.usermicroservice.web.dto.RoleDetailDto;
import com.php25.usermicroservice.web.dto.RolePageDto;
import com.php25.usermicroservice.web.service.RoleService;
import com.php25.usermicroservice.web.vo.req.ReqCreateRoleVo;
import com.php25.usermicroservice.web.vo.req.ReqRoleChangeInfoVo;
import com.php25.usermicroservice.web.vo.req.ReqUnableRoleVo;
import com.php25.usermicroservice.web.vo.req.SearchParamVo;
import com.php25.usermicroservice.web.vo.req.SearchVo;
import com.php25.usermicroservice.web.vo.res.ResRolePageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:25
 * @description:
 */
@RestController
@RequestMapping("/role")
public class RoleController extends JSONController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/queryPage")
    public JSONResponse queryPage(@RequestAttribute String appId, @Valid @RequestBody SearchVo searchVo) {
        List<SearchParamVo> searchParamVoList = searchVo.getSearchParamVoList();
        List<SearchParam> searchParams;
        if (null == searchParamVoList || searchParamVoList.isEmpty()) {
            searchParams = new ArrayList<>();
        } else {
            searchParams = searchParamVoList.stream().map(searchParamVo ->
                    SearchParam.of(searchParamVo.getFieldName(),
                            searchParamVo.getOperator(),
                            searchParamVo.getValue())).collect(Collectors.toList());

            searchParams = searchParams.stream().filter(searchParam -> !"appId".equals(searchParam.getFieldName())).collect(Collectors.toList());
            searchParams.add(SearchParam.of("appId", Operator.EQ, appId));

        }
        List<RolePageDto> rolePageDtos = roleService.queryPage(searchVo.getPageNum(), searchVo.getPageSize(), searchParams, "id", Sort.Direction.ASC);

        if (rolePageDtos == null || rolePageDtos.isEmpty()) {
            return succeed(Lists.newArrayList());
        } else {
            List<ResRolePageVo> result = rolePageDtos.stream().map(rolePageDto -> {
                ResRolePageVo resRolePageVo = new ResRolePageVo();
                BeanUtils.copyProperties(rolePageDto, resRolePageVo);
                return resRolePageVo;
            }).collect(Collectors.toList());
            return succeed(result);
        }
    }

    @PostMapping("/unableRole")
    public JSONResponse unableRole(@RequestAttribute String appId, @Valid @RequestBody ReqUnableRoleVo reqUnableRoleVo) {
        Boolean result = roleService.unableRole(appId, reqUnableRoleVo.getRoleId());
        return succeed(result);
    }

    @PostMapping("/create")
    public JSONResponse createRole(@RequestAttribute String appId, @Valid @RequestBody ReqCreateRoleVo reqCreateRoleVo) {
        RoleCreateDto roleCreateDto = new RoleCreateDto();
        roleCreateDto.setName(reqCreateRoleVo.getName());
        roleCreateDto.setDescription(reqCreateRoleVo.getDescription());
        Boolean result = roleService.create(appId, roleCreateDto);
        return succeed(result);
    }

    @PostMapping("/changeInfo")
    public JSONResponse changeInfo(@RequestAttribute String appId, @Valid @RequestBody ReqRoleChangeInfoVo reqRoleChangeInfoVo) {
        Boolean result = roleService.changeInfo(appId, reqRoleChangeInfoVo.getRoleId(), reqRoleChangeInfoVo.getDescription());
        return succeed(result);
    }

    @PostMapping("/detailInfo")
    public JSONResponse detailInfo(@RequestAttribute String appId, @Min(0) Long roleId) {
        RoleDetailDto roleDetailDto = roleService.detailInfo(roleId);
        if (!roleDetailDto.getAppId().equals(appId)) {
            throw Exceptions.throwIllegalStateException("此用户无法查询此角色详细信息");
        }
        return succeed(roleDetailDto);
    }


}
