package com.jointAuth.repository;

import com.jointAuth.model.user.TwoFactorAuthVerificationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class TwoFactorAuthVerificationCodeRepositoryTest {
    private TwoFactorAuthVerificationCodeRepository verificationCodeRepository;

    @Mock
    private TwoFactorAuthVerificationCode mockVerificationCode;

    @BeforeEach
    void setUp() {
        verificationCodeRepository = mock(TwoFactorAuthVerificationCodeRepository.class);
    }

    @Test
    public void testFindByUserIdSuccessful() {
        Long userId = 1L;
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(mockVerificationCode));

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(mockVerificationCode, result.get());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdNotFound() {
        Long userId = 2L;
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdException() {
        Long userId = 3L;
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenThrow(new RuntimeException("Exception"));

        assertThrows(RuntimeException.class, () -> {
            verificationCodeRepository.findByUserId(userId);
        });
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdNonExistingUserId() {
        Long userId = 4L;
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdEmptyUserId() {

        when(verificationCodeRepository
                .findByUserId(any()))
                .thenReturn(Optional.empty());

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(null);

        assertFalse(result.isPresent());
        verify(verificationCodeRepository, times(1))
                .findByUserId(null);
    }

    @Test
    public void testFindByUserIdFoundButNotTwoFactorAuthCode() {
        Long userId = 5L;
        TwoFactorAuthVerificationCode anotherVerificationCode = new TwoFactorAuthVerificationCode();
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(anotherVerificationCode));

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(anotherVerificationCode, result.get());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdForDifferentUser() {
        Long userId = 6L;
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdMultipleVerificationCodesForUser() {
        Long userId = 7L;
        TwoFactorAuthVerificationCode verificationCode1 = new TwoFactorAuthVerificationCode();
        TwoFactorAuthVerificationCode verificationCode2 = new TwoFactorAuthVerificationCode();
        List<TwoFactorAuthVerificationCode> verificationCodes = new ArrayList<>();
        verificationCodes.add(verificationCode1);
        verificationCodes.add(verificationCode2);
        when(verificationCodeRepository
                .findByUserId(userId))
                .thenReturn(Optional.of(verificationCode1));

        Optional<TwoFactorAuthVerificationCode> result = verificationCodeRepository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(verificationCode1, result.get());
        verify(verificationCodeRepository, times(1))
                .findByUserId(userId);
    }

}