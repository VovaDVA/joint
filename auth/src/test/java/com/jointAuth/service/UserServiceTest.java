package com.jointAuth.service;

import com.jointAuth.model.User;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,passwordEncoder);
    }

    //регистрация
    @Test
    public void testRegisterUserExistingEmailThrowsException() {
        User existingUser = new User();
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("ExistingPassword123@");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        User newUser = new User();
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setEmail("existing@example.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verify(userRepository, never()).save(any());
    }


    @Test
    public void testRegisterUserInvalidPasswordThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Vladimir");
        newUser.setLastName("Proven");
        newUser.setEmail("vlprovin@gmail.com");
        newUser.setPassword("pass");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserValidUserReturnsRegisteredUser() {
        User newUser = new User();

        newUser.setFirstName("Vladimir");
        newUser.setLastName("Proven");
        newUser.setEmail("vlprovin@gmail.com");
        newUser.setPassword("PasswordTest123@");

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
        User newUser = new User();

        newUser.setFirstName("");
        newUser.setLastName("Simple");
        newUser.setEmail("simpleemail@gmail.com");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserEmptyLastNameThrowsException() {
        User newUser = new User();
        newUser.setFirstName("New");
        newUser.setLastName("  "); // Empty or whitespace-only last name
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("Last name cannot be empty or contain only whitespace", exception.getMessage());

        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
    }


    @Test
    public void testRegisterUserWithEmptyEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Danil");
        newUser.setLastName("Korovin");
        newUser.setEmail("");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterUserWithInvalidEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Kolya");
        newUser.setLastName("Vinilov");
        newUser.setEmail("invalid_email");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
    }

    @Test
    public void testFirstNameContainsProhibitedCharacters() {
        User user = new User();
        user.setFirstName("Vitya!");
        user.setLastName("Larin");
        user.setEmail("ViLa123@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    public void testLastNameContainsProhibitedCharacters() {
        User user = new User();
        user.setFirstName("Svetlana");
        user.setLastName("Sovina#");
        user.setEmail("Sveta01@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    public void testFirstNameTooLong() {
        User user = new User();
        user.setFirstName("AllaathanIsATooLongNameForThisTest");
        user.setLastName("Yorina");
        user.setEmail("allayo@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
    }

    @Test
    public void testLastNameTooLong() {
        User user = new User();
        user.setFirstName("Karina");
        user.setLastName("BostrovaIsATooLongLastNameForThisTest");
        user.setEmail("Bostrova2001@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        });
    }

    //авторизация
    @Test
    public void testLoginValidCredentialsReturnsUser() {
        String email = "provin@gmail.com";
        String password = "PasswordVit123@";
        String encodedPassword = "encodedPassword";
        User user = new User();

        user.setFirstName("Vitally");
        user.setLastName("Provin");
        user.setEmail(email);
        user.setPassword(encodedPassword);

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
        User user = new User();

        user.setFirstName("Konstantin");
        user.setLastName("Molin");
        user.setEmail(email);
        user.setPassword(encodedPassword);

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
        User user = new User();

        user.setId(1L);
        user.setFirstName("Volodymyr");
        user.setLastName("Sovin");
        user.setEmail("volSol1093@gmail.com");
        user.setPassword("Password123+");

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
        User firstUser = new User();
        User secondUser = new User();

        firstUser.setFirstName("Jennie");
        firstUser.setLastName("Farina");
        firstUser.setEmail("JennieEm@gmail.com");
        firstUser.setPassword("PassEm123+");

        secondUser.setFirstName("Jane");
        secondUser.setLastName("janeSmith@gmail.com");
        secondUser.setEmail("janeSmith@gmail.com");
        secondUser.setPassword("PassJane12@");

        List<User> users = List.of(
                firstUser,
                secondUser
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
        User user = new User();

        user.setId(1L);
        user.setFirstName("Vita");
        user.setLastName("Erina");
        user.setEmail("ViEr@gmail.com");
        user.setPassword("oldPass123@");

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
        User user = new User();

        user.setId(1L);
        user.setFirstName("Nasty");
        user.setLastName("Doina");
        user.setEmail("NasDo@gmail.com");
        user.setPassword("oldPass123@");

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
        User user = new User();

        user.setId(1L);
        user.setFirstName("Vanya");
        user.setLastName("Fiji");
        user.setEmail("vano123@gmail.com");
        user.setPassword("testPassword123@");

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
        User user = new User();

        user.setId(1L);
        user.setFirstName("Karim");
        user.setLastName("Sonos");
        user.setEmail("figureBest@example.com");
        user.setPassword("myPass12345@");

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