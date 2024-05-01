package com.jointAuth.repository;

import com.jointAuth.model.verification.PasswordResetVerificationCode;
import com.jointAuth.model.verification.UserVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetVerificationCodeRepository extends JpaRepository<PasswordResetVerificationCode,Long> {
    Optional<PasswordResetVerificationCode> findByUserId(Long userId);

    Optional<PasswordResetVerificationCode> findByUserIdAndCode(Long userId, String code);

    List<PasswordResetVerificationCode> findAllByExpirationTimeBefore(LocalDateTime currentTime);

}
