package com.jointAuth.service;

import com.jointAuth.model.user.VerificationCode;
import com.jointAuth.repository.UserRepository;
import com.jointAuth.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    private final UserRepository userRepository;

    public VerificationCodeService(@Autowired VerificationCodeRepository verificationCodeRepository,
                                   @Autowired UserRepository userRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.userRepository = userRepository;
    }

    public void saveOrUpdateVerificationCode(Long userId, String newCode) {
        Optional<VerificationCode> existingCodeOptional = verificationCodeRepository.findByUserId(userId);

        VerificationCode verificationCode;

        if (existingCodeOptional.isPresent()) {
            verificationCode = existingCodeOptional.get();
        } else {
            verificationCode = new VerificationCode();
            verificationCode.setUser(userRepository.getOne(userId));
        }
        verificationCode.setCode(newCode);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(2));

        verificationCodeRepository.save(verificationCode);
    }


    public boolean verifyVerificationCode(Long userId, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Verification code not found for userId: " + userId));

        if (verificationCode.getCode().equals(code) && !isVerificationCodeExpired(verificationCode)) {
            verificationCodeRepository.delete(verificationCode);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 1209600000)
    public void cleanExpiredVerificationCodes() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<VerificationCode> expiredCodes = verificationCodeRepository.findAllByExpirationTimeBefore(currentTime);

        verificationCodeRepository.deleteAll(expiredCodes);
    }

    private boolean isVerificationCodeExpired(VerificationCode verificationCode) {
        return verificationCode.getExpirationTime().isBefore(LocalDateTime.now());
    }
}
