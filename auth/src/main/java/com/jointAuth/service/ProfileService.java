package com.jointAuth.service;

import com.jointAuth.bom.profile.ProfileBom;
import com.jointAuth.converter.ProfileConverter;
import com.jointAuth.model.profile.Profile;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ProfileRepository profileRepository;

    public ProfileBom getCurrentProfile(String token) {
        try {
            Long currentProfileId = jwtTokenUtils.getCurrentProfileId(token);

            Optional<Profile> currentProfile = profileRepository.findByUserId(currentProfileId);

            return ProfileConverter.converterToBom(currentProfile);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching the current profile", e);
        }
    }
}
