package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:42
 * @description:
 */
@Setter
@Getter
public class ReqCreateGroupVo {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

}
