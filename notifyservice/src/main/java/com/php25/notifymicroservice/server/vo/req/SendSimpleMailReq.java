package com.php25.notifymicroservice.server.vo.req;

import com.php25.common.validation.annotation.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:09
 * @description:
 */
@Setter
@Getter
public class SendSimpleMailReq {

    /**
     * 收件人邮箱
     **/
    @Email
    private String sendTo;

    /**
     * 邮件标题
     **/
    @NotBlank
    private String title;

    /**
     * 邮件内容
     **/
    @NotBlank
    private String content;
}
