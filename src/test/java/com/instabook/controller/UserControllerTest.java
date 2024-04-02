package com.instabook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instabook.common.exception.ClientException;
import com.instabook.model.dos.User;
import com.instabook.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;


import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        user.setPassword("password123");
        user.setHeadImg("headImg.jpg");
    }

    @Test
    void whenRegisterWithValidUser_thenReturnsUser() throws Exception {
        given(userService.register(any(User.class))).willReturn(user);

        mockMvc.perform(post("/user")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value(user.getUserName()));
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnsUser() throws Exception {
        given(userService.login(any(User.class))).willReturn(user);

        mockMvc.perform(get("/user")
                        .param("userName", user.getUserName())
                        .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value(user.getUserName()));
    }

    @Test
    void whenSearchWithValidUserName_thenReturnsUsers() throws Exception {
        given(userService.search(user.getUserName())).willReturn(Arrays.asList(user));

        mockMvc.perform(get("/user/search")
                        .param("userName", user.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].userName").value(user.getUserName()));
    }

    @Test
    void whenProfile_thenReturnsUserProfile() throws Exception {
        given(userService.getById(user.getUserId())).willReturn(user);

        // Assume UserTokenInterceptor.getUser().getUserId() is mocked to return user.getUserId()
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value(user.getUserName()));
    }

    @Test
    void whenUploadHeadImg_thenReturnsUpdatedUser() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        given(userService.uploadHeadImg(any(MultipartFile.class))).willReturn(user);

        mockMvc.perform(multipart("/user/profile").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.headImg").value(user.getHeadImg()));
    }

    // Additional tests can include error scenarios, such as invalid inputs or when the service layer throws an exception.
}

