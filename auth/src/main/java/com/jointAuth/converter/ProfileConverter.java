package com.jointAuth.converter;

import com.jointAuth.bom.profile.ProfileBom;
import com.jointAuth.model.profile.Profile;

import java.util.Optional;

public class ProfileConverter {
    public static ProfileBom converterToBom(Optional<Profile> currentProfile) {
        ProfileBom bom = new ProfileBom();
        bom.setProfileId(currentProfile.get().getId());
        bom.setUserId(currentProfile.get().getUser().getId());
        bom.setDescription(currentProfile.get().getDescription());
        bom.setBirthday(currentProfile.get().getBirthday());
        bom.setCountry(currentProfile.get().getCountry());
        bom.setCity(currentProfile.get().getCity());
        bom.setPhone(currentProfile.get().getPhone());
        bom.setLastEdited(String.valueOf(currentProfile.get().getLastEdited()));

        return bom;
    }
}
