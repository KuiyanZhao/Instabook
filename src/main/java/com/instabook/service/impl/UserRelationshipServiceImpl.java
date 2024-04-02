package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.Message;
import com.instabook.model.dos.UserRelationship;
import com.instabook.mapper.UserRelationshipMapper;
import com.instabook.service.MessageService;
import com.instabook.service.UserRelationshipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 作者
 */
@Service
public class UserRelationshipServiceImpl extends ServiceImpl<UserRelationshipMapper, UserRelationship> implements UserRelationshipService {

    @Lazy
    @Resource
    private MessageService messageService;
    @Override
    public UserRelationship operate(Long userId, int operator) {
        if (!List.of(-2, -1, 1).contains(operator)) {
            throw new ClientException(ClientErrorEnum.ParamError, "operator not legal");
        }

        UserRelationship userRelationship = this.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", UserTokenInterceptor.getUser().getUserId())
                .eq("another_user_id", userId));

        //del friends
        if (operator == -2) {
            if (userRelationship == null || userRelationship.getFriendStatus() != 1) {
                throw new ClientException(ClientErrorEnum.ParamError, "he isn't your friend yet");
            }

            boolean update = this.update(new UpdateWrapper<UserRelationship>()
                    .eq("user_relationship_id", userRelationship.getUserRelationshipId())
                    .set("friend_status", 0));
            userRelationship.setFriendStatus(0);
            this.update(new UpdateWrapper<UserRelationship>()
                    .eq("user_id", userId)
                    .eq("another_user_id", UserTokenInterceptor.getUser().getUserId())
                    .set("friend_status", 0));

            if (update) {
                //delete all message
                messageService.update(new UpdateWrapper<Message>()
                        .eq("user_id", UserTokenInterceptor.getUser().getUserId())
                        .set("del_flag", 1));
            }
            return userRelationship;
        }


        //block
        if (operator == -1) {
            if (userRelationship == null) {
                userRelationship = new UserRelationship();
                userRelationship.setUserId(UserTokenInterceptor.getUser().getUserId());
                userRelationship.setAnotherUserId(userId);
                userRelationship.setUserRelationshipId(IdUtil.getSnowflakeNextId());
                userRelationship.setRelationStatus(-1);
                this.save(userRelationship);
                return userRelationship;
            }

            this.update(new UpdateWrapper<UserRelationship>()
                    .eq("user_relationship_id", userRelationship.getUserRelationshipId())
                    .set("relation_status", -1));
            userRelationship.setRelationStatus(-1);
            return userRelationship;
        }

        //unblock
        if (userRelationship == null) {
            throw new ClientException(ClientErrorEnum.RelationshipNotExist);
        }

        userRelationship.setRelationStatus(0);
        this.update(new UpdateWrapper<UserRelationship>()
                .eq("user_relationship_id", userRelationship.getUserRelationshipId())
                .set("relation_status", 0));
        return userRelationship;
    }

    @Override
    public void validateRelationship(Long anotherUserId) {
        UserRelationship userRelationship = this.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", anotherUserId)
                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId())
                .eq("relation_status", -1));

        if (userRelationship != null) {
            throw new ClientException(ClientErrorEnum.Blocked);
        }
    }

    @Override
    public void validateFriendship(Long anotherUserId) {
        UserRelationship userRelationship = this.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", anotherUserId)
                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId()));
        if (userRelationship == null) {
            throw new ClientException(ClientErrorEnum.RelationshipNotExist);
        }
        if (userRelationship.getRelationStatus() == -1) {
            throw new ClientException(ClientErrorEnum.Blocked);
        }

        if (userRelationship.getFriendStatus() != 1) {
            throw new ClientException(ClientErrorEnum.RelationshipNotExist);
        }
    }
}
