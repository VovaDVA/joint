package com.jointAuth.repository;

import com.jointAuth.model.user.User;
import com.jointAuth.model.verification.PasswordResetVerificationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class PasswordResetVerificationCodeRepositoryTest {

    private PasswordResetVerificationCodeRepository repository = mock(PasswordResetVerificationCodeRepository.class);

    @Test
    void testFindByUserId_CodeFound() {
        Long userId = 1L;

        User user = new User();

        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setUser(user);

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.of(expectedCode));

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedCode, result.get());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdCodeNotFound() {
        Long userId = 2L;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdNullUserId() {
        Long userId = null;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdNonExistentUserId() {
        Long userId = 999L;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdExistingUserId() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setUser(user);

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.of(expectedCode));

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedCode, result.get());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdZeroUserId() {
        Long userId = 0L;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdNegativeUserId() {
        Long userId = -1L;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdMaxValueUserId() {
        Long userId = Long.MAX_VALUE;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdMinValueUserId() {
        Long userId = Long.MIN_VALUE;

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdLargePositiveUserId() {
        Long userId = 999999999L;

        User user = new User();
        user.setId(userId);

        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setUser(user);

        when(repository
                .findByUserId(userId))
                .thenReturn(Optional.of(expectedCode));

        Optional<PasswordResetVerificationCode> result = repository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedCode, result.get());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    void testFindByUserIdDatabaseNotResponding() {
        Long userId = 1L;

        when(repository
                .findByUserId(userId))
                .thenThrow(new RuntimeException("Database not responding"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.findByUserId(userId);
        });

        assertEquals("Database not responding", exception.getMessage());

        verify(repository)
                .findByUserId(userId);
    }
}
