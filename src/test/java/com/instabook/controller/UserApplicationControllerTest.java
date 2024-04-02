package com.instabook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instabook.common.model.Page;
import com.instabook.common.model.R;
import com.instabook.model.dos.UserApplication;
import com.instabook.service.UserApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserApplicationController.class)
public class UserApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApplicationService userApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserApplication userApplication;

    @BeforeEach
    void setUp() {
        userApplication = new UserApplication();
        // Assume these are properly initialized based on your class structure
        userApplication.setApplicationId(1L);
        userApplication.setUserId(2L); // Assuming this is the ID of the applying user
        userApplication.setAnotherUserId(3L); // Assuming this is the ID of the user being applied to
        userApplication.setStatus(0); // Assuming 0 is the initial status
    }

    @Test
    void whenApplyForFriend_thenReturnsSuccess() throws Exception {
        given(userApplicationService.apply(eq(userApplication.getUserId()))).willReturn(userApplication);

        mockMvc.perform(post("/user-application/apply/{user_id}", userApplication.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.applicationId").value(userApplication.getApplicationId()));
    }

    @Test
    void whenReplyToApplication_thenReturnsSuccess() throws Exception {
        given(userApplicationService.reply(eq(userApplication.getApplicationId()), anyInt())).willReturn(userApplication);

        mockMvc.perform(put("/user-application/reply/{application_id}/{status}", userApplication.getApplicationId(), 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value(1));
    }

    @Test
    void whenGetAllApplications_thenReturnsPage() throws Exception {
        Page<UserApplication> page = new Page<>();
        page.setRecords(java.util.Collections.singletonList(userApplication));

        given(userApplicationService.page(any(), any())).willReturn(page);

        mockMvc.perform(get("/user-application/page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records[0].applicationId").value(userApplication.getApplicationId()));
    }


}

