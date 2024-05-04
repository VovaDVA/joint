package com.jointAuth.service;

import com.jointAuth.model.verification.PasswordResetVerificationCode;
import com.jointAuth.model.verification.RequestType;
import com.jointAuth.model.verification.TwoFactorAuthVerificationCode;
import com.jointAuth.model.verification.UserVerificationCode;
import com.jointAuth.repository.PasswordResetVerificationCodeRepository;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.TwoFactorAuthVerificationCodeRepository;
import com.jointAuth.repository.UserVerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationCodeService {

    private final TwoFactorAuthVerificationCodeRepository verificationCodeRepository;

    private final UserRepository userRepository;

    private final UserVerificationCodeRepository userVerificationCodeRepository;

    private final PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository;

    public VerificationCodeService(@Autowired TwoFactorAuthVerificationCodeRepository verificationCodeRepository,
                                   @Autowired UserRepository userRepository,
                                   @Autowired UserVerificationCodeRepository userVerificationCodeRepository,
                                   @Autowired PasswordResetVerificationCodeRepository passwordResetVerificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
        this.userVerificationCodeRepository = userVerificationCodeRepository;
        this.passwordResetVerificationCodeRepository = passwordResetVerificationCodeRepository;
    }

    public void saveOrUpdateVerificationCodeFor2FA(Long userId, String newCode) {
        Optional<TwoFactorAuthVerificationCode> existingCodeOptional = verificationCodeRepository.findByUserId(userId);

        TwoFactorAuthVerificationCode verificationCode;

        if (existingCodeOptional.isPresent()) {
            verificationCode = existingCodeOptional.get();
        } else {
            verificationCode = new TwoFactorAuthVerificationCode();
            verificationCode.setUser(userRepository.getOne(userId));
        }
        verificationCode.setCode(newCode);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        verificationCodeRepository.save(verificationCode);
    }


    public boolean verifyVerificationCodeFor2FA(Long userId, String code) {
        TwoFactorAuthVerificationCode verificationCode = verificationCodeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Не найден проверочный код для идентификатора пользователя: " + userId));

        if (verificationCode.getCode().equals(code) && !isVerificationCodeExpiredFor2FA(verificationCode)) {
            verificationCodeRepository.delete(verificationCode);
            return true;
        }
        return false;
    }

    public boolean verifyUserVerificationCodeForUser(Long userId, String code) {
        Optional<UserVerificationCode> optionalVerificationCode = userVerificationCodeRepository.findByUserId(userId);

        if (optionalVerificationCode.isEmpty()) {
            throw new RuntimeException("Не найден проверочный код для идентификатора пользователя: " + userId);
        }

        UserVerificationCode verificationCode = optionalVerificationCode.get();

        if (verificationCode.getCode().equals(code) && !isVerificationCodeExpiredForUser(verificationCode)) {
            userVerificationCodeRepository.delete(verificationCode);
            return true;
        }
        return false;
    }

    private boolean isVerificationCodeExpiredForUser(UserVerificationCode verificationCode) {
        return verificationCode.getExpirationTime().isBefore(LocalDateTime.now());
    }

    private boolean isVerificationCodeExpiredFor2FA(TwoFactorAuthVerificationCode verificationCode) {
        return verificationCode.getExpirationTime().isBefore(LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1209600000)
    public void cleanExpiredVerificationCodesFor2FA() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<TwoFactorAuthVerificationCode> expiredCodes = verificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        verificationCodeRepository.deleteAll(expiredCodes);
    }

    public void saveOrUpdateVerificationCodeForChangePassword(Long userId, String verificationCode, RequestType requestType, LocalDateTime expirationTime) {
        if (userId == null) {
            throw new NullPointerException("Идентификатор пользователя не может быть пустым");
        }

        Optional<UserVerificationCode> existingCodeOptionalForPassword = userVerificationCodeRepository.findByUserIdAndRequestType(userId, requestType);
        Optional<UserVerificationCode> existingCodeOptionalForDeletion = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);

        UserVerificationCode userVerificationCode;

        if (existingCodeOptionalForPassword.isPresent()) {
            userVerificationCode = existingCodeOptionalForPassword.get();
        } else if (existingCodeOptionalForDeletion.isPresent()) {
            userVerificationCode = existingCodeOptionalForDeletion.get();
            userVerificationCode.setRequestType(requestType);
        } else {
            userVerificationCode = new UserVerificationCode();
            userVerificationCode.setUser(userRepository.getById(userId));
            userVerificationCode.setRequestType(requestType);
        }

        userVerificationCode.setCode(verificationCode);
        userVerificationCode.setExpirationTime(expirationTime);

        userVerificationCodeRepository.save(userVerificationCode);
    }


    public void saveOrUpdateVerificationCodeForAccountDeletion(Long userId, String newVerificationCode, LocalDateTime expirationTime) {
        if (userId == null) {
            throw new NullPointerException("Идентификатор пользователя не может быть пустым");
        }

        Optional<UserVerificationCode> existingCodeOptionalForDeletion = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        Optional<UserVerificationCode> existingCodeOptionalForPassword = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.PASSWORD_CHANGE);
        UserVerificationCode userVerificationCode;

        if (existingCodeOptionalForDeletion.isPresent()) {
            userVerificationCode = existingCodeOptionalForDeletion.get();
        } else if (existingCodeOptionalForPassword.isPresent()) {
            userVerificationCode = existingCodeOptionalForPassword.get();
            userVerificationCode.setRequestType(RequestType.ACCOUNT_DELETION);
        } else {
            userVerificationCode = new UserVerificationCode();
            userVerificationCode.setUser(userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
            userVerificationCode.setRequestType(RequestType.ACCOUNT_DELETION);
        }

        userVerificationCode.setCode(newVerificationCode);
        userVerificationCode.setExpirationTime(expirationTime);

        userVerificationCodeRepository.save(userVerificationCode);
    }

    @Scheduled(fixedRate = 1209600000)
    public void cleanExpiredVerificationCodesForPasswordChange() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<UserVerificationCode> expiredCodes = userVerificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        userVerificationCodeRepository.deleteAll(expiredCodes);
    }

    public void saveOrUpdateVerificationCodeForPasswordReset(Long userId, String verificationCode, LocalDateTime expirationTime) {
        if (userId == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не может быть пустым");
        }

        Optional<PasswordResetVerificationCode> existingCodeOptional = passwordResetVerificationCodeRepository.findByUserId(userId);

        PasswordResetVerificationCode passwordResetVerificationCode;

        if (existingCodeOptional.isPresent()) {
            passwordResetVerificationCode = existingCodeOptional.get();
            passwordResetVerificationCode.setCode(verificationCode);
            passwordResetVerificationCode.setExpirationTime(expirationTime);
        } else {
            passwordResetVerificationCode = new PasswordResetVerificationCode();
            passwordResetVerificationCode.setUser(userRepository.getById(userId));
            passwordResetVerificationCode.setCode(verificationCode);
            passwordResetVerificationCode.setExpirationTime(expirationTime);
        }

        passwordResetVerificationCodeRepository.save(passwordResetVerificationCode);
    }

    @Scheduled(fixedRate = 1209600000)
    public void cleanExpiredVerificationCodesForPasswordReset() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<PasswordResetVerificationCode> expiredCodes = passwordResetVerificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        passwordResetVerificationCodeRepository.deleteAll(expiredCodes);
    }

}
