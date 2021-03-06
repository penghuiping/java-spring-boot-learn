package com.php25.usermicroservice.web.vo.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/8/22 13:47
 * @description:
 */
@Setter
@Getter
public class ReqGroupChangeInfoVo {

    @Min(0)
    Long groupId;

    @NotBlank
    String description;
}
