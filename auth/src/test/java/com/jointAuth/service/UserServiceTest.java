package com.jointAuth.service;

import com.jointAuth.model.verification.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.model.profile.Profile;
import com.jointAuth.bom.user.UserBom;
import com.jointAuth.bom.user.UserProfileBom;
import com.jointAuth.model.verification.UserVerificationCode;
import com.jointAuth.repository.PasswordResetVerificationCodeRepository;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
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
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationCodeService verificationCodeService;

    @Mock
    private EmailService emailService;

    @Mock
    UserVerificationCodeRepository userVerificationCodeRepository;

    @Mock
    PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository;

    @InjectMocks
    private UserService userService;

    private UserService userServiceSpy;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        profileRepository = mock(ProfileRepository.class);
        verificationCodeService = mock(VerificationCodeService.class);
        emailService = mock(EmailService.class);
        userVerificationCodeRepository = mock(UserVerificationCodeRepository.class);
        passwordResetVerificationCodeRepository = mock(PasswordResetVerificationCodeRepository.class);

        userServiceSpy = spy(userService);

        userService = new UserService(userRepository,
                passwordEncoder,
                profileRepository,
                verificationCodeService,
                emailService,
                userVerificationCodeRepository,
                passwordResetVerificationCodeRepository);

        userRepository.deleteAll();
        profileRepository.deleteAll();
    }

    //регистрация
    @Test
    public void testRegisterUserExistingEmailThrowsException() {
        User existingUser = new User();

        existingUser.setFirstName("Карина");
        existingUser.setLastName("Иванова");
        existingUser.setEmail("Ivanova10@gmail.com");
        existingUser.setPassword("ExistingPassword123@");

        when(userRepository
                .findByEmail(existingUser.getEmail()))
                .thenReturn(existingUser);

        User newUser = new User();

        newUser.setFirstName("Ирина");
        newUser.setLastName("Иванова");
        newUser.setEmail("Ivanova10@gmail.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail("Ivanova10@gmail.com");
        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }


    @Test
    public void testRegisterUserInvalidPasswordThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Владимир");
        newUser.setLastName("Провин");
        newUser.setEmail("vlprovin@gmail.com");
        newUser.setPassword("pass");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testRegisterUserValidUserReturnsRegisteredUser() {
        User newUser = new User();

        newUser.setFirstName("Владимир");
        newUser.setLastName("Провин");
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

        assertEquals("Владимир", registeredUser.getFirstName());
        assertEquals("Провин", registeredUser.getLastName());
        assertEquals("vlprovin@gmail.com", registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());

        assertNotNull(registeredUser.getRegistrationDate());

        verify(profileRepository, times(1))
                .save(any());
    }

    @Test
    public void testRegisterUserWithEmptyFirstNameThrowsException() {
        User newUser = new User();

        newUser.setFirstName("");
        newUser.setLastName("Симпл");
        newUser.setEmail("simpleemail@gmail.com");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testRegisterUserEmptyLastNameThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Полина");
        newUser.setLastName("  ");
        newUser.setEmail("polinaR1@gmail.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("Last name cannot be empty or contain only whitespace", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(any());
        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testRegisterUserWithEmptyEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Данил");
        newUser.setLastName("Коровин");
        newUser.setEmail("");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testRegisterFirstNameEmpty() {
        User user = new User();
        user.setFirstName("");
        user.setLastName("Дорамин");
        user.setEmail("oneTo@gmail.com");
        user.setPassword("StrongPassword123!");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        }, "First name cannot be empty or contain only whitespace");
    }

    @Test
    public void testRegisterFirstNameWhitespace() {
        User user = new User();
        user.setFirstName("   ");
        user.setLastName("Форламов");
        user.setEmail("hello0001@gmail.com");
        user.setPassword("StrongPassword123!");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        }, "First name cannot be empty or contain only whitespace");
    }

    @Test
    public void testRegisterUserWithInvalidEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Коля");
        newUser.setLastName("Винилов");
        newUser.setEmail("invalid_email");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testFirstNameContainsProhibitedCharacters() {
        User user = new User();

        user.setFirstName("Витя!");
        user.setLastName("Ларин");
        user.setEmail("ViLa123@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testLastNameContainsProhibitedCharacters() {
        User user = new User();

        user.setFirstName("Светлана");
        user.setLastName("Совина#");
        user.setEmail("Sveta01@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testFirstNameTooLong() {
        User user = new User();

        user.setFirstName("СЛИШКОМДЛИННЫЙТЕКСДЛЯИМЕНИ");
        user.setLastName("Юрина");
        user.setEmail("allayo@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never())
                .save(any());
        verify(profileRepository, never())
                .save(any());
    }

    @Test
    public void testRegisterInvalidEmailNull() {
        User user = new User();
        user.setFirstName("Виктор");
        user.setLastName("Ворламов");
        user.setEmail(null);
        user.setPassword("StrongPassword123!");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        }, "Invalid email format");
    }

    @Test
    public void testRegisterInvalidEmailFormat() {
        User user = new User();
        user.setFirstName("Джон");
        user.setLastName("Киришев");
        user.setEmail("john.doe@com");
        user.setPassword("StrongPassword123!");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.register(user);
        }, "Invalid email format");
    }

    @Test
    public void testLastNameTooLong() {
        User user = new User();

        user.setFirstName("Карина");
        user.setLastName("СЛИШКОМДЛИННЫЙТЕКСДЛЯФАМИЛИИ");
        user.setEmail("Bostrova2001@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    //авторизация
    @Test
    public void testLoginValidCredentialsReturnsUser() {
        String email = "provin@gmail.com";
        String password = "PasswordVit123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Виталий");
        user.setLastName("Провин");
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(passwordEncoder
                .matches(password, encodedPassword))
                .thenReturn(true);

        User loggedInUser = userService.login(email, password);

        verify(userRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(password, encodedPassword);

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
        assertNotNull(loggedInUser.getLastLogin());
    }

    @Test
    public void testLoginInvalidEmailThrowsException() {
        String email = "maxim2001@gmail.com";
        String password = "PasswordMax123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Карим");
        user.setLastName("Валов");
        user.setEmail("Karim03@gmail.com");
        user.setPassword(encodedPassword);

        when(userRepository
                .findByEmail(email))
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.login(email, password));

        verify(userRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    public void testLoginInvalidPasswordThrowsException() {
        String email = "kostya01@example.com";
        String password = "badPassword123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Константин");
        user.setLastName("Молин");
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(passwordEncoder
                .matches(password, encodedPassword))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.login(email, password));

        verify(userRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(password, encodedPassword);
    }

    @Test
    public void testLoginWithNullEmail() {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Петр");
        user.setLastName("Елов");
        user.setEmail(null);
        user.setPassword(encodedPassword);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(user.getEmail(), password);
        });
    }

    @Test
    public void testLoginWithEmptyEmail() {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Селим");
        user.setLastName("Ванов");
        user.setEmail("");
        user.setPassword(encodedPassword);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(user.getEmail(), password);
        });
    }

    @Test
    public void testLoginWithNullPassword() {
        String email = "forexample@gmail.com";

        User user = new User();

        user.setFirstName("Максим");
        user.setLastName("Орлов");
        user.setEmail(email);
        user.setPassword(null);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(email, user.getPassword());
        });
    }

    @Test
    public void testLoginWithEmptyPassword() {
        String email = "forexample@gmail.com";

        User user = new User();

        user.setFirstName("Владимир");
        user.setLastName("Крелин");
        user.setEmail(email);
        user.setPassword("");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.login(email, "");
        });
    }

    @Test
    public void testLoginWithValidCredentialsAnd2FA() {
        String email = "vov4ik@gmail.com";
        String password = "ValidPassword123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setFirstName("Вова");
        user.setLastName("Петров");
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setTwoFactorVerified(true);

        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(passwordEncoder
                .matches(password, encodedPassword))
                .thenReturn(true);

        doAnswer(invocation -> {
            return null;
        }).when(emailService).sendVerificationCodeByEmail(eq(user), anyString());

        User loggedInUser = userService.login(email, password);

        verify(userRepository, times(1))
                .findByEmail(email);
        verify(passwordEncoder, times(1))
                .matches(password, encodedPassword);
        verify(emailService, times(1))
                .sendVerificationCodeByEmail(eq(user), anyString());

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
        assertNotNull(loggedInUser.getLastLogin());
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
        user.setFirstName("Владимир");
        user.setLastName("Совин");
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

    //получение пользователя по почте
    @Test
    void testGetUserByEmailUserFound() {
        String email = "test@gmail.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        when(userRepository
                .findByEmail(email))
                .thenReturn(expectedUser);

        Optional<User> result = userService.getUserByEmail(email);

        assertEquals(Optional.of(expectedUser), result);
        verify(userRepository, times(1))
                .findByEmail(email);
    }

    @Test
    void testGetUserByEmailUserNotFound() {
        String email = "notfound@gmail.com";
        when(userRepository.findByEmail(email))
                .thenReturn(null);

        Optional<User> result = userService.getUserByEmail(email);

        assertEquals(Optional.empty(), result);
        verify(userRepository, times(1))
                .findByEmail(email);
    }

    @Test
    void testGetUserByEmailNullEmail() {
        when(userRepository
                .findByEmail(null))
                .thenReturn(null);

        Optional<User> result = userService.getUserByEmail(null);

        assertEquals(Optional.empty(), result);
        verify(userRepository, times(1))
                .findByEmail(null);
    }

    @Test
    void testGetUserByEmailEmptyEmail() {
        String emptyEmail = "";
        when(userRepository
                .findByEmail(emptyEmail))
                .thenReturn(null);

        Optional<User> result = userService.getUserByEmail(emptyEmail);

        assertEquals(Optional.empty(), result);
        verify(userRepository, times(1))
                .findByEmail(emptyEmail);
    }

    @Test
    void testGetUserByEmailWithMultipleRequests() {

        String email1 = "user1@gmail.com";
        User user1 = new User();
        user1.setEmail(email1);

        String email2 = "user2@gmail.com";
        User user2 = new User();
        user2.setEmail(email2);

        when(userRepository
                .findByEmail(email1))
                .thenReturn(user1);
        when(userRepository
                .findByEmail(email2))
                .thenReturn(user2);

        Optional<User> result1 = userService.getUserByEmail(email1);
        Optional<User> result2 = userService.getUserByEmail(email2);

        assertEquals(Optional.of(user1), result1);
        assertEquals(Optional.of(user2), result2);

        verify(userRepository, times(1))
                .findByEmail(email1);
        verify(userRepository, times(1))
                .findByEmail(email2);
    }

    @Test
    void testGetUserByEmailWhenRepositoryThrowsException() {
        String email = "vanya0239@gmail.com";
        when(userRepository
                .findByEmail(email))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            userService.getUserByEmail(email);
        });

        verify(userRepository, times(1))
                .findByEmail(email);
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
        String password1 = "PassEm123+";
        String encodedPassword1 = passwordEncoder.encode(password1);

        User secondUser = new User();
        String password2 = "PassJane12@";
        String encodedPassword2 = passwordEncoder.encode(password2);

        firstUser.setFirstName("Жанна");
        firstUser.setLastName("Фарина");
        firstUser.setEmail("JennieEm@gmail.com");
        firstUser.setPassword(encodedPassword1);

        secondUser.setFirstName("Джейн");
        secondUser.setLastName("Провина");
        secondUser.setEmail("janeSmith@gmail.com");
        secondUser.setPassword(encodedPassword2);

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

    //infoById
    @Test
    public void testGetUserInfoByIdSuccess() throws ParseException {
        User user = new User();

        user.setId(1L);
        user.setFirstName("Ваня");
        user.setLastName("Прохоров");
        user.setEmail("ivanko@gmail.com");
        user.setLastLogin(new Date());

        Profile userProfile = new Profile();

        userProfile.setId(1L);
        userProfile.setDescription("Description");
        userProfile.setBirthday("01.01.2001");
        userProfile.setCountry("Russia");
        userProfile.setCity("Moscow");
        userProfile.setPhone("123456789");
        userProfile.setLastEdited(null);
        user.setTwoFactorVerified(true);

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));
        when(profileRepository
                .findByUserId(1L))
                .thenReturn(Optional.of(userProfile));

        UserProfileBom result = userService.getUserInfoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Ваня", result.getFirstName());
        assertEquals("Прохоров", result.getLastName());
        assertEquals("ivanko@gmail.com", result.getEmail());
        assertEquals(user.getTwoFactorVerified(), result.getTwoFactorEnabled());
        assertEquals("01.01.2001", result.getBirthday());
        assertEquals("Russia", result.getCountry());
        assertEquals("Moscow", result.getCity());
        assertEquals("123456789", result.getPhone());
        assertNull(result.getLastEdited());
    }

    @Test
    public void testGetUserInfoByIdUserNotFound() {
        when(userRepository
                .findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserInfoById(1L));
    }

    @Test
    public void testGetUserInfoByIdProfileNotFound() {
        User user = new User();

        user.setId(1L);
        user.setFirstName("Владимир");
        user.setLastName("Поков");
        user.setEmail("vlados@gmail.com");

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));
        when(profileRepository
                .findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserInfoById(1L));
    }

    //infoWithoutToken
    @Test
    public void testGetUserByIdWithoutTokenUserAndProfileFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Владимир");
        user.setLastName("Поков");
        user.setLastLogin(new Date());

        Profile userProfile = new Profile();

        userProfile.setId(userId);
        userProfile.setDescription("Description");
        userProfile.setBirthday("01.01.2004");
        userProfile.setCountry("USA");
        userProfile.setCity("Washington");

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(profileRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(userProfile));

        UserBom userDetailsDTO = userService.getUserByIdWithoutToken(userId);

        assertNotNull(userDetailsDTO);
        assertEquals("Владимир", userDetailsDTO.getFirstName());
        assertEquals("Поков", userDetailsDTO.getLastName());
        assertEquals(userProfile.getDescription(), userDetailsDTO.getDescription());
        assertEquals(userProfile.getBirthday(), userDetailsDTO.getBirthday());
        assertEquals(userProfile.getCountry(), userDetailsDTO.getCountry());
        assertEquals(userProfile.getCity(), userDetailsDTO.getCity());
    }

    @Test
    public void testGetUserByIdWithoutTokenUserNotFound() {
        Long userId = 1L;

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByIdWithoutToken(userId));
    }

    @Test
    public void testGetUserByIdWithoutTokenProfileNotFound() {
        Long userId = 1L;
        User user = new User();

        user.setId(userId);
        user.setFirstName("Лёва");
        user.setLastName("Дуров");
        user.setLastLogin(new Date());

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(profileRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByIdWithoutToken(userId));
    }

    //change pass
    @Test
    public void testResetPasswordUserNotFound() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123!";
        String currentPassword = "CurrentPassword123!";

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testResetPasswordInvalidVerificationCode() {
        Long userId = 1L;
        String verificationCode = "invalid";
        String newPassword = "NewPassword123!";
        String currentPassword = "CurrentPassword123!";

        User user = new User();
        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("Invalid verification code", exception.getMessage());
    }

    @Test
    public void testResetPasswordInvalidRequestType() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123!";
        String currentPassword = "CurrentPassword123!";

        User user = new User();
        user.setId(userId);

        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setUser(user);
        userVerificationCode.setCode(verificationCode);
        userVerificationCode.setRequestType(RequestType.ANOTHER_TYPE);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("Invalid request type for password change", exception.getMessage());
    }

    @Test
    public void testResetPasswordVerificationCodeExpired() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123!";
        String currentPassword = "CurrentPassword123!";

        User user = new User();
        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setUser(user);
        userVerificationCode.setCode(verificationCode);
        userVerificationCode.setRequestType(RequestType.PASSWORD_CHANGE);
        userVerificationCode.setExpirationTime(LocalDateTime.now().minusMinutes(2));

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("Verification code has expired", exception.getMessage());
    }

    @Test
    public void testResetPasswordInvalidCurrentPassword() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123!";
        String currentPassword = "InvalidCurrentPassword";

        User user = new User();
        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setUser(user);
        userVerificationCode.setCode(verificationCode);
        userVerificationCode.setRequestType(RequestType.PASSWORD_CHANGE);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder
                .matches(currentPassword, user.getPassword()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("Invalid current password", exception.getMessage());
    }

    @Test
    public void testResetPasswordInvalidPassword() {
        Long userId = 1L;
        String invalidVerificationCode = "validCode";
        String invalidPassword = "123";
        String currentPassword = "currentValidPassword123@";

        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(currentPassword));

        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setCode(invalidVerificationCode);
        userVerificationCode.setRequestType(RequestType.PASSWORD_CHANGE);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, invalidVerificationCode))
                .thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder
                .matches(currentPassword, user.getPassword()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, invalidVerificationCode, invalidPassword, currentPassword);
        });
        assertEquals("Password does not meet the complexity requirements", exception.getMessage());
    }

    @Test
    public void testResetPasswordSuccess() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123@";
        String currentPassword = "CurrentPassword123@";

        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(currentPassword));

        UserVerificationCode userVerificationCode = new UserVerificationCode();
        userVerificationCode.setUser(user);
        userVerificationCode.setCode(verificationCode);
        userVerificationCode.setRequestType(RequestType.PASSWORD_CHANGE);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userVerificationCodeRepository
                .findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder
                .matches(currentPassword, user.getPassword()))
                .thenReturn(true);

        when(passwordEncoder
                .encode(newPassword))
                .thenReturn("encodedNewPassword");

        boolean result = userService.changePassword(userId, verificationCode, newPassword, currentPassword);

        assertTrue(result);
        verify(userRepository, times(1))
                .save(user);
        verify(userVerificationCodeRepository, times(1))
                .delete(userVerificationCode);

        assertEquals("encodedNewPassword", user.getPassword());
    }

    //findByEmail

    @Test
    public void testGetUserEmailByIdUserFound() {
        Long userId = 1L;
        String expectedEmail = "vovan012@gmail.com";
        User user = new User();
        user.setId(userId);
        user.setEmail(expectedEmail);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        String actualEmail = userService.getUserEmailById(userId);

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void testGetUserEmailByIdUserNotFound() {
        Long userId = 1L;

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        String actualEmail = userService.getUserEmailById(userId);

        assertNull(actualEmail);
    }

    //resetRequestPass
    @Test
    public void testSendPasswordResetRequestUserNotFound() {
        Long userId = 1L;

        when(userService
                .getUserEmailById(userId))
                .thenReturn(null);

        boolean result = userService.sendPasswordChangeRequest(userId);

        assertFalse(result);

        verify(emailService, never())
                .sendPasswordChangeConfirmationEmail(any(User.class), anyString());
        verify(verificationCodeService, never())
                .saveOrUpdateVerificationCodeForChangePassword(anyLong(), anyString(), any(RequestType.class), any(LocalDateTime.class));
    }

    @Test
    public void testSendPasswordResetRequestSuccess() {
        Long userId = 1L;
        String email = "Valera09@gmail.com";

        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        user.setTwoFactorVerified(true);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository
                .findByEmail(email))
                .thenReturn(user);

        when(emailService
                .sendPasswordChangeConfirmationEmail(any(User.class), anyString()))
                .thenReturn(true);

        boolean result = userService.sendPasswordChangeRequest(userId);

        assertTrue(result);

        verify(userRepository, times(1))
                .findById(userId);
        verify(userRepository, times(1))
                .findByEmail(email);

        verify(emailService, times(1))
                .sendPasswordChangeConfirmationEmail(any(User.class), anyString());
    }

    @Test
    public void testSendPasswordResetRequestEmailSendingFailure() {
        Long userId = 1L;
        String email = "DaryaVoronina@gmail.com";

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        when(userRepository
                .findByEmail(email))
                .thenReturn(user);
        when(emailService.sendPasswordChangeConfirmationEmail(any(User.class), anyString())).thenReturn(false);

        boolean result = userService.sendPasswordChangeRequest(userId);

        assertFalse(result);

        verify(verificationCodeService, times(1))
                .saveOrUpdateVerificationCodeForChangePassword(eq(userId), anyString(), eq(RequestType.PASSWORD_CHANGE), any(LocalDateTime.class));
        verify(emailService, times(1))
                .sendPasswordChangeConfirmationEmail(any(User.class), anyString());
    }

    //delete

    @Test
    public void testDeleteUserUserNotFound() {
        Long userId = 1L;
        String verificationCode = "someCode";

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result);
    }

    @Test
    public void testDeleteUserVerificationFailed() {
        Long userId = 1L;
        String verificationCode = "someCode";
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        when(verificationCodeService
                .verifyUserVerificationCodeForUser(userId, verificationCode))
                .thenReturn(false);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result);
    }

    @Test
    public void testDeleteUserSuccess() {
        Long userId = 1L;
        String verificationCode = "testCode";

        User user = new User();
        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        when(verificationCodeService
                .verifyUserVerificationCodeForUser(userId, verificationCode))
                .thenReturn(true);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertTrue(result, "User deletion should be successful");

        verify(userVerificationCodeRepository, times(1))
                .deleteByUserId(userId);
        verify(profileRepository, times(1))
                .deleteByUserId(userId);
        verify(userRepository, times(1))
                .deleteById(userId);
    }

    @Test
    public void testDeleteUserInvalidVerificationCode() {
        Long userId = 1L;
        String verificationCode = "invalidCode";

        User user = new User();
        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        when(verificationCodeService
                .verifyUserVerificationCodeForUser(userId, verificationCode))
                .thenReturn(false);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result, "User should not be deleted because verification code is invalid");
    }

    //DelRequest
    @Test
    public void testSendAccountDeletionRequestSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        doNothing()
                .when(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));

        when(emailService
                .sendAccountDeletionConfirmationEmail(eq(user), anyString()))
                .thenReturn(true);

        boolean result = userService.sendAccountDeletionRequest(userId);

        assertTrue(result, "Account deletion request should be successfully sent");

        verify(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));
        verify(emailService)
                .sendAccountDeletionConfirmationEmail(eq(user), anyString());
    }

    @Test
    public void testSendAccountDeletionRequestUserNotFound() {
        Long userId = 1L;

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        boolean result = userService.sendAccountDeletionRequest(userId);

        assertFalse(result, "Account deletion request should not be sent because user was not found");
    }

    @Test
    public void testSendAccountDeletionRequestEmailSendingFailure() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        doNothing().when(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));

        when(emailService
                .sendAccountDeletionConfirmationEmail(eq(user), anyString()))
                .thenReturn(false);

        boolean result = userService.sendAccountDeletionRequest(userId);

        assertFalse(result, "Account deletion request should fail due to email sending failure");

        verify(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));
        verify(emailService)
                .sendAccountDeletionConfirmationEmail(eq(user), anyString());
    }

    @Test
    public void testSendAccountDeletionRequestVerificationCodeUpdateFailure() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        doThrow(new RuntimeException("Verification code update failed"))
                .when(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));

        boolean result = userService.sendAccountDeletionRequest(userId);

        assertFalse(result, "Account deletion request should fail due to verification code update failure");

        verify(verificationCodeService)
                .saveOrUpdateVerificationCodeForAccountDeletion(eq(userId), anyString(), any(LocalDateTime.class));
    }

    //Enable2FA
    @Test
    public void testEnableTwoFactorAuthAlreadyEnabled() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setTwoFactorVerified(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.enableTwoFactorAuth(userId);
        });

        assertEquals("Two-factor authentication already enabled", exception.getMessage());
    }

    @Test
    public void testEnableTwoFactorAuthSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        userService.enableTwoFactorAuth(userId);

        assertTrue(user.getTwoFactorVerified(), "Two-factor authentication should be enabled");

        verify(userRepository)
                .save(user);
    }

    @Test
    public void testEnableTwoFactorAuthUserNotFound() {
        Long userId = 1L;

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.enableTwoFactorAuth(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testEnableTwoFactorAuthSaveFailure() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        doThrow(new RuntimeException("Failed to save user"))
                .when(userRepository)
                .save(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.enableTwoFactorAuth(userId);
        });

        assertEquals("Failed to save user", exception.getMessage());
    }


    //Disable2FA
    @Test
    public void testDisableTwoFactorAuthUserNotFound() {
        Long userId = 1L;

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.disableTwoFactorAuth(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDisableTwoFactorAuthAlreadyDisabled() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setTwoFactorVerified(false);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.disableTwoFactorAuth(userId);
        });

        assertEquals("Two-factor authentication already disabled", exception.getMessage());
    }

    @Test
    public void testDisableTwoFactorAuthSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setTwoFactorVerified(true);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));

        userService.disableTwoFactorAuth(userId);

        assertFalse(user.getTwoFactorVerified());

        verify(userRepository)
                .save(user);
    }

    @Test
    void testDisableTwoFactorAuthSaveFailure() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setTwoFactorVerified(true);

        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Save failed"))
                .when(userRepository)
                .save(user);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.disableTwoFactorAuth(userId);
        });

        assertEquals("Save failed", exception.getMessage());
    }

    @Test
    public void testSendVerificationCodeSuccess() {
        User user = new User();
        user.setEmail("Dodo0329@gmail.com");
        String verificationCode = "12345";

        userService.sendVerificationCode(user, verificationCode);

        verify(emailService)
                .sendVerificationCodeByEmail(user, verificationCode);
    }

    @Test
    public void testSendVerificationCodeEmailSendingError() {
        User user = new User();
        user.setEmail("Vasek123@gmail.com");
        String verificationCode = "12345";

        doThrow(new RuntimeException("Email sending error"))
                .when(emailService)
                .sendVerificationCodeByEmail(user, verificationCode);

        assertThrows(RuntimeException.class, () -> userService.sendVerificationCode(user, verificationCode));
    }
}