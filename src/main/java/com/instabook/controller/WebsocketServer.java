package com.instabook.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.config.WebSocketConfig;
import com.instabook.model.dos.Message;
import com.instabook.model.dos.User;
import com.instabook.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocket", configurator = WebSocketConfig.class)
@Component
@Slf4j
public class WebsocketServer {

    private static MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        WebsocketServer.messageService = messageService;
    }

    private static final Map<Long, Map<String, Session>> onlineSessionClientMap = new ConcurrentHashMap<>();
    private static final AtomicInteger onlineSessionClientCount = new AtomicInteger(0);
    private Long userId;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        Object userProperty = session.getUserProperties().get("user");
        if (userProperty == null) {
            throw new ClientException(ClientErrorEnum.TokenError);
        }
        User user = (User) userProperty;
        Map<String, Session> integerSessionMap = onlineSessionClientMap.get(user.getUserId());
        if (integerSessionMap == null) {
            integerSessionMap = new ConcurrentHashMap<>();
            onlineSessionClientCount.incrementAndGet();
        }
        integerSessionMap.put(session.getId(), session);
        onlineSessionClientMap.put(user.getUserId(), integerSessionMap);

        this.userId = user.getUserId();
        this.session = session;

        log.info("connect success，online num：{} ==> listening：session_id = {}， user_id = {},。", onlineSessionClientCount, session.getId(), userId);
    }

    @OnClose
    public void onClose(Session session) {
        Map<String, Session> userSessionMap = onlineSessionClientMap.get(userId);
        userSessionMap.remove(session.getId());
        if (userSessionMap.isEmpty()) {
            onlineSessionClientCount.decrementAndGet();
            onlineSessionClientMap.remove(userId);
        }
        log.info("disconnect success，online num：{} ==> listening：session_id = {}， user_id = {},。", onlineSessionClientCount, session.getId(), userId);
    }

    //{"userId": "1772586728977190914", "message": {"content": "hello"}, "type": 0}
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSON.parseObject(message);
        Long toUserId = jsonObject.getLong("userId");
        JSONObject msg = jsonObject.getJSONObject("message");
        int type = jsonObject.getIntValue("type");
        String requestId = jsonObject.getString("requestId");
        try {
            Message send = messageService.send(requestId, userId, toUserId, type, msg);
            sendMessage(send);
            send.setType(200);
            reply(send);
        } catch (ClientException e) {
            Message error = new Message();
            error.setAnotherUserId(toUserId);
            error.setUserId(userId);
            error.setChatId(Math.min(error.getUserId(), error.getAnotherUserId()) + "" + Math.max(error.getUserId(), error.getAnotherUserId()));
            error.setType(-1);
            JSONObject errorMsg = new JSONObject();
            errorMsg.put("content", e.getMessage());
            error.setMessage(errorMsg);
            reply(error);
        }
    }

    private void reply(Message message) {
        Map<String, Session> userSessionMap = onlineSessionClientMap.get(userId);
        for (Session session : userSessionMap.values()) {
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(message));
            } catch (IOException ignored) {
            }
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error：" + error.getMessage());
        error.printStackTrace();
    }

    //sendMsg when another user is online
    private void sendMessage(Message message) {
        Map<String, Session> userSessionMap = onlineSessionClientMap.get(message.getAnotherUserId());
        if (userSessionMap != null) {
            for (Session session : userSessionMap.values()) {
                try {
                    session.getBasicRemote().sendText(JSON.toJSONString(message));
                } catch (IOException ignored) {
                }
            }
        }
    }

}
