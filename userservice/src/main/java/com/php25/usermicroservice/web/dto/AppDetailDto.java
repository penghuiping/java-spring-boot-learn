package com.php25.usermicroservice.web.dto;

import com.php25.common.flux.web.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
@Setter
@Getter
public class AppDetailDto extends BaseDto implements Serializable {

    private String appId;

    private String appName;

    private String appSecret;

    private String registeredRedirectUri;
}
