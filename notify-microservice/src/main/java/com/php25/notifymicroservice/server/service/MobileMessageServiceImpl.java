package com.php25.notifymicroservice.server.service;

import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.trace.TracedWrapper;
import com.php25.common.redis.RedisService;
import com.php25.notifymicroservice.server.constant.Constant;
import com.php25.notifymicroservice.server.dto.SendSMSDto;
import com.php25.notifymicroservice.server.dto.ValidateSMSDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: penghuiping
 * @date: 2018/7/19 12:56
 * @description:
 */
@Slf4j
@Service
public class MobileMessageServiceImpl implements MobileMessageService {


    @Autowired
    private RedisService redisService;


    @Autowired
    private TracedWrapper tracedWrapper;

    /**
     * 发送验证码
     */
    @Override
    public Boolean sendSMS(SendSMSDto sendSMSDto) {
        return tracedWrapper.wrap("selfDefinedSpan", () -> {
            String mobile = sendSMSDto.getMobile();
            log.info("手机号为:{}", mobile);
            String message = "1111";
            redisService.set("sms" + mobile, message, Constant.SMS_EXPIRE_TIME);
            return true;
        });
    }

    /**
     * 通过电话号码查询有效验证码数据
     */
    @Override
    public Boolean validateSMS(ValidateSMSDto validateSMSDto) {
        String mobile = validateSMSDto.getMobile();
        String code = validateSMSDto.getMsgCode();

        String mobileCode = redisService.get("sms" + mobile, String.class);
        if (!StringUtil.isBlank(mobileCode) && mobileCode.equals(code)) {
            redisService.remove("sms" + mobile);
            return true;
        } else {
            return false;
        }
    }
}
