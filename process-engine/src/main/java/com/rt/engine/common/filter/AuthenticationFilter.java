package com.rt.engine.common.filter;

import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.rt.engine.common.component.ApplicationContextUtil;
import com.rt.engine.common.constants.CodeEnum;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.common.util.RedisUtil;
import com.rt.engine.common.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationFilter implements Filter {

    private final String authorizationToken = "Bearer c78aa1d9-4ab4-4c9c-97cc-a9a548ea38aa";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        log.debug("----------------鉴权开始------------------------");
        try {
            String authorization = request.getHeader(EngineConstants.INTERFACE_AUTHORIZATION);
            // 暂时屏蔽鉴权
            authorization = "Bearer c78aa1d9-4ab4-4c9c-97cc-a9a548ea38aa";
            if (StringUtils.isNotBlank(authorization) && authorizationToken.equals(authorization.trim())) {
                filterChain.doFilter(request, response);
                return;
            }
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                ResponseUtil.returnBaseResponse(response, CodeEnum.NOT_LOGIN);
                return;
            }
            Cookie bgToken = Arrays.stream(cookies)
                .filter(cookie -> EngineConstants.COOKIE_TOKEN_NAME.equals(cookie.getName())).findFirst().orElseThrow();
            RedisUtil redisUtil = ApplicationContextUtil.getBean(RedisUtil.class);
            String userInfoStr =
                (String)redisUtil.get(EngineConstants.KEY_PREFIX + StringUtils.upperCase(bgToken.getValue()));
            if (StringUtils.isBlank(userInfoStr)) {
                ResponseUtil.returnBaseResponse(response, CodeEnum.NOT_LOGIN);
                return;
            }
            String userId = JSON.parseObject(userInfoStr).getString(EngineConstants.USER_ID);
            if (StringUtils.isBlank(userId)) {
                ResponseUtil.returnBaseResponse(response, CodeEnum.NOT_LOGIN);
                return;
            }
            log.info("current user is " + userId);
            request.setAttribute(EngineConstants.USER_ID, userId);
            filterChain.doFilter(request, response);
            log.debug("----------------鉴权结束------------------------");
        } catch (Exception ex) {
            log.error("authenticationFilter Exception: ", ex);
            ResponseUtil.returnBaseResponse(response, CodeEnum.NOT_LOGIN);
        }
    }

}
