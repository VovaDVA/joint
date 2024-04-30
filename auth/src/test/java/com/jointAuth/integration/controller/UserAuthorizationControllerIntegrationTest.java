package com.jointAuth.integration.controller;

import com.jointAuth.model.user.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthorizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
        User user = new User();
        user.setFirstName("Максим");
        user.setLastName("Волсин");
        user.setEmail("maxVol@gmail.com");
        user.setPassword(passwordEncoder.encode("Password123@"));

        userRepository.save(user);

        String loginRequestJson = "{\"email\": \"maxVol@gmail.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    String token = response.substring(response.indexOf(":\"") + 2, response.lastIndexOf("\""));

                    Long userId = jwtTokenUtils.getCurrentUserId(token);
                    User decodedUser = userRepository.findById(userId).orElse(null);

                    assertTrue(decodedUser != null);
                    assertTrue(decodedUser.getFirstName().equals(user.getFirstName()));
                    assertTrue(decodedUser.getLastName().equals(user.getLastName()));
                    assertTrue(decodedUser.getEmail().equals(user.getEmail()));
                });
    }

    @Test
    public void testLoginUserWithTwoFactorAuthentication() throws Exception {
        User user = new User();
        user.setFirstName("Иван");
        user.setLastName("Петров");
        user.setEmail("ivan.petrov@example.com");
        user.setPassword(passwordEncoder.encode("Password123@"));
        user.setTwoFactorVerified(true);

        userRepository.save(user);

        String loginRequestJson = "{\"email\": \"ivan.petrov@example.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Two-factor authentication enabled. Verification code sent."));
    }

    @Test
    public void testLoginUserInvalidEmail() throws Exception {
        User currentUser = new User();
        String password = passwordEncoder.encode("Password123@");

        currentUser.setFirstName("Денис");
        currentUser.setLastName("Стомин");
        currentUser.setEmail("DenSto@gmail.com");
        currentUser.setPassword(password);

        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"invalid-email\", \"password\": \"Password123@\"}";

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isUnauthorized());
        });

        assertTrue(exception.getMessage().contains("Invalid email or password."));
    }

    @Test
    public void testLoginUserUserNotFound() throws Exception {
        User currentUser = new User();
        String password = passwordEncoder.encode("Password123@");

        currentUser.setFirstName("Костя");
        currentUser.setLastName("Вершин");
        currentUser.setEmail("Kostya05@gmail.com");
        currentUser.setPassword(password);

        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"usserNotFound@gmail.com\", \"password\": \"Password123@\"}";

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isUnauthorized());
        });

        assertTrue(exception.getMessage().contains("Invalid email or password."));
    }

    @Test
    public void testLoginUserIncorrectPassword() throws Exception {
        User currentUser = new User();
        String password = passwordEncoder.encode("CorrectPassword123@");

        currentUser.setFirstName("Максим");
        currentUser.setLastName("Дорин");
        currentUser.setEmail("Maxik94@gmail.com");
        currentUser.setPassword(password);

        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"Maxik94@gmail.com\", \"password\": \"WrongPassword123@\"}";

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isUnauthorized());
        });

        assertTrue(exception.getMessage().contains("Invalid email or password."));
    }

    @Test
    public void testLoginUserMissingEmail() throws Exception {
        User currentUser = new User();
        String password = passwordEncoder.encode("Password123@");

        currentUser.setFirstName("Sanya");
        currentUser.setLastName("Petrov");
        currentUser.setEmail("Alexandr01@gmail.com");
        currentUser.setPassword(password);

        userRepository.save(currentUser);

        String loginRequestJson = "{\"password\": \"Password123@\"}";

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isBadRequest());
        });

        assertTrue(exception.getMessage().contains("Missing email."));
    }

    @Test
    public void testLoginUserMissingPassword() throws Exception {
        User currentUser = new User();
        String password = passwordEncoder.encode("Password123@");

        currentUser.setFirstName("Elnara");
        currentUser.setLastName("Forina");
        currentUser.setEmail("ElFor91@gmail.com");
        currentUser.setPassword(password);

        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"ElFor91@gmail.com\"}";

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson))
                    .andExpect(status().isBadRequest());
        });

        assertTrue(exception.getMessage().contains("Missing password."));
    }
}