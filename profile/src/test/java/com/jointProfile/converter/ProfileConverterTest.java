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

    
}
