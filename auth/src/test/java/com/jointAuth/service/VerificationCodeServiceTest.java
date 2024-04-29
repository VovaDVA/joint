package com.jointAuth.service;

import com.jointAuth.model.user.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.User;
import com.jointAuth.model.user.UserVerificationCode;
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


    @BeforeEach
    public void setUp() {
        verificationCodeRepository = mock(TwoFactorAuthVerificationCodeRepository.class);
        userVerificationCodeRepository = mock(UserVerificationCodeRepository.class);
        userRepository = mock(UserRepository.class);

        verificationCodeService = new VerificationCodeService(verificationCodeRepository,
                userRepository,
                userVerificationCodeRepository);

        verificationCodeRepository.deleteAll();
        verificationCodeRepository.deleteAll();
        userRepository.deleteAll();
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
                .thenThrow(new IllegalArgumentException("User not found"));

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
    void testVerifyVerificationCodeFor2FASuccessful() {
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
    void testVerifyVerificationCodeFor2FAInvalidCode() {
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
    void testVerifyVerificationCodeFor2FAExpiredCode() {
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
    void testVerifyVerificationCodeFor2FACodeNotFound() {
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

        assertEquals("Verification code not found for userId: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyVerificationCodeFor2FAEmptyCode() {
        Long userId = 1L;
        String emptyCode = "";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyVerificationCodeFor2FA(userId, emptyCode);
        });

        assertEquals("Verification code not found for userId: " + userId, exception.getMessage());
    }

    @Test
    public void testVerifyVerificationCodeFor2FANullCode() {
        Long userId = 1L;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            verificationCodeService.verifyVerificationCodeFor2FA(userId, null);
        });

        assertEquals("Verification code not found for userId: " + userId, exception.getMessage());
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

        assertTrue(result, "Code should be valid and return true");
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

        assertFalse(result, "Code should be expired and return false");
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

        assertFalse(result, "Invalid code should return false");
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

        assertEquals("Verification code not found for userId: " + userId, exception.getMessage());
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

        assertFalse(result, "Empty code should return false");
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

        assertFalse(result, "Null code should return false");
        verify(userVerificationCodeRepository, never())
                .delete(verificationCode);
    }

    @Test
    void testCleanExpiredVerificationCodesFor2FANoExpiredCodes() {

        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(new ArrayList<>());
    }

    @Test
    void testCleanExpiredVerificationCodesFor2FAExpiredCodesPresent() {
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
    void testCleanExpiredVerificationCodesFor2FAMixedCodes() {
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
    void testCleanExpiredVerificationCodesFor2FAEmptyExpiredCodes() {
        when(verificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        verificationCodeService.cleanExpiredVerificationCodesFor2FA();

        verify(verificationCodeRepository)
                .deleteAll(Collections.emptyList());
    }

    @Test
    void testCleanExpiredVerificationCodesFor2FALargeNumberOfExpiredCodes() {
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
    void testCleanExpiredVerificationCodesFor2FACodesPartiallyExpired() {
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
    void testCleanExpiredVerificationCodesForPasswordResetNoExpiredCodes() {

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository, never())
                .deleteAll();
    }


    @Test
    void testCleanExpiredVerificationCodesForPasswordResetExpiredCodesPresent() {
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

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    void testCleanExpiredVerificationCodesForPasswordResetMixedCodes() {
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

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    void testCleanExpiredVerificationCodesForPasswordResetEmptyExpiredCodes() {

        when(userVerificationCodeRepository
                .findAllByExpirationTimeBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository)
                .deleteAll(Collections.emptyList());
    }

    @Test
    void testCleanExpiredVerificationCodesForPasswordResetLargeNumberOfExpiredCodes() {
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

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository)
                .deleteAll(expiredCodes);
    }

    @Test
    void testCleanExpiredVerificationCodesForPasswordResetCodesPartiallyExpired() {
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

        verificationCodeService.cleanExpiredVerificationCodesForPasswordReset();

        verify(userVerificationCodeRepository)
                .deleteAll(partiallyExpiredCodes);
    }
}
