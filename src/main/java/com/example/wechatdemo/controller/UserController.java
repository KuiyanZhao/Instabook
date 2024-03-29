package com.example.wechatdemo.controller;


import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.User;
import com.example.wechatdemo.service.FileService;
import com.example.wechatdemo.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * user controller
 * </p>
 *
 * @author 作者
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //register import userName, password userName is unique
    @PostMapping
    public R<User> register(@RequestBody User user) {
        if (user.getUserName() == null) {
            throw new ClientException("user name can't be null");
        }
        if (user.getPassword() == null) {
            throw new ClientException("password can't be null");
        }
        return R.success(userService.register(user));
    }

    @GetMapping
    public R<User> login(User user) {
        if (user.getUserName() == null) {
            throw new ClientException("user name can't be null");
        }
        if (user.getPassword() == null) {
            throw new ClientException("password can't be null");
        }
        return R.success(userService.login(user));
    }

    //search user by userName need login
    @GetMapping("/search")
    public R<List<User>> search(@RequestParam String userName) {
        if (userName == null) {
            throw new ClientException("user name can't be null");
        }
        return R.success(userService.search(userName));
    }

    @GetMapping("/profile")
    public R<User> profile() {
        User user = userService.getById(UserTokenInterceptor.getUser().getUserId());
        if (user == null) {
            throw new ClientException("user not exist");
        }
        user.setPassword(null);
        user.setSalt(null);
        return R.success(user);
    }

    @PutMapping("/profile")
    public R<User> uploadHeadImg(@RequestPart("file") MultipartFile file) {
        return R.success(userService.uploadHeadImg(file));
    }

}
