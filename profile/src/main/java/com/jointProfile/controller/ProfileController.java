package com.jointProfile.controller;

import com.jointProfile.bom.user.ErrorResponse;
import com.jointProfile.connector.AuthConnector;
import com.jointProfile.converter.ProfileConverter;
import com.jointProfile.bom.profile.ProfileBom;
import com.jointProfile.entity.ProfileDTO;
import com.jointProfile.entity.Profiles;
import com.jointProfile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping( "profile")
public class ProfileController {

    @Autowired
    private AuthConnector authConnector;

    @Autowired
    private ProfileService profileService;

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestHeader(name = "Authorization") String token,
                                                    @RequestBody ProfileDTO updatedProfile) {

        try {
            ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

            Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

            ProfileBom updatedProfileDto = profileService.updateProfile(currentProfile, updatedProfile);

            return ResponseEntity.ok(updatedProfileDto);

        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Профиль не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PutMapping("/update-avatar")
    public ResponseEntity<?> updateAvatar(@RequestHeader(name = "Authorization") String token,
                                                   @RequestParam("avatar") MultipartFile avater) {
        try {
            ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

            Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

            ProfileBom updatedProfile = profileService.updateAvatar(avater, currentProfile);

            return ResponseEntity.ok(updatedProfile);

        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Профиль не найден");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @PutMapping("/update-banner")
    public ResponseEntity<?> updateBanner(@RequestHeader(name = "Authorization") String token,
                                                   @RequestParam("banner") MultipartFile banner) {

       try {
           ProfileBom currentBomProfile = authConnector.getCurrentProfile(token);

           Profiles currentProfile = ProfileConverter.converterToEntity(currentBomProfile);

           ProfileBom updatedProfile = profileService.updateBanner(banner, currentProfile);

           return ResponseEntity.ok(updatedProfile);
       } catch (RuntimeException e) {
           ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Профиль не найден");
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
       } catch (Exception e) {
           ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутренняя ошибка сервера");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
       }
    }

}
