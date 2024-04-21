package com.jointAuth.integration.controller;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jointAuth.model.User;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.jointAuth.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static final int NAME_MAX_LENGTH = 15;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        String newUserJson = "{\"firstName\": \"Maxim\", " +
                             "\"lastName\": \"Volsin\", " +
                             "\"email\": \"maxVol@gmail.com\", " +
                             "\"password\": \"Password123@\"}";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Maxim"))
                .andExpect(jsonPath("$.lastName").value("Volsin"))
                .andExpect(jsonPath("$.email").value("maxVol@gmail.com"))
                .andExpect(jsonPath("$.registrationDate").exists())
                .andExpect(jsonPath("$.lastLogin").isEmpty());
    }

    @Test
    public void testRegisterUserInvalidEmail() throws Exception {
        String newUserJson = "{\"firstName\": \"Alexandra\", " +
                             "\"lastName\": \"Durova\", " +
                             "\"email\": \"invalid-email\", " +
                             "\"password\": \"Password123+\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Ожидалась ошибка, связанная с недопустимым форматом email.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Invalid email format"));
        }
    }

    @Test
    public void testRegisterUserEmptyFirstName() throws Exception {
        String newUserJson = "{\"firstName\": \"\", " +
                             "\"lastName\": \"Savalina\", " +
                             "\"email\": \"emptyfield@gmail.com\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Expected exception for empty first name.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("First name cannot be empty or contain only whitespace"));
        }
    }

    @Test
    public void testRegisterUserEmptyLastName() throws Exception {
        String newUserJson = "{\"firstName\": \"Kolya\", " +
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
            assertTrue(e.getMessage().contains("Last name cannot be empty or contain only whitespace"));
        }
    }

    @Test
    public void testRegisterUserEmptyEmail() {
        String newUserJson = "{\"firstName\": \"Ekaterina\", " +
                             "\"lastName\": \"Petrovna\", " +
                             "\"email\": \"\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());

            fail("Ожидалась ошибка, связанная с пустым полем email.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid email format"));
        }
    }

    @Test
    public void testRegisterUserInvalidPassword() throws Exception {
        String newUserJson = "{\"firstName\": \"Darya\", " +
                             "\"lastName\": \"Parova\", " +
                             "\"email\": \"DaHello@gmail.com\", " +
                             "\"password\": \"1234\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Expected exception for invalid password.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("Password does not meet the complexity requirements"));
        }
    }

    @Test
    public void testRegisterUserExistingEmail() throws Exception {
        String existingEmail = "existingEmail@gmail.com";

        User currentNewUser = new User();

        currentNewUser.setFirstName("Vlada");
        currentNewUser.setLastName("Astrova");
        currentNewUser.setEmail(existingEmail);
        currentNewUser.setPassword("Password123@");

        userRepository.save(currentNewUser);

        String newUserJson = "{\"firstName\": \"Darina\", " +
                             "\"lastName\": \"Dremina\", " +
                             "\"email\": \"" + existingEmail + "\", " +
                             "\"password\": \"Password123@\"}";

        try {
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newUserJson))
                    .andExpect(status().isBadRequest());
            fail("Expected exception for existing email.");
        } catch (ServletException e) {
            assertTrue(e.getMessage().contains("User with this email already exists"));
        }
    }

    @Test
    public void testRegisterUserInvalidFirstNameLength() {
        String longFirstName = "A".repeat(NAME_MAX_LENGTH + 1);

        String newUserJson = String.format("{\"firstName\": \"%s\", " +
                                           "\"lastName\": \"Teodorovich\", " +
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
            assertTrue(e.getMessage().contains("length must not exceed"));
        }
    }

    @Test
    public void testRegisterUserInvalidLastNameLength() {
        String longLastName = "B".repeat(NAME_MAX_LENGTH + 1);

        String newUserJson = String.format("{\"firstName\": \"Polina\", " +
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
            assertTrue(e.getMessage().contains("length must not exceed"));
        }
    }

    @Test
    public void testRegisterUserInvalidFirstNameCharacters() {
        String invalidFirstName = "M@xim!";

        String newUserJson = String.format("{\"firstName\": \"%s\", " +
                                           "\"lastName\": \"Forsin\", " +
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
            assertTrue(e.getMessage().contains("must contain only letters"));
        }
    }

    @Test
    public void testRegisterUserInvalidLastNameCharacters() {
        String invalidLastName = "V@ls!n";

        String newUserJson = String.format("{\"firstName\": \"Maxim\", " +
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
            assertTrue(e.getMessage().contains("must contain only letters"));
        }
    }
}