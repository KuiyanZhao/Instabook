package com.instabook.service;

import com.instabook.model.dos.UserRelationship;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * 
 */
public interface UserRelationshipService extends IService<UserRelationship> {

    UserRelationship operate(Long userId, int operator);

    void validateRelationship(Long anotherUserId);

    void validateFriendship(Long anotherUserId);
}
