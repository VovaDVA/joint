package com.jointAuth.repository;

import com.jointAuth.model.user.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.model.user.UserVerificationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserVerificationCodeRepositoryTest {

    @Autowired
    private UserVerificationCodeRepository userVerificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userVerificationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByUserIdAndCodeWhenCodeExistsReturnsUserVerificationCode() {
        User curUser = new User();
        curUser.setEmail("test@example.com");
        User savedUser = userRepository.save(curUser);

        String code = "123456";
        UserVerificationCode userVerificationCode = new UserVerificationCode();

        userVerificationCode.setUser(curUser);
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setCode(code);

        userVerificationCodeRepository.save(userVerificationCode);

        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(savedUser.getId(), code);

        assertTrue(result.isPresent());
        assertEquals(userVerificationCode, result.get());
    }

    @Test
    void findByUserIdAndCodeWhenCodeDoesNotExistReturnsEmptyOptional() {
        User user = new User();
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(savedUser.getId(), "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndCodeWhenUserIdIsNullReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(null, "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndCodeWhenCodeIsNullReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(1L, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndCodeWhenIncorrectCodeForExistingUserReturnsEmptyOptional() {
        User curUser = new User();
        curUser.setEmail("test@example.com");
        User savedUser = userRepository.save(curUser);

        String code = "123456";
        UserVerificationCode userVerificationCode = new UserVerificationCode();

        userVerificationCode.setUser(curUser);
        userVerificationCode.setRequestType(RequestType.PASSWORD_RESET);
        userVerificationCode.setCode(code);

        userVerificationCodeRepository.save(userVerificationCode);

        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(savedUser.getId(), "654321");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndCodeWhenCorrectCodeForNonExistingUserReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(9999L, "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserIdAndCodeWhenIncorrectCodeForNonExistingUserReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(9999L, "654321");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserId_WhenRecordDoesNotExist_ReturnsEmptyOptional() {
        // Act
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserId(999L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserId_WhenUserIdIsNull_ReturnsEmptyOptional() {
        // Act
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserId(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserId_WhenNoRecordExists_ReturnsEmptyOptional() {
        // Предполагается, что база данных пуста
        Long userId = 1L;
        Optional<UserVerificationCode> userVerificationCode = userVerificationCodeRepository.findByUserId(userId);
        assertTrue(userVerificationCode.isEmpty());
    }

    @Test
    void findByUserId_WhenMultipleRecordsWithDifferentUserId_ReturnsCorrectUserVerificationCode() {
        User user1 = new User();
        user1.setEmail("test1@example.com");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("test2@example.com");
        User savedUser2 = userRepository.save(user2);

        UserVerificationCode verificationCode1 = new UserVerificationCode();
        verificationCode1.setCode("123456");
        verificationCode1.setUser(savedUser1);
        userVerificationCodeRepository.save(verificationCode1);

        UserVerificationCode verificationCode2 = new UserVerificationCode();
        verificationCode2.setCode("654321");
        verificationCode2.setUser(savedUser2);
        userVerificationCodeRepository.save(verificationCode2);

        Long userId = savedUser2.getId();
        Optional<UserVerificationCode> userVerificationCode = userVerificationCodeRepository.findByUserId(userId);
        assertTrue(userVerificationCode.isPresent());
        assertEquals(userId, userVerificationCode.get().getUser().getId());
    }

    @Test
    void findByUserId_WhenRecordExists_ReturnsCorrectUserVerificationCode() {
        User user = new User();
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        UserVerificationCode verificationCode = new UserVerificationCode();
        verificationCode.setCode("123456");
        verificationCode.setUser(savedUser);
        userVerificationCodeRepository.save(verificationCode);

        Long userId = savedUser.getId();
        Optional<UserVerificationCode> userVerificationCode = userVerificationCodeRepository.findByUserId(userId);
        assertTrue(userVerificationCode.isPresent());
        assertEquals(userId, userVerificationCode.get().getUser().getId());
    }

}
