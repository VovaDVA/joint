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

@RestController
@RequestMapping(path = "profiles")
public class ProfileController {

    @Autowired
    private AuthConnector authConnector;

    @Autowired
    private ProfileService profileService;

    @PutMapping(path ="/update")
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
}
