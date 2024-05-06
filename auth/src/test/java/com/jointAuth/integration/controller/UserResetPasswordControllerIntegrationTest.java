package com.jointAuth.integration.controller;

import com.jointAuth.model.user.User;
import com.jointAuth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserResetPasswordControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void requestPasswordResetUserNotFound() throws Exception {
        String email = "nonexistent@gmail.com";
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с таким email не найден"));
    }

    @Test
    public void requestPasswordResetEmailSentSuccessfully() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.sendPasswordResetRequest(email)).thenReturn(true);
        String maskedEmail = "u***@example.com";
        when(userService.maskEmail(email)).thenReturn(maskedEmail);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Код отправлен на email: " + maskedEmail));
    }

    @Test
    public void requestPasswordResetEmailNotSent() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.sendPasswordResetRequest(email)).thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на сброс пароля"));
    }

    @Test
    public void requestPasswordResetUserFoundButNotSaved() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.sendPasswordResetRequest(email)).thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на сброс пароля"));
    }

    @Test
    public void requestPasswordResetUserFoundButVerificationCodeNotSaved() throws Exception {
        String email = "user@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(userService.sendPasswordResetRequest(email)).thenReturn(false);

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на сброс пароля"));
    }


    @Test
    public void requestPasswordResetInvalidEmailFormat() throws Exception {
        String invalidEmail = "invalid-email";

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", invalidEmail))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с таким email не найден"));
    }

    @Test
    public void requestPasswordResetServiceValidationError() throws Exception {
        String email = "user@gmail.com";
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));
        when(userService.sendPasswordResetRequest(email)).thenThrow(new IllegalArgumentException("Invalid email"));

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на сброс пароля"));
    }

    @Test
    public void requestPasswordResetServiceExceptionHandling() throws Exception {
        String email = "user@gmail.com";
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));
        when(userService.sendPasswordResetRequest(email)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на сброс пароля"));
    }

    @Test
    public void requestPasswordResetEmptyEmail() throws Exception {
        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", ""))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с таким email не найден"));
    }

    @Test
    public void requestPasswordResetLongEmail() throws Exception {
        String longEmail = "a".repeat(100) + "@gmail.com";

        mockMvc.perform(post("/auth/request-reset-password")
                        .param("email", longEmail))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с таким email не найден"));
    }
}
