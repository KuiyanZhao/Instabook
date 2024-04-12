package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.User;
import com.instabook.model.dos.UserApplication;
import com.instabook.mapper.UserApplicationMapper;
import com.instabook.model.dos.UserRelationship;
import com.instabook.service.UserApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.instabook.service.UserRelationshipService;
import com.instabook.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * 
 */
@Service
public class UserApplicationServiceImpl extends ServiceImpl<UserApplicationMapper, UserApplication> implements UserApplicationService {

    @Lazy
    @Resource
    private UserRelationshipService userRelationshipService;

    @Lazy
    @Resource
    private UserService userService;

    @Override
    public UserApplication apply(Long userId) {
        if (userId.equals(UserTokenInterceptor.getUser().getUserId())) {
            throw new ClientException(ClientErrorEnum.ParamError, "can't apply for yourself!");
        }
        //validate not block
        UserRelationship userRelationship = userRelationshipService.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", userId)
                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId()));
        if (userRelationship != null) {
            if (userRelationship.getFriendStatus() == 1) {
                throw new ClientException(ClientErrorEnum.AlreadyFriend);
            } else if (userRelationship.getRelationStatus() == -1){
                throw new ClientException(ClientErrorEnum.Blocked);
            }
        }

        UserApplication userApplication = this.getOne(new QueryWrapper<UserApplication>()
                .eq("user_id", UserTokenInterceptor.getUser().getUserId())
                .eq("another_user_id", userId)
                .last("limit 1"));
        if (userApplication != null && userApplication.getStatus() == 0) {
            userApplication.setUpdateTime(new Date());
            this.update(new UpdateWrapper<UserApplication>()
                    .eq("application_id", userApplication.getApplicationId())
                    .set("update_time", userApplication.getUpdateTime()));
            return userApplication;
        }

        User user = userService.getById(UserTokenInterceptor.getUser().getUserId());

        //create a application
        userApplication = new UserApplication();
        userApplication.setApplicationId(IdUtil.getSnowflakeNextId());
        userApplication.setUserId(UserTokenInterceptor.getUser().getUserId());
        userApplication.setUserHeadImg(user.getHeadImg());
        userApplication.setUserName(user.getUserName());
        userApplication.setAnotherUserId(userId);
        userApplication.setStatus(0);
        this.save(userApplication);
        return userApplication;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserApplication reply(Long applicationId, int status) {
        UserApplication userApplication = this.getById(applicationId);
        if (userApplication == null) {
            throw new ClientException(ClientErrorEnum.ApplicationNotExist);
        }

        if (userApplication.getStatus() != 0) {
            throw new ClientException(ClientErrorEnum.AlreadyReplied);
        }

        if (!userApplication.getAnotherUserId().equals(UserTokenInterceptor.getUser().getUserId())) {
            throw new ClientException(ClientErrorEnum.ApplicationNotExist);
        }

        if (!List.of(-1, 1).contains(status)) {
            throw new ClientException(ClientErrorEnum.ParamError);
        }

        userApplication.setStatus(status);
        this.update(new UpdateWrapper<UserApplication>()
                .eq("application_id", applicationId)
                .set("status", status));

        if (status == -1) {
            return userApplication;
        }

        User me = userService.getById(UserTokenInterceptor.getUser().getUserId());
        User he = userService.getById(userApplication.getUserId());

        UserRelationship userRelationship1 = userRelationshipService.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", UserTokenInterceptor.getUser().getUserId())//me
                .eq("another_user_id", userApplication.getUserId()));//he
        if (userRelationship1 != null) {
            userRelationshipService.update(new UpdateWrapper<UserRelationship>()
                    .eq("user_relationship_id", userRelationship1.getUserRelationshipId())
                    .set("relation_status", 0)
                    .set("friend_status", 1));
        } else {
            //create a relationship
            userRelationship1 = new UserRelationship();
            userRelationship1.setUserRelationshipId(IdUtil.getSnowflakeNextId());
            userRelationship1.setUserId(me.getUserId());
            userRelationship1.setUserName(me.getUserName());
            userRelationship1.setUserHeadImg(me.getHeadImg());
            userRelationship1.setAnotherUserId(he.getUserId());
            userRelationship1.setAnotherUserName(he.getUserName());
            userRelationship1.setAnotherUserHeadImg(he.getHeadImg());
            userRelationship1.setFriendStatus(1);
            userRelationship1.setRelationStatus(0);
            userRelationshipService.save(userRelationship1);
        }

        UserRelationship userRelationship2 = userRelationshipService.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", userApplication.getUserId())//he
                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId()));//me
        if (userRelationship2 != null) {
            userRelationshipService.update(new UpdateWrapper<UserRelationship>()
                    .eq("user_relationship_id", userRelationship2.getUserRelationshipId())
                    .set("friend_status", 1));
        } else {
            userRelationship2 = new UserRelationship();
            userRelationship2.setUserRelationshipId(IdUtil.getSnowflakeNextId());
            userRelationship2.setAnotherUserId(me.getUserId());
            userRelationship2.setAnotherUserName(me.getUserName());
            userRelationship2.setAnotherUserHeadImg(me.getHeadImg());
            userRelationship2.setUserId(he.getUserId());
            userRelationship2.setUserName(he.getUserName());
            userRelationship2.setUserHeadImg(he.getHeadImg());
            userRelationship2.setFriendStatus(1);
            userRelationship2.setRelationStatus(0);
            userRelationshipService.save(userRelationship2);
        }

        return userApplication;
    }
}
