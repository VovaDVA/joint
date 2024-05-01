package com.jointAuth.integration.controller;

import com.jointAuth.model.verification.ConfirmPasswordChangeRequest;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private UserVerificationCodeRepository userVerificationCodeRepository;

    private User testUser;
    private String validToken;
    private ConfirmPasswordChangeRequest validRequest;


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
    public void testRequestPasswordResetSuccess() throws Exception {
        when(userService.sendPasswordChangeRequest(anyLong())).thenReturn(true);

        mockMvc.perform(post("/auth/change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Password change request sent to email."));

        verify(userService).sendPasswordChangeRequest(anyLong());
    }

    @Test
    public void testRequestPasswordInvalidToken() throws Exception {
        String invalidToken = "invalid.token";

        try {
            mockMvc.perform(post("/auth/change-password")
                            .header("Authorization", "Bearer " + invalidToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.err.println("MalformedJwtException: " + e.getMessage());
        }
    }

    @Test
    public void testRequestPasswordResetMissingToken() throws Exception {
        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRequestPasswordUserNotFound() throws Exception {
        User nonexistentUser = new User();
        nonexistentUser.setId(99999L);
        String tokenForNonexistentUser = jwtTokenUtils.generateToken(nonexistentUser);

        mockMvc.perform(post("/auth/change-password")
                        .header("Authorization", "Bearer " + tokenForNonexistentUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed to send password change request."));
    }

    @Test
    public void testRequestPasswordMissingToken() throws Exception {
        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testConfirmPasswordResetSuccess() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword()))
                .thenReturn(true);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + validRequest.getUserId() + ",\"verificationCode\":\"" + validRequest.getVerificationCode() + "\",\"newPassword\":\"" + validRequest.getNewPassword() + "\",\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password change successfully."));

        verify(userService)
                .changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword());
    }

    @Test
    public void testConfirmPasswordResetInvalidCode() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                "INVALID_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + validRequest.getUserId() + ",\"verificationCode\":\"INVALID_VERIFICATION_CODE\",\"newPassword\":\"" + validRequest.getNewPassword() + "\",\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));

        verify(userService)
                .changePassword(validRequest.getUserId(),
                "INVALID_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword());
    }

    @Test
    public void testConfirmPasswordResetInvalidCurrentPassword() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                "InvalidPassword"))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + validRequest.getUserId() + ",\"verificationCode\":\"" + validRequest.getVerificationCode() + "\",\"newPassword\":\"" + validRequest.getNewPassword() + "\",\"currentPassword\":\"InvalidPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));

        verify(userService)
                .changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                validRequest.getNewPassword(),
                "InvalidPassword");
    }

    @Test
    public void testConfirmPasswordResetInvalidNewPassword() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                "weakpassword",
                validRequest.getCurrentPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + validRequest.getUserId() + ",\"verificationCode\":\"" + validRequest.getVerificationCode() + "\",\"newPassword\":\"weakpassword\",\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));

        verify(userService)
                .changePassword(validRequest.getUserId(),
                validRequest.getVerificationCode(),
                "weakpassword",
                validRequest.getCurrentPassword());
    }

    @Test
    public void testConfirmPasswordResetExpiredVerificationCode() throws Exception {
        when(userService.changePassword(validRequest.getUserId(),
                "EXPIRED_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword()))
                .thenReturn(false);

        mockMvc.perform(post("/auth/confirm-change-password")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + validRequest.getUserId() + ",\"verificationCode\":\"EXPIRED_VERIFICATION_CODE\",\"newPassword\":\"" + validRequest.getNewPassword() + "\",\"currentPassword\":\"" + validRequest.getCurrentPassword() + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));

        verify(userService).changePassword(validRequest.getUserId(),
                "EXPIRED_VERIFICATION_CODE",
                validRequest.getNewPassword(),
                validRequest.getCurrentPassword());
    }
}
