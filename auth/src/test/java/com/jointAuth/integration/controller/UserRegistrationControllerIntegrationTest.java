package com.jointAuth.integration.controller;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.jointAuth.repository.UserRepository;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private static final int NAME_MAX_LENGTH = 15;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        String newUserJson = "{\"firstName\": \"Максим\", " +
                "\"lastName\": \"Волсин\", " +
                "\"email\": \"maxVol@gmail.com\", " +
                "\"password\": \"Password123@\"}";

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Максим"))
                .andExpect(jsonPath("$.lastName").value("Волсин"))
                .andExpect(jsonPath("$.email").value("maxVol@gmail.com"))
                .andExpect(jsonPath("$.registrationDate").exists())
                .andExpect(jsonPath("$.lastLogin").doesNotExist())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();

        String responseContent = result.getResponse().getContentAsString();
        Long userId = objectMapper.readTree(responseContent).get("id").asLong();

        assertNotNull(profileRepository.findByUserId(userId), "Профиль пользователя должен быть создан");
    }

    @Test
    public void testRegisterUserInvalidEmail() throws Exception {
        String newUserJson = "{\"firstName\": \"Александра\", " +
                             "\"lastName\": \"Дурова\", " +
                             "\"email\": \"invalid-email\", " +
                             "\"password\": \"Password123+\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Ожидалась ошибка, связанная с недопустимым форматом email.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Неверный формат электронной почты"));
        }
    }

    @Test
    public void testRegisterUserEmptyFirstName() throws Exception {
        String newUserJson = "{\"firstName\": \"\", " +
                             "\"lastName\": \"Савалина\", " +
                             "\"email\": \"emptyfield@gmail.com\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Expected exception for empty first name.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("First name не может быть пустым или состоять только из пробелов"));
        }
    }

    @Test
    public void testRegisterUserEmptyLastName() throws Exception {
        String newUserJson = "{\"firstName\": \"Коля\", " +
                             "\"lastName\": \"\", " +
                             "\"email\": \"kolyan1208@gmail.com\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Expected exception for empty last name.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Last name не может быть пустым или состоять только из пробелов"));
        }
    }

    @Test
    public void testRegisterUserEmptyEmail() {
        String newUserJson = "{\"firstName\": \"Екатерина\", " +
                             "\"lastName\": \"Петровна\", " +
                             "\"email\": \"\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с пустым полем email.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Неверный формат электронной почты"));
        }
    }

    @Test
    public void testRegisterUserInvalidPassword() throws Exception {
        String newUserJson = "{\"firstName\": \"Дарья\", " +
                             "\"lastName\": \"Парова\", " +
                             "\"email\": \"DaHello@gmail.com\", " +
                             "\"password\": \"1234\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Ожидаемое исключение для неверного пароля");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Пароль не соответствует требованиям безопасности"));
        }
    }

    @Test
    public void testRegisterUserExistingEmail() throws Exception {
        String existingEmail = "existingEmail@gmail.com";

        User currentNewUser = new User();

        currentNewUser.setFirstName("Влада");
        currentNewUser.setLastName("Астрова");
        currentNewUser.setEmail(existingEmail);
        currentNewUser.setPassword("Password123@");

        userRepository.save(currentNewUser);

        String newUserJson = "{\"firstName\": \"Дарина\", " +
                             "\"lastName\": \"Дремина\", " +
                             "\"email\": \"" + existingEmail + "\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Ожидаемое исключение для существующей электронной почты");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Пользователь с такой электронной почтой уже существует"));
        }
    }

    @Test
    public void testRegisterUserInvalidFirstNameLength() {
        String longFirstName = "О".repeat(NAME_MAX_LENGTH + 1);

        String newUserJson = String.format("{\"firstName\": \"%s\", " +
                                           "\"lastName\": \"Теодорович\", " +
                                           "\"email\": \"knowingem@gmail.com\", " +
                                           "\"password\": \"Password123@\"}",
                                           longFirstName);

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с недопустимой длиной firstName.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("не должно превышать"));
        }
    }

    @Test
    public void testRegisterUserInvalidLastNameLength() {
        String longLastName = "А".repeat(NAME_MAX_LENGTH + 1);

        String newUserJson = String.format("{\"firstName\": \"Полина\", " +
                                           "\"lastName\": \"%s\", " +
                                           "\"email\": \"polibeauty@gmail.com\", " +
                                           "\"password\": \"Password123@\"}",
                                           longLastName);

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с недопустимой длиной lastName.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("не должно превышать"));
        }
    }

    @Test
    public void testRegisterUserInvalidFirstNameCharacters() {
        String invalidFirstName = "М@ксим!";

        String newUserJson = String.format("{\"firstName\": \"%s\", " +
                                           "\"lastName\": \"Форсин\", " +
                                           "\"email\": \"Forsin2003@gmail.com\", " +
                                           "\"password\": \"Password123@\"}",
                                           invalidFirstName);

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с firstName, содержащим запрещенные символы.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("должно содержать только буквы"));
        }
    }

    @Test
    public void testRegisterUserInvalidLastNameCharacters() {
        String invalidLastName = "В@слин!n";

        String newUserJson = String.format("{\"firstName\": \"Максим\", " +
                                           "\"lastName\": \"%s\", " +
                                           "\"email\": \"maxVol@gmail.com\", " +
                                           "\"password\": \"Password123@\"}",
                                           invalidLastName);

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с lastName, содержащим запрещенные символы.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("должно содержать только буквы"));
        }
    }

    @Test
    public void testRegisterUserProfileCreated() throws Exception {
        String newUserJson = "{\"firstName\": \"Максим\", " +
                "\"lastName\": \"Волсин\", " +
                "\"email\": \"maxVol@gmail.com\", " +
                "\"password\": \"Password123@\"}";

        try {
            var result = mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isOk())
                    .andReturn();

            Long userId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id", Long.class);

            assertNotNull(profileRepository.findByUserId(userId), "Профиль пользователя должен быть создан");
        } catch (Exception e) {
            fail("Тест завершился неудачей из-за исключения: " + e.getMessage());
        }
    }
}
