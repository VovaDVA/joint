package com.jointAuth.service;

import com.jointAuth.model.user.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.model.profile.Profile;
import com.jointAuth.bom.user.UserBom;
import com.jointAuth.bom.user.UserProfileBom;
import com.jointAuth.model.user.UserVerificationCode;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;
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

        userServiceSpy = spy(userService);

        userService = new UserService(userRepository,
                passwordEncoder,
                profileRepository,
                verificationCodeService,
                emailService,
                userVerificationCodeRepository);

        userRepository.deleteAll();
        profileRepository.deleteAll();
    }

    //регистрация
    @Test
    public void testRegisterUserExistingEmailThrowsException() {
        User existingUser = new User();

        existingUser.setFirstName("Karina");
        existingUser.setLastName("Ivanova");
        existingUser.setEmail("Ivanova10@gmail.com");
        existingUser.setPassword("ExistingPassword123@");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        User newUser = new User();

        newUser.setFirstName("Irina");
        newUser.setLastName("Ivanova");
        newUser.setEmail("Ivanova10@gmail.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail("Ivanova10@gmail.com");
        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }


    @Test
    public void testRegisterUserInvalidPasswordThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Vladimir");
        newUser.setLastName("Proven");
        newUser.setEmail("vlprovin@gmail.com");
        newUser.setPassword("pass");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
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

        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterUserWithEmptyFirstNameThrowsException() {
        User newUser = new User();

        newUser.setFirstName("");
        newUser.setLastName("Simple");
        newUser.setEmail("simpleemail@gmail.com");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testRegisterUserEmptyLastNameThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Polina");
        newUser.setLastName("  ");
        newUser.setEmail("polinaR1@gmail.com");
        newUser.setPassword("NewPassword123@");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));
        assertEquals("Last name cannot be empty or contain only whitespace", exception.getMessage());

        verify(userRepository, never()).findByEmail(any());
        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }


    @Test
    public void testRegisterUserWithEmptyEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Danil");
        newUser.setLastName("Korovin");
        newUser.setEmail("");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testRegisterUserWithInvalidEmailThrowsException() {
        User newUser = new User();

        newUser.setFirstName("Kolya");
        newUser.setLastName("Vinilov");
        newUser.setEmail("invalid_email");
        newUser.setPassword("PasswordTest123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(newUser));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testFirstNameContainsProhibitedCharacters() {
        User user = new User();

        user.setFirstName("Vitya!");
        user.setLastName("Larin");
        user.setEmail("ViLa123@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testLastNameContainsProhibitedCharacters() {
        User user = new User();

        user.setFirstName("Svetlana");
        user.setLastName("Sovina#");
        user.setEmail("Sveta01@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testFirstNameTooLong() {
        User user = new User();

        user.setFirstName("AllaathanIsATooLongNameForThisTest");
        user.setLastName("Yorina");
        user.setEmail("allayo@gmail.com");
        user.setPassword("Password123@");

        assertThrows(IllegalArgumentException.class, () -> userService.register(user));

        verify(userRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void testLastNameTooLong() {
        User user = new User();

        user.setFirstName("Karina");
        user.setLastName("BostrovaIsATooLongLastNameForThisTest");
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
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Karim");
        user.setLastName("Valov");
        user.setEmail("Karim03@gmail.com");
        user.setPassword(encodedPassword);

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
        String password = "badPassword123@";
        String encodedPassword = passwordEncoder.encode(password);

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

    @Test
    public void testLoginWithNullEmail() {
        String password = "Password123@";
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();

        user.setFirstName("Petr");
        user.setLastName("Elov");
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

        user.setFirstName("Selim");
        user.setLastName("Vanov");
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

        user.setFirstName("Maxim");
        user.setLastName("Orlov");
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

        user.setFirstName("Vladimir");
        user.setLastName("Krelin");
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
        user.setFirstName("Vova");
        user.setLastName("Petrov");
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setTwoFactorVerified(true);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        doNothing().when(emailService).sendVerificationCodeByEmail(eq(user), anyString());

        User loggedInUser = userService.login(email, password);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(emailService, times(1)).sendVerificationCodeByEmail(eq(user), anyString());

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
        String password1 = "PassEm123+";
        String encodedPassword1 = passwordEncoder.encode(password1);

        User secondUser = new User();
        String password2 = "PassJane12@";
        String encodedPassword2 = passwordEncoder.encode(password2);

        firstUser.setFirstName("Jennie");
        firstUser.setLastName("Farina");
        firstUser.setEmail("JennieEm@gmail.com");
        firstUser.setPassword(encodedPassword1);

        secondUser.setFirstName("Jane");
        secondUser.setLastName("janeSmith@gmail.com");
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
        user.setFirstName("Vanya");
        user.setLastName("Prohorov");
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

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(userProfile));

        UserProfileBom result = userService.getUserInfoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Vanya", result.getFirstName());
        assertEquals("Prohorov", result.getLastName());
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
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserInfoById(1L));
    }

    @Test
    public void testGetUserInfoByIdProfileNotFound() {
        User user = new User();

        user.setId(1L);
        user.setFirstName("Vladislav");
        user.setLastName("Pokov");
        user.setEmail("vlados@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserInfoById(1L));
    }

    //infoWithoutToken
    @Test
    public void testGetUserByIdWithoutTokenUserAndProfileFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Vladislav");
        user.setLastName("Pokov");
        user.setLastLogin(new Date());

        Profile userProfile = new Profile();

        userProfile.setId(userId);
        userProfile.setDescription("Description");
        userProfile.setBirthday("01.01.2004");
        userProfile.setCountry("USA");
        userProfile.setCity("Washington");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        UserBom userDetailsDTO = userService.getUserByIdWithoutToken(userId);

        assertNotNull(userDetailsDTO);
        assertEquals("Vladislav", userDetailsDTO.getFirstName());
        assertEquals("Pokov", userDetailsDTO.getLastName());
        assertEquals(userProfile.getDescription(), userDetailsDTO.getDescription());
        assertEquals(userProfile.getBirthday(), userDetailsDTO.getBirthday());
        assertEquals(userProfile.getCountry(), userDetailsDTO.getCountry());
        assertEquals(userProfile.getCity(), userDetailsDTO.getCity());
    }

    @Test
    public void testGetUserByIdWithoutTokenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByIdWithoutToken(userId));
    }

    @Test
    public void testGetUserByIdWithoutTokenProfileNotFound() {
        Long userId = 1L;
        User user = new User();

        user.setId(userId);
        user.setFirstName("Leva");
        user.setLastName("Durov");
        user.setLastLogin(new Date());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByIdWithoutToken(userId));
    }

    //change pass
    @Test
    public void testResetPasswordUserNotFound() {
        Long userId = 1L;
        String verificationCode = "123456";
        String newPassword = "NewPassword123!";
        String currentPassword = "CurrentPassword123!";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, verificationCode, newPassword, currentPassword);
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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, verificationCode, newPassword, currentPassword);
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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, verificationCode, newPassword, currentPassword);
        });

        assertEquals("Invalid request type for password reset", exception.getMessage());
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
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setExpirationTime(LocalDateTime.now().minusMinutes(2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, verificationCode, newPassword, currentPassword);
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
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder.matches(currentPassword, user.getPassword()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, verificationCode, newPassword, currentPassword);
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
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, invalidVerificationCode)).thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(userId, invalidVerificationCode, invalidPassword, currentPassword);
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
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userVerificationCodeRepository.findByUserIdAndCode(userId, verificationCode))
                .thenReturn(Optional.of(userVerificationCode));
        when(passwordEncoder.matches(currentPassword, user.getPassword()))
                .thenReturn(true);

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        boolean result = userService.resetPassword(userId, verificationCode, newPassword, currentPassword);

        assertTrue(result);
        verify(userRepository, times(1)).save(user);
        verify(userVerificationCodeRepository, times(1)).delete(userVerificationCode);

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String actualEmail = userService.getUserEmailById(userId);

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void testGetUserEmailByIdUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        String actualEmail = userService.getUserEmailById(userId);

        assertNull(actualEmail);
    }

    //resetRequestPass
    @Test
    public void testSendPasswordResetRequestUserNotFound() {
        Long userId = 1L;

        when(userService.getUserEmailById(userId)).thenReturn(null);

        boolean result = userService.sendPasswordResetRequest(userId);

        assertFalse(result);

        verify(emailService, never()).sendPasswordResetConfirmationEmail(any(User.class), anyString());
        verify(verificationCodeService, never()).saveOrUpdateVerificationCodeForResetPassword(anyLong(), anyString(), any(RequestType.class), any(LocalDateTime.class));
    }

    @Test
    public void testSendPasswordResetRequestSuccess() {
        Long userId = 1L;
        String email = "Valera09@gmail.com";

        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        user.setTwoFactorVerified(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(email)).thenReturn(user);

        when(emailService.sendPasswordResetConfirmationEmail(any(User.class), anyString())).thenReturn(true);

        boolean result = userService.sendPasswordResetRequest(userId);

        assertTrue(result);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).findByEmail(email);

        verify(emailService, times(1)).sendPasswordResetConfirmationEmail(any(User.class), anyString());
    }

    @Test
    public void testSendPasswordResetRequestEmailSendingFailure() {
        Long userId = 1L;
        String email = "DaryaVoronina@gmail.com";

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(emailService.sendPasswordResetConfirmationEmail(any(User.class), anyString())).thenReturn(false);

        boolean result = userService.sendPasswordResetRequest(userId);

        assertFalse(result);

        verify(verificationCodeService, times(1)).saveOrUpdateVerificationCodeForResetPassword(eq(userId), anyString(), eq(RequestType.PASSWORD_RESET), any(LocalDateTime.class));
        verify(emailService, times(1)).sendPasswordResetConfirmationEmail(any(User.class), anyString());
    }

    //delete

    @Test
    void testDeleteUserUserNotFound() {
        Long userId = 1L;
        String verificationCode = "someCode";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result);
    }

    @Test
    void testDeleteUserVerificationFailed() {
        Long userId = 1L;
        String verificationCode = "someCode";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(verificationCodeService.verifyUserVerificationCode(userId, verificationCode)).thenReturn(false);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result);
    }

    @Test
    void testDeleteUserSuccess() {
        Long userId = 1L;
        String verificationCode = "testCode";

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(verificationCodeService.verifyUserVerificationCode(userId, verificationCode)).thenReturn(true);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertTrue(result, "User deletion should be successful");

        verify(userVerificationCodeRepository, times(1)).deleteByUserId(userId);
        verify(profileRepository, times(1)).deleteByUserId(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserInvalidVerificationCode() {
        Long userId = 1L;
        String verificationCode = "invalidCode";

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(verificationCodeService.verifyUserVerificationCode(userId, verificationCode)).thenReturn(false);

        boolean result = userService.deleteUser(userId, verificationCode);

        assertFalse(result, "User should not be deleted because verification code is invalid");
    }
}