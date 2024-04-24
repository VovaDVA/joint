package com.jointAuth.repository;

import com.jointAuth.model.profile.Profile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class ProfileRepositoryTest {

    @Mock
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        profileRepository.deleteAll();
    }

    @Test
    public void testFindByUserIdProfileExists() {
        Long userId = 1L;
        Profile profile = new Profile();

        profile.setId(userId);
        profile.setDescription("Description");
        profile.setBirthday("01.11.1990");
        profile.setCountry("Ukraine");
        profile.setCity("Kiev");

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));

        Optional<Profile> result = profileRepository.findByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(profile, result.get());
        verify(profileRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdProfileDoesNotExist() {
        Long userId = 99L;

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<Profile> result = profileRepository.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(profileRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdNullUserId() {
        Long userId = null;

        when(profileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<Profile> result = profileRepository.findByUserId(userId);

        assertFalse(result.isPresent());
        verify(profileRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testSaveNewProfile() {
        Profile profile = new Profile();

        profile.setId(1L);
        profile.setDescription("New profile");
        profile.setBirthday("01.01.1990");
        profile.setCountry("Russia");
        profile.setCity("Orel");

        when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileRepository.save(profile);

        assertEquals(profile, result);
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testSaveExistingProfile() {
        Profile existingProfile = new Profile();

        existingProfile.setId(1L);
        existingProfile.setDescription("Existing profile");
        existingProfile.setBirthday("01.01.1990");
        existingProfile.setCountry("Russia");
        existingProfile.setCity("Murmansk");

        when(profileRepository.save(existingProfile)).thenReturn(existingProfile);

        Profile result = profileRepository.save(existingProfile);

        assertEquals(existingProfile, result);
        verify(profileRepository, times(1)).save(existingProfile);
    }

    @Test
    public void testSaveNullProfile() {
        Profile profile = null;

        when(profileRepository.save(profile)).thenReturn(null);

        Profile result = profileRepository.save(profile);

        assertNull(result);
        verify(profileRepository, times(1)).save(profile);
    }
}
