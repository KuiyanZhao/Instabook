package com.example.wechatdemo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.wechatdemo.common.model.Page;
import com.example.wechatdemo.model.dos.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * 
 */
public interface MessageService extends IService<Message> {

    Message send(Message message);

    Page<Message> getChatsAll();

    Message send(String requestId, Long userId, Long toUserId, int type, JSONObject msg);
}
