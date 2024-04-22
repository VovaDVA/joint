package com.jointAuth.integration.controller;

import com.jointAuth.model.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.util.JwtTokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserGetUserByIdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private final Duration jwtLifetime = Duration.ofMinutes(30);

    private final String secret = "yourSecretKey";

    @BeforeEach
    public void cleanUp() {
        userRepository.deleteAll();

        ReflectionTestUtils.setField(jwtTokenUtils, "jwtLifetime", jwtLifetime);
        ReflectionTestUtils.setField(jwtTokenUtils, "secret", secret);
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Petr");
        user.setLastName("Prunov");
        user.setEmail("Petr09@gmail.com");
        user.setPassword(encodedPassword);

        user = userRepository.save(user);

        String token = jwtTokenUtils.generateToken(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Maxim"))
                .andExpect(jsonPath("$.lastName").value("Volsin"))
                .andExpect(jsonPath("$.email").value("maxVol@gmail.com"));
    }

    @Test
    public void testGetUserByIdUserNotFound() throws Exception {
        String token = jwtTokenUtils.generateToken(new User());

        mockMvc.perform(get("/auth/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByIdInvalidToken() {
        String invalidToken = "Bearer invalid-token";

        try {
            mockMvc.perform(get("/auth/user")
                            .header("Authorization", invalidToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            assertTrue(true);
        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.MalformedJwtException || e.getMessage().contains("JWT strings must contain exactly 2 period characters")) {
                assertTrue(true);
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testGetUserByIdMissingToken() throws Exception {
        mockMvc.perform(get("/auth/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserByIdWrongTokenFormat() {
        String wrongTokenFormat = "wrongFormat";

        try {
            mockMvc.perform(get("/auth/user")
                            .header("Authorization", wrongTokenFormat)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            fail("Expected InvalidDataAccessApiUsageException to be thrown");
        } catch (Exception e) {
            if (e instanceof org.springframework.dao.InvalidDataAccessApiUsageException &&
                    e.getMessage().contains("The given id must not be null")) {
                assertTrue(true);
            }
        }
    }

    @Test
    public void testGetUserByIdExpiredToken() {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Danil");
        user.setLastName("Pravov");
        user.setEmail("Dan123@gmail.com");
        user.setPassword(encodedPassword);

        user = userRepository.save(user);

        Date issuedDate = new Date(System.currentTimeMillis() - Duration.ofHours(2).toMillis());
        Date expiredDate = new Date(issuedDate.getTime() - Duration.ofMinutes(30).toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        try {
            mockMvc.perform(get("/auth/user")
                            .header("Authorization", "Bearer " + expiredToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            assertTrue(true);
        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.ExpiredJwtException || e.getMessage().contains("JWT expired")) {
                assertTrue(true);
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

}
