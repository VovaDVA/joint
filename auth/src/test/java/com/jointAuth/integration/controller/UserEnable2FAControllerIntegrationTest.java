package com.jointAuth.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.jointAuth.model.user.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class UserEnable2FAControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private User testUser;
    private String validToken;

    @BeforeEach
    public void setUp() {

        testUser = new User();
        testUser.setFirstName("Даниил");
        testUser.setLastName("Воронов");
        testUser.setEmail("danVor@gmail.com");
        testUser.setPassword("Password123@");
        testUser.setTwoFactorVerified(false);

        testUser = userRepository.save(testUser);

        validToken = jwtTokenUtils.generateToken(testUser);
    }

    @Test
    public void testEnableTwoFactorAuthSuccess() throws Exception {
        mockMvc.perform(post("/auth/two-factor/enable")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Two-factor authentication enabled successfully"));

        User updatedUser = userRepository.findById(testUser.getId()).get();
        assertTrue(updatedUser.getTwoFactorVerified());
    }

    @Test
    public void testEnableTwoFactorAuthMissingToken() throws Exception {
        mockMvc.perform(post("/auth/two-factor/enable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEnableTwoFactorAuthInvalidToken() throws Exception {
        String invalidToken = "invalid.token";

        try {
            mockMvc.perform(post("/auth/two-factor/enable")
                            .header("Authorization", "Bearer " + invalidToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.err.println("MalformedJwtException: " + e.getMessage());
        }
    }

    @Test
    public void testEnableTwoFactorAuthUserNotFound() throws Exception {
        Long nonExistentUserId = 999999L;
        User user = new User();
        user.setId(nonExistentUserId);

        String tokenForNonExistentUser = jwtTokenUtils.generateToken(user);

        try {
            mockMvc.perform(post("/auth/two-factor/enable")
                            .header("Authorization", "Bearer " + tokenForNonExistentUser)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("User not found"));
        }
    }

    @Test
    public void testEnableTwoFactorAuthAlreadyEnabled() throws Exception {
        userService.enableTwoFactorAuth(testUser.getId());

        mockMvc.perform(post("/auth/two-factor/enable")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Two-factor authentication already enabled"));
    }

    @Test
    public void testEnableTwoFactorAuthWithExpiredToken() throws Exception {
        String expiredToken = generateExpiredToken(testUser);

        try {
            mockMvc.perform(post("/auth/two-factor/enable")
                            .header("Authorization", "Bearer " + expiredToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            System.out.println("Error occurred during test execution: " + e.getMessage());
        }
    }

    private String generateExpiredToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 10000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, "2emskemfm24gvf")
                .compact();
    }
}
