package com.instabook.controller;

import com.instabook.model.dos.Message;
import com.instabook.model.dos.User;
import com.instabook.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebsocketServerTest {

    @Mock
    private Session session;

    @Mock
    private RemoteEndpoint.Basic basicRemote;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private WebsocketServer websocketServer;

    private User user;
    private Message message;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock session behavior
        when(session.getBasicRemote()).thenReturn(basicRemote);
        // Mock user properties
        Map<String, Object> userProperties = new HashMap<>();
        user = new User(); // Assuming User has appropriate setters
        user.setUserId(1L);
        userProperties.put("user", user);
        when(session.getUserProperties()).thenReturn(userProperties);

        // Set up message details
        message = new Message();
        message.setUserId(user.getUserId());
        message.setAnotherUserId(2L);
        message.setContent("Hello");
    }

    @Test
    public void onOpen_validSession_userJoins() {
        websocketServer.onOpen(session);
        // Assert that the user is now part of the session
        // This could be a check on a session map or a user count
    }

    @Test
    public void onClose_userDisconnects_userLeaves() {
        websocketServer.onClose(session);
        // Assert the user session is removed and count decrements
    }

    @Test
    public void onMessage_validMessage_messageSent() throws Exception {
        String jsonMessage = "{\"userId\":2,\"message\":{\"content\":\"Hello\"},\"type\":0}";
        websocketServer.onMessage(jsonMessage, session);
        // Verify the message service is called and the message is sent
        verify(messageService, times(1)).send(anyString(), anyLong(), anyLong(), anyInt(), any());
        // Verify the reply method is called
        // Note: This may involve refactoring your WebSocketServer code to be more testable
    }

    @Test
    public void onError_exceptionThrown_errorLogged() {
        Throwable error = new RuntimeException("Error");
        websocketServer.onError(session, error);
        // Assert that the error is logged
        // This may involve inspecting the logs or using a logging framework that supports assertions
    }

}

