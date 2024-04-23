package com.jointAuth.integration.controller;

import com.jointAuth.model.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserChangePasswordControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private User createTestUser(String email, String password) {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setFirstName("Petr");
        newUser.setLastName("Panin");
        newUser.setEmail(email);
        newUser.setPassword(password);

        userRepository.save(newUser);
        return newUser;
    }

    @Test
    public void testChangePasswordValidUserAndToken() throws Exception {
        User testUser = createTestUser("dosoQ1@gmail.com", "oldPassword123@");

        String validToken = jwtTokenUtils.generateToken(testUser);

        assertTrue(userService.getUserById(testUser.getId()).isPresent());

        String newPassword = "PassNew123@";

        mockMvc.perform(put("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(newPassword))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangePasswordInvalidToken() throws Exception {
        User testUser = createTestUser("vor01@gmail.com", "oldPassword123@");

        String invalidToken = "invalid_token";

        String newPassword = "New_password123@";

        assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", invalidToken)
                            .content(newPassword))
                    .andExpect(status().isForbidden());
        });

    }

    @Test
    public void testChangePasswordEmptyToken() throws Exception {
        User testUser = createTestUser("needit01@gmail.com", "old123@Password");

        String emptyToken = "";

        String newPassword = "@123newPassword";

        assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", emptyToken)
                            .content(newPassword))
                    .andExpect(status().isForbidden());
        });
    }

    @Test
    public void testChangePasswordNonExistentUser() throws Exception {
        String invalidToken = "invalid_token";

        String newPassword = "newPassword123@";

        mockMvc.perform(put("/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", invalidToken)
                        .content(newPassword))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangePasswordEmptyPassword() throws Exception {
        User testUser = createTestUser("Welcome201@gmail.com", "oldPassword234++");
        String validToken = jwtTokenUtils.generateToken(testUser);

        String emptyPassword = "";

        mockMvc.perform(put("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", validToken)
                        .content(emptyPassword))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePasswordShortPassword() throws Exception {
        User testUser = createTestUser("Petr05@gmail.com", "ValidPassword@123");

        String shortPassword = "Short1";

        String validToken = jwtTokenUtils.generateToken(testUser);

        try {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", validToken)
                            .content(shortPassword))
                    .andExpect(status().isBadRequest());

            fail("Expected IllegalArgumentException due to short password");
        } catch (Exception ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                assertEquals("Password does not meet the complexity requirements", ex.getCause().getMessage());
            } else {
                throw ex;
            }
        }
    }

    @Test
    public void testChangePasswordMissingSpecialCharacter() throws Exception {
        User testUser = createTestUser("worker999@gmail.com", "ValidPassword@123");

        String passwordWithoutSpecialCharacter = "Password123";

        String validToken = jwtTokenUtils.generateToken(testUser);

        try {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", validToken)
                            .content(passwordWithoutSpecialCharacter))
                    .andExpect(status().isBadRequest());

            fail("Expected IllegalArgumentException due to missing special character in password");
        } catch (Exception ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                assertEquals("Password does not meet the complexity requirements", ex.getCause().getMessage());
            } else {
                throw ex;
            }
        }
    }

    @Test
    public void testChangePasswordMissingUpperCaseLetter() throws Exception {
        User testUser = createTestUser("caser0001@gmail.com", "ValidPassword@123");

        String passwordWithoutUpperCase = "validpassword@123";

        String validToken = jwtTokenUtils.generateToken(testUser);

        try {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", validToken)
                            .content(passwordWithoutUpperCase))
                    .andExpect(status().isBadRequest());

            fail("Expected IllegalArgumentException due to missing uppercase letter in password");
        } catch (Exception ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                assertEquals("Password does not meet the complexity requirements", ex.getCause().getMessage());
            } else {
                throw ex;
            }
        }
    }

    @Test
    public void testChangePasswordMissingDigits() throws Exception {
        User testUser = createTestUser("stracer777@gmail.com", "ValidPassword@123");

        String passwordWithoutDigits = "ValidPassword@";

        String validToken = jwtTokenUtils.generateToken(testUser);

        try {
            mockMvc.perform(put("/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", validToken)
                            .content(passwordWithoutDigits))
                    .andExpect(status().isBadRequest());

            fail("Expected IllegalArgumentException due to missing digits in password");
        } catch (Exception ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                assertEquals("Password does not meet the complexity requirements", ex.getCause().getMessage());
            } else {
                throw ex;
            }
        }
    }

}
