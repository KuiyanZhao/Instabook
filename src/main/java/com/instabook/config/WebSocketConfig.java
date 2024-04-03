package com.instabook.config;

import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.model.dos.User;
import com.instabook.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * WebSocketConfig
 */
@Configuration
@Slf4j
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        String authorization = null;
        Map<String, List<String>> headers = request.getHeaders();
        if (headers != null) {
            List<String> authorizations = request.getHeaders().get("authorization");
            if (authorizations != null && authorizations.size() != 0) {
                authorization = authorizations.get(0);
            }
        }

        if (authorization == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
                authorization = attributes.getRequest().getParameter("authorization");
            }
        }
        if (authorization == null) {
            throw new ClientException(ClientErrorEnum.TokenError);
        }

        log.info("Authorization->" + authorization);

        URI requestURI = request.getRequestURI();
        log.info("requestURI->" + requestURI.getPath());

        Map<String, Object> userProperties = sec.getUserProperties();
        if (StringUtils.isNotBlank(authorization)) {
            authorization = authorization.replace("Bearer ", "");
            Claims userClaims = null;
            try {
                userClaims = JWTUtil.verifyToken(authorization);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (userClaims == null) {
                throw new ClientException(ClientErrorEnum.TokenError);
            }

            try {
                User user = new User();
                user.setUserId(Long.parseLong((String) userClaims.get("user_id")));
                try {
                    user.setUserName((String) userClaims.get("user_name"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userProperties.put("user", user);
                log.debug("user->" + user);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return super.getEndpointInstance(clazz);
    }


}
