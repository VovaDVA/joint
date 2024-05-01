package com.jointAuth.repository;

import com.jointAuth.model.verification.TwoFactorAuthVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TwoFactorAuthVerificationCodeRepository extends JpaRepository<TwoFactorAuthVerificationCode,Long> {
    Optional<TwoFactorAuthVerificationCode> findByUserId(Long userId);

    List<TwoFactorAuthVerificationCode> findAllByExpirationTimeBefore(LocalDateTime expirationTime);
}
