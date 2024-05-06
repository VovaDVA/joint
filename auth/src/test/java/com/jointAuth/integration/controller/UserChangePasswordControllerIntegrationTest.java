package com.jointAuth.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jointAuth.bom.user.ErrorResponse;
import com.jointAuth.model.verification.ConfirmPasswordChangeRequest;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserChangePasswordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private User testUser;
    private String validToken;

    private ConfirmPasswordChangeRequest validRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setFirstName("Максим");
        testUser.setLastName("Дуров");
        testUser.setEmail("maximka03@gmail.com");
        testUser.setPassword("Password123@");

        testUser = userRepository.save(testUser);
        validToken = jwtTokenUtils.generateToken(testUser);

        validRequest = new ConfirmPasswordChangeRequest();
        validRequest.setUserId(testUser.getId());
        validRequest.setVerificationCode("VALID_VERIFICATION_CODE");
        validRequest.setNewPassword("NewPassword123@");
        validRequest.setCurrentPassword("Password123@");
    }

    @Test
    public void testRequestPasswordChangeSuccess() throws Exception {
        when(userService
                .sendPasswordChangeRequest(anyLong()))
                .thenReturn(true);

        mockMvc.perform(post("/auth/change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Запрос на изменение пароля отправлен на электронную почту"));

        verify(userService)
                .sendPasswordChangeRequest(anyLong());
    }

    @Test
    public void testRequestPasswordInvalidToken() throws Exception {
        String invalidToken = "invalid.token";

        try {
            mockMvc.perform(post("/auth/change-password")
                            .header("Authorization", "Bearer " + invalidToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        } catch (MalformedJwtException e) {
            System.err.println("MalformedJwtException: " + e.getMessage());
        }
    }

    @Test
    public void testRequestPasswordChangeMissingToken() throws Exception {
        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRequestPasswordChangeUserNotFound() throws Exception {

        mockMvc.perform(post("/auth/change-password")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiLQnNCw0LrRgdC40LwiLCJsYXN0TmFtZSI6ItCf0YDQuNC80LDQuiIsInByb2ZpbGVJZCI6NCwiZXhwIjoxNzE2MTU0OTc5LCJ1c2VySWQiOjQsImlhdCI6MTcxNDk0NTM3OSwiZW1haWwiOiJwbTIyMjcxMEBtYWlsLnJ1In0.-GklNWiz1ktLJ0UKn-CJYm_XpSqRyfIPzGxDHr8t180")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Не удалось отправить запрос на изменение пароля"));

    }

    @Test
    public void testRequestPasswordMissingToken() throws Exception {
        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testConfirmPasswordChangeSuccess() throws Exception {
        when(userService.changePassword(
                validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword()))
                .thenReturn(true);

        String requestBody = "{\"userId\":" + validRequest.getUserId() + ","
                + "\"verificationCode\":\"" + validRequest.getVerificationCode() + "\","
                + "\"newPassword\":\"" + validRequest.getNewPassword() + "\","
                + "\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}";

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Пароль успешно изменен"));

        verify(userService).changePassword(
                validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword());
    }

    @Test
    public void testConfirmPasswordChangeInvalidCode() throws Exception {
        ConfirmPasswordChangeRequest invalidRequest = new ConfirmPasswordChangeRequest();
        invalidRequest.setUserId(validRequest.getUserId());
        invalidRequest.setVerificationCode("INVALID_VERIFICATION_CODE");
        invalidRequest.setNewPassword(validRequest.getNewPassword());
        invalidRequest.setCurrentPassword(validRequest.getCurrentPassword());

        when(userService.changePassword(
                invalidRequest.getUserId(),
                invalidRequest.getVerificationCode(),
                invalidRequest.getNewPassword(),
                invalidRequest.getCurrentPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(asJsonString(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Не удалось изменить пароль"))));
    }

    @Test
    public void testConfirmPasswordChangeInvalidCurrentPassword() throws Exception {
        ConfirmPasswordChangeRequest invalidRequest = new ConfirmPasswordChangeRequest();
        invalidRequest.setUserId(validRequest.getUserId());
        invalidRequest.setVerificationCode(validRequest.getVerificationCode());
        invalidRequest.setNewPassword(validRequest.getNewPassword());
        invalidRequest.setCurrentPassword("invalidPassword");

        when(userService.changePassword(
                invalidRequest.getUserId(),
                invalidRequest.getVerificationCode(),
                invalidRequest.getNewPassword(),
                invalidRequest.getCurrentPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(asJsonString(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Не удалось изменить пароль"))));
    }

    @Test
    public void testConfirmPasswordChangeInvalidNewPassword() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                "weakpassword",
                validRequest.getCurrentPassword()))
                .thenReturn(false);

        String requestBody = "{\"userId\":" + validRequest.getUserId() + ","
                + "\"verificationCode\":\"" + validRequest.getVerificationCode() + "\","
                + "\"newPassword\":\"weakpassword\","
                + "\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}";

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Не удалось изменить пароль"));  // Ожидаем сообщение о неудаче.

        verify(userService).changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                "weakpassword",
                validRequest.getCurrentPassword());
    }

    @Test
    public void testConfirmPasswordChangeExpiredVerificationCode() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                "EXPIRED_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword()))
                .thenReturn(false);

        String requestBody = "{\"userId\":" + validRequest.getUserId() + ","
                + "\"verificationCode\":\"EXPIRED_VERIFICATION_CODE\","
                + "\"newPassword\":\"" + validRequest.getNewPassword() + "\","
                + "\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}";

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Не удалось изменить пароль"));  // Ожидаем сообщение о неудаче.

        verify(userService).changePassword(validRequest.getUserId(),
                "EXPIRED_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword());
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при преобразовании объекта в JSON строку", e);
        }
    }
}
