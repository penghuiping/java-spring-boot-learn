package com.joinsoft.api.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinsoft.api.base.constant.AccessRequired;
import com.joinsoft.common.controller.JSONController;
import com.joinsoft.common.dto.JSONResponse;
import com.joinsoft.common.exception.JsonException;
import com.joinsoft.userservice.dto.CustomerDto;
import com.joinsoft.userservice.dto.JwtCredentialDto;
import com.joinsoft.userservice.service.CustomerService;
import com.joinsoft.userservice.service.KongJwtService;
import com.joinsoft.userservice.service.TokenService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Controller
@RequestMapping("/api")
public class ApiController extends JSONController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private KongJwtService kongJwtService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 登入接口，返回access_token与refresh_token
     * access_token用于应用访问
     * refresh_token用于重新获取access_token
     *
     * @param mobile
     * @param password
     * @return
     * @throws JsonException
     * @author penghuiping
     * @Time 1/6/15.
     */
    @RequestMapping(value = "/insecure/common/SSOLogin.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONResponse SSSLogin(@NotEmpty String mobile, @NotEmpty String password) throws JsonException {
        CustomerDto customer = customerService.findOneByPhoneAndPassword(mobile, password);
        if (null != customer) {
            String jwtCustomerId = kongJwtService.generateJwtCustomerId(customer);
            kongJwtService.createJwtCustomer(jwtCustomerId);
            JwtCredentialDto jwtCredentialDto = kongJwtService.generateJwtCredential(jwtCustomerId);
            String jwt = kongJwtService.generateJwtToken(jwtCredentialDto);
            return succeed(jwt);
        } else {
            return failed("登入失败");
        }
    }

    /**
     * 登出
     *
     * @return
     * @throws Throwable
     */
    @AccessRequired
    @RequestMapping(value = "/secure/common/SSOLogout.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONResponse SSOLogout(@NotEmpty String token, @NotEmpty String refreshToken) throws JsonException {
        tokenService.cleanToken(token, refreshToken);
        return succeed(true);
    }
}