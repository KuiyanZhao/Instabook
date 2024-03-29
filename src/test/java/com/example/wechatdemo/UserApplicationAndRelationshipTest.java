package com.example.wechatdemo;

import com.alibaba.fastjson.JSON;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.controller.UserApplicationController;
import com.example.wechatdemo.controller.UserRelationshipController;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.User;
import com.example.wechatdemo.model.dos.UserApplication;
import com.example.wechatdemo.model.dos.UserRelationship;
import com.example.wechatdemo.service.UserApplicationService;
import com.example.wechatdemo.service.UserRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class UserApplicationAndRelationshipTest {

    @Resource
    private UserApplicationController userApplicationController;

    @Resource
    private UserRelationshipController userRelationshipController;

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

    @Test
    public void applyTest() {
        loginUser1();
        R<UserApplication> apply = userApplicationController.apply(1772590518656602114L);
        log.info("result:\n{}", JSON.toJSONString(apply));
    }

    @Test
    public void replyTest() {
        loginUser2();
        R<UserApplication> reply = userApplicationController.reply(1772592354595749888L, 1);
        log.info("result:\n{}", JSON.toJSONString(reply));
    }

    @Test
    public void blockTest() {
        loginUser1();
        R<UserRelationship> operate = userRelationshipController.operate(1772590518656602114L, -1);
        log.info("result:\n{}", JSON.toJSONString(operate));
    }

    @Test
    public void unblockTest() {
        loginUser1();
        R<UserRelationship> operate = userRelationshipController.operate(1772590518656602114L, 1);
        log.info("result:\n{}", JSON.toJSONString(operate));
    }

    @Test
    public void deleteFriendTest() {
        loginUser1();
        R<UserRelationship> operate = userRelationshipController.operate(1772590518656602114L, -2);
        log.info("result:\n{}", JSON.toJSONString(operate));
    }

}
