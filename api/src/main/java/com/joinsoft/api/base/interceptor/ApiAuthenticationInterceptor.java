package com.joinsoft.api.base.interceptor;

import com.joinsoft.common.util.StringUtil;
import com.joinsoft.api.base.constant.AccessRequired;
import com.joinsoft.userservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Oauth2 token认证
 * Created by penghuiping on 16/8/30.
 */
@Component
public class ApiAuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AccessRequired annotation = method.getAnnotation(AccessRequired.class);
        if (null != annotation) {
            //如果此此方法上有AccessRequired注解，需要token认证
            //先获取loginToken
            String token = (String) request.getParameter("token");
            String value = request.getHeader("Content-Type");
            if ("application/json".equals(value))
                response.addHeader("Content-Type", "application/json;charset=UTF-8");

            //如果为空直接返回未登入提示
            if (StringUtil.isEmpty(token)) {
                //返回403状态码 访问被禁止
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            //直接判断redis里的token值是否有效
            if (tokenService.checkTokenValidation(token)) return true;

            //返回403状态码 访问被禁止
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            //直接通过
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
