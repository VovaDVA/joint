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

    @Scheduled(fixedRate = 1209600000)
    public void cleanExpiredVerificationCodesFor2FA() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<TwoFactorAuthVerificationCode> expiredCodes = verificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        verificationCodeRepository.deleteAll(expiredCodes);
    }

    private boolean isVerificationCodeExpiredFor2FA(TwoFactorAuthVerificationCode verificationCode) {
        return verificationCode.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public void saveOrUpdateVerificationCodeForResetPassword(Long userId, String verificationCode, RequestType requestType, LocalDateTime expirationTime) {
        Optional<UserVerificationCode> existingCodeOptional = userVerificationCodeRepository.findByUserId(userId);

        UserVerificationCode userVerificationCode;

        if (existingCodeOptional.isPresent()) {
            userVerificationCode = existingCodeOptional.get();
        } else {
            userVerificationCode = new UserVerificationCode();

            userVerificationCode.setUser(userRepository.getById(userId));
        }

        userVerificationCode.setCode(verificationCode);

        userVerificationCode.setRequestType(requestType);

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
