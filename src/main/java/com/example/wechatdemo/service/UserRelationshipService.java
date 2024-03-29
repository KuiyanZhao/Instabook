package com.example.wechatdemo.service;

import com.example.wechatdemo.model.dos.UserRelationship;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * 
 */
public interface UserRelationshipService extends IService<UserRelationship> {

    UserRelationship operate(Long userId, int operator);

    void validateRelationship(Long anotherUserId);

    void validateFriendship(Long anotherUserId);
}
