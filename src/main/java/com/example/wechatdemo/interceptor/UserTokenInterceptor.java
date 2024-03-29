package com.example.wechatdemo.interceptor;

import com.example.wechatdemo.common.exception.ClientErrorEnum;
import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.model.dos.User;
import com.example.wechatdemo.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserTokenInterceptor implements HandlerInterceptor {

    public static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static User getUser() {
        User user = userThreadLocal.get();
        if (user == null) {
            throw new ClientException(ClientErrorEnum.TokenError);
        }
        return user;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {

        String referer = request.getHeader("referer");
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                authorization = attributes.getRequest().getParameter("authorization");
            }
        }
        log.info("referer->" + referer);
        log.info("Authorization->" + authorization);

        String host = request.getServerName();
        log.info("host->" + host);

        String requestURI = request.getRequestURI();
        log.info("requestURI->" + requestURI);

        String attribute = String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        log.info("attribute->" + attribute);

        String method = request.getMethod();
        log.info("method->" + method);
        if ("OPTIONS".equals(method)) {
            return true;
        }

        if (StringUtils.isNotBlank(authorization)) {
            authorization = authorization.replace("Bearer ", "");
            Claims userClaims = null;
            try {
                userClaims = JWTUtil.verifyToken(authorization);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (userClaims != null) {
                try {
                    User user = new User();
                    user.setUserId(Long.parseLong((String) userClaims.get("user_id")));
                    try {
                        user.setUserName((String) userClaims.get("user_name"));
                    } catch (Exception ignored) {
                    }
                    log.debug("user->" + user);
                    userThreadLocal.set(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, @Nullable Exception ex) {
        userThreadLocal.remove();
    }

}
