package com.instabook.service;

import com.alibaba.fastjson.JSONObject;
import com.instabook.common.model.Page;
import com.instabook.model.dos.Message;
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
public interface MessageService extends IService<Message> {

    Message send(Message message);

    Page<Message> getChatsAll();

    Message send(String requestId, Long userId, Long toUserId, int type, JSONObject msg);
}
