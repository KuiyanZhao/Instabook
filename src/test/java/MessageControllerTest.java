package com.example.wechatdemo.controller;

import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.common.model.Page;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.model.dos.Message;
import com.example.wechatdemo.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;


@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setContent("Test message");
        testMessage.setUserId(1L); // Sender ID
        testMessage.setAnotherUserId(2L); // Receiver ID
    }

    @Test
    void sendMessageSuccess() {
        given(messageService.send(any(Message.class))).willReturn(testMessage);
        R<Message> response = messageController.send(testMessage);
        assertNotNull(response.getData());
        assertEquals("Test message", response.getData().getContent());
    }

    @Test
    void sendMessageFailureNoRecipient() {
        testMessage.setAnotherUserId(null); // No recipient
        assertThrows(ClientException.class, () -> messageController.send(testMessage));
    }

    @Test
    void getChatsAllSuccess() {
        Page<Message> page = new Page<>();
        page.setRecords(Collections.singletonList(testMessage));
        given(messageService.getChatsAll()).willReturn(page);
        R<Page<Message>> response = messageController.getChatsAll();
        assertFalse(response.getData().getRecords().isEmpty());
        assertEquals(1, response.getData().getRecords().size());
    }

    @Test
    void pageSuccess() {
        Page<Message> page = new Page<>();
        page.setRecords(Collections.singletonList(testMessage));
        given(messageService.page(any(), any(QueryWrapper.class))).willReturn(page);
        R<Page<Message>> response = messageController.page("chatId");
        assertFalse(response.getData().getRecords().isEmpty());
    }

    @Test
    void pageFailureChatNotExist() {
        // Assuming UserTokenInterceptor.getUser().getUserId() returns a user ID not contained in `chatId`
        // This requires mocking static method UserTokenInterceptor.getUser(), which is beyond basic Mockito capabilities
        // Consider refactoring for better testability or using additional tools like PowerMock
        assertThrows(ClientException.class, () -> messageController.page("invalidChatId"));
    }

    @Test
    void deleteMessageSuccess() {
        // Mock the messageService.getById() response
        given(messageService.getById(anyLong())).willReturn(testMessage);
        // Assume the messageService.update() operation is successful
        doNothing().when(messageService).update(any(UpdateWrapper.class));

        R<Message> response = messageController.delMessage(1L);
        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
    }

    @Test
    void deleteMessageFailureMessageNotExist() {
        given(messageService.getById(anyLong())).willReturn(null);
        assertThrows(ClientException.class, () -> messageController.delMessage(1L));
    }

    // Add more tests as necessary to cover other edge cases and scenarios
}

