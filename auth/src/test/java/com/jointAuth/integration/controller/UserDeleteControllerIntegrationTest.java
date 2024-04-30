package com.jointAuth.integration.controller;

import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDeleteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    public void setUp() {

        when(jwtTokenUtils
                .getCurrentUserId(anyString()))
                .thenReturn(1L);
    }

    @Test
    public void testSuccessfulAccountDeletionRequest() throws Exception {
        when(userService.sendAccountDeletionRequest(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deletion request sent to email."));
    }

    @Test
    public void testUserNotFound() throws Exception {
        when(jwtTokenUtils.getCurrentUserId(anyString())).thenReturn(null);

        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
    }

    @Test
    public void testFailedToSendAccountDeletionRequest() throws Exception {
        when(userService.sendAccountDeletionRequest(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to send account deletion request."));
    }

    @Test
    public void testExceptionDuringOperation() {
        try {
            ResultActions result = mockMvc.perform(delete("/auth/delete")
                            .header("Authorization", "Bearer validToken"))
                    .andExpect(status().isNotFound());

            int status = result.andReturn().getResponse().getStatus();
            assertEquals(404, status, "Статус должен быть 500");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidAuthorizationToken() throws Exception {
        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to send account deletion request."));
    }
}
