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

import java.time.LocalDateTime;
import java.util.List;
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
    public void findByUserIdAndCodeWhenCodeExistsReturnsUserVerificationCode() {
        User curUser = new User();
        curUser.setEmail("test@gmail.com");
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
    public void findByUserIdAndCodeWhenCodeDoesNotExistReturnsEmptyOptional() {
        User user = new User();
        user.setEmail("test@gmail.com");
        User savedUser = userRepository.save(user);

        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(savedUser.getId(), "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdAndCodeWhenUserIdIsNullReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(null, "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdAndCodeWhenCodeIsNullReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(1L, null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdAndCodeWhenIncorrectCodeForExistingUserReturnsEmptyOptional() {
        User curUser = new User();
        curUser.setEmail("test@gmail.com");
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
    public void findByUserIdAndCodeWhenCorrectCodeForNonExistingUserReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(9999L, "123456");

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdAndCodeWhenIncorrectCodeForNonExistingUserReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserIdAndCode(9999L, "654321");

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdWhenRecordDoesNotExistReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdWhenUserIdIsNullReturnsEmptyOptional() {
        Optional<UserVerificationCode> result = userVerificationCodeRepository.findByUserId(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findByUserIdWhenNoRecordExistsReturnsEmptyOptional() {
        Long userId = 1L;

        Optional<UserVerificationCode> userVerificationCode = userVerificationCodeRepository.findByUserId(userId);
        assertTrue(userVerificationCode.isEmpty());
    }

    @Test
    public void findByUserIdWhenMultipleRecordsWithDifferentUserIdReturnsCorrectUserVerificationCode() {
        User user1 = new User();
        user1.setEmail("test1@gmail.com");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("test2@gmail.com");
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
    public void findByUserIdWhenRecordExistsReturnsCorrectUserVerificationCode() {
        User user = new User();
        user.setEmail("test@gmail.com");
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

    @Test
    public void findAllByExpirationTimeBeforeWhenRecordsExistReturnsAllRecords() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime1 = currentTime.minusHours(10);
        LocalDateTime expirationTime2 = currentTime.minusMinutes(30);
        LocalDateTime expirationTime3 = currentTime.minusDays(1);

        UserVerificationCode userVerificationCode1 = new UserVerificationCode();

        userVerificationCode1.setCode("code1");
        userVerificationCode1.setExpirationTime(expirationTime1);

        UserVerificationCode userVerificationCode2 = new UserVerificationCode();

        userVerificationCode2.setCode("code2");
        userVerificationCode2.setExpirationTime(expirationTime2);

        UserVerificationCode userVerificationCode3 = new UserVerificationCode();

        userVerificationCode3.setCode("code3");
        userVerificationCode3.setExpirationTime(expirationTime3);

        userVerificationCodeRepository.save(userVerificationCode1);
        userVerificationCodeRepository.save(userVerificationCode2);
        userVerificationCodeRepository.save(userVerificationCode3);

        List<UserVerificationCode> codes = userVerificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        assertEquals(3, codes.size());
        assertTrue(codes.stream().allMatch(code -> code.getExpirationTime().isBefore(currentTime)));
    }

    @Test
    public void findAllByExpirationTimeBeforeWhenNoRecordsBeforeCurrentTimeReturnsEmptyList() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime1 = currentTime.plusHours(1);
        LocalDateTime expirationTime2 = currentTime.plusMinutes(30);
        LocalDateTime expirationTime3 = currentTime.plusDays(1);

        UserVerificationCode userVerificationCode1 = new UserVerificationCode();

        userVerificationCode1.setCode("code1");
        userVerificationCode1.setExpirationTime(expirationTime1);

        UserVerificationCode userVerificationCode2 = new UserVerificationCode();

        userVerificationCode2.setCode("code2");
        userVerificationCode2.setExpirationTime(expirationTime2);

        UserVerificationCode userVerificationCode3 = new UserVerificationCode();

        userVerificationCode3.setCode("code3");
        userVerificationCode3.setExpirationTime(expirationTime3);

        userVerificationCodeRepository.save(userVerificationCode1);
        userVerificationCodeRepository.save(userVerificationCode2);
        userVerificationCodeRepository.save(userVerificationCode3);

        List<UserVerificationCode> codes = userVerificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        assertTrue(codes.isEmpty());
    }

    @Test
    public void findAllByExpirationTimeBeforeWhenNoRecordsReturnsEmptyList() {
        List<UserVerificationCode> codes = userVerificationCodeRepository.findAllByExpirationTimeBefore(LocalDateTime.now());

        assertTrue(codes.isEmpty());
    }

    @Test
    public void findAllByExpirationTimeBeforeWhenRecordsExistBeforeCurrentTimeReturnsCorrectRecords() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime1 = currentTime.minusHours(1);
        LocalDateTime expirationTime2 = currentTime.plusMinutes(30);
        LocalDateTime expirationTime3 = currentTime.plusDays(1);

        UserVerificationCode userVerificationCode1 = new UserVerificationCode();

        userVerificationCode1.setCode("code1");
        userVerificationCode1.setExpirationTime(expirationTime1);

        UserVerificationCode userVerificationCode2 = new UserVerificationCode();

        userVerificationCode2.setCode("code2");
        userVerificationCode2.setExpirationTime(expirationTime2);

        UserVerificationCode userVerificationCode3 = new UserVerificationCode();

        userVerificationCode3.setCode("code3");
        userVerificationCode3.setExpirationTime(expirationTime3);

        userVerificationCodeRepository.save(userVerificationCode1);
        userVerificationCodeRepository.save(userVerificationCode2);
        userVerificationCodeRepository.save(userVerificationCode3);

        List<UserVerificationCode> codes = userVerificationCodeRepository.findAllByExpirationTimeBefore(LocalDateTime.now());

        assertEquals(1, codes.size());
    }
}
