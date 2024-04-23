package com.jointAuth.integration.controller;

import com.jointAuth.model.Profile;
import com.jointAuth.model.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserGetUserByIdWithoutTokenControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testGetUserByIdExistingUser() throws Exception {
        User newUser = new User();

        newUser.setFirstName("Petr");
        newUser.setLastName("Prunov");
        newUser.setEmail("petr09@gmail.com");
        newUser.setPassword("Password123@");
        newUser.setLastLogin(new Date());

        User savedUser = userRepository.save(newUser);

        Profile newProfile = new Profile();

        newProfile.setUser(savedUser);
        newProfile.setDescription("Description");
        newProfile.setBirthday("01.01.2002");
        newProfile.setCountry("USA");
        newProfile.setCity("Washington");

        profileRepository.save(newProfile);

        String requestBody = String.format("{\"userId\": %d}", savedUser.getId());

        mockMvc.perform(get("/auth/user/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Petr"))
                .andExpect(jsonPath("$.lastName").value("Prunov"))
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.birthday").value("01.01.2002"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.city").value("Washington"));
    }

    @Test
    public void testGetUserByIdExistingUserWithFullProfile() throws Exception {
        User newUser = new User();

        newUser.setFirstName("Kristina");
        newUser.setLastName("Poshina");
        newUser.setEmail("Faster123@gmail.com");
        newUser.setPassword("Password123@");
        newUser.setLastLogin(new Date());

        User savedUser = userRepository.save(newUser);

        Profile newProfile = new Profile();

        newProfile.setUser(savedUser);
        newProfile.setDescription("Description");
        newProfile.setBirthday("01.01.2001");
        newProfile.setCountry("Russia");
        newProfile.setCity("Moscow");

        profileRepository.save(newProfile);

        mockMvc.perform(get("/auth/user/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": " + savedUser.getId() + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Kristina"))
                .andExpect(jsonPath("$.lastName").value("Poshina"))
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.birthday").value("01.01.2001"))
                .andExpect(jsonPath("$.country").value("Russia"))
                .andExpect(jsonPath("$.city").value("Moscow"));
    }

    @Test
    public void testGetUserByIdNonExistentUserId() throws Exception {
        Long nonExistentUserId = 999L;
        String requestBody = String.format("{\"userId\": %d}", nonExistentUserId);

        try {
            mockMvc.perform(get("/auth/user/get")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());

        } catch (jakarta.servlet.ServletException e) {
            Throwable rootCause = e.getRootCause();

            if (rootCause instanceof RuntimeException && rootCause.getMessage().contains("User not found with userId: 999")) {

            } else {
                throw e;
            }
        }
    }

    @Test
    public void testGetUserByIdMissingUserId() {
        try {
            mockMvc.perform(get("/auth/user/get")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
            fail("Expected InvalidDataAccessApiUsageException to be thrown due to missing userId.");
        } catch (Exception e) {
            if (e.getCause() instanceof org.springframework.dao.InvalidDataAccessApiUsageException) {
                if (e.getCause().getMessage().contains("The given id must not be null")) {
                    assertTrue(true);
                    return;
                }
            }
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserByIdInvalidFormat() throws Exception {
        String requestBody = "{\"userId\": \"invalid-format\"}";

        mockMvc.perform(get("/auth/user/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
