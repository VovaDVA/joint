package com.jointAuth.service;

import com.jointAuth.bom.profile.ProfileBom;
import com.jointAuth.model.profile.Profile;
import com.jointAuth.model.user.User;
import com.jointAuth.repository.ProfileRepository;
import com.jointAuth.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private ProfileRepository profileRepository;

    private String validToken;
    private String invalidToken;

    @BeforeEach
    public void setUp() {
        profileRepository.deleteAll();

        validToken = "validToken";
        invalidToken = "invalidToken";

        lenient()
                .when(jwtTokenUtils
                .getCurrentProfileId(validToken))
                .thenReturn(1L);
    }

    @Test
    public void testGetCurrentProfileSuccess() {
        User user = new User();
        user.setId(1L);

        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUser(user);

        when(profileRepository
                .findByUserId(1L))
                .thenReturn(Optional.of(profile));

        ProfileBom profileBom = profileService.getCurrentProfile(validToken);

        assertNotNull(profileBom);
        assertEquals(1L, profileBom.getProfileId());
        assertEquals(1L, profileBom.getUserId());
    }

    @Test
    public void testGetCurrentProfileNotFound() {
        when(jwtTokenUtils
                .getCurrentProfileId(validToken))
                .thenReturn(1L);

        when(profileRepository
                .findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            profileService.getCurrentProfile(validToken);
        });
    }

    @Test
    public void testGetCurrentProfileInvalidToken() {
        assertThrows(RuntimeException.class, () -> {
            profileService.getCurrentProfile(invalidToken);
        });
    }

    @Test
    public void testGetCurrentProfileRepositoryError() {
        when(profileRepository
                .findByUserId(1L))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            profileService.getCurrentProfile(validToken);
        });
    }
}
