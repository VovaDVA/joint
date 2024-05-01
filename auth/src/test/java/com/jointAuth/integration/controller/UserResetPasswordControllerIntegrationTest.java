package com.jointAuth.integration.controller;

import com.jointAuth.model.user.User;
import com.jointAuth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserResetPasswordControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void requestPasswordResetUserNotFound() throws Exception {
        String email = "nonexistent@gmail.com";
        when(userService
                .getUserByEmail(email))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
    }

    @Test
    void requestPasswordResetEmailSentSuccessfully() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService
                .getUserByEmail(email))
                .thenReturn(Optional.of(user));
        when(userService
                .sendPasswordResetRequest(email))
                .thenReturn(true);
        String maskedEmail = "u***@example.com";
        when(userService
                .maskEmail(email))
                .thenReturn(maskedEmail);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset request sent to email: " + maskedEmail));
    }

    @Test
    void requestPasswordResetEmailNotSent() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService
                .getUserByEmail(email))
                .thenReturn(Optional.of(user));
        when(userService
                .sendPasswordResetRequest(email))
                .thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to send password reset request."));
    }

    @Test
    void requestPasswordResetUserFoundButNotSaved() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.sendPasswordResetRequest(email)).thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to send password reset request."));
    }

    @Test
    void requestPasswordResetUserFoundButVerificationCodeNotSaved() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService
                .getUserByEmail(email))
                .thenReturn(Optional.of(user));

        when(userService
                .sendPasswordResetRequest(email))
                .thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to send password reset request."));
    }
}
