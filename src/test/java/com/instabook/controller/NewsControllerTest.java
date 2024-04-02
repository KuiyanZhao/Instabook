package com.instabook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instabook.common.model.Page;
import com.instabook.model.dos.News;
import com.instabook.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    private News validNews;

    @BeforeEach
    void setUp() {
        validNews = new News();
        validNews.setNewsId(1L);
        validNews.setMessage("Test News");
        validNews.setUserId(1L);
    }

    @Test
    void whenPublishNewsWithValidInput_thenReturnsSuccess() throws Exception {
        given(newsService.publish(any(News.class))).willReturn(validNews);

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validNews)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newsId", is(validNews.getNewsId().intValue())));
    }

    @Test
    void whenPublishNewsWithInvalidInput_thenReturnsBadRequest() throws Exception {
        // Simulating an invalid request scenario
        validNews.setMessage(""); // Invalid news content

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validNews)))
                .andExpect(status().isBadRequest());
        // Note: Actual behavior might depend on validation logic in your controller/service
    }

    @Test
    void whenPageRequest_thenReturnsNewsPage() throws Exception {
        Page<News> page = new Page<>();
        page.setRecords(java.util.Collections.singletonList(validNews));

        given(newsService.getFriendNewsAndComments()).willReturn(page);

        mockMvc.perform(get("/news/page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records", hasSize(1)))
                .andExpect(jsonPath("$.data.records[0].newsId", is(validNews.getNewsId().intValue())));
    }

    @Test
    void whenLikeNewsThatExists_thenReturnsSuccess() throws Exception {
        given(newsService.like(eq(validNews.getNewsId()), anyInt())).willReturn(validNews);

        mockMvc.perform(put("/news/{newsId}/{like}", validNews.getNewsId(), 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newsId", is(validNews.getNewsId().intValue())));
    }

    @Test
    void whenLikeNewsThatDoesNotExist_thenReturnsNotFound() throws Exception {
        given(newsService.like(eq(2L), anyInt())).willThrow(new RuntimeException("News not found"));

        mockMvc.perform(put("/news/{newsId}/{like}", 2L, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // Note: The actual status code might vary based on how your controller/service handles such cases
    }

    @Test
    void whenServiceThrowsException_thenHandleGracefully() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(newsService).publish(any(News.class));

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validNews)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.msg", containsString("Unexpected error")));
        // Note: This assumes your global exception handler returns a message in the 'msg' field of the response
    }
}
