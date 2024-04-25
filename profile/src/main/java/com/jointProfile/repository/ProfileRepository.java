package com.jointProfile.repository;

// используется только для связки, чтобы указать, ЧТО мы будем использовать из JPA

import com.jointProfile.entity.Profiles;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NonNullApi
public interface ProfileRepository extends JpaRepository<Profiles, Long> {
    Optional<Profiles> findByUserId(Long userId);
}
