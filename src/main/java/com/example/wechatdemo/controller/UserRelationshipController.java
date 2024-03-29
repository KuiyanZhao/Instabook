package com.example.wechatdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.UserRelationship;
import com.example.wechatdemo.service.UserRelationshipService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  user-relationship controller
 * </p>
 *
 */
@RestController
@RequestMapping("/user-relationship")
public class UserRelationshipController {

    @Resource
    private UserRelationshipService userRelationshipService;

    //operate the relationship -1:block 1:unBlock -2:remove
    @PutMapping("/operate/{user_id}/{operator}")
    public R<UserRelationship> operate(@PathVariable("user_id") Long userId,
                                     @PathVariable int operator) {
        return R.success(userRelationshipService.operate(userId, operator));
    }

    @GetMapping("/list")
    public R<List<UserRelationship>> list() {
        return R.success(userRelationshipService.list(new QueryWrapper<UserRelationship>()
                .eq("user_id", UserTokenInterceptor.getUser().getUserId())
                .eq("friend_status", 1)));
    }


}
