package com.jointAuth.service;

import com.jointAuth.model.User;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
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

    @Test
    public void testRegisterUserWithEmptyFirstNameThrowsException() {
        User newUser = new User("", "Simple", "simpleemail@gmail.com", "PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserWithEmptyEmailThrowsException() {
        User newUser = new User("Danil", "korovin", "", "PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserWithInvalidEmailThrowsException() {
        User newUser = new User("Kolya", "Vinilov", "invalid_email", "PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
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
        User user = new User(
                "Vita",
                "Erina",
                   "ViEr@gmail.com",
                "oldPass123@");
        user.setId(1L);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder
                .encode("newPass123@"))
                .thenReturn("encodedPassword");

        userService.changePassword(1L, "newPass123@");

        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testChangePasswordUserNotFoundThrowsException() {
        when(userRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(1L, "newPassword123@"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testChangePasswordInvalidPasswordThrowsException() {
        User user = new User(
                "Nasty",
                "Doina",
                   "NasDo@gmail.com",
                "oldPass123@");
        user.setId(1L);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        String invalidPassword = "newPass!";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.changePassword(1L, invalidPassword));

        assertEquals("Password does not meet the complexity requirements", exception.getMessage());

        assertEquals("oldPass123@", user.getPassword());
    }

    //удаление пользователя
    @Test
    public void testDeleteUserSuccessfulDeletion() {
        User user = new User(
                "Vanya",
                "Fiji",
                   "vano123@gmail.com",
                "testPassword123@");
        user.setId(1L);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUserUserNotFound() {
        when(userRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteUserDatabaseError() {
        User user = new User(
                "Karim",
                "Sonos",
                   "figureBest@example.com",
                "myPass12345@");
        user.setId(1L);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        doThrow(new DataAccessException("Database error") {})
                .when(userRepository)
                .deleteById(1L);

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> userService.deleteUser(1L));

        assertEquals("Database error", exception.getMessage());

        verify(userRepository, times(1)).deleteById(1L);
    }
}