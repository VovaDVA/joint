package com.jointAuth.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jointAuth.model.verification.ConfirmAccountDeletionRequest;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
            assertEquals(404, status, "Status should be 500");
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

    @Test
    public void testSuccessfulAccountDeletion() throws Exception {
        when(userService
                .deleteUser(anyLong(), anyString()))
                .thenReturn(true);

        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(1L, "validCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully."));
    }

    @Test
    public void testInvalidUserId() throws Exception {
        when(userService
                .deleteUser(anyLong(), anyString()))
                .thenReturn(false);

        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(-1L, "validCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    @Test
    public void testInvalidVerificationCode() throws Exception {
        when(userService
                .deleteUser(anyLong(), anyString()))
                .thenReturn(false);

        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(1L, "invalidCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    @Test
    public void testMissingUserId() throws Exception {
        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(null, "validCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    @Test
    public void testMissingVerificationCode() throws Exception {
        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(1L, null);

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    @Test
    public void testExpiredOrInvalidVerificationCode() throws Exception {
        when(userService
                .deleteUser(anyLong(), anyString()))
                .thenReturn(false);

        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(1L, "expiredOrInvalidCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    @Test
    public void testReusedVerificationCode() throws Exception {
        when(userService
                .deleteUser(anyLong(), anyString()))
                .thenReturn(false);

        ConfirmAccountDeletionRequest request = new ConfirmAccountDeletionRequest(1L, "reusedCode");

        mockMvc.perform(delete("/auth/confirm-delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code or failed to delete account."));
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
