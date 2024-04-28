package com.jointAuth.repository;

import com.jointAuth.model.user.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    Optional<VerificationCode> findByUserId(Long userId);

    List<VerificationCode> findAllByExpirationTimeBefore(LocalDateTime expirationTime);
}
