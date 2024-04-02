package com.instabook.controller;

import com.instabook.common.model.R;
import com.instabook.model.dos.UserRelationship;
import com.instabook.service.UserRelationshipService;
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
import static org.hamcrest.Matchers.hasSize;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRelationshipController.class)
public class UserRelationshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRelationshipService userRelationshipService;

    private UserRelationship userRelationship;
    private Long userId;
    private int operator;

    @BeforeEach
    void setUp() {
        userId = 1L; // Mocked user ID
        operator = 1; // Mocked operation, e.g., 1 for unblock

        userRelationship = new UserRelationship();
        userRelationship.setUserRelationshipId(1L);
        userRelationship.setUserId(userId);
        userRelationship.setAnotherUserId(2L);
        userRelationship.setRelationStatus(operator);
    }

    @Test
    void whenOperateRelationship_thenReturnsSuccess() throws Exception {
        given(userRelationshipService.operate(eq(userId), eq(operator))).willReturn(userRelationship);

        mockMvc.perform(put("/user-relationship/operate/{user_id}/{operator}", userId, operator)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.relationStatus").value(operator));
    }

    @Test
    void whenListUserRelationships_thenReturnsList() throws Exception {
        List<UserRelationship> relationships = Arrays.asList(userRelationship);
        given(userRelationshipService.list(any())).willReturn(relationships);

        // Mock UserTokenInterceptor to return mocked user ID
        mockMvc.perform(get("/user-relationship/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].userRelationshipId").value(userRelationship.getUserRelationshipId()));
    }

}

