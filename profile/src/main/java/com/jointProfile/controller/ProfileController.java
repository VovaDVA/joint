package com.jointProfile.controller;

import com.jointProfile.connector.AuthConnector;
import com.jointProfile.converter.ProfileConverter;
import com.jointProfile.bom.ProfileBom;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping( "profile")
public class ProfileController {

    @Autowired
    private AuthConnector authConnector;

    @Autowired
    private ProfileService profileService;

    @PutMapping("/update")
    public ResponseEntity<ProfileBom> updateProfile(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @RequestBody ProfileDTO updatedProfile) {

        ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

        Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

        ProfileBom updatedProfileDto = profileService.updateProfile(currentProfile,updatedProfile);

        if (updatedProfileDto != null) {
            return ResponseEntity.ok(updatedProfileDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-avatar")
    public ResponseEntity<ProfileBom> updateAvatar(@RequestHeader(name = "Authorization", required = false) String token,
                                                   @RequestParam("avatar") MultipartFile avater) {

        ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

        Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

        ProfileBom updatedProfile = profileService.updateAvatar(avater, currentProfile);

        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/update-banner")
    public ResponseEntity<ProfileBom> updateBanner(@RequestHeader(name = "Authorization", required = false) String token,
                                                   @RequestParam("banner") MultipartFile banner) {

        ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

        Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

        ProfileBom updatedProfile = profileService.updateBanner(banner, currentProfile);

        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }

    }




}
