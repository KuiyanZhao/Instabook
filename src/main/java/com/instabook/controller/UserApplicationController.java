package com.instabook.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.instabook.common.model.Page;
import com.instabook.common.model.R;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.UserApplication;
import com.instabook.service.UserApplicationService;
import com.instabook.utils.PageInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * user-application controller
 * </p>
 *
 * @author
 */
@RestController
@RequestMapping("/user-application")
public class UserApplicationController {

    @Resource
    private UserApplicationService userApplicationService;

    //apply for a friend
    @PostMapping("/apply/{user_id}")
    public R<UserApplication> apply(@PathVariable("user_id") Long userId) {
        return R.success(userApplicationService.apply(userId));
    }

    //reply an application 1 for pass -1 for refuse
    @PutMapping("/reply/{application_id}/{status}")
    public R<UserApplication> reply(@PathVariable("application_id") Long applicationId,
                                    @PathVariable int status) {
        return R.success(userApplicationService.reply(applicationId, status));
    }

    //get all applications
    // if myApplication == 1 then user_id = my user id
    // if myApplication == 0 then another_user_id = my user id
    // if myApplication == null then return all
    @GetMapping("/page")
    public R<Page<UserApplication>> page(@RequestParam(required = false) Integer myApplication) {
        return R.success(userApplicationService.page(PageInfoUtil.startPage(), new QueryWrapper<UserApplication>()
                .eq(myApplication != null && myApplication == 1, "user_id", UserTokenInterceptor.getUser().getUserId())
                .eq(myApplication != null && myApplication == 0, "another_user_id", UserTokenInterceptor.getUser().getUserId())
                .and(and -> and.eq("user_id", UserTokenInterceptor.getUser().getUserId())
                        .or()
                        .eq("another_user_id", UserTokenInterceptor.getUser().getUserId()))
                .orderByDesc("application_id")));
    }
}
