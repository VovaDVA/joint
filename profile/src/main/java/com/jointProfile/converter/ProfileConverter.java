package com.jointProfile.converter;

import com.jointProfile.bom.ProfileBom;
import com.jointProfile.entity.Profiles;

import java.util.Date;

public class ProfileConverter {

    public static ProfileBom converterToBom(Profiles profile) {
        if (profile == null) {
            return null;
        }

        ProfileBom profileBom = new ProfileBom();
        profileBom.setProfileId(profile.getId());
        profileBom.setUserId(profile.getId());
        profileBom.setAvatar(profile.getAvatar());
        profileBom.setBanner(profile.getBanner());
        profileBom.setDescription(profile.getDescription());
        profileBom.setBirthday(profile.getBirthday());
        profileBom.setCountry(profile.getCountry());
        profileBom.setCity(profile.getCity());
        profileBom.setPhone(profile.getPhone());
        profileBom.setLastEdited(String.valueOf(profile.getLastEdited()));

        return profileBom;
    }

    public static Profiles converterToEntity(ProfileBom profileBom) {
        if (profileBom == null) {
            return null;
        }

        Profiles profile = new Profiles();
        profile.setId(profileBom.getProfileId());
        profile.setUserId(profileBom.getUserId());
        profile.setDescription(profileBom.getDescription());
        profile.setBirthday(profileBom.getBirthday());
        profile.setCountry(profileBom.getCountry());
        profile.setCity(profileBom.getCity());
        profile.setPhone(profileBom.getPhone());
        profile.setLastEdited(new Date());

        profile.setAvatar(profileBom.getAvatar());
        profile.setBanner(profileBom.getBanner());

        return profile;
    }
}