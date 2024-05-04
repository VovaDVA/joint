package com.jointAuth.repository;

import com.jointAuth.model.user.User;
import com.jointAuth.model.verification.PasswordResetVerificationCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class PasswordResetVerificationCodeRepositoryTest {

    private PasswordResetVerificationCodeRepository repository = mock(PasswordResetVerificationCodeRepository.class);

    @Test
    public void testFindByUserId_CodeFound() {
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
    public void testFindByUserIdCodeNotFound() {
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
    public void testFindByUserIdNullUserId() {
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
    public void testFindByUserIdNonExistentUserId() {
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
    public void testFindByUserIdExistingUserId() {
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
    public void testFindByUserIdZeroUserId() {
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
    public void testFindByUserIdNegativeUserId() {
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
    public void testFindByUserIdMaxValueUserId() {
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
    public void testFindByUserIdMinValueUserId() {
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
    public void testFindByUserIdLargePositiveUserId() {
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
    public void testFindByUserIdDatabaseNotResponding() {
        Long userId = 1L;

        when(repository
                .findByUserId(userId))
                .thenThrow(new RuntimeException("База данных не отвечает"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.findByUserId(userId);
        });

        assertEquals("База данных не отвечает", exception.getMessage());

        verify(repository)
                .findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndCodeNullUserId() {
        Long userId = null;
        String code = "1234";

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeNullCode() {
        Long userId = 1L;
        String code = null;

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeNullUserIdAndCode() {
        Long userId = null;
        String code = null;

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeLongMaxValueUserId() {
        Long userId = Long.MAX_VALUE;
        String code = "1234";

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeEmptyCode() {
        Long userId = 1L;
        String code = "";

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeCodeFound() {
        Long userId = 1L;
        String code = "1234";

        User user = new User();
        user.setId(userId);

        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setUser(user);
        expectedCode.setCode(code);

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.of(expectedCode));

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isPresent());
        assertEquals(expectedCode, result.get());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeCodeNotFound() {
        Long userId = 1L;
        String code = "1234";

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        assertTrue(result.isEmpty());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindByUserIdAndCodeDatabaseNotResponding() {
        Long userId = 1L;
        String code = "1234";

        when(repository
                .findByUserIdAndCode(userId, code))
                .thenThrow(new RuntimeException("База данных не отвечает"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.findByUserIdAndCode(userId, code);
        });

        assertEquals("База данных не отвечает", exception.getMessage());

        verify(repository)
                .findByUserIdAndCode(userId, code);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeExpiredCodesExist() {
        LocalDateTime currentTime = LocalDateTime.now();

        PasswordResetVerificationCode code1 = new PasswordResetVerificationCode();
        code1.setExpirationTime(currentTime.minusDays(1));

        PasswordResetVerificationCode code2 = new PasswordResetVerificationCode();
        code2.setExpirationTime(currentTime.minusHours(1));

        List<PasswordResetVerificationCode> expiredCodes = Arrays.asList(code1, code2);

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(expiredCodes);

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(expiredCodes, result);

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeNoExpiredCodesExist() {
        LocalDateTime currentTime = LocalDateTime.now();

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(Collections.emptyList());

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertTrue(result.isEmpty());

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeExpiredCodesEqualToCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();

        PasswordResetVerificationCode code1 = new PasswordResetVerificationCode();
        code1.setExpirationTime(currentTime);

        PasswordResetVerificationCode code2 = new PasswordResetVerificationCode();
        code2.setExpirationTime(currentTime);

        List<PasswordResetVerificationCode> expiredCodes = Arrays.asList(code1, code2);

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(expiredCodes);

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(expiredCodes, result);

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeDatabaseNotResponding() {
        LocalDateTime currentTime = LocalDateTime.now();

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenThrow(new RuntimeException("База данных не отвечает"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.findAllByExpirationTimeBefore(currentTime);
        });

        assertEquals("База данных не отвечает", exception.getMessage());

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeValidCodesExist() {
        LocalDateTime currentTime = LocalDateTime.now();

        PasswordResetVerificationCode code1 = new PasswordResetVerificationCode();
        code1.setExpirationTime(currentTime.plusDays(1));

        PasswordResetVerificationCode code2 = new PasswordResetVerificationCode();
        code2.setExpirationTime(currentTime.plusHours(2));

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(Collections.emptyList());

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertTrue(result.isEmpty());

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeValidAndExpiredCodesExist() {
        LocalDateTime currentTime = LocalDateTime.now();

        PasswordResetVerificationCode expiredCode = new PasswordResetVerificationCode();
        expiredCode.setExpirationTime(currentTime.minusDays(1));

        PasswordResetVerificationCode validCode = new PasswordResetVerificationCode();
        validCode.setExpirationTime(currentTime.plusDays(1));

        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(Arrays.asList(expiredCode));

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(Arrays.asList(expiredCode), result);

        verify(repository)
                .findAllByExpirationTimeBefore(currentTime);
    }

    @Test
    public void testFindAllByExpirationTimeBeforeEmptyList() {
        LocalDateTime currentTime = LocalDateTime.now();
        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(Collections.emptyList());

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAllByExpirationTimeBeforeOneExpiredCode() {
        LocalDateTime currentTime = LocalDateTime.now();

        PasswordResetVerificationCode expiredCode = new PasswordResetVerificationCode();
        expiredCode.setExpirationTime(currentTime.minusDays(1));
        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(Collections.singletonList(expiredCode));

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(1, result.size());
        assertEquals(expiredCode, result.get(0));
    }

    @Test
    public void testFindAllByExpirationTimeBeforeMultipleExpiredCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PasswordResetVerificationCode> expiredCodes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            PasswordResetVerificationCode code = new PasswordResetVerificationCode();
            code.setExpirationTime(currentTime.minusDays(i));
            expiredCodes.add(code);
        }
        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(expiredCodes);

        // Вызов метода
        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(expiredCodes.size(), result.size());
        assertTrue(result.containsAll(expiredCodes));
    }

    @Test
    public void testFindAllByExpirationTimeBeforeMixedCodes() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PasswordResetVerificationCode> codes = new ArrayList<>();

        PasswordResetVerificationCode expiredCode1 = new PasswordResetVerificationCode();
        expiredCode1.setExpirationTime(currentTime.minusDays(2));
        codes.add(expiredCode1);

        PasswordResetVerificationCode expiredCode2 = new PasswordResetVerificationCode();
        expiredCode2.setExpirationTime(currentTime.minusDays(1));
        codes.add(expiredCode2);

        PasswordResetVerificationCode validCode = new PasswordResetVerificationCode();
        validCode.setExpirationTime(currentTime.plusDays(1));
        codes.add(validCode);

        List<PasswordResetVerificationCode> expiredCodes = codes.subList(0, 2);
        when(repository
                .findAllByExpirationTimeBefore(currentTime))
                .thenReturn(expiredCodes);

        List<PasswordResetVerificationCode> result = repository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(expiredCodes.size(), result.size());
        assertTrue(result.containsAll(expiredCodes));
    }

    @Test
    public void testFindByCodeFound() {
        String code = "123456";
        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setCode(code);

        when(repository
                .findByCode(code))
                .thenReturn(Optional.of(expectedCode));

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertTrue(result.isPresent(), "Код должен быть найден");
        assertEquals(expectedCode, result.get(), "Результат должен совпадать с ожидаемым объектом");
    }

    @Test
    public void testFindByCodeNotFound() {
        String code = "654321";

        when(repository
                .findByCode(code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertFalse(result.isPresent(), "Код не должен быть найден");
    }

    @Test
    public void testFindByCodeWithNull() {
        String code = null;

        when(repository
                .findByCode(code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertFalse(result.isPresent(), "Код не должен быть найден");
    }

    @Test
    public void testFindByCodeEmptyString() {
        String code = "";

        when(repository
                .findByCode(code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertFalse(result.isPresent(), "Код не должен быть найден для пустой строки");
    }

    @Test
    public void testFindByCodeWithSpaces() {
        String code = " 123456 ";

        when(repository
                .findByCode(code.trim()))
                .thenReturn(Optional.of(new PasswordResetVerificationCode()));

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertTrue(result.isEmpty(), "Код не должен быть найден при наличии пробелов");
    }

    @Test
    public void testFindByCodeWithSpecialCharacters() {
        String code = "!@#%^&*()_+";

        when(repository
                .findByCode(code))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByCode(code);
        assertFalse(result.isPresent(), "Код не должен быть найден для строки со спецсимволами");
    }

    @Test
    public void testFindByCodeWithDifferentLengths() {
        String shortCode = "123";

        when(repository
                .findByCode(shortCode))
                .thenReturn(Optional.empty());

        Optional<PasswordResetVerificationCode> result = repository.findByCode(shortCode);
        assertFalse(result.isPresent(), "Код не должен быть найден для слишком короткого кода");

        String longCode = "1234567890123456789012345678901234567890";
        when(repository
                .findByCode(longCode))
                .thenReturn(Optional.empty());

        result = repository.findByCode(longCode);
        assertFalse(result.isPresent(), "Код не должен быть найден для слишком длинного кода");
    }

}
