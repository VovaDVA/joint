package com.jointProfile.converter;

import com.jointProfile.bom.profile.ProfileBom;
import com.jointProfile.entity.Profiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProfileConverterTest {

    @Test
    public void testConverterToBomSuccess() {
        Profiles profile = new Profiles();

        profile.setId(1L);
        profile.setAvatar("avatar.jpg");
        profile.setBanner("banner.jpg");
        profile.setDescription("This is a profile description.");
        profile.setBirthday("21.01.2003");
        profile.setCountry("USA");
        profile.setCity("New York");
        profile.setPhone("1234567890");
        profile.setLastEdited(new Date());

        ProfileBom profileBom = ProfileConverter.converterToBom(profile);

        assertNotNull(profileBom);
        assertEquals(profile.getId(), profileBom.getProfileId());
        assertEquals(profile.getId(), profileBom.getUserId());
        assertEquals(profile.getAvatar(), profileBom.getAvatar());
        assertEquals(profile.getBanner(), profileBom.getBanner());
        assertEquals(profile.getDescription(), profileBom.getDescription());
        assertEquals(profile.getBirthday(), profileBom.getBirthday());
        assertEquals(profile.getCountry(), profileBom.getCountry());
        assertEquals(profile.getCity(), profileBom.getCity());
        assertEquals(profile.getPhone(), profileBom.getPhone());
        assertEquals(String.valueOf(profile.getLastEdited()), profileBom.getLastEdited());

    }

    @Test
    public void testConverterToBomWithNullProfile() {
        ProfileConverter.converterToBom(null);
        ProfileBom profileBom = null;

        assertNull(profileBom);
    }


    @Test
    public void testConverterToEntity() {
      ProfileBom profileBom = new ProfileBom();

        profileBom.setProfileId(1L);
        profileBom.setAvatar("avatar.jpg");
        profileBom.setBanner("banner.jpg");
        profileBom.setDescription("This is a profile description.");
        profileBom.setBirthday("21.01.2003");
        profileBom.setCountry("USA");
        profileBom.setCity("New York");
        profileBom.setPhone("1234567890");
        profileBom.setLastEdited("2023-04-25T12:34:56");

        Profiles profile = ProfileConverter.converterToEntity(profileBom);

        assertNotNull(profile);
        assertEquals(profileBom.getProfileId(), profile.getId());
        assertEquals(profileBom.getUserId(), profile.getUserId());
        assertEquals(profileBom.getAvatar(), profile.getAvatar());
        assertEquals(profileBom.getBanner(), profile.getBanner());
        assertEquals(profileBom.getDescription(), profile.getDescription());
        assertEquals(profileBom.getBirthday(), profile.getBirthday());
        assertEquals(profileBom.getCountry(), profile.getCountry());
        assertEquals(profileBom.getCity(), profile.getCity());
        assertEquals(profileBom.getPhone(), profile.getPhone());
        assertNotNull(profile.getLastEdited());
    }

    @Test
    void testConverterToEntityWithNullProfileBom() {
        Profiles profile = ProfileConverter.converterToEntity(null);

        assertNull(profile);
    }



}
