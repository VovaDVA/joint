package com.jointAuth.integration.controller;

import com.jointAuth.model.verification.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.VerificationCodeService;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthorizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwoFactorAuthVerificationCodeRepository twoFactorAuthVerificationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() {
        twoFactorAuthVerificationCodeRepository.deleteAll();
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
                .andExpect(jsonPath("$.token").exists())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    String token = response.substring(response.indexOf(":\"") + 2, response.lastIndexOf("\""));

                    Long userId = jwtTokenUtils.getCurrentUserId(token);
                    User decodedUser = userRepository.findById(userId).orElse(null);

                    assertNotNull(decodedUser);
                    assertEquals(decodedUser.getFirstName(), user.getFirstName());
                    assertEquals(decodedUser.getLastName(), user.getLastName());
                    assertEquals(decodedUser.getEmail(), user.getEmail());
                });
    }

    @Test
    public void testLoginUserWithTwoFactorAuthentication() throws Exception {
        User user = new User();
        user.setFirstName("Иван");
        user.setLastName("Петров");
        user.setEmail("ivan.petrov@gmail.com");
        user.setPassword(passwordEncoder.encode("Password123@"));
        user.setTwoFactorVerified(true);

        userRepository.save(user);

        String loginRequestJson = "{\"email\": \"ivan.petrov@gmail.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.twoFactorVerified").value(true))
                .andExpect(jsonPath("$.token").value((String) null));
    }

    @Test
    public void testLoginUserInvalidEmail() throws Exception {
        User currentUser = new User();
        currentUser.setFirstName("Денис");
        currentUser.setLastName("Стомин");
        currentUser.setEmail("DenSto@gmail.com");
        currentUser.setPassword(passwordEncoder.encode("Password123@"));
        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"invalid-email\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Неверный email или пароль"));
    }

    @Test
    public void testLoginUserUserNotFound() throws Exception {
        User currentUser = new User();
        currentUser.setFirstName("Костя");
        currentUser.setLastName("Вершин");
        currentUser.setEmail("Kostya05@gmail.com");
        currentUser.setPassword(passwordEncoder.encode("Password123@"));
        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"usserNotFound@gmail.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Неверный email или пароль"));
    }

    @Test
    public void testLoginUserIncorrectPassword() throws Exception {
        User currentUser = new User();
        currentUser.setFirstName("Максим");
        currentUser.setLastName("Дорин");
        currentUser.setEmail("Maxik94@gmail.com");
        currentUser.setPassword(passwordEncoder.encode("CorrectPassword123@"));
        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"Maxik94@gmail.com\", \"password\": \"WrongPassword123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Неверный email или пароль"));
    }

    @Test
    public void testLoginUserMissingEmail() throws Exception {
        User currentUser = new User();
        currentUser.setFirstName("Саня");
        currentUser.setLastName("Петров");
        currentUser.setEmail("Alexandr01@gmail.com");
        currentUser.setPassword(passwordEncoder.encode("Password123@"));
        userRepository.save(currentUser);

        String loginRequestJson = "{\"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Отсутствует email"));
    }

    @Test
    public void testLoginUserMissingPassword() throws Exception {
        User currentUser = new User();
        currentUser.setFirstName("Эльнара");
        currentUser.setLastName("Форина");
        currentUser.setEmail("ElFor91@gmail.com");
        currentUser.setPassword(passwordEncoder.encode("Password123@"));
        userRepository.save(currentUser);

        String loginRequestJson = "{\"email\": \"ElFor91@gmail.com\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Отсутствует пароль"));
    }

    @Test
    public void testLoginUserWithInvalidVerificationCode() throws Exception {
        User user = new User();
        user.setFirstName("Алекс");
        user.setLastName("Кузнецов");
        user.setEmail("alexkuznetsov@gmail.com");
        user.setPassword(passwordEncoder.encode("Password123@"));
        user.setTwoFactorVerified(true);
        userRepository.save(user);

        String validCode = "123456";
        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(user.getId(), validCode);

        String loginRequestJson = "{\"email\": \"alexkuznetsov@gmail.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.twoFactorVerified").value(true))
                .andExpect(jsonPath("$.token").isEmpty());

        String invalidCode = "wrongCode";
        String verifyCodeRequestJson = "{\"userId\": " + user.getId() + ", \"code\": \"" + invalidCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Пользователь не найден по коду: wrongCode"));
    }

    @Test
    public void testVerifyCodeSuccess() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Алексей");
        testUser.setLastName("Иванов");
        testUser.setEmail("alexey.ivanov@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Password123@"));
        testUser.setTwoFactorVerified(true);

        testUser = userRepository.save(testUser);

        String validCode = "123456";
        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(testUser.getId(), validCode);

        String verifyCodeRequestJson = "{\"userId\": " + testUser.getId() + ", \"code\": \"" + validCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testVerifyCodeInvalidCode() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Олег");
        testUser.setLastName("Сергеев");
        testUser.setEmail("oleg.sergeev@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Password123@"));
        testUser.setTwoFactorVerified(true);
        testUser = userRepository.save(testUser);

        String existingCode = "123456";
        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(testUser.getId(), existingCode);

        String invalidCode = "wrongCode";
        String verifyCodeRequestJson = "{\"userId\": " + testUser.getId() + ", \"code\": \"" + invalidCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Пользователь не найден по коду: wrongCode"));
    }

    @Test
    public void testVerifyCodeMissingCode() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Ирина");
        testUser.setLastName("Кузьмина");
        testUser.setEmail("irina.kuzmina@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Password123@"));
        testUser.setTwoFactorVerified(true);
        testUser = userRepository.save(testUser);

        String existingCode = "123456";
        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(testUser.getId(), existingCode);

        String verifyCodeRequestJson = "{\"userId\": " + testUser.getId() + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testVerifyCodeMissingUserIdReturnsBadRequest() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Ирина");
        testUser.setLastName("Кузьмина");
        testUser.setEmail("irina.kuzmina@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Password123@"));
        testUser.setTwoFactorVerified(true);

        testUser = userRepository.save(testUser);

        String existingCode = "123456";
        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(testUser.getId(), existingCode);

        String verifyCodeRequestJson = "{\", \"code\": \"" + existingCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isBadRequest());
    }
}