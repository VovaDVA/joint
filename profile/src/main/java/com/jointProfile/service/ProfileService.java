package com.jointProfile.service;

import com.jointProfile.converter.ProfileConverter;
import com.jointProfile.bom.ProfileBom;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.repository.ProfileRepository;
import com.jointProfile.utils.RemoteFileUploader;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;


@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;


    public ProfileBom updateProfile(Profiles currentProfile, ProfileDTO updatedProfile) {

        Profiles currentUpdatedProfile = profileRepository.findById(currentProfile.getId()).orElseThrow(() ->
                new EntityNotFoundException("Profile not found"));

        if (updatedProfile.getDescription() != null) {
            currentUpdatedProfile.setDescription(updatedProfile.getDescription());
        }
        if (updatedProfile.getBirthday() != null) {
            currentUpdatedProfile.setBirthday(updatedProfile.getBirthday());
        }
        if (updatedProfile.getCountry() != null) {
            currentUpdatedProfile.setCountry(updatedProfile.getCountry());
        }
        if (updatedProfile.getCity() != null) {
            currentUpdatedProfile.setCity(updatedProfile.getCity());
        }
        if (updatedProfile.getPhone() != null) {
            currentUpdatedProfile.setPhone(updatedProfile.getPhone());
        }

        currentProfile.setLastEdited(new Date());

        profileRepository.save(currentProfile);

        return ProfileConverter.converterToBom(currentProfile);
    }

}