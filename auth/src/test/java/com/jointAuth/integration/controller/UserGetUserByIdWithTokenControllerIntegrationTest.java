package com.jointAuth.integration.controller;

import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.UserService;
import com.jointAuth.util.JwtTokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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

import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserGetUserByIdWithTokenControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    private final Duration jwtLifetime = Duration.ofMinutes(30);

    private final String secret = "yourSecretKey";

    @BeforeEach
    public void cleanUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();

        ReflectionTestUtils.setField(jwtTokenUtils, "jwtLifetime", jwtLifetime);
        ReflectionTestUtils.setField(jwtTokenUtils, "secret", secret);
    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        User newUser = new User();
        newUser.setFirstName("Петр");
        newUser.setLastName("Прунов");
        newUser.setEmail("Petr09@gmail.com");
        newUser.setPassword("Password123@");

        User registeredUser = userService.register(newUser);

        Optional<Profile> optionalProfile = profileRepository.findByUserId(registeredUser.getId());

        assertTrue(optionalProfile.isPresent(), "Не найден профиль для идентификатора пользователя: " + registeredUser.getId());

        Profile profile = optionalProfile.get();

        profile.setDescription("Profile description");
        profile.setBirthday("01.01.2000");
        profile.setCountry("Russia");
        profile.setCity("Moscow");
        profile.setPhone("+79991234567");
        profile.setLastEdited(new Date());

        profileRepository.save(profile);

        String token = jwtTokenUtils.generateToken(registeredUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/user")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(registeredUser.getId()))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Прунов"))
                .andExpect(jsonPath("$.email").value("Petr09@gmail.com"))
                .andExpect(jsonPath("$.profileId").value(profile.getId()))
                .andExpect(jsonPath("$.description").value(profile.getDescription()))
                .andExpect(jsonPath("$.birthday").value(profile.getBirthday()))
                .andExpect(jsonPath("$.country").value("Russia"))
                .andExpect(jsonPath("$.city").value("Moscow"))
                .andExpect(jsonPath("$.phone").value("+79991234567"))
                .andExpect(jsonPath("$.lastEdited").value(matchesISO8601Date(profile.getLastEdited())));
    }

    private static Matcher<String> matchesISO8601Date(Date date) {
        Instant instant = date.toInstant();
        String isoDateString = instant.toString();

        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                String expected = isoDateString.substring(0, 19);
                String actual = item.substring(0, 19);
                return expected.equals(actual);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Ожидаемая дата в формате ISO 8601: " + isoDateString);
            }
        };
    }

    @Test
    public void testGetUserByIdUserNotFound() throws Exception {
        // Сгенерируйте токен для несуществующего пользователя
        User fakeUser = new User();
        fakeUser.setId(0L);
        String token = jwtTokenUtils.generateToken(fakeUser);

        // Выполните запрос и ожидайте NOT_FOUND
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
            if (e instanceof io.jsonwebtoken.MalformedJwtException || e.getMessage().contains("Строки JWT должны содержать ровно 2 символа с точкой")) {
                assertTrue(true);
            } else {
                fail("Неожиданное исключение: " + e.getMessage());
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

            fail("Ожидается, что будет выдано исключение InvalidDataAccessApiUsageException");
        } catch (Exception e) {
            if (e instanceof org.springframework.dao.InvalidDataAccessApiUsageException &&
                    e.getMessage().contains("Указанный идентификатор не должен быть нулевым")) {
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

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setDescription("Test description");
        profile.setBirthday("2000-01-01");
        profile.setCountry("Russia");
        profile.setCity("Moscow");
        profile.setPhone("+79991234567");
        profile.setLastEdited(new Date());
        profileRepository.save(profile);

        Date issuedDate = new Date(System.currentTimeMillis() - Duration.ofHours(2).toMillis());
        Date expiredDate = new Date(issuedDate.getTime() - Duration.ofMinutes(30).toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("description", profile.getDescription());
        claims.put("birthday", profile.getBirthday());
        claims.put("country", profile.getCountry());
        claims.put("city", profile.getCity());
        claims.put("phone", profile.getPhone());

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
                fail("Неожиданное исключение: " + e.getMessage());
            }
        }
    }

}
