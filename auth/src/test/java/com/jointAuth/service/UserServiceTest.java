package com.jointAuth.service;

import com.jointAuth.model.User;
import com.jointAuth.model.UserDTO;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    //регистрация
    @Test
    public void testRegisterUserExistingEmailThrowsException() {
        User existingUser = new User(
                "Dan",
                "Dorin",
                "testing@gmail.com",
                "password");

        when(userRepository
                .findByEmail(existingUser
                        .getEmail()))
                .thenReturn(existingUser);

        User newUser = new User(
                "Vladimir",
                "Proven",
                "testing@gmail.com",
                "newpassword");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserInvalidPasswordThrowsException() {
        User newUser = new User(
                "Vladimir",
                "Proven",
                "vlprovin@gmail.com",
                "password");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserValidUserReturnsRegisteredUser() {
        User newUser = new User(
                "Vladimir",
                "Proven",
                "vlprovin@gmail.com",
                "PasswordTest123@");

        when(userRepository
                .findByEmail(anyString()))
                .thenReturn(null);

        when(userRepository
                .save(any()))
                .thenReturn(newUser);

        when(passwordEncoder.encode(newUser
                .getPassword()))
                .thenReturn("encodedPassword");

        User registeredUser = userService.register(newUser);

        assertNotNull(registeredUser);

        assertEquals("Vladimir", registeredUser.getFirstName());
        assertEquals("Proven", registeredUser.getLastName());
        assertEquals("vlprovin@gmail.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());

        assertNotNull(registeredUser.getRegistrationDate());
    }

    //авторизация
    @Test
    public void testLoginValidCredentialsReturnsUser() {
        String email = "provin@gmail.com";
        String password = "PasswordVit123@";
        String encodedPassword = "encodedPassword";
        User user = new User(
                "Vitally",
                "Provin",
                email,
                encodedPassword);

        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(passwordEncoder
                .matches(password, encodedPassword))
                .thenReturn(true);

        User loggedInUser = userService.login(email, password);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
        assertNotNull(loggedInUser.getLastLogin());
    }

    @Test
    public void testLoginInvalidEmailThrowsException() {
        String email = "maxim2001@gmail.com";
        String password = "PasswordMax123@";
        when(userRepository
                .findByEmail(email))
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.login(email, password));

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    public void testLoginInvalidPasswordThrowsException() {
        String email = "kostya01@example.com";
        String password = "badPassword";
        String encodedPassword = "encodedPassword";
        User user = new User(
                "Konstantin",
                "Molin",
                email,
                encodedPassword);

        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(passwordEncoder
                .matches(password, encodedPassword))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login(email, password));

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    //соответствие пароля и его же, но в хэшированном виде
    @Test
    public void testPasswordsMatchValidPasswordsReturnsTrue() {
        String plainPassword = "PasswordMatch123@";
        String hashedPassword = passwordEncoder.encode(plainPassword);

        when(passwordEncoder
                .matches(plainPassword, hashedPassword))
                .thenReturn(true);

        assertTrue(userService.passwordsMatch(hashedPassword, plainPassword));
    }

    @Test
    public void testPasswordsMatchInvalidPasswordsReturnsFalse() {
        String correctPassword = "correctPassword123@";
        String incorrectPassword = "incorrectPassword123@";

        String hashedPassword = passwordEncoder.encode(correctPassword);

        when(passwordEncoder
                .matches(incorrectPassword, hashedPassword))
                .thenReturn(false);

        assertFalse(userService.passwordsMatch(hashedPassword, incorrectPassword));
    }

    //конвертация в модель DTO
    @Test
    public void testConvertToDtoConvertsUserToUserDTO() {
        User user = new User(
                "Viktor",
                "Doramov",
                "vidor@gmail.com",
                "PassViktor123=");
        user.setId(1L);
        user.setRegistrationDate(new Date());
        user.setLastLogin(new Date());

        UserDTO dto = userService.convertToDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRegistrationDate(), dto.getRegistrationDate());
        assertEquals(user.getLastLogin(), dto.getLastLogin());
    }

    @Test
    public void testConvertToDtoNullFieldsInUserReturnsDtoWithNullFields() {
        User user = new User(
                "Alexander",
                null,
                "Sanya24@example.com",
                "passForTest1+");
        user.setId(1L);
        user.setRegistrationDate(new Date());
        user.setLastLogin(null);

        UserDTO dto = userService.convertToDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertNull(dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRegistrationDate(), dto.getRegistrationDate());
        assertNull(dto.getLastLogin());
    }

    //получение пользователя по Id
    @Test
    public void testGetUserByIdUserExistsReturnsUser() {
        User user = new User(
                "John",
                "Doe",
                "johndoe@example.com",
                "password");
        user.setId(1L);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testGetUserByIdUserDoesNotExistReturnsEmptyOptional() {
        when(userRepository
                .findById(2L))
                .thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetUserByIdNegativeIdReturnsEmptyOptional() {
        Optional<User> result = userService.getUserById(-1L);

        assertFalse(result.isPresent());
    }

    //получение всех пользователей
    @Test
    public void getAllUsersEmptyListReturnsEmpty() {
        when(userRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllUsersNonEmptyListReturnsListWithUsers() {
        List<User> users = List.of(
                new User(
                        "Jennie",
                        "Farina",
                           "JennieEm@gmail.com",
                        "PassEm123+"),
                new User(
                        "Jane",
                        "Smith",
                           "janeSmith@gmail.com",
                        "PassJane12@")
        );

        when(userRepository
                .findAll())
                .thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(users.size(), result.size());
        assertTrue(result.containsAll(users));
    }

    //Изменение пароля
    @Test
    public void testChangePasswordUserFoundPasswordChanged() {
        User user = new User("John", "Doe", "johndoe@example.com", "oldPass123@");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newPass123@")).thenReturn("encodedPassword");

        userService.changePassword(1L, "newPass123@");

        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testChangePasswordUserNotFoundThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(1L, "newPassword"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testChangePasswordInvalidPasswordThrowsException() {
        User user = new User("John", "Doe", "johndoe@example.com", "oldPassword");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String invalidPassword = "weak";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(1L, invalidPassword));

        assertEquals("Password does not meet the complexity requirements", exception.getMessage());

        assertEquals("oldPassword", user.getPassword());
    }
}