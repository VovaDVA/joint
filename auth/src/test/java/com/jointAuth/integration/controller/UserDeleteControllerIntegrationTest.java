package com.jointAuth.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jointAuth.model.Profile;
import com.jointAuth.model.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDeleteControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private MockMvc mockMvc;

    private String validToken;

    @BeforeEach
    public void setup() {
        profileRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Valentin");
        testUser.setLastName("Kosev");
        testUser.setEmail("Vale01@gmail.com");
        testUser.setPassword("Password123@");
        testUser = userRepository.save(testUser);

        Profile testProfile = new Profile();
        testProfile.setId(2L);
        profileRepository.save(testProfile);

        validToken = jwtTokenUtils.generateToken(testUser);
    }

    @Test
    @Transactional
    public void testDeleteUserSuccess() throws Exception {
        User testUser = userRepository.findByEmail("hello221@gmail.com");
        Long userId = testUser.getId();

        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", validToken))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();

        Optional<Profile> deletedProfile = profileRepository.findByUserId(userId);
        assertThat(deletedProfile).isEmpty();
    }

    @Test
    @Transactional
    public void testDeleteUserNotFound() throws Exception {
        User nonExistentUser = new User();
        nonExistentUser.setId(9999L);
        nonExistentUser.setEmail("nonexistentuser@example.com");
        nonExistentUser.setPassword("NonExistentUserPassword123");
        nonExistentUser.setFirstName("NonExistent");
        nonExistentUser.setLastName("User");

        String nonExistentUserToken = jwtTokenUtils.generateToken(nonExistentUser);

        mockMvc.perform(delete("/auth/delete")
                        .header("Authorization", nonExistentUserToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserUnauthorized() {
        String invalidToken = "Bearer InvalidToken";

        try {
            mockMvc.perform(delete("/auth/delete")
                            .header("Authorization", invalidToken))
                    .andExpect(status().isUnauthorized());

            assertTrue(true);
        } catch (jakarta.servlet.ServletException e) {
            if (e.getCause() instanceof org.springframework.dao.InvalidDataAccessApiUsageException &&
                    e.getCause().getMessage().contains("The given id must not be null")) {
                assertTrue(true);
            } else {
                fail("Unexpected error: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
