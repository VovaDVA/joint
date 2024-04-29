package com.jointAuth.service;

import com.jointAuth.model.user.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceTest {

    private TwoFactorAuthVerificationCodeRepository verificationCodeRepository;
    private UserRepository userRepository;
    private VerificationCodeService verificationCodeService;

    @BeforeEach
    public void setUp() {
        verificationCodeRepository = mock(TwoFactorAuthVerificationCodeRepository.class);
        userRepository = mock(UserRepository.class);

        verificationCodeService = new VerificationCodeService(verificationCodeRepository,
                userRepository,
                null);

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
}
