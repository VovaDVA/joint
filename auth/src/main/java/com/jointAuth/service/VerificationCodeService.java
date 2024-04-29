package com.jointAuth.service;

import com.jointAuth.model.user.RequestType;
import com.jointAuth.model.user.TwoFactorAuthVerificationCode;
import com.jointAuth.model.user.UserVerificationCode;
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

    public VerificationCodeService(@Autowired TwoFactorAuthVerificationCodeRepository verificationCodeRepository,
                                   @Autowired UserRepository userRepository,
                                   @Autowired UserVerificationCodeRepository userVerificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
        this.userVerificationCodeRepository = userVerificationCodeRepository;
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
                .orElseThrow(() -> new RuntimeException("Verification code not found for userId: " + userId));

        if (verificationCode.getCode().equals(code) && !isVerificationCodeExpiredFor2FA(verificationCode)) {
            verificationCodeRepository.delete(verificationCode);
            return true;
        }
        return false;
    }

    public boolean verifyUserVerificationCodeForUser(Long userId, String code) {
        Optional<UserVerificationCode> optionalVerificationCode = userVerificationCodeRepository.findByUserId(userId);

        if (optionalVerificationCode.isEmpty()) {
            throw new RuntimeException("Verification code not found for userId: " + userId);
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

    public void saveOrUpdateVerificationCodeForResetPassword(Long userId, String verificationCode, RequestType requestType, LocalDateTime expirationTime) {
        if (userId == null) {
            throw new NullPointerException("User ID cannot be null");
        }

        Optional<UserVerificationCode> existingCodeOptionalForPassword = userVerificationCodeRepository.findByUserIdAndRequestType(userId, requestType);
        Optional<UserVerificationCode> existingCodeOptionalForDeletion = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);

        UserVerificationCode userVerificationCode;

        if (existingCodeOptionalForPassword.isPresent()) {
            userVerificationCode = existingCodeOptionalForPassword.get();
        } else if (existingCodeOptionalForDeletion.isPresent()) {
            userVerificationCode = existingCodeOptionalForDeletion.get();
            userVerificationCode.setRequestType(requestType); // Change the request type to PASSWORD_RESET
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
            throw new NullPointerException("User ID cannot be null");
        }

        Optional<UserVerificationCode> existingCodeOptionalForDeletion = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.ACCOUNT_DELETION);
        Optional<UserVerificationCode> existingCodeOptionalForPassword = userVerificationCodeRepository.findByUserIdAndRequestType(userId, RequestType.PASSWORD_RESET);
        UserVerificationCode userVerificationCode;

        if (existingCodeOptionalForDeletion.isPresent()) {
            userVerificationCode = existingCodeOptionalForDeletion.get();
        } else if (existingCodeOptionalForPassword.isPresent()) {
            userVerificationCode = existingCodeOptionalForPassword.get();
            userVerificationCode.setRequestType(RequestType.ACCOUNT_DELETION); // Change the request type to ACCOUNT_DELETION
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
    public void cleanExpiredVerificationCodesForPasswordReset() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<UserVerificationCode> expiredCodes = userVerificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        userVerificationCodeRepository.deleteAll(expiredCodes);
    }

}
