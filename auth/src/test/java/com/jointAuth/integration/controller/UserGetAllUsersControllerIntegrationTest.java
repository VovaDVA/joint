package com.jointAuth.integration.controller;

import com.jointAuth.model.user.User;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserGetAllUsersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsersEmptyDatabase() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auth/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }


    @Test
    public void testGetAllUsersSuccess() throws Exception {
        String password1 = "Password123@";
        String encodedPassword1 = passwordEncoder.encode(password1);

        User user1 = new User();

        user1.setId(1L);
        user1.setFirstName("Данил");
        user1.setLastName("Дорамов");
        user1.setEmail("simpleBoy1@gmail.com");
        user1.setPassword(encodedPassword1);
        user1.setRegistrationDate(new Date());
        user1.setLastLogin(new Date());

        String password2 = "Password123@";
        String encodedPassword2 = passwordEncoder.encode(password2);

        User user2 = new User();

        user2.setId(2L);
        user2.setFirstName("Кристина");
        user2.setLastName("Зорина");
        user2.setEmail("KrisZOO@gmail.com");
        user2.setPassword(encodedPassword2);
        user2.setRegistrationDate(new Date());
        user2.setLastLogin(new Date());

        List<User> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/auth/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[0].registrationDate").isNotEmpty())
                .andExpect(jsonPath("$[0].lastLogin").isNotEmpty())
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(user2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(user2.getLastName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[1].registrationDate").isNotEmpty())
                .andExpect(jsonPath("$[1].lastLogin").isNotEmpty());
    }


    @Test
    public void testGetAllUsers_InternalServerError() throws Exception {
        doThrow(new RuntimeException("Внутренняя ошибка сервера"))
                .when(userService)
                .getAllUsers();

        mockMvc.perform(get("/auth/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUserConversionToDTO() throws Exception {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User testUser = new User();

        testUser.setId(1L);
        testUser.setFirstName("Валентин");
        testUser.setLastName("Кравчук");
        testUser.setEmail("valKra1@gmail.com");
        testUser.setPassword(encodedPassword);
        testUser.setRegistrationDate(new Date());
        testUser.setLastLogin(new Date());

        List<User> testUsers = Collections.singletonList(testUser);
        when(userService.getAllUsers()).thenReturn(testUsers);

        mockMvc.perform(get("/auth/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testUser.getId()))
                .andExpect(jsonPath("$[0].firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$[0].email").value(testUser.getEmail()))
                .andExpect(jsonPath("$[0].registrationDate").isNotEmpty())
                .andExpect(jsonPath("$[0].lastLogin").isNotEmpty())
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }
}
