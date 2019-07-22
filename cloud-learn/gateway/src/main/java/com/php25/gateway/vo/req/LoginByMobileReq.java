package com.php25.gateway.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/18 16:40
 * @description:
 */
@Setter
@Getter
public class LoginByMobileReq {

    @NotBlank
    private String mobile;

    @NotBlank
    private String msgCode;
}
