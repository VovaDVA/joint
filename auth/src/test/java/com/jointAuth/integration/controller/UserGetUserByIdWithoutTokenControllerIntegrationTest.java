package com.jointAuth.integration.controller;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Date;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testGetUserByIdExistingUser() throws Exception {
        User newUser = new User();

        newUser.setFirstName("Петр");
        newUser.setLastName("Прунов");
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

        mockMvc.perform(get("/auth/user/get")
                        .param("userId", String.valueOf(savedUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Прунов"))
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.birthday").value("01.01.2002"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.city").value("Washington"));
    }

    @Test
    public void testGetUserByIdExistingUserWithFullProfile() throws Exception {
        User newUser = new User();

        newUser.setFirstName("Кристина");
        newUser.setLastName("Пошина");
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
                        .param("userId", String.valueOf(savedUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Кристина"))
                .andExpect(jsonPath("$.lastName").value("Пошина"))
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.birthday").value("01.01.2001"))
                .andExpect(jsonPath("$.country").value("Russia"))
                .andExpect(jsonPath("$.city").value("Moscow"));
    }

    @Test
    public void testGetUserByIdNonExistentUserId() throws Exception {
        User savedUser = new User();
        Long nonExistentUserId = 999L;

        savedUser.setId(nonExistentUserId);

        try {
            mockMvc.perform(get("/auth/user/get")
                            .param("userId", String.valueOf(savedUser.getId())))
                    .andExpect(status().isNotFound());

        } catch (jakarta.servlet.ServletException e) {
            Throwable rootCause = e.getRootCause();

            if (rootCause instanceof RuntimeException && rootCause.getMessage().contains("Пользователь не найден с userId: 999")) {

            } else {
                throw e;
            }
        }
    }

    @Test
    public void testGetUserByIdMissingUserId() {
        try {
            mockMvc.perform(get("/auth/user/get"))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        String errorMessage = result.getResponse().getErrorMessage();
                        assertFalse(errorMessage.contains("Required request parameter 'userId' for method parameter type Long is not present."));
                    });
        } catch (Exception e) {
            if (e instanceof MissingServletRequestParameterException) {
                MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
                assertTrue(ex.getParameterType().equals("Long"));
                assertTrue(ex.getParameterName().equals("userId"));
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }





    @Test
    public void testGetUserByIdInvalidFormat() throws Exception {

        mockMvc.perform(get("/auth/user/get")
                        .param("userId", "invalid-format"))
                .andExpect(status().isBadRequest());
    }
}
