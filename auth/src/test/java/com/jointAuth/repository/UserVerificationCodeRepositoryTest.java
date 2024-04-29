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
}
