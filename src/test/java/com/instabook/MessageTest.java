package com.instabook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.instabook.common.model.Page;
import com.instabook.common.model.R;
import com.instabook.controller.MessageController;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.Message;
import com.instabook.model.dos.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class MessageTest {

    private void loginUser1() {
        User user = new User();
        user.setUserId(1772586728977190914L);
        user.setUserName("example user");
        UserTokenInterceptor.userThreadLocal.set(user);
    }

    private void loginUser2() {
        User user = new User();
        user.setUserId(1772590518656602114L);
        user.setUserName("example user2");
        UserTokenInterceptor.userThreadLocal.set(user);
    }

    @Resource
    private MessageController messageController;

    @Test
    public void sendTest() {
        loginUser1();
        Message message = new Message();
        message.setAnotherUserId(1772590518656602114L);
        JSONObject messageContent = new JSONObject();
        messageContent.put("content", "hello");
        message.setMessage(messageContent);
        messageController.send(message);
    }

    @Test
    public void getChatsAllTest() {
        loginUser1();
        R<Page<Message>> chats = messageController.getChatsAll();
        log.info("\nresult:\n{}", JSON.toJSONString(chats));
    }

    @Test
    public void getChatTest() {
        loginUser1();
        R<Page<Message>> chats = messageController.page("17725867289771909141772590518656602114");
        log.info("\nresult:\n{}", JSON.toJSONString(chats));
    }
}
