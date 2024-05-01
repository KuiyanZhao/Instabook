package com.instabook;

import com.alibaba.fastjson.JSON;
import com.instabook.common.model.R;
import com.instabook.controller.UserController;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * test for user
 * Author - Aarnav Bomma
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserTests {

    @Resource
    private UserController userController;

    @Test
    public void registerTest() {
        User user = new User();
        user.setUserName("example user");
        user.setPassword("123456");
        R<User> register = userController.register(user);
        log.info("result:\n{}", JSON.toJSONString(register));
    }

    @Test
    public void registerTest2() {
        User user = new User();
        user.setUserName("example user2");
        user.setPassword("123456");
        R<User> register = userController.register(user);
        log.info("result:\n{}", JSON.toJSONString(register));
    }

    @Test
    public void loginTest() {
        User user = new User();
        user.setUserName("example user");
        user.setPassword("123456");
        R<User> login = userController.login(user);
        log.info("result:\n{}", JSON.toJSONString(login));
    }

    @Test
    public void loginTest2() {
        User user = new User();
        user.setUserName("example user2");
        user.setPassword("123456");
        R<User> login = userController.login(user);
        log.info("result:\n{}", JSON.toJSONString(login));
    }

    @BeforeAll
    public static void login() {
        User user = new User();
        user.setUserId(1772586728977190914L);
        user.setUserName("example user");
        UserTokenInterceptor.userThreadLocal.set(user);
    }

    @Test
    public void searchTest() {
        R<List<User>> search = userController.search("example");
        log.info("result:\n{}", JSON.toJSONString(search));
    }

    @Test
    public void profileTest() {
        R<User> profile = userController.profile();
        log.info("result:\n{}", JSON.toJSONString(profile));
    }

}
