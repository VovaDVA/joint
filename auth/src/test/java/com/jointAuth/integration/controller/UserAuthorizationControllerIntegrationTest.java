package com.jointAuth.integration.controller;

import com.jointAuth.model.user.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.VerificationCodeService;
import com.jointAuth.util.JwtTokenUtils;
import org.checkerframework.checker.units.qual.A;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        user.setEmail("ivan.petrov@gmail.com");
        user.setPassword(passwordEncoder.encode("Password123@"));
        user.setTwoFactorVerified(true);

        userRepository.save(user);

        String loginRequestJson = "{\"email\": \"ivan.petrov@gmail.com\", \"password\": \"Password123@\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Two-factor authentication enabled. Verification code sent."));
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

        currentUser.setFirstName("Саня");
        currentUser.setLastName("Петров");
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

        currentUser.setFirstName("Эльнара");
        currentUser.setLastName("Форина");
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
                .andExpect(content().string("Two-factor authentication enabled. Verification code sent."));

        String invalidCode = "wrongCode";
        String verifyCodeRequestJson = "{\"userId\": " + user.getId() + ", \"code\": \"" + invalidCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
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
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid verification code."));
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
                .andExpect(status().isBadRequest());
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

    @Test
    public void testVerifyCodeExpiredCode() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Дмитрий");
        testUser.setLastName("Петров");
        testUser.setEmail("dmitriy.petrov@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Password123@"));
        testUser.setTwoFactorVerified(true);

        testUser = userRepository.save(testUser);

        String expiredCode = "123456";
        TwoFactorAuthVerificationCode twoFactorAuthVerificationCode = new TwoFactorAuthVerificationCode();

        twoFactorAuthVerificationCode.setUser(testUser);
        twoFactorAuthVerificationCode.setCode(expiredCode);
        twoFactorAuthVerificationCode.setExpirationTime(LocalDateTime.now().minusMinutes(5));

        twoFactorAuthVerificationCodeRepository.save(twoFactorAuthVerificationCode);

        String verifyCodeRequestJson = "{\"userId\": " + testUser.getId() + ", \"code\": \"" + expiredCode + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyCodeRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid verification code."));
    }
}