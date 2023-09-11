package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 *
 * @author : tians
 * @date : 2023/9/11 12:19
 * @modyified By :
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 拦截jwt请求进行解析
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否为controller的方法，否就放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // 开始校验令牌
        try {
            log.info("jwt开始校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户的id：{}", userId);
            // 通过局部线程取出当前线程里的userID
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception e) {
            //     校验不通过
            response.setStatus(401);
            return false;
        }
        //
    }
}
