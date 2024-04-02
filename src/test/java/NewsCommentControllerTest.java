package com.example.wechatdemo.controller;

import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.model.dos.NewsComment;
import com.example.wechatdemo.service.NewsCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NewsCommentControllerTest {

    @Mock
    private NewsCommentService newsCommentService;

    @InjectMocks
    private NewsCommentController newsCommentController;

    private NewsComment newsComment;

    @BeforeEach
    void setUp() {
        newsComment = new NewsComment();
        // Set up newsComment with appropriate test data
    }

    @Test
    void replyCommentSuccess() {
        given(newsCommentService.replyComment(any(NewsComment.class))).willReturn(newsComment);
        R<NewsComment> response = newsCommentController.replyComment(newsComment);
        assertNotNull(response);
        assertEquals(newsComment, response.getData());
    }

    @Test
    void replyCommentServiceFailure() {
        given(newsCommentService.replyComment(any(NewsComment.class)))
                .willThrow(new RuntimeException("Service exception"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                newsCommentController.replyComment(newsComment));
        assertEquals("Service exception", exception.getMessage());
    }

    @Test
    void replyNewsSuccess() {
        given(newsCommentService.replyNews(any(NewsComment.class))).willReturn(newsComment);
        R<NewsComment> response = newsCommentController.replyNews(newsComment);
        assertNotNull(response);
        assertEquals(newsComment, response.getData());
    }

    @Test
    void replyNewsServiceFailure() {
        given(newsCommentService.replyNews(any(NewsComment.class)))
                .willThrow(new RuntimeException("Service exception"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                newsCommentController.replyNews(newsComment));
        assertEquals("Service exception", exception.getMessage());
    }

    // Additional tests could include validating the response when null input is given,
    // ensuring proper handling of invalid data, testing authorization if applicable, etc.

    // Tests for null inputs
    @Test
    void replyCommentNullInput() {
        assertThrows(IllegalArgumentException.class, () ->
                        newsCommentController.replyComment(null),
                "Controller should throw exception on null input");
    }

    @Test
    void replyNewsNullInput() {
        assertThrows(IllegalArgumentException.class, () ->
                        newsCommentController.replyNews(null),
                "Controller should throw exception on null input");
    }
}

