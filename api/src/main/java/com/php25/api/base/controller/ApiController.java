package com.php25.api.base.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.php25.common.controller.JSONController;
import com.php25.common.dto.JSONResponse;
import com.php25.common.exception.JsonException;
import com.php25.notifyservice.client.rpc.MailRpc;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.rpc.CustomerRpc;
import com.php25.userservice.client.rpc.TokenJwtRpc;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Controller
@RequestMapping("/api")
public class ApiController extends JSONController {

    @Reference(check = false)
    private CustomerRpc customerRest;

    @Reference(check = false)
    private TokenJwtRpc tokenJwtRpc;

    @Reference(check = false)
    private MailRpc mailRpc;


    /**
     * 登入接口，返回access_token与refresh_token
     * access_token用于应用访问
     * refresh_token用于重新获取access_token
     *
     * @param mobile
     * @param password
     * @return
     * @author penghuiping
     * @Time 1/6/15.
     */
    @ApiOperation(value = "登入", notes = "输入手机号与密码登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/common/SSOLogin.do", method = RequestMethod.GET)
    public ResponseEntity<JSONResponse> SSSLogin(@RequestParam @NotEmpty String mobile, @RequestParam @NotEmpty String password) throws JsonException {
        CustomerDto customer = customerRest.findOneByPhoneAndPassword(mobile, password);
        if (customer != null) {
            String jwt = tokenJwtRpc.getToken(customer.getId() + "");
            return ResponseEntity.ok(succeed(jwt));
        } else {
            return ResponseEntity.ok(failed("登入失败"));
        }
    }

    /**
     * 登出
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "登出", notes = "登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @RequestMapping(value = "/common/SSOLogout.do", method = RequestMethod.GET)
    public ResponseEntity<JSONResponse> SSOLogout(@NotEmpty @RequestHeader(name = "jwt") String jwt) throws JsonException {
        return ResponseEntity.ok(succeed(tokenJwtRpc.cleanToken(jwt)));
    }

    /**
     * 显示客户信息
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "查询客户信息", notes = "查询客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @RequestMapping(value = "/common/showCustomerInfo.do", method = RequestMethod.GET)
    public ResponseEntity<JSONResponse> showCustomerInfo(@NotEmpty @RequestHeader(name = "customerId") String customerId) throws JsonException {
        CustomerDto customerDto = customerRest.findOne(Long.parseLong(customerId));
        return ResponseEntity.ok(succeed(customerDto));
    }
}
