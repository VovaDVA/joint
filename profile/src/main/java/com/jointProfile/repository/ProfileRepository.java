package com.jointProfile.repository;

// используется только для связки, чтобы указать, ЧТО мы будем использовать из JPA

import com.jointProfile.entity.Profile;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@NonNullApi
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
