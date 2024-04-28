package com.jointAuth.repository;

import com.jointAuth.model.user.RequestType;
import com.jointAuth.model.user.User;
import com.jointAuth.model.user.UserVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVerificationCodeRepository extends JpaRepository<UserVerificationCode, Long> {
    Optional<UserVerificationCode> findByUserIdAndCode(Long userId, String code);

    Optional<UserVerificationCode> findByUserId(Long userId);

    List<UserVerificationCode> findAllByExpirationTimeBefore(LocalDateTime currentTime);

    void deleteByUserId(Long userId);

    Optional<UserVerificationCode> findByUserIdAndRequestType(Long userId, RequestType requestType);

}