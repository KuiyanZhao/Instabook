package com.example.wechatdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.wechatdemo.common.exception.ClientErrorEnum;
import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.common.model.Page;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.Message;
import com.example.wechatdemo.service.MessageService;
import com.example.wechatdemo.utils.PageInfoUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * message controller
 * </p>
 *
 * @author 作者
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    //send message
    //the message param will be like {"content": "xxxxxxxxxx"}
    //message is a json object can make it more flexible for expending
    @PostMapping
    public R<Message> send(@RequestBody Message message) {
        if (message.getAnotherUserId() == null) {
            throw new ClientException(ClientErrorEnum.ParamError, "please choose a user to send");
        }
        return R.success(messageService.send(message));
    }

    //need param pageNo , pageSize then return all chats
    //facebook's chat delete is supported by local app. I support it online but it is not very efficient.
    @GetMapping("/chat/page")
    public R<Page<Message>> getChatsAll() {
        return R.success(messageService.getChatsAll());
    }

    //message page in a chat
    @GetMapping("/page")
    public R<Page<Message>> page(@RequestParam String chatId) {
        if (!chatId.contains(String.valueOf(UserTokenInterceptor.getUser().getUserId()))) {
            throw new ClientException(ClientErrorEnum.DataNotExist, "chat not exist");
        }

        return R.success(messageService.page(PageInfoUtil.startPage(),
                new QueryWrapper<Message>()
                        .eq("chat_id", chatId)));
    }

    @DeleteMapping("/{message_id}")
    public R<Message> delMessage(@PathVariable("message_id") Long messageId) {
        Message message = messageService.getById(messageId);
        if (message == null) {
            throw new ClientException(ClientErrorEnum.ParamError, "message not exist");
        }

        int isSelf = message.getUserId().equals(UserTokenInterceptor.getUser().getUserId()) ? 1 : 0;
        if (isSelf != 1) {
            isSelf = message.getAnotherUserId().equals(UserTokenInterceptor.getUser().getUserId()) ? 2 : 0;
        }
        if (isSelf == 0) {
            throw new ClientException(ClientErrorEnum.ParamError, "message not exist");
        }

        messageService.update(new UpdateWrapper<Message>()
                .eq("message_id", messageId)
                .set(isSelf == 1, "del_flag", 1)
                .set(isSelf == 2, "another_del_flag", 1));
        return R.success(message);
    }


}
