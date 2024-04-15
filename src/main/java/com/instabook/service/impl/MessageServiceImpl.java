package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.common.model.Page;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.Message;
import com.instabook.mapper.MessageMapper;
import com.instabook.model.dos.User;
import com.instabook.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.instabook.service.UserRelationshipService;
import com.instabook.service.UserService;
import com.instabook.utils.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Lazy
    @Resource
    private UserRelationshipService userRelationshipService;

    @Lazy
    @Resource
    private UserService userService;

    @Override
    public Message send(Message message) {
        userRelationshipService.validateFriendship(message.getAnotherUserId());

        User user = userService.getById(UserTokenInterceptor.getUser().getUserId());
        User anotherUser = userService.getById(message.getAnotherUserId());
        if (user == null || anotherUser == null) {
            throw new ClientException(ClientErrorEnum.UserNotExist);
        }

        if (StringUtils.isBlank(message.getRequestId())) {
            message.setRequestId(IdUtil.getSnowflakeNextIdStr());
        }
        message.setUserName(user.getUserName());
        message.setUserHeadImg(user.getHeadImg());
        message.setAnotherUserName(anotherUser.getUserName());
        message.setAnotherUserHeadImg(anotherUser.getHeadImg());
        message.setMessageId(IdUtil.getSnowflakeNextId());
        message.setUserId(UserTokenInterceptor.getUser().getUserId());
        message.setChatId(Math.min(message.getUserId(), message.getAnotherUserId()) + "" + Math.max(message.getUserId(), message.getAnotherUserId()));
        this.save(message);
        return message;
    }

    @Override
    public Message send(String requestId, Long userId, Long toUserId, int type, JSONObject msg) {
        User user = new User();
        user.setUserId(userId);
        UserTokenInterceptor.userThreadLocal.set(user);
        try {
            Message message = new Message();
            message.setAnotherUserId(toUserId);
            message.setMessage(msg);
            message.setType(type);
            message.setRequestId(requestId);
            return send(message);
        } finally {
            UserTokenInterceptor.userThreadLocal.remove();
        }
    }

    @Override
    public Page<Message> getChatsAll() {
        Page<Message> page = this.page(PageInfoUtil.startPage(), new QueryWrapper<Message>()
                .select("max(message_id) as message_id", "chat_id")
                .and(and -> and.eq("user_id", UserTokenInterceptor.getUser().getUserId())
                        .eq("del_flag", 0).or(or -> or
                                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId())
                                .eq("another_del_flag", 0)))
                .groupBy("chat_id")
                .orderByDesc("message_id"));
        List<Message> records = page.getRecords();
        if (records.size() != 0) {
            List<Message> list = this.list(new QueryWrapper<Message>()
                    .in("message_id", records.stream().map(Message::getMessageId).toList())
                    .orderByDesc("message_id"));
            page.setRecords(list);
        }
        return page;
    }


}
