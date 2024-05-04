package com.jointAuth.service;

import com.jointAuth.model.verification.PasswordResetVerificationCode;
import com.jointAuth.model.verification.RequestType;
import com.jointAuth.model.verification.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.User;
import com.jointAuth.model.verification.UserVerificationCode;
import com.jointAuth.repository.PasswordResetVerificationCodeRepository;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceTest {

    private TwoFactorAuthVerificationCodeRepository verificationCodeRepository;
    private UserRepository userRepository;
    private VerificationCodeService verificationCodeService;

    private UserVerificationCodeRepository userVerificationCodeRepository;

    private PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository;

    @BeforeEach
    public void setUp() {
        verificationCodeRepository = mock(TwoFactorAuthVerificationCodeRepository.class);
        userVerificationCodeRepository = mock(UserVerificationCodeRepository.class);
        userRepository = mock(UserRepository.class);
        passwordResetVerificationCodeRepository = mock(PasswordResetVerificationCodeRepository.class);

        verificationCodeService = new VerificationCodeService(verificationCodeRepository,
                userRepository,
                userVerificationCodeRepository,
                passwordResetVerificationCodeRepository);

        verificationCodeRepository.deleteAll();
    }
    @Test
    public void testSaveOrUpdateVerificationCodeFor2FANewCode() {
        Long userId = 1L;
        String newCode = "123456";
        User user = new User();
        user.setId(userId);

        when(userRepository
                .getOne(userId))
                .thenReturn(user);
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(userId, newCode);

        ArgumentCaptor<TwoFactorAuthVerificationCode> verificationCodeCaptor = ArgumentCaptor.forClass(TwoFactorAuthVerificationCode.class);

        verify(verificationCodeRepository)
                .save(verificationCodeCaptor.capture());

        TwoFactorAuthVerificationCode savedCode = verificationCodeCaptor.getValue();

        assertNotNull(savedCode);
        assertEquals(user, savedCode.getUser());
        assertEquals(newCode, savedCode.getCode());
        assertTrue(savedCode.getExpirationTime().isAfter(LocalDateTime.now()));
    }

    @Test
    public void testSaveOrUpdateVerificationCodeFor2FAExistingCode() {
        User curUser = new User();
        Long userId = 1L;
        String existingCode = "oldCode";
        curUser.setId(userId);
        TwoFactorAuthVerificationCode existingVerificationCode = new TwoFactorAuthVerificationCode();
        existingVerificationCode.setCode(existingCode);
        existingVerificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));
        existingVerificationCode.setUser(curUser);

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(existingVerificationCode));

        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(userId, existingCode);

        verify(verificationCodeRepository)
                .save(existingVerificationCode);
    }

    @Test
    public void testSaveOrUpdateVerificationCodeFor2FAInvalidUserId() {
        Long invalidUserId = -1L;
        String newCode = "123456";

        when(userRepository
                .getOne(invalidUserId))
                .thenThrow(new IllegalArgumentException("Пользователь не найден"));

        assertThrows(IllegalArgumentException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeFor2FA(invalidUserId, newCode);
        });

        verify(verificationCodeRepository, never())
                .save(any());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeFor2FAInvalidCode() {
        Long userId = 1L;
        String invalidCode = "";

        User user = new User();
        user.setId(userId);

        when(userRepository
                .getOne(userId))
                .thenReturn(user);
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(userId, invalidCode);

        ArgumentCaptor<TwoFactorAuthVerificationCode> captor = ArgumentCaptor.forClass(TwoFactorAuthVerificationCode.class);

        verify(verificationCodeRepository)
                .save(captor.capture());

        TwoFactorAuthVerificationCode savedCode = captor.getValue();

        assertNotNull(savedCode);
        assertNotEquals(null, savedCode.getCode());
        assertEquals(invalidCode, savedCode.getCode());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeFor2FAExpirationTime() {
        Long userId = 1L;
        String newCode = "123456";
        User user = new User();
        user.setId(userId);

        when(userRepository
                .getOne(userId))
                .thenReturn(user);
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        verificationCodeService.saveOrUpdateVerificationCodeFor2FA(userId, newCode);

        ArgumentCaptor<TwoFactorAuthVerificationCode> captor = ArgumentCaptor.forClass(TwoFactorAuthVerificationCode.class);

        verify(verificationCodeRepository)
                .save(captor.capture());

        TwoFactorAuthVerificationCode savedCode = captor.getValue();

        LocalDateTime expirationTime = savedCode.getExpirationTime();
        assertNotNull(expirationTime);
        assertTrue(expirationTime.isAfter(LocalDateTime.now()));
        assertTrue(expirationTime.isBefore(LocalDateTime.now().plusHours(1)));
    }

    @Test
    public void testVerifyVerificationCodeFor2FASuccessful() {
        Long userId = 1L;
        String code = "123456";
        TwoFactorAuthVerificationCode verificationCode = new TwoFactorAuthVerificationCode();
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));
        verificationCode.setUser(userRepository.getById(userId));

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyVerificationCodeFor2FA(userId, code);

        assertTrue(result);
        verify(verificationCodeRepository)
                .delete(verificationCode);
    }

    @Test
    public void testVerifyVerificationCodeFor2FAInvalidCode() {
        Long userId = 1L;
        String code = "654321";
        TwoFactorAuthVerificationCode verificationCode = new TwoFactorAuthVerificationCode();
        verificationCode.setCode("123456");
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUser(userRepository.getById(userId));

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyVerificationCodeFor2FA(userId, code);

        assertFalse(result);
        verify(verificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    public void testVerifyVerificationCodeFor2FAExpiredCode() {
        Long userId = 1L;
        String code = "123456";
        TwoFactorAuthVerificationCode verificationCode = new TwoFactorAuthVerificationCode();
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().minusMinutes(5));
        verificationCode.setUser(userRepository.getById(userId));

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyVerificationCodeFor2FA(userId, code);

        assertFalse(result);
        verify(verificationCodeRepository, never()).delete(verificationCode);
    }

    @Test
    public void testVerifyVerificationCodeFor2FACodeNotFound() {
        Long userId = 1L;
        String code = "123456";

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> verificationCodeService.verifyVerificationCodeFor2FA(userId, code));
    }

    @Test
    public void testVerifyVerificationCodeFor2FAUserNotFound() {
        Long userId = 1L;
        String code = "12345";

        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyVerificationCodeFor2FA(userId, code);
        });

        assertEquals("Не найден проверочный код для идентификатора пользователя: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyVerificationCodeFor2FAEmptyCode() {
        Long userId = 1L;
        String emptyCode = "";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyVerificationCodeFor2FA(userId, emptyCode);
        });

        assertEquals("Не найден проверочный код для идентификатора пользователя: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyVerificationCodeFor2FANullCode() {
        Long userId = 1L;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyVerificationCodeFor2FA(userId, null);
        });

        assertEquals("Не найден проверочный код для идентификатора пользователя: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyUserVerificationCodeForUserSuccessfulVerification() {
        Long userId = 1L;
        String code = "12345";

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyUserVerificationCodeForUser(userId, code);

        assertTrue(result, "Код должен быть действительным и возвращать значение true");
        verify(userVerificationCodeRepository)
                .delete(verificationCode);
    }

    @Test
    public void testVerifyUserVerificationCodeForUserCodeExpired() {
        Long userId = 1L;
        String code = "12345";

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().minusMinutes(10));

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyUserVerificationCodeForUser(userId, code);

        assertFalse(result, "Срок действия кода должен истечь, и он должен возвращать значение false");
        verify(userVerificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    public void testVerifyUserVerificationCodeForUserInvalidCode() {
        Long userId = 1L;
        String invalidCode = "54321";

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode("12345");
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyUserVerificationCodeForUser(userId, invalidCode);

        assertFalse(result, "Неверный код должен возвращать значение false");
        verify(userVerificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    public void testVerifyUserVerificationCodeForUserUserNotFound() {
        Long userId = 1L;
        String code = "12345";

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyUserVerificationCodeForUser(userId, code);
        });

        assertEquals("Не найден проверочный код для идентификатора пользователя: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyUserVerificationCodeForUserEmptyCode() {
        Long userId = 1L;
        String emptyCode = "";

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode("12345");
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyUserVerificationCodeForUser(userId, emptyCode);

        assertFalse(result, "Пустой код должен возвращать значение false");
        verify(userVerificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    public void testVerifyUserVerificationCodeForUserNullCode() {
        Long userId = 1L;
        String nullCode = null;

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode("12345");
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        when(userVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode));

        boolean result = verificationCodeService.verifyUserVerificationCodeForUser(userId, nullCode);

        assertFalse(result, "Нулевой код должен возвращать значение false");
        verify(userVerificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FANoExpiredCodes() {

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(new ArrayList<>());
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FAExpiredCodesPresent() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minus(1, ChronoUnit.DAYS);

        List<TwoFactorAuthVerificationCode> expiredCodes = new ArrayList<>();
        TwoFactorAuthVerificationCode expiredCode1 = new TwoFactorAuthVerificationCode();
        expiredCode1.setExpirationTime(expiredTime);
        expiredCodes.add(expiredCode1);

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FAMixedCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minus(1, ChronoUnit.DAYS);
        LocalDateTime validTime = currentTime.plus(1, ChronoUnit.DAYS);

        List<TwoFactorAuthVerificationCode> codes = new ArrayList<>();
        TwoFactorAuthVerificationCode expiredCode = new TwoFactorAuthVerificationCode();
        expiredCode.setExpirationTime(expiredTime);
        codes.add(expiredCode);

        TwoFactorAuthVerificationCode validCode = new TwoFactorAuthVerificationCode();
        validCode.setExpirationTime(validTime);
        codes.add(validCode);

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(expiredCode));

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(List.of(expiredCode));
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FAEmptyExpiredCodes() {
        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(Collections.emptyList());
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FALargeNumberOfExpiredCodes() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<TwoFactorAuthVerificationCode> expiredCodes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            TwoFactorAuthVerificationCode code = new TwoFactorAuthVerificationCode();
            code.setExpirationTime(currentTime.minusDays(1));
            expiredCodes.add(code);
        }

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesFor2FACodesPartiallyExpired() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<TwoFactorAuthVerificationCode> partiallyExpiredCodes = new ArrayList<>();
        List<TwoFactorAuthVerificationCode> validCodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TwoFactorAuthVerificationCode expiredCode = new TwoFactorAuthVerificationCode();
            expiredCode.setExpirationTime(currentTime.minusDays(1));
            partiallyExpiredCodes.add(expiredCode);
        }

        for (int i = 0; i < 10; i++) {
            TwoFactorAuthVerificationCode validCode = new TwoFactorAuthVerificationCode();
            validCode.setExpirationTime(currentTime.plusDays(1));
            validCodes.add(validCode);
        }

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(partiallyExpiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(partiallyExpiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeNoExpiredCodes() {

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository, never())
                .deleteAll();
    }


    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeExpiredCodesPresent() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minusDays(1);

        List<UserVerificationCode> expiredCodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserVerificationCode code = new UserVerificationCode();
            code.setExpirationTime(expiredTime);
            expiredCodes.add(code);
        }

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeMixedCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minusDays(1);
        LocalDateTime validTime = currentTime.plusDays(1);

        List<UserVerificationCode> expiredCodes = new ArrayList<>();
        List<UserVerificationCode> validCodes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            UserVerificationCode expiredCode = new UserVerificationCode();
            expiredCode.setExpirationTime(expiredTime);
            expiredCodes.add(expiredCode);
        }

        for (int i = 0; i < 5; i++) {
            UserVerificationCode validCode = new UserVerificationCode();
            validCode.setExpirationTime(validTime);
            validCodes.add(validCode);
        }

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeEmptyExpiredCodes() {

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository)
                .deleteAll(Collections.emptyList());
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeLargeNumberOfExpiredCodes() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<UserVerificationCode> expiredCodes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            UserVerificationCode code = new UserVerificationCode();
            code.setExpirationTime(currentTime.minusDays(1));
            expiredCodes.add(code);
        }

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordChangeCodesPartiallyExpired() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<UserVerificationCode> partiallyExpiredCodes = new ArrayList<>();
        List<UserVerificationCode> validCodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UserVerificationCode expiredCode = new UserVerificationCode();
            expiredCode.setExpirationTime(currentTime.minusDays(1));
            partiallyExpiredCodes.add(expiredCode);
        }

        for (int i = 0; i < 10; i++) {
            UserVerificationCode validCode = new UserVerificationCode();
            validCode.setExpirationTime(currentTime.plusDays(1));
            validCodes.add(validCode);
        }

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(partiallyExpiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordChange();

        verify(userVerificationCodeRepository)
                .deleteAll(partiallyExpiredCodes);
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordNewCode() {
        Long userId = 1L;
        String verificationCode = "123456";
        RequestType requestType = RequestType.PASSWORD_CHANGE;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, requestType)).thenReturn(Optional.empty());
        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION)).thenReturn(Optional.empty());
        when(userRepository.getById(userId)).thenReturn(new User());

        verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(userId, verificationCode, requestType, expirationTime);

        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, requestType);
        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        verify(userRepository).getById(userId);
        verify(userVerificationCodeRepository).save(any(UserVerificationCode.class));
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordExistingCode() {
        Long userId = 1L;
        String verificationCode = "123456";
        RequestType requestType = RequestType.PASSWORD_CHANGE;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        UserVerificationCode existingCode = new UserVerificationCode();
        existingCode.setUser(new User());
        existingCode.setCode("oldCode");
        existingCode.setRequestType(RequestType.PASSWORD_CHANGE);
        existingCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, requestType)).thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(userId, verificationCode, requestType, expirationTime);

        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, requestType);
        verify(userVerificationCodeRepository).save(existingCode);

        assertEquals(verificationCode, existingCode.getCode());
        assertEquals(expirationTime, existingCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordExistingCodeForDeletion() {
        Long userId = 1L;
        String verificationCode = "123456";
        RequestType requestType = RequestType.PASSWORD_CHANGE;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        UserVerificationCode existingCodeForDeletion = new UserVerificationCode();
        existingCodeForDeletion.setUser(new User());
        existingCodeForDeletion.setCode("oldCode");
        existingCodeForDeletion.setRequestType(RequestType.ACCOUNT_DELETION);
        existingCodeForDeletion.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, requestType)).thenReturn(Optional.empty());
        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION)).thenReturn(Optional.of(existingCodeForDeletion));

        verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(userId, verificationCode, requestType, expirationTime);

        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, requestType);
        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        verify(userVerificationCodeRepository).save(any(UserVerificationCode.class));
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordInvalidInputs() {
        Long invalidUserId = null;
        String verificationCode = "123456";
        RequestType requestType = RequestType.PASSWORD_CHANGE;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(NullPointerException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(invalidUserId, verificationCode, requestType, expirationTime);
        });
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordNullInputs() {
        Long userId = null;
        String verificationCode = null;
        RequestType requestType = null;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(NullPointerException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(userId, verificationCode, requestType, expirationTime);
        });
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForChangePasswordExistingCodeSameValues() {
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        UserVerificationCode existingCode = new UserVerificationCode();
        existingCode.setCode(newVerificationCode);
        existingCode.setRequestType(RequestType.PASSWORD_CHANGE);
        existingCode.setExpirationTime(expirationTime);

        User curUser = new User();
        curUser.setId(userId);
        existingCode.setUser(curUser);

        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.PASSWORD_CHANGE)).thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForChangePassword(userId, newVerificationCode, RequestType.PASSWORD_CHANGE, expirationTime);

        ArgumentCaptor<UserVerificationCode> captor = ArgumentCaptor.forClass(UserVerificationCode.class);
        verify(userVerificationCodeRepository).save(captor.capture());

        UserVerificationCode savedCode = captor.getValue();
        assertNotNull(savedCode.getUser());
        assertEquals(userId, savedCode.getUser().getId());
        assertEquals(newVerificationCode, savedCode.getCode());
        assertEquals(RequestType.PASSWORD_CHANGE, savedCode.getRequestType());
        assertEquals(expirationTime, savedCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionNewCode() {
        User curUser = new User();
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        curUser.setId(userId);

        when(userVerificationCodeRepository
                .findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION))
                .thenReturn(Optional.empty());
        when(userRepository
                .findById(userId))
                .thenReturn(Optional.of(curUser));

        verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, newVerificationCode, expirationTime);

        verify(userVerificationCodeRepository)
                .findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        verify(userRepository)
                .findById(userId);
        verify(userVerificationCodeRepository)
                .save(any(UserVerificationCode.class));
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionExistingCode() {
        User cureUser = new User();
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        cureUser.setId(userId);

        UserVerificationCode existingCode = new UserVerificationCode();
        existingCode.setUser(cureUser);
        existingCode.setCode("oldCode");
        existingCode.setRequestType(RequestType.ACCOUNT_DELETION);
        existingCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(userVerificationCodeRepository
                .findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION))
                .thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, newVerificationCode, expirationTime);

        verify(userVerificationCodeRepository)
                .findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        verify(userVerificationCodeRepository)
                .save(existingCode);

        assertEquals(newVerificationCode, existingCode.getCode());
        assertEquals(expirationTime, existingCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionExistingCodeForPasswordReset() {
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        UserVerificationCode existingCodeForPasswordReset = new UserVerificationCode();
        existingCodeForPasswordReset.setUser(new User());
        existingCodeForPasswordReset.setCode("oldCode");
        existingCodeForPasswordReset.setRequestType(RequestType.PASSWORD_CHANGE);
        existingCodeForPasswordReset.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION)).thenReturn(Optional.empty());
        when(userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.PASSWORD_CHANGE)).thenReturn(Optional.of(existingCodeForPasswordReset));

        verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, newVerificationCode, expirationTime);

        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        verify(userVerificationCodeRepository).findByUserIdAndRequestType(userId, RequestType.PASSWORD_CHANGE);
        verify(userVerificationCodeRepository).save(any(UserVerificationCode.class));
    }


    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionInvalidInputs() {
        Long invalidUserId = null;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(NullPointerException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(invalidUserId, newVerificationCode, expirationTime);
        });
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionNullInputs() {
        Long userId = null;
        String newVerificationCode = null;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(NullPointerException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, newVerificationCode, expirationTime);
        });
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForAccountDeletionExistingCodeSameValues() {
        User curUser = new User();
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        curUser.setId(userId);

        UserVerificationCode existingCode = new UserVerificationCode();
        existingCode.setUser(curUser);
        existingCode.setCode(newVerificationCode);
        existingCode.setRequestType(RequestType.ACCOUNT_DELETION);
        existingCode.setExpirationTime(expirationTime);

        when(userVerificationCodeRepository
                .findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION))
                .thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForAccountDeletion(userId, newVerificationCode, expirationTime);

        ArgumentCaptor<UserVerificationCode> captor = ArgumentCaptor.forClass(UserVerificationCode.class);
        verify(userVerificationCodeRepository)
                .save(captor.capture());

        UserVerificationCode savedCode = captor.getValue();
        assertEquals(userId, savedCode.getUser().getId());
        assertEquals(newVerificationCode, savedCode.getCode());
        assertEquals(expirationTime, savedCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetNewCode() {
        Long userId = 1L;
        String verificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        when(passwordResetVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        User user = new User();
        user.setId(userId);

        when(userRepository
                .getById(userId))
                .thenReturn(user);

        verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(userId, verificationCode, expirationTime);

        verify(passwordResetVerificationCodeRepository)
                .findByUserId(userId);

        ArgumentCaptor<PasswordResetVerificationCode> captor = ArgumentCaptor.forClass(PasswordResetVerificationCode.class);
        verify(passwordResetVerificationCodeRepository)
                .save(captor.capture());

        PasswordResetVerificationCode savedCode = captor.getValue();

        assertEquals(userId, savedCode.getUser().getId());
        assertEquals(verificationCode, savedCode.getCode());
        assertEquals(expirationTime, savedCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetExistingCode() {
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime newExpirationTime = LocalDateTime.now().plusMinutes(10);

        PasswordResetVerificationCode existingCode = new PasswordResetVerificationCode();
        existingCode.setUser(new User());
        existingCode.setCode("oldCode");
        existingCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(passwordResetVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(userId, newVerificationCode, newExpirationTime);

        verify(passwordResetVerificationCodeRepository)
                .findByUserId(userId);

        verify(passwordResetVerificationCodeRepository)
                .save(existingCode);

        assertEquals(newVerificationCode, existingCode.getCode());
        assertEquals(newExpirationTime, existingCode.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetExistingCodeForDeletion() {
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime newExpirationTime = LocalDateTime.now().plusMinutes(10);

        PasswordResetVerificationCode existingCodeForDeletion = new PasswordResetVerificationCode();
        existingCodeForDeletion.setUser(new User());
        existingCodeForDeletion.setCode("oldCode");
        existingCodeForDeletion.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(passwordResetVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(existingCodeForDeletion));

        verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(userId, newVerificationCode, newExpirationTime);

        verify(passwordResetVerificationCodeRepository)
                .findByUserId(userId);

        verify(passwordResetVerificationCodeRepository)
                .save(existingCodeForDeletion);

        assertEquals(newVerificationCode, existingCodeForDeletion.getCode());
        assertEquals(newExpirationTime, existingCodeForDeletion.getExpirationTime());
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetInvalidInputs() {
        Long invalidUserId = null;
        String verificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(IllegalArgumentException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(invalidUserId, verificationCode, expirationTime);
        });
    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetNullInputs() {
        Long userId = null;
        String verificationCode = null;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        assertThrows(IllegalArgumentException.class, () -> {
            verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(userId, verificationCode, expirationTime);
        });

    }

    @Test
    public void testSaveOrUpdateVerificationCodeForPasswordResetExistingCodeSameValues() {
        Long userId = 1L;
        String newVerificationCode = "123456";
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        PasswordResetVerificationCode existingCode = new PasswordResetVerificationCode();
        existingCode.setCode(newVerificationCode);
        existingCode.setExpirationTime(expirationTime);
        existingCode.setUser(new User());
        existingCode.getUser().setId(userId);

        when(passwordResetVerificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(existingCode));

        verificationCodeService.saveOrUpdateVerificationCodeForPasswordReset(userId, newVerificationCode, expirationTime);

        ArgumentCaptor<PasswordResetVerificationCode> captor = ArgumentCaptor.forClass(PasswordResetVerificationCode.class);
        verify(passwordResetVerificationCodeRepository)
                .save(captor.capture());

        PasswordResetVerificationCode savedCode = captor.getValue();
        assertNotNull(savedCode.getUser());
        assertEquals(userId, savedCode.getUser().getId());
        assertEquals(newVerificationCode, savedCode.getCode());
        assertEquals(expirationTime, savedCode.getExpirationTime());
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetNoExpiredCodes() {
        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository, never())
                .deleteAll();
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetExpiredCodesPresent() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minusDays(1);

        List<PasswordResetVerificationCode> expiredCodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PasswordResetVerificationCode code = new PasswordResetVerificationCode();
            code.setExpirationTime(expiredTime);
            expiredCodes.add(code);
        }

        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetMixedCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.minusDays(1);
        LocalDateTime validTime = currentTime.plusDays(1);

        List<PasswordResetVerificationCode> expiredCodes = new ArrayList<>();
        List<PasswordResetVerificationCode> validCodes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            PasswordResetVerificationCode expiredCode = new PasswordResetVerificationCode();
            expiredCode.setExpirationTime(expiredTime);
            expiredCodes.add(expiredCode);
        }

        for (int i = 0; i < 5; i++) {
            PasswordResetVerificationCode validCode = new PasswordResetVerificationCode();
            validCode.setExpirationTime(validTime);
            validCodes.add(validCode);
        }

        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetEmptyExpiredCodes() {

        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository)
                .deleteAll(Collections.emptyList());
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetLargeNumberOfExpiredCodes() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<PasswordResetVerificationCode> expiredCodes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            PasswordResetVerificationCode code = new PasswordResetVerificationCode();
            code.setExpirationTime(currentTime.minusDays(1));
            expiredCodes.add(code);
        }

        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(expiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    public void testCleanExpiredVerificationCodesForPasswordResetPartiallyExpired() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<PasswordResetVerificationCode> partiallyExpiredCodes = new ArrayList<>();
        List<PasswordResetVerificationCode> validCodes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PasswordResetVerificationCode expiredCode = new PasswordResetVerificationCode();
            expiredCode.setExpirationTime(currentTime.minusDays(1));
            partiallyExpiredCodes.add(expiredCode);
        }

        for (int i = 0; i < 10; i++) {
            PasswordResetVerificationCode validCode = new PasswordResetVerificationCode();
            validCode.setExpirationTime(currentTime.plusDays(1));
            validCodes.add(validCode);
        }

        List<PasswordResetVerificationCode> allCodes = new ArrayList<>();
        allCodes.addAll(partiallyExpiredCodes);
        allCodes.addAll(validCodes);

        when(passwordResetVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(partiallyExpiredCodes);

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(passwordResetVerificationCodeRepository)
                .deleteAll(partiallyExpiredCodes);
    }
}