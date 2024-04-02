package com.instabook;

import cn.hutool.core.util.IdUtil;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.common.model.Page;
import com.instabook.common.model.R;
import com.instabook.controller.*;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstabookApplicationTests {

    @Resource
    private FileController fileController;

    @Resource
    private MessageController messageController;

    @Resource
    private NewsCommentController newsCommentController;

    @Resource
    private NewsController newsController;

    @Resource
    private UserApplicationController userApplicationController;

    @Resource
    private UserController userController;

    @Resource
    private UserRelationshipController userRelationshipController;

    private User user1;

    private User user2;

    private User user3;

    private User user4;

    private void findAllUsers() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("mock");
        UserTokenInterceptor.userThreadLocal.set(user);

        try {
            user1 = userController.search("username1").getData().get(0);
        } catch (Exception ignored) {}
        try {
            user2 = userController.search("username2").getData().get(0);
        } catch (Exception ignored) {}
        try {
            user3 = userController.search("username3").getData().get(0);
        } catch (Exception ignored) {}
        try {
            user4 = userController.search("username4").getData().get(0);
        } catch (Exception ignored) {}

        UserTokenInterceptor.userThreadLocal.remove();
    }


    @Test
    void case1_1Test() {
        User user = new User();
        user.setUserName("username1");
        user.setPassword("password");

        Exception exception = assertThrows(ClientException.class, () ->
                        userController.login(user),
                ClientErrorEnum.UserNotExist.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case1_2Test() {
        User registerUser = new User();
        registerUser.setUserName("username1");
        registerUser.setPassword("password");

        R<User> register = userController.register(registerUser);
        assertEquals(register.getCode(), 200);

        User loginUser = new User();
        loginUser.setUserName("username1");
        loginUser.setPassword("password");

        R<User> login = userController.login(loginUser);
        assertEquals(login.getCode(), 200);
        assertNotNull(login.getData());

        assertNotNull(login.getData().getToken());
        System.out.println(login.getData().getToken());
    }

    @Test
    void case1_3Test() {
        User registerUser = new User();
        registerUser.setUserName("username2");
        registerUser.setPassword("password");

        R<User> register = userController.register(registerUser);
        assertEquals(register.getCode(), 200);

        User registerUser2 = new User();
        registerUser2.setUserName("username2");
        registerUser2.setPassword("password");

        Exception exception = assertThrows(ClientException.class, () ->
                        userController.register(registerUser2),
                ClientErrorEnum.UserExist.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case1_4Test() {
        User registerUser = new User();
        registerUser.setUserName("username3");
        registerUser.setPassword("password");

        R<User> register = userController.register(registerUser);
        assertEquals(register.getCode(), 200);

        User loginUser = new User();
        loginUser.setUserName("username3");
        loginUser.setPassword("pass");

        Exception exception = assertThrows(ClientException.class, () ->
                        userController.login(loginUser),
                ClientErrorEnum.TokenError.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case1_5Test() {
        User registerUser = new User();
        registerUser.setUserName("username4");
        registerUser.setPassword("password4");

        R<User> register = userController.register(registerUser);
        assertEquals(register.getCode(), 200);

        User loginUser = new User();
        loginUser.setUserName("username3");
        loginUser.setPassword("password4");

        Exception exception = assertThrows(ClientException.class, () ->
                        userController.login(loginUser),
                ClientErrorEnum.TokenError.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case2_1Test() {
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        R<List<User>> username2 = userController.search("username2");
        assertNotNull(username2.getData());
        assertNotNull(username2.getData().get(0));

        //clear token
        UserTokenInterceptor.userThreadLocal.remove();

        Message message = new Message();
        message.setContent("hello world");
        message.setAnotherUserId(username2.getData().get(0).getUserId());
        Exception exception = assertThrows(ClientException.class, () ->
                        messageController.send(message),
                ClientErrorEnum.TokenError.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case2_2Test() {
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        R<List<User>> username2 = userController.search("username2");
        assertNotNull(username2.getData());
        assertNotNull(username2.getData().get(0));

        Message message = new Message();
        message.setContent("hello world");
        message.setAnotherUserId(username2.getData().get(0).getUserId());
        Exception exception = assertThrows(ClientException.class, () ->
                        messageController.send(message),
                ClientErrorEnum.RelationshipNotExist.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case2_3Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        R<List<User>> username2 = userController.search("username2");
        assertNotNull(username2.getData());
        assertNotNull(username2.getData().get(0));

        R<UserApplication> apply = userApplicationController.apply(username2.getData().get(0).getUserId());
        assertEquals(apply.getCode(), 200);

        Message message = new Message();
        message.setContent("hello world");
        message.setAnotherUserId(username2.getData().get(0).getUserId());
        Exception exception = assertThrows(ClientException.class, () ->
                        messageController.send(message),
                ClientErrorEnum.RelationshipNotExist.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case2_4Test() {
        findAllUsers();
        //mock login username2 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user2);

        R<Page<UserApplication>> page = userApplicationController.page(0);
        assertNotNull(page.getData().getRecords().get(0));

        R<UserApplication> reply = userApplicationController.reply(page.getData().getRecords().get(0).getApplicationId(), 1);
        assertEquals(reply.getCode(), 200);

        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        Message message = new Message();
        message.setContent("hello world");
        message.setAnotherUserId(user2.getUserId());
        message.setRequestId(IdUtil.getSnowflakeNextIdStr());

        R<Message> send = messageController.send(message);
        assertEquals(send.getCode(), 200);

        //mock login username2 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user2);

        R<Page<Message>> pageChatAll = messageController.getChatsAll();
        assertNotNull(pageChatAll.getData().getRecords().get(0));

        R<Page<Message>> pageChat = messageController.page(pageChatAll.getData().getRecords().get(0).getChatId());
        Message message1 = pageChat.getData().getRecords().stream()
                .filter(obj -> obj.getRequestId().equals(message.getRequestId())).toList().get(0);
        assertNotNull(message1);
    }

    @Test
    void case2_5Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);
        //can find2 with search or friend list
        R<UserRelationship> operate = userRelationshipController.operate(user2.getUserId(), -1);
        assertEquals(operate.getCode(), 200);

        UserTokenInterceptor.userThreadLocal.set(user2);
        Message message = new Message();
        message.setContent("hello world");
        message.setAnotherUserId(user1.getUserId());
        message.setRequestId(IdUtil.getSnowflakeNextIdStr());

        Exception exception = assertThrows(ClientException.class, () ->
                        messageController.send(message),
                ClientErrorEnum.Blocked.getMessage()
        );
        System.out.println(exception.getMessage());
    }

    @Test
    void case3_1Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "png".getBytes());
        R<User> upload = userController.uploadHeadImg(file);
        assertEquals(upload.getCode(), 200);

        R<User> profile = userController.profile();
        assertEquals(profile.getData().getHeadImg().contains("test.png"), Boolean.TRUE);
    }

    @Test
    void case4_1Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        News news = new News();
        news.setMessage("hello world");

        R<News> publish = newsController.publish(news);
        assertEquals(publish.getCode(), 200);
    }

    @Test
    void case4_2Test() {
        findAllUsers();
        UserTokenInterceptor.userThreadLocal.set(user2);

        R<Page<News>> page = newsController.page();
        assertEquals(page.getData().getRecords().size() == 0, Boolean.TRUE);
    }

    @Test
    void case4_3Test() {
        findAllUsers();
        UserTokenInterceptor.userThreadLocal.set(user3);

        R<Page<News>> page = newsController.page();
        assertEquals(page.getData().getRecords().size() == 0, Boolean.TRUE);
    }

    @Test
    void case4_4Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user1);

        //can find2 with search or friend list
        R<UserRelationship> operate = userRelationshipController.operate(user2.getUserId(), 1);
        assertEquals(operate.getCode(), 200);

        UserTokenInterceptor.userThreadLocal.set(user2);
        R<Page<News>> page = newsController.page();
        assertEquals(page.getData().getRecords().size() != 0, Boolean.TRUE);

    }

    @Test
    void case5_1Test() {
        findAllUsers();
        //mock login username1 cause there is no header
        UserTokenInterceptor.userThreadLocal.set(user2);
        R<Page<News>> page = newsController.page();
        News news = page.getData().getRecords().get(0);
        assertNotNull(news);

        NewsComment newsComment = new NewsComment();
        newsComment.setNewsId(news.getNewsId());
        newsComment.setComment("hello world");
        R<NewsComment> newsCommentR = newsCommentController.replyNews(newsComment);
        assertEquals(newsCommentR.getCode(), 200);
    }

    @Test
    void case5_2Test() {
        findAllUsers();

        //user3 login and apply user1 user2
        UserTokenInterceptor.userThreadLocal.set(user3);
        R<UserApplication> apply = userApplicationController.apply(user2.getUserId());
        assertEquals(apply.getCode(), 200);
        R<UserApplication> apply2 = userApplicationController.apply(user1.getUserId());
        assertEquals(apply2.getCode(), 200);

        //user2 reply
        UserTokenInterceptor.userThreadLocal.set(user2);
        R<UserApplication> reply = userApplicationController.reply(apply.getData().getApplicationId(), 1);
        assertEquals(reply.getCode(), 200);

        //user1 reply
        UserTokenInterceptor.userThreadLocal.set(user1);
        R<UserApplication> reply1 = userApplicationController.reply(apply2.getData().getApplicationId(), 1);
        assertEquals(reply1.getCode(), 200);

        //find the comment and comment to it
        UserTokenInterceptor.userThreadLocal.set(user3);
        R<Page<News>> page2 = newsController.page();
        assertNotNull(page2.getData().getRecords().get(0));
        assertNotNull(page2.getData().getRecords().get(0).getComments());
        NewsComment newsComment1 = page2.getData().getRecords().get(0).getComments().get(0);

        NewsComment newsComment = new NewsComment();
        newsComment.setReplyCommentId(newsComment1.getNewsCommentId());
        newsComment.setComment("hello world");
        R<NewsComment> newsCommentR = newsCommentController.replyComment(newsComment);
        assertEquals(newsCommentR.getCode(), 200);
    }
}
