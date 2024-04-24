package com.jointAuth.controller;

import com.jointAuth.bom.profile.ProfileBom;
import com.jointAuth.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "auth")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/getCurrentProfile")
    public ProfileBom getCurrentUser(@RequestHeader(name = "Authorization", required = false) String token) {
        return profileService.getCurrentProfile(token);
    }
}