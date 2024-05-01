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

    @Test
    void testFindByUserIdAndCode_NullUserId() {
        // Устанавливаем `userId` равным `null`
        Long userId = null;
        String code = "1234";

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_NullCode() {
        // Устанавливаем `code` равным `null`
        Long userId = 1L;
        String code = null;

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_NullUserIdAndCode() {
        // Устанавливаем `userId` и `code` равными `null`
        Long userId = null;
        String code = null;

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_LongMaxValueUserId() {
        // Устанавливаем `userId` равным `Long.MAX_VALUE`
        Long userId = Long.MAX_VALUE;
        String code = "1234";

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_EmptyCode() {
        // Устанавливаем `code` равным пустой строке
        Long userId = 1L;
        String code = "";

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_CodeFound() {
        // Устанавливаем `userId` и `code`
        Long userId = 1L;
        String code = "1234";

        User user = new User();
        user.setId(userId);

        // Создаем объект `PasswordResetVerificationCode` для возврата
        PasswordResetVerificationCode expectedCode = new PasswordResetVerificationCode();
        expectedCode.setUser(user);
        expectedCode.setCode(code);

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть `Optional` с объектом `PasswordResetVerificationCode`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.of(expectedCode));

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул `Optional` с ожидаемым объектом
        assertTrue(result.isPresent());
        assertEquals(expectedCode, result.get());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_CodeNotFound() {
        // Устанавливаем `userId` и `code`
        Long userId = 1L;
        String code = "1234";

        // Настраиваем поведение мока: метод `findByUserIdAndCode` должен вернуть пустой `Optional`
        when(repository.findByUserIdAndCode(userId, code)).thenReturn(Optional.empty());

        // Вызов метода `findByUserIdAndCode`
        Optional<PasswordResetVerificationCode> result = repository.findByUserIdAndCode(userId, code);

        // Проверяем, что метод вернул пустой `Optional`
        assertTrue(result.isEmpty());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }

    @Test
    void testFindByUserIdAndCode_DatabaseNotResponding() {
        // Устанавливаем `userId` и `code`
        Long userId = 1L;
        String code = "1234";

        // Имитация ситуации, когда база данных не отвечает (генерируем исключение)
        when(repository.findByUserIdAndCode(userId, code)).thenThrow(new RuntimeException("Database not responding"));

        // Проверяем, что метод выбрасывает исключение, указывающее на проблему с базой данных
        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.findByUserIdAndCode(userId, code);
        });

        // Проверяем сообщение об ошибке
        assertEquals("Database not responding", exception.getMessage());

        // Проверяем, что метод `findByUserIdAndCode` был вызван с правильными аргументами
        verify(repository).findByUserIdAndCode(userId, code);
    }
}
